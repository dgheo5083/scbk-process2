package com.scbank.process.api.fw.channel.converter.multipart;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.scbank.process.api.fw.channel.converter.AbstractMessageHttpConverter;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextHolder;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.mapper.multipart.MultipartMessageMapper;

/**
 * Multipart/form-data 형식의 HTTP 요청을 IMessageObject로 역직렬화하는 컨버터.
 *
 * 이 컨버터는 DispatcherServlet 수준에서 MultipartResolver가 먼저 작동한다는 전제하에,
 * MultipartHttpServletRequest에서 파일 및 필드 데이터를 추출하여 IMessageObject 형태로 변환한다.
 *
 * MultipartMessageMapper를 통해 실제 파싱 로직을 위임하며, 바디를 byte[]로 읽지 않고 직접 request 객체에서
 * 필드 추출.
 *
 * @param <T> IMessageObject 구현 타입
 * @author Min-jun
 */
public class MultipartMessageHttpConverter<T extends IMessageObject> extends AbstractMessageHttpConverter<T> {

    /** Multipart 요청을 IMessageObject로 매핑하는 Mapper */
    private final MultipartMessageMapper multipartMessageMapper;

    /**
     * 디폴트 생성자.
     *
     * @param multipartMessageMapper Multipart 요청을 IMessageObject로 변환하는 매퍼
     */
    public MultipartMessageHttpConverter(MultipartMessageMapper multipartMessageMapper) {
        super(Charset.forName(RuntimeContext.getDefaultEncoding()), MediaType.MULTIPART_FORM_DATA);
        this.multipartMessageMapper = multipartMessageMapper;
    }

    /**
     * IMessageObject 타입인지 여부 확인.
     *
     * @param clazz 대상 클래스
     * @return IMessageObject의 하위 타입 여부
     */
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return clazz.isAssignableFrom(IMessageObject.class);
    }

    /**
     * Multipart 요청을 IMessageObject로 역직렬화.
     *
     * DispatcherServlet 수준에서 MultipartResolver가 이미 MultipartHttpServletRequest로
     * 변환했기 때문에
     * RequestContextHolder에서 해당 request를 꺼내 직접 필드를 읽는다.
     *
     * @param clazz        역직렬화 대상 클래스
     * @param inputMessage 무시됨 (multipart/form은 직접 요청에서 읽음)
     * @return 역직렬화된 객체
     * @throws IOException                     multipart 요청이 아닌 경우 예외 발생
     * @throws HttpMessageNotReadableException 역직렬화 실패 시 예외
     */
    @Override
    protected T readInternal(@NonNull Class<? extends T> clazz, @NonNull HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) RequestContextHolder
                .getRequestAttributes()
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        if (multipartRequest == null) {
            throw new IllegalStateException("MultipartHttpServletRequest를 찾을 수 없습니다.");
        }

        try {
            MessageContext context = createMessageContext(MessageFormat.MULTIPART_FORM, getDefaultCharset().name());
            MessageContextHolder.set(context);

            byte[] dummy = new byte[0]; // multipart/form은 바디 전체를 byte[]로 읽지 않음
            return this.multipartMessageMapper.deserialize(dummy, clazz, context);
        } catch (Exception e) {
            throw new HttpMessageNotReadableException("Multipart 역직렬화 실패", e, inputMessage);
        } finally {
            MessageContextHolder.clear();
        }
    }

    /**
     * Multipart 응답은 지원하지 않음. 현재는 무시됨.
     *
     * @param t             직렬화 대상 객체
     * @param outputMessage HTTP 응답 메시지
     * @throws IOException                     출력 실패
     * @throws HttpMessageNotWritableException 직렬화 불가 시 예외
     */
    @Override
    protected void writeInternal(@NonNull T t, @NonNull HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        // 현재는 multipart 응답을 지원하지 않음
        throw new UnsupportedOperationException("multipart 응답 지원하지 않습니다.");
    }
}
