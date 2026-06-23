package com.scbank.process.api.fw.channel.converter.json;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scbank.process.api.fw.channel.converter.AbstractMessageHttpConverter;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextHolder;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.mapper.jackson.JacksonMessageMapper;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;
import com.scbank.process.api.fw.message.utils.MessageUtils;

/**
 * <pre>
 * 프레임워크 전문 메타데이터 기반 JSON HTTP 메시지 변환기입니다.
 *
 * 주요 기능:
 * - HTTP 요청 본문(JSON)을 {@link IMessageObject}로 변환 (deserialize)
 * - {@link IMessageObject}를 HTTP 응답 본문(JSON)으로 변환 (serialize)
 * - 내부적으로 {@link JacksonMessageMapper} 및 변환 컨텍스트를 활용합니다.
 * 
 * Jackson ObjectMapper 기반으로, 전문 메타데이터를 해석하여 구조화된 JSON 생성/파싱을 수행합니다.
 * </pre>
 *
 * @param <T> 처리 대상 메시지 타입 (일반적으로 {@link IMessageObject})
 * 
 * @author sungdon.choi
 */
public class JacksonMessageHttpConverter<T extends IMessageObject> extends AbstractMessageHttpConverter<T> {

    /** Jackson 기반 메시지 매퍼 */
    private final JacksonMessageMapper jacksonMessageMapper;

    /**
     * 생성자
     *
     * @param jacksonMessageMapper Jackson 메시지 매퍼
     */
    public JacksonMessageHttpConverter(JacksonMessageMapper jacksonMessageMapper) {
        super(Charset.forName(RuntimeContext.getDefaultEncoding()), MediaType.APPLICATION_JSON);
        this.jacksonMessageMapper = jacksonMessageMapper;
    }

    /**
     * 지원 대상 클래스 여부 검사
     *
     * @param clazz 변환할 클래스 타입
     * @return 지원하면 true
     */
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return IMessageObject.class.isAssignableFrom(clazz);
    }

    /**
     * 입력 HTTP 메시지를 읽어 {@link IMessageObject}로 변환합니다.
     *
     * @param clazz        변환할 클래스 타입
     * @param inputMessage 입력 HTTP 메시지
     * @return 변환된 객체
     * @throws IOException                     변환 실패 시 발생
     * @throws HttpMessageNotReadableException 읽기 실패 시 발생
     */
    @Override
    @NonNull
    protected T readInternal(@NonNull Class<? extends T> clazz, @NonNull HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        long length = inputMessage.getHeaders().getContentLength();
        byte[] requestBytes = inputMessage.getBody().readNBytes((int) length);

        Charset defaultCharset = this.resolveCharset(inputMessage,
                Charset.forName(RuntimeContext.getDefaultEncoding()));

        MessageContext messageContext = this.createMessageContext(MessageFormat.JSON, defaultCharset.name());
        MessageContextHolder.set(messageContext);

        try {
            return this.jacksonMessageMapper.deserialize(requestBytes, clazz, messageContext);
        } catch (Exception e) {
            throw new HttpMessageNotReadableException("JSON 데이터 역직렬화 실패", e, inputMessage);
        } finally {
            MessageContextHolder.clear();
        }
    }

    /**
     * {@link IMessageObject}를 HTTP 응답 본문(JSON)으로 변환하여 작성합니다.
     *
     * @param t             변환할 객체
     * @param outputMessage 출력 HTTP 메시지
     * @throws HttpMessageNotWritableException 쓰기 실패 시 발생
     */
    @Override
    protected void writeInternal(@NonNull T t, @NonNull HttpOutputMessage outputMessage)
            throws HttpMessageNotWritableException {

        Charset defaultCharset = this.resolveCharset(outputMessage,
                Charset.forName(RuntimeContext.getDefaultEncoding()));

        MessageContext messageContext = this.createMessageContext(MessageFormat.JSON, defaultCharset.name());
        MessageContextHolder.set(messageContext);

        try {
            ObjectMapper objectMapper = this.jacksonMessageMapper.getObjectMapper();
            IIntegrationMessageMetadataRegistrar metadataRegistrar = RuntimeContext
                    .getBean(IIntegrationMessageMetadataRegistrar.class);
            ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

            List<Field> fields = MessageUtils.getAllFields(t.getClass());
            for (Field field : fields) {
                Object child = MessageUtils.getFieldValue(t, field.getName());
                if (!(child instanceof IMessageObject msg)) {
                    continue;
                }

                IIntegrationMessageMetadata metadata = metadataRegistrar.getMetadata(child.getClass());
                if (metadata == null) {
                    continue;
                }

                byte[] bytes = this.jacksonMessageMapper.serialize(msg, metadata, messageContext);
                rootNode.set(field.getName(), objectMapper.readTree(bytes));
            }

            byte[] responseBytes = objectMapper.writeValueAsBytes(rootNode);
            outputMessage.getHeaders().setContentLength(responseBytes.length);
            outputMessage.getBody().write(responseBytes);
        } catch (Exception e) {
            throw new HttpMessageNotWritableException("JSON 데이터 직렬화 실패", e);
        } finally {
            MessageContextHolder.clear();
        }
    }
}
