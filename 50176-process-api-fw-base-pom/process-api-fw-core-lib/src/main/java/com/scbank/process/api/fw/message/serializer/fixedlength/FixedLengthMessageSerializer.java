package com.scbank.process.api.fw.message.serializer.fixedlength;

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
import com.scbank.process.api.fw.message.metadata.IExtE2EIgnoreSegmentFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.metadata.ISegmentMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IVariableLengthMessageFieldMetadata;
import com.scbank.process.api.fw.message.serializer.AbstractMessageSerializer;
import com.scbank.process.api.fw.message.serializer.IMessageSerializer;
import com.scbank.process.api.fw.message.utils.MessageUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 DTO를 고정 길이 바이트 배열로 직렬화하는 구현체입니다.
 * <p>
 * {@link IMessageObject} 인터페이스를 구현한 DTO 클래스 기반으로,
 * 메타데이터 정보({@link IIntegrationMessageMetadata})를 참고하여
 * 고정 길이 포맷의 바이트 배열로 변환합니다.
 * </p>
 */
@Slf4j
public class FixedLengthMessageSerializer extends AbstractMessageSerializer implements IMessageSerializer {

    /**
     * DTO 객체를 고정 길이 바이트 배열로 직렬화합니다.
     *
     * @param source 직렬화할 DTO 객체
     * @param ctx    메시지 컨텍스트
     * @return 직렬화된 바이트 배열
     */
    @Override
    public <T extends IMessageObject> byte[] serialize(T source, MessageContext ctx) throws Exception {
    	if (source == null) {
    		return null;
    	}
        // log.debug("### writeField metadata source class ={}", source.getClass());
        IIntegrationMessageMetadata metadata = this.findIntegrationMessageMetadata(source.getClass());
        if (metadata == null) {
            log.debug("metadata is null source class is {}", source.getClass());
            //throw new MessageMetadataNotFoundException("null", "등록된 전문 메타데이터를 찾지 못하였습니다.");
            return null;
        }
        return this.serialize(source, metadata, ctx);
    }

    /**
     * DTO와 메타데이터를 바탕으로 바이트 배열을 생성합니다.
     *
     * @param source   직렬화할 DTO 객체
     * @param metadata 메타데이터
     * @param ctx      메시지 컨텍스트
     * @return 직렬화된 바이트 배열
     */
    @Override
    public <T extends IMessageObject> byte[] serialize(T source, IIntegrationMessageMetadata metadata,
            MessageContext ctx) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        this.writeField(output, metadata, source, ctx);
        return output.toByteArray();
    }

    /**
     * 최상위 메시지 객체의 하위 필드를 재귀적으로 직렬화합니다.
     *
     * @param out      출력 스트림
     * @param metadata 메타데이터
     * @param source   메시지 객체
     * @param ctx      메시지 컨텍스트
     */
    protected <T extends IMessageObject> void writeField(ByteArrayOutputStream out,
            IIntegrationMessageMetadata metadata, T source, MessageContext ctx) throws Exception {
        for (IMessageMetadata child : metadata.getChildren()) {
            writeField(out, child, source, ctx);
        }
    }

    /**
     * 자식 필드 리스트를 반복하여 직렬화합니다.
     *
     * @param out      출력 스트림
     * @param children 자식 메타데이터 목록
     * @param source   메시지 객체
     * @param ctx      메시지 컨텍스트
     */
    protected <T extends IMessageObject> void writeField(ByteArrayOutputStream out, List<IMessageMetadata> children,
            T source, MessageContext ctx) throws Exception {
        for (IMessageMetadata child : children) {
            writeField(out, child, source, ctx);
        }
    }

    /**
     * 메타데이터 타입에 따라 필드를 분기 직렬화합니다.
     *
     * @param out      출력 스트림
     * @param metadata 필드 메타데이터
     * @param source   메시지 객체
     * @param ctx      메시지 컨텍스트
     */
    protected <T extends IMessageObject> void writeField(ByteArrayOutputStream out, IMessageMetadata metadata, T source,
            MessageContext ctx) throws Exception {
        Object value = MessageUtils.getFieldValue(source, metadata.getFieldName());
        switch (metadata.getType()) {
            case OBJECT -> {
                if (metadata instanceof ISegmentMessageFieldMetadata segmentMeta) {
                    writeSegmentObjectField(out, segmentMeta, (IMessageObject) value, ctx);
                } else {
                    writeField(out, ((IMessageFieldMetadata) metadata).getChildren(), (IMessageObject) value, ctx);
                }
            }
            case REPEATED -> writeRepeatedField(out, (IMessageFieldMetadata) metadata, value, ctx);
            case STRING, BYTE, CHAR, SHORT, INTEGER, LONG, DOUBLE, FLOAT, BIGDECIMAL ->
                writePrimitiveField(out, (IMessageFieldMetadata) metadata, value, -1, ctx);
            case VARIABLE_LENGTH -> writeVariableLengthField(out, (IMessageFieldMetadata) metadata, value, ctx);
            default -> log.error("Unexpected value: " + metadata.getId() + "/" + metadata.getType());
        }
    }

    /**
     * 세그먼트 필드를 처리하며, 위치별 구분자를 포함합니다.
     *
     * @param out   출력 스트림
     * @param meta  세그먼트 메타데이터
     * @param value 메시지 객체 값
     * @param ctx   메시지 컨텍스트
     */
    protected void writeSegmentObjectField(ByteArrayOutputStream out, ISegmentMessageFieldMetadata meta,
            IMessageObject value, MessageContext ctx) throws Exception {
        if (value == null)
            return;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        writeField(buffer, meta.getChildren(), value, ctx);
        byte[] body = buffer.toByteArray();
        byte[] delimiter = meta.getDelimiter();

        switch (meta.getDelimiterPosition()) {
            case PREFIX -> {
                out.write(delimiter);
                out.write(body);
            }
            case SUFFIX -> {
                out.write(body);
                out.write(delimiter);
            }
            case WRAPPED -> {
                out.write(delimiter);
                out.write(body);
                out.write(delimiter);
            }
            case NONE -> {
                out.write(body);
            }
        }

        ctx.addPathValue(meta.getPath(), body.length);
    }

    /**
     * 반복 필드를 처리합니다. 각 자식 필드는 반복 인덱스를 고려하여 직렬화됩니다.
     *
     * @param out      출력 스트림
     * @param metadata 반복 필드 메타데이터
     * @param value    반복 객체 리스트
     * @param ctx      메시지 컨텍스트
     */
    protected void writeRepeatedField(ByteArrayOutputStream out, IMessageFieldMetadata metadata, Object value,
            MessageContext ctx) throws Exception {
        if (!(value instanceof List<?> list)) {
            //throw new IllegalArgumentException("필드 [" + metadata.getId() + "] 는 List 타입이 아닙니다.");
        	return;
        }

        List<IMessageMetadata> children = metadata.getChildren();
        if (CollectionUtils.isEmpty(children))
            return;

        for (int i = 0; i < list.size(); i++) {
            Object element = list.get(i);
            ctx.pushIndex(i);
            for (IMessageMetadata child : children) {
                IMessageFieldMetadata childMeta = (IMessageFieldMetadata) child;
                Object childValue = MessageUtils.getFieldValue(element, childMeta.getFieldName());
                if (childMeta.getType() == MessageType.OBJECT && childValue instanceof IMessageObject nested) {
                    writeField(out, childMeta, nested, ctx);
                } else {
                    writePrimitiveField(out, childMeta, childValue, i, ctx);
                }
            }
            ctx.popIndex();
        }
    }

    /**
     * 원시 타입 필드를 고정길이 포맷으로 직렬화합니다.
     *
     * <p>
     * 특히 {@link IExtE2EIgnoreSegmentFieldMetadata}가 적용된 경우,
     * 값이 지정된 시작/종료 문자열로 감싸져 있으면 해당 값 전체를 원형 그대로 직렬화합니다.
     * </p>
     *
     * @param out      출력 스트림
     * @param metadata 필드 메타데이터
     * @param value    직렬화할 필드 값
     * @param ctx      메시지 컨텍스트 (인코딩, 컨버터 레지스트리, 경로 추적 등 포함)
     *
     * @throws RuntimeException 직렬화 중 예외가 발생한 경우
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void writePrimitiveField(
            ByteArrayOutputStream out,
            IMessageFieldMetadata metadata,
            Object value,
            int index,
            MessageContext ctx) {
        try {
            // ExtE2EIgnoreSegment 처리
            if (tryWriteExtE2EIgnoreField(out, metadata, value, index, ctx)) {
                return;
            }

            // 일반 원시 타입 직렬화 처리
            IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(metadata.getType());
            if (converter == null) {
                throw new IllegalStateException("지원하지 않는 타입: " + metadata.getFieldType());
            }

            byte[] result = (byte[]) converter.write(value, metadata, ctx);
            out.write(result);

            String resolvedPath = index == -1 ? metadata.getPath()
                    : metadata.getPath().replace("[*]", "[" + index + "]");
            ctx.addPathValue(resolvedPath, value);

        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.MSG_SERIALIZATION_FAILED,
                    "[" + metadata.getId() + "] 필드 직렬화 실패", e);
        }
    }

    /**
     * ExtE2EIgnoreSegment 어노테이션이 적용된 경우 해당 로직을 처리합니다.
     *
     * <p>
     * 시작/종료 문자열로 감싸진 경우 원문 그대로 출력 스트림에 write 합니다.
     * </p>
     *
     * @param out      출력 스트림
     * @param metadata 필드 메타데이터
     * @param value    필드 값
     * @param ctx      메시지 컨텍스트
     * @param index    필드 인덱스, 반복부 하위 필드인 경우 사용
     * @return 해당 로직으로 처리되었는지 여부 (true: 처리됨, false: 기본 로직 필요)
     */
    protected boolean tryWriteExtE2EIgnoreField(
            ByteArrayOutputStream out,
            IMessageFieldMetadata metadata,
            Object value,
            int index,
            MessageContext ctx) {
        if (MessageType.STRING.equals(metadata.getType())
                && metadata instanceof IExtE2EIgnoreSegmentFieldMetadata extMeta
                && value instanceof String strValue) {

            String start = extMeta.getStart();
            String end = extMeta.getEnd();

            if (strValue.startsWith(start) && strValue.endsWith(end)) {
                try {
                    byte[] raw = strValue.getBytes(this.getEncoding(metadata, ctx.getDefaultEncoding()));
                    out.write(raw);

                    String resolvedPath = index == -1 ? metadata.getPath()
                            : metadata.getPath().replace("[*]", "[" + index + "]");
                    ctx.addPathValue(resolvedPath, value);
                    return true;
                } catch (Exception e) {
                    throw new FrameworkRuntimeException(FrameworkErrorCode.MSG_SERIALIZATION_FAILED,
                            "[" + metadata.getId() + "] ExtE2EIgnoreSegment 직렬화 실패", e);
                }
            }
        }
        return false;
    }

    /**
     * 가변 길이 필드를 직렬화합니다.
     *
     * @param output   출력 스트림
     * @param metadata 필드 메타데이터
     * @param value    필드 값
     * @param ctx      메시지 컨텍스트
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T extends IMessageObject> void writeVariableLengthField(ByteArrayOutputStream output,
            IMessageFieldMetadata metadata, Object value, MessageContext ctx) throws Exception {
        IVariableLengthMessageFieldMetadata vMeta = (IVariableLengthMessageFieldMetadata) metadata;

        byte[] encoded = new byte[0];
        Class<?> fieldType = metadata.getFieldType();

        if (value == byte[].class) {
            encoded = (byte[]) value;
        } else if (fieldType == String.class) {
        	// 일반 원시 타입 직렬화 처리
            IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(MessageType.VARIABLE_LENGTH);
            byte[] result = (byte[]) converter.write(value, metadata, ctx);
            encoded = result;
        } else if (fieldType == ByteBuffWrap.class && value != null) {
            encoded = ((ByteBuffWrap) value).getByteArray();
        } else {
            throw new UnsupportedOperationException("지원하지 않는 타입: " + fieldType);
        }

        ctx.addPathValue(metadata.getPath(), encoded.length);

        switch (vMeta.getVariableFieldType()) {
            case FIELD_REFERENCE -> output.write(encoded);
            case LENGTH_PREFIX -> {
                int prefixSize = vMeta.getLengthPrefixSize();
                ByteBuffer buffer = ByteBuffer.allocate(prefixSize);
                switch (prefixSize) {
                    case 1 -> buffer.put((byte) encoded.length);
                    case 2 -> buffer.putShort((short) encoded.length);
                    case 4 -> buffer.putInt(encoded.length);
                    default -> throw new IllegalArgumentException("지원하지 않는 lengthPrefixSize");
                }
                output.write(buffer.array());
                output.write(encoded);
            }
            case DELIMITER -> {
                output.write(encoded);
                output.write(vMeta.getDelimiter());
            }
            case FIXED -> {
                int fixedLen = vMeta.getFixedLength();
                if (encoded.length > fixedLen) {
                    throw new IllegalArgumentException(
                            "필드 [" + metadata.getId() + "] 의 값이 고정 길이(" + fixedLen + ")를 초과했습니다.");
                }
                output.write(encoded);
                if (encoded.length < fixedLen) {
                    output.write(new byte[fixedLen - encoded.length]);
                }
            }
            default -> throw new IllegalStateException("지원하지 않는 VariableFieldType: " + vMeta.getVariableFieldType());
        }
    }
}
