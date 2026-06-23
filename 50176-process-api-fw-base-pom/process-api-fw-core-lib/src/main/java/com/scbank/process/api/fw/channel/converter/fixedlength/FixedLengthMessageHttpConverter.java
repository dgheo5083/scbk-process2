package com.scbank.process.api.fw.channel.converter.fixedlength;

import java.io.ByteArrayOutputStream;
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

import com.scbank.process.api.fw.channel.converter.AbstractMessageHttpConverter;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextHolder;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.mapper.fixedlength.FixedLengthMessageMapper;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;
import com.scbank.process.api.fw.message.utils.MessageUtils;

/**
 * 고정 길이 전문 메시지 전송을 위한 HTTP 메시지 컨버터
 *
 * <p>
 * {@link IMessageObject} 기반 DTO를 HTTP 요청/응답 바디에서 고정 길이 바이트 배열로
 * 변환하거나 역변환하는 기능을 수행합니다.
 *
 * <p>
 * 내부적으로 {@link FixedLengthMessageMapper}, {@link IIntegrationMessageMetadata},
 * {@link MessageContext} 등을 활용하여 전문 메시지를 메타데이터 기반으로 직렬화/역직렬화합니다.
 *
 * <p>
 * 지원 미디어 타입: {@code application/octet-stream}, {@code text/plain}
 *
 * @param <T> 처리 대상 메시지 객체 타입
 * @author sungdon.choi
 */
public class FixedLengthMessageHttpConverter<T extends IMessageObject> extends AbstractMessageHttpConverter<T> {

    /** 고정 길이 메시지 직렬화/역직렬화 매퍼 */
    private final FixedLengthMessageMapper fixedLengthMessageMapper;

    /**
     * 생성자
     *
     * @param fixedLengthMessageMapper 고정 길이 메시지 매퍼
     */
    public FixedLengthMessageHttpConverter(
            FixedLengthMessageMapper fixedLengthMessageMapper) {
        super(Charset.forName(RuntimeContext.getDefaultEncoding()),
                // MediaType.APPLICATION_OCTET_STREAM,
                MediaType.TEXT_PLAIN);
        this.fixedLengthMessageMapper = fixedLengthMessageMapper;
    }

    /**
     * 지원 여부 판단: {@code IMessageObject} 타입만 처리 가능
     *
     * @param clazz 변환 대상 클래스
     * @return 처리 가능 여부
     */
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return IMessageObject.class.isAssignableFrom(clazz);
    }

    /**
     * 고정 길이 전문 → DTO 객체로 역직렬화
     *
     * @param clazz        변환할 클래스 타입
     * @param inputMessage 입력 메시지
     * @return 역직렬화된 DTO 객체
     * @throws IOException 읽기 실패 시
     */
    @Override
    @NonNull
    protected T readInternal(@NonNull Class<? extends T> clazz, @NonNull HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        long length = inputMessage.getHeaders().getContentLength();
        byte[] requestBytes = inputMessage.getBody().readNBytes((int) length);

        Charset defaultCharset = this.resolveCharset(inputMessage,
                Charset.forName(RuntimeContext.getDefaultEncoding()));

        MessageContext messageContext = this.createMessageContext(MessageFormat.FIXEDLENGTH, defaultCharset.name());
        MessageContextHolder.set(messageContext);
        try {
            return this.fixedLengthMessageMapper.deserialize(requestBytes, clazz, messageContext);
        } catch (Exception e) {
            throw new HttpMessageNotReadableException(e.getMessage(), e, inputMessage);
        } finally {
            MessageContextHolder.clear();
        }
    }

    /**
     * DTO 객체 → 고정 길이 전문 직렬화 후 출력 스트림에 씀
     *
     * @param t             출력할 객체
     * @param outputMessage 출력 메시지
     * @throws HttpMessageNotWritableException 쓰기 실패 시
     */
    @Override
    protected void writeInternal(@NonNull T t, @NonNull HttpOutputMessage outputMessage)
            throws HttpMessageNotWritableException {

        Charset defaultCharset = this.resolveCharset(outputMessage,
                Charset.forName(RuntimeContext.getDefaultEncoding()));

        MessageContext messageContext = this.createMessageContext(MessageFormat.FIXEDLENGTH, defaultCharset.name());
        MessageContextHolder.set(messageContext);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            IIntegrationMessageMetadataRegistrar integrationMessageMetadataRegistrar = RuntimeContext
                    .getBean(IIntegrationMessageMetadataRegistrar.class);

            List<Field> fields = MessageUtils.getAllFields(t.getClass());
            for (Field field : fields) {
                Object child = MessageUtils.getFieldValue(t, field.getName());
                if (!(child instanceof IMessageObject msg))
                    continue;

                IIntegrationMessageMetadata metadata = integrationMessageMetadataRegistrar
                        .getMetadata(child.getClass());
                if (metadata == null)
                    continue;

                byte[] bytes = this.fixedLengthMessageMapper.serialize(msg, metadata, messageContext);
                out.write(bytes, 0, bytes.length);
            }

            byte[] responseBytes = out.toByteArray();
            outputMessage.getHeaders().setContentLength(responseBytes.length);
            outputMessage.getBody().write(responseBytes);
        } catch (Exception e) {
            throw new HttpMessageNotWritableException(e.getMessage(), e);
        } finally {
            MessageContextHolder.clear();
        }
    }
}
