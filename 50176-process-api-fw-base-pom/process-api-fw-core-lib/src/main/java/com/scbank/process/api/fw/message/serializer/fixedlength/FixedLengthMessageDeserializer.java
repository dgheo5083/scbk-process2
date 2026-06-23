package com.scbank.process.api.fw.message.serializer.fixedlength;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;
import com.scbank.process.api.fw.core.utils.ByteBuffWrap;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverter;
import com.scbank.process.api.fw.message.evaluate.ConditionEvaluatorComposite;
import com.scbank.process.api.fw.message.metadata.IConditionalFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IExtE2EIgnoreSegmentFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.metadata.ISegmentMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IVariableLengthMessageFieldMetadata;
import com.scbank.process.api.fw.message.serializer.AbstractMessageDeserializer;
import com.scbank.process.api.fw.message.serializer.IMessageDeserializer;
import com.scbank.process.api.fw.message.utils.MessageUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 고정 길이 바이트 배열을 프레임워크 DTO로 역직렬화하는 클래스
 * <p>
 * 메시지 메타데이터 기반으로 각 필드를 읽고, 변환 후 DTO에 설정합니다.
 * </p>
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 14.
 */
@Slf4j
public class FixedLengthMessageDeserializer extends AbstractMessageDeserializer implements IMessageDeserializer {

    /**
     * 바이트 배열을 주어진 DTO 타입으로 역직렬화합니다.
     *
     * @param source     역직렬화할 바이트 배열
     * @param targetType DTO 클래스 타입
     * @param ctx        메시지 변환 컨텍스트
     * @param <T>        IMessageObject 를 구현한 DTO 타입
     * @return 역직렬화된 DTO 객체
     * @throws Exception 역직렬화 중 오류 발생 시
     */
    @Override
    public <T extends IMessageObject> T deserialize(byte[] source, Class<T> targetType, MessageContext ctx)
            throws Exception {
        IIntegrationMessageMetadata integrationMessageMetadata = this.findIntegrationMessageMetadata(targetType);
        ByteArrayInputStream input = new ByteArrayInputStream(source);
        try {
        	return readFromStream(input, integrationMessageMetadata, ctx);
        } finally {
        	int remainLenth = input.available();
        	ctx.setRemainLength(remainLenth);
        	ctx.setRemainBytes(input.readNBytes(remainLenth));
        }
    }

    /**
     * 바이트 배열을 주어진 메타데이터 기반으로 역직렬화합니다.
     *
     * @param source   역직렬화할 바이트 배열
     * @param metadata 대상 DTO의 메시지 메타데이터
     * @param ctx      메시지 변환 컨텍스트
     * @param <T>      IMessageObject를 구현한 DTO 타입
     * @return 역직렬화된 DTO 객체
     * @throws Exception 역직렬화 중 오류 발생 시
     */
    @Override
    public <T extends IMessageObject> T deserialize(byte[] source,
            IIntegrationMessageMetadata metadata, MessageContext ctx) throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(source);
        return readFromStream(input, metadata, ctx);
    }

    /**
     * 입력 스트림에서 필드들을 순차적으로 읽고, DTO 객체를 생성하여 반환합니다.
     *
     * @param input    입력 바이트 스트림
     * @param metadata 전문 메타데이터
     * @param ctx      메시지 변환 컨텍스트
     * @param <T>      메시지 객체 타입
     * @return 파싱된 메시지 객체
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T extends IMessageObject> T readFromStream(
            ByteArrayInputStream input,
            IIntegrationMessageMetadata metadata,
            MessageContext ctx) {
        try {
            Class clazz = metadata.getTargetClass();
            T obj = (T) clazz.getDeclaredConstructor().newInstance();
            this.readFromStream(input, obj, obj, metadata.getChildren(), ctx);

            return obj;
        } catch (Exception e) {
            throw new FrameworkRuntimeException(e);
        }
    }

    protected <T extends IMessageObject> void readFromStream(
            ByteArrayInputStream input,
            T data,
            T root,
            List<IMessageMetadata> metadataList,
            MessageContext ctx) {
        if (CollectionUtils.isEmpty(metadataList)) {
            return;
        }

        metadataList.forEach(
                metadata -> this.readFromStream(input, data, root, (IMessageFieldMetadata) metadata, ctx));
    }

    protected <T extends IMessageObject> void readFromStream(
            ByteArrayInputStream input,
            T data,
            T root,
            IMessageFieldMetadata metadata,
            MessageContext ctx) {
        // 조건부 필드 처리
        if (!this.checkConditionalField(metadata, ctx)) {
            return;
        }

        switch (metadata.getType()) {
            case OBJECT -> {
                if (metadata instanceof ISegmentMessageFieldMetadata segmentMeta) {
                    this.readSegmentObjectFromStream(input, segmentMeta, data, ctx);
                } else {
                    this.readObjectFromStream(input, metadata, data, ctx);
                }
            }
            case REPEATED -> this.readRepeatedField(input, metadata, data, root, ctx);
            case STRING, BYTE, CHAR, SHORT, INTEGER, LONG, DOUBLE, FLOAT, BIGDECIMAL ->
                this.readPrimitiveTypeField(input, metadata, data, -1, ctx);
            case VARIABLE_LENGTH -> this.readVariableLengthField(input, metadata, data, ctx);
            default -> log.error("Unexpected value: " + metadata.getId() + "/" + metadata.getType());
        }
    }

    protected <T extends IMessageObject> void readObjectFromStream(
            ByteArrayInputStream input,
            IMessageFieldMetadata metadata,
            T parent,
            MessageContext ctx) {
        try {
            T data = MessageUtils.createInstance(parent, metadata.getId());

            List<IMessageMetadata> children = metadata.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                children.forEach(c -> this.readFromStream(input, data, parent, (IMessageFieldMetadata) c, ctx));
            }
        } catch (Exception e) {
            throw new FrameworkRuntimeException(e);
        }
    }

    /**
     * 세그먼트 필드를 읽어 파싱합니다.
     *
     * @param input    입력 스트림
     * @param metadata 세그먼트 필드 메타데이터
     * @param parent   상위 객체
     * @param ctx      메시지 변환 컨텍스트
     */
    protected <T extends IMessageObject> void readSegmentObjectFromStream(
            ByteArrayInputStream input,
            ISegmentMessageFieldMetadata metadata,
            T parent,
            MessageContext ctx) {
        try {
            byte[] segmentBytes;

            switch (metadata.getDelimiterPosition()) {
                case PREFIX -> {
                    readUntilDelimiter(input, metadata.getDelimiter()); // skip prefix
                    segmentBytes = readFixedSegment(input, metadata); // 실제 바디
                }
                case SUFFIX -> {
                    segmentBytes = readUntilDelimiter(input, metadata.getDelimiter());
                }
                case WRAPPED -> {
                    readUntilDelimiter(input, metadata.getDelimiter()); // 앞 delimiter 제거
                    segmentBytes = readUntilDelimiter(input, metadata.getDelimiter()); // 바디
                }
                case NONE -> {
                    // 기본적으로 FIXED 길이로 간주 (혹은 예외)
                    throw new UnsupportedOperationException("지원하지 않는 세그먼트 위치: NONE");
                }
                default -> throw new IllegalStateException("알 수 없는 delimiter 위치");
            }

            IMessageObject child = (IMessageObject) metadata.getFieldType().getDeclaredConstructor().newInstance();

            // 세그먼트 바디를 재귀적으로 처리
            ByteArrayInputStream segmentIn = new ByteArrayInputStream(segmentBytes);
            readFromStream(segmentIn, child, child, metadata.getChildren(), ctx);

            MessageUtils.setFieldValue(parent, metadata.getFieldName(), child);
            ctx.addPathValue(metadata.getPath(), segmentBytes.length);
        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.MSG_DESERIALIZATION_FAILED,
                    "세그먼트 필드 [" + metadata.getFieldName() + "] 역직렬화 실패", e);
        }
    }

    /**
     * 입력 스트림에서 주어진 구분자까지 바이트를 읽습니다.
     *
     * @param input     입력 스트림
     * @param delimiter 구분자 바이트 배열
     * @return 구분자 앞까지 읽은 바이트 배열
     * @throws Exception 읽기 실패 시 예외 발생
     */
    protected byte[] readUntilDelimiter(ByteArrayInputStream input, byte[] delimiter) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int dIndex = 0;

        int b;
        while ((b = input.read()) != -1) {
            if ((byte) b == delimiter[dIndex]) {
                dIndex++;
                if (dIndex == delimiter.length) {
                    break; // 전체 delimiter 일치
                }
            } else {
                // 부분 일치 실패 시 다시 저장
                if (dIndex > 0) {
                    buffer.write(delimiter, 0, dIndex);
                    dIndex = 0;
                }
                buffer.write(b);
            }
        }
        return buffer.toByteArray();
    }

    /**
     * 고정 길이 세그먼트 바이트를 읽습니다.
     *
     * @param input    입력 스트림
     * @param metadata 세그먼트 메타데이터
     * @return 고정 길이 바이트 배열
     * @throws Exception 읽기 실패 시 예외 발생
     */
    protected byte[] readFixedSegment(ByteArrayInputStream input, ISegmentMessageFieldMetadata metadata)
            throws Exception {
        int totalLength = metadata.getChildren().stream()
                .mapToInt(field -> {
                    if (field instanceof IMessageFieldMetadata m) {
                        return m.getLength();
                    }
                    return 0;
                })
                .sum();

        return input.readNBytes(totalLength);
    }

    protected <T extends IMessageObject> void readRepeatedField(
            ByteArrayInputStream input,
            IMessageFieldMetadata metadata,
            T parent,
            T ignoredRoot,
            MessageContext ctx) {
        List<IMessageMetadata> children = metadata.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        int repeatCount = this.getRepeatCount(metadata, parent, ctx);
        for (int i = 0; i < repeatCount; i++) {
            Object element = MessageUtils.createInstance(parent, metadata.getId(), i);

            ctx.pushIndex(i);

            for (IMessageMetadata child : children) {
                IMessageFieldMetadata childMeta = (IMessageFieldMetadata) child;
                if (childMeta.getType() == MessageType.OBJECT) {
                    readObjectFromStream(input, childMeta, (IMessageObject) element, ctx);
                } else {
                    readPrimitiveTypeField(input, childMeta, (IMessageObject) element, i, ctx);
                }
            }

            ctx.popIndex();

            MessageUtils.addToListField(parent, metadata.getId(), element);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T extends IMessageObject> void readVariableLengthField(
            ByteArrayInputStream input,
            IMessageFieldMetadata metadata,
            T parent,
            MessageContext ctx) {
        try {

            IVariableLengthMessageFieldMetadata vMeta = (IVariableLengthMessageFieldMetadata) metadata;

            byte[] value;
            switch (vMeta.getVariableFieldType()) {
                case FIELD_REFERENCE -> {
                    String refFieldPath = vMeta.getReferenceField();
                    int refFieldValue = (Integer) ctx.getPathValue(refFieldPath);
                    value = input.readNBytes(refFieldValue);
                }

                case LENGTH_PREFIX -> {
                    int prefixSize = vMeta.getLengthPrefixSize();
                    byte[] prefixBytes = input.readNBytes(prefixSize);

                    ByteBuffer buffer = ByteBuffer.wrap(prefixBytes);
                    int len = switch (prefixSize) {
                        case 1 -> buffer.get() & 0xFF;
                        case 2 -> buffer.getShort() & 0xFFFF;
                        case 4 -> buffer.getInt();
                        default -> throw new IllegalArgumentException("지원하지 않는 lengthPrefixSize");
                    };
                    value = input.readNBytes(len);
                }

                case DELIMITER -> {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    int b;
                    while ((b = input.read()) != -1) {
                        if ((byte) b == vMeta.getDelimiter()) {
                            break;
                        }
                        out.write(b);
                    }
                    value = out.toByteArray();
                }

                case FIXED -> {
                    int fixedLen = vMeta.getFixedLength();
                    value = input.readNBytes(fixedLen);
                }
                default ->
                    throw new IllegalStateException("지원하지 않는 VariableFieldType: " + vMeta.getVariableFieldType());
            }

            Class<?> fieldType = metadata.getFieldType();
            if (fieldType == String.class) {
            	IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(MessageType.VARIABLE_LENGTH);
                Object _value = converter != null ? converter.read(value, metadata, ctx) : new String(value, this.getEncoding(metadata, ctx.getDefaultEncoding()));
                // DTO 필드에 값 세팅
                MessageUtils.setFieldValue(parent, metadata.getFieldName(), _value);
            } else if (fieldType == ByteBuffWrap.class) {
                // DTO 필드에 값 세팅
                MessageUtils.setFieldValue(parent, metadata.getFieldName(), ByteBuffWrap.wrap(value));
            } else {
                // DTO 필드에 값 세팅
                MessageUtils.setFieldValue(parent, metadata.getFieldName(), value);
            }

            ctx.addPathValue(metadata.getPath(), value);

        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.MSG_DESERIALIZATION_FAILED,
                    "필드 [" + metadata.getFieldName() + "] 읽기 실패", e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T extends IMessageObject> void readPrimitiveTypeField(
            ByteArrayInputStream input,
            IMessageFieldMetadata metadata,
            T parent,
            int index,
            MessageContext ctx) {
        try {
            // ExtE2EIgnoreSegment 처리
            if (metadata instanceof IExtE2EIgnoreSegmentFieldMetadata extE2EMeta
                    && MessageType.STRING.equals(metadata.getType())) {

                String start = extE2EMeta.getStart();
                String end = extE2EMeta.getEnd();
                String encoding = getEncoding(metadata, ctx.getDefaultEncoding());

                // start까지 읽기 (무시)
                ByteArrayOutputStream prefixBuffer = new ByteArrayOutputStream();
                int b;
                while ((b = input.read()) != -1) {
                    prefixBuffer.write(b);
                    if (endsWith(prefixBuffer.toByteArray(), start.getBytes(encoding))) {
                        break;
                    }
                }

                // 본문 읽기
                ByteArrayOutputStream contentBuffer = new ByteArrayOutputStream();
                while ((b = input.read()) != -1) {
                    contentBuffer.write(b);
                    if (endsWith(contentBuffer.toByteArray(), end.getBytes(encoding))) {
                        break;
                    }
                }

                byte[] rawBytes = contentBuffer.toByteArray();
                String rawString = new String(rawBytes, encoding);
                String body = rawString.substring(0, rawString.length() - end.length());

                MessageUtils.setFieldValue(parent, metadata.getFieldName(), body);
                String resolvedPath = index == -1 ? metadata.getPath()
                        : metadata.getPath().replace("[*]", "[" + index + "]");
                ctx.addPathValue(resolvedPath, body);
                return;
            }

            // 조건부 필드 처리
            if (metadata instanceof IConditionalFieldMetadata conditionalFieldMetadata) {
                String condition = conditionalFieldMetadata.getCondition();

                if (log.isDebugEnabled()) {
                    log.debug("# 조건부 필드 처리 condition: {}", condition);
                }

                ConditionEvaluatorComposite conditionEvaluator = ctx.getConditionEvaluator();
                if (conditionEvaluator == null || !conditionEvaluator.evaluate(condition, ctx)) {
                    return;
                }
            }

            // 일반 고정길이 처리
            int length = metadata.getLength();
            byte[] buffer = input.readNBytes(length);

            IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(metadata.getType());
            if (converter == null) {
                throw new IllegalStateException("지원하지 않는 타입: " + metadata.getType());
            }

            Object value = converter.read(buffer, metadata, ctx);
            MessageUtils.setFieldValue(parent, metadata.getFieldName(), value);

            String resolvedPath = index == -1 ? metadata.getPath()
                    : metadata.getPath().replace("[*]", "[" + index + "]");
            ctx.addPathValue(resolvedPath, value);

        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.MSG_DESERIALIZATION_FAILED,
                    "필드 [" + metadata.getId() + "] 읽기 실패", e);
        }
    }

    /**
     * byte 배열의 끝이 특정 바이트 시퀀스로 끝나는지 확인
     */
    private boolean endsWith(byte[] full, byte[] suffix) {
        if (full.length < suffix.length)
            return false;
        for (int i = 1; i <= suffix.length; i++) {
            if (full[full.length - i] != suffix[suffix.length - i])
                return false;
        }
        return true;
    }
}
