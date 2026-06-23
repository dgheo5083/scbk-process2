package com.scbank.process.api.fw.message.serializer.form;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverter;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.serializer.AbstractMessageSerializer;
import com.scbank.process.api.fw.message.serializer.IMessageSerializer;
import com.scbank.process.api.fw.message.utils.MessageUtils;

/**
 * {@link IMessageObject}를 application/x-www-form-urlencoded 형식으로 직렬화하는
 * Serializer입니다.
 * <p>
 * 프레임워크 메타데이터 기반으로 동작하며, 단일 원시 타입과 List&lt;원시 타입&gt; 필드만 지원합니다.
 * 중첩 객체는 무시되며, 컨버터를 이용해 각 값을 문자열로 변환하여 직렬화합니다.
 * </p>
 *
 * @author sungdon.choi
 * @since 2025.04.30
 */
public class FormMessageSerializer extends AbstractMessageSerializer implements IMessageSerializer {

    /**
     * DTO 객체를 메타데이터 기반으로 application/x-www-form-urlencoded 문자열로 직렬화합니다.
     *
     * @param source 직렬화할 DTO 객체
     * @param ctx    메시지 변환 컨텍스트
     * @return 직렬화된 form 데이터
     * @throws Exception 직렬화 중 오류 발생 시
     */
    @Override
    public <T extends IMessageObject> byte[] serialize(T source, MessageContext ctx) throws Exception {
        IIntegrationMessageMetadata metadata = this.findIntegrationMessageMetadata(source.getClass());
        return serialize(source, metadata, ctx);
    }

    /**
     * 지정된 메타데이터를 기준으로 DTO를 직렬화합니다.
     *
     * @param source   직렬화 대상 객체
     * @param metadata 메시지 메타데이터
     * @param ctx      메시지 변환 컨텍스트
     * @return 직렬화된 form 문자열 바이트
     * @throws Exception 직렬화 실패 시
     */
    @Override
    public <T extends IMessageObject> byte[] serialize(T source, IIntegrationMessageMetadata metadata,
            MessageContext ctx) throws Exception {
        StringBuilder sb = new StringBuilder();
        writeFields(sb, source, metadata.getChildren(), ctx);
        return sb.toString().getBytes(ctx.getDefaultEncoding());
    }

    /**
     * 메타데이터 필드 리스트를 순회하며 개별 필드를 직렬화합니다.
     *
     * @param sb     form 문자열 버퍼
     * @param source 대상 객체
     * @param fields 필드 메타데이터 리스트
     * @param ctx    메시지 컨텍스트
     */
    protected <T extends IMessageObject> void writeFields(
            StringBuilder sb,
            T source,
            List<IMessageMetadata> fields,
            MessageContext ctx) throws Exception {
        if (CollectionUtils.isEmpty(fields))
            return;

        for (IMessageMetadata meta : fields) {
            IMessageFieldMetadata fieldMetadata = (IMessageFieldMetadata) meta;
            writeField(sb, source, fieldMetadata, ctx);
        }
    }

    /**
     * 단일 필드를 타입에 따라 직렬화 처리합니다.
     *
     * @param sb       form 문자열 버퍼
     * @param source   대상 객체
     * @param metadata 필드 메타데이터
     * @param ctx      메시지 컨텍스트
     */
    protected <T extends IMessageObject> void writeField(
            StringBuilder sb,
            T source,
            IMessageFieldMetadata metadata,
            MessageContext ctx) throws Exception {
        Object value = MessageUtils.getFieldValue(source, metadata.getFieldName());
        if (value == null)
            return;

        MessageType type = metadata.getType();
        switch (type) {
            case STRING, BYTE, CHAR, SHORT, INTEGER, LONG, DOUBLE, FLOAT, BIGDECIMAL ->
                writePrimitiveField(sb, value, metadata, ctx);
            case REPEATED -> writeRepeatedField(sb, (Collection<?>) value, metadata, ctx);
            default -> {
                // 중첩 객체(OBJECT)는 무시함
            }
        }
    }

    /**
     * 단일 원시 타입 필드를 직렬화합니다.
     *
     * @param sb       form 문자열 버퍼
     * @param value    필드 값
     * @param metadata 필드 메타데이터
     * @param ctx      메시지 컨텍스트
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void writePrimitiveField(
            StringBuilder sb,
            Object value,
            IMessageFieldMetadata metadata,
            MessageContext ctx) throws Exception {
        IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(metadata.getType());
        if (converter == null) {
            throw new IllegalStateException("지원하지 않는 타입: " + metadata.getType());
        }

        String encoding = this.getEncoding(metadata, ctx.getDefaultEncoding());
        String rendered = converter.write(value, metadata, ctx).toString();
        appendParam(sb, metadata.getId(), rendered, encoding);
    }

    /**
     * List&lt;원시타입&gt; 필드를 순회하며 각각 직렬화합니다.
     *
     * @param sb       form 문자열 버퍼
     * @param list     반복 필드 값 리스트
     * @param metadata 필드 메타데이터
     * @param ctx      메시지 컨텍스트
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void writeRepeatedField(
            StringBuilder sb,
            Collection<?> list,
            IMessageFieldMetadata metadata,
            MessageContext ctx) throws Exception {
        if (CollectionUtils.isEmpty(list))
            return;

        IMessageFieldMetadata childMetadata = (IMessageFieldMetadata) metadata.getChildren().get(0);
        IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(childMetadata.getType());
        if (converter == null) {
            throw new IllegalStateException("지원하지 않는 타입: " + childMetadata.getType());
        }

        String encoding = this.getEncoding(metadata, ctx.getDefaultEncoding());
        int i = 0;
        for (Object item : list) {
            if (item != null) {
                ctx.pushIndex(i);
                String rendered = converter.write(item, metadata, ctx).toString();
                appendParam(sb, metadata.getId(), rendered, encoding);
                ctx.popIndex();
                i++;
            }
        }
    }

    /**
     * form 문자열에 파라미터를 추가합니다.
     *
     * @param sb       form 문자열 버퍼
     * @param name     파라미터 이름
     * @param value    파라미터 값
     * @param encoding 인코딩 문자셋
     */
    private void appendParam(StringBuilder sb, String name, String value, String encoding)
            throws UnsupportedEncodingException {
        if (sb.length() > 0)
            sb.append("&");
        sb.append(encode(name, encoding)).append("=").append(encode(value, encoding));
    }

    /**
     * 문자열을 application/x-www-form-urlencoded 기준으로 인코딩합니다.
     *
     * @param value    인코딩할 값
     * @param encoding 인코딩 문자셋
     * @return 인코딩된 문자열
     */
    private String encode(String value, String encoding) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
    }
}
