package com.scbank.process.api.fw.channel.converter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.scbank.process.api.fw.message.IMessageObject;

/**
 * <pre>
 * 프레임워크에서 지원하는 다양한 메시지 포맷(JSON, XML, 고정길이 등)을 처리하기 위한
 * {@link HttpMessageConverter}의 Composite 구현체입니다.
 *
 * 내부적으로 주어진 {@code
 * delegates
 * } 리스트에서, 요청된 클래스/미디어타입을 처리할 수 있는 Converter를 찾아 위임합니다.
 *
 * 주요 역할:
 * - 읽기 시 {@link #read(Class, HttpInputMessage)} 에서 적절한 Converter를 찾아 위임
 * - 쓰기 시 {@link #write(IMessageObject, MediaType, HttpOutputMessage)} 에서 위임
 * - 지원 미디어타입 목록 병합: {@link #getSupportedMediaTypes()}
 * </pre>
 *
 * @param <T> 처리 대상 메시지 타입 (일반적으로 {@link IMessageObject})
 *
 * @author sungdon.choi
 */

public class HttpMessageConverterComposite<T extends IMessageObject> implements HttpMessageConverter<T> {

    /**
     * 실제 변환을 수행할 위임 대상 {@link HttpMessageConverter} 목록
     */
    private final List<HttpMessageConverter<T>> delegates;

    /**
     * 생성자
     *
     * @param delegates 사용할 {@link HttpMessageConverter} 목록
     */
    public HttpMessageConverterComposite(List<HttpMessageConverter<T>> delegates) {
        this.delegates = delegates;
    }

    /**
     * 특정 클래스와 미디어 타입을 읽을 수 있는지 검사합니다.
     *
     * @param clazz     변환할 클래스 타입
     * @param mediaType 요청 미디어 타입
     * @return 읽기가 가능한 경우 true
     */
    @Override
    public boolean canRead(@NonNull Class<?> clazz, @Nullable MediaType mediaType) {
        return delegates.stream().anyMatch(d -> d.canRead(clazz, mediaType));
    }

    /**
     * 특정 클래스와 미디어 타입을 쓸 수 있는지 검사합니다.
     *
     * @param clazz     변환할 클래스 타입
     * @param mediaType 응답 미디어 타입
     * @return 쓰기가 가능한 경우 true
     */
    @Override
    public boolean canWrite(@NonNull Class<?> clazz, @Nullable MediaType mediaType) {
        return delegates.stream().anyMatch(d -> d.canWrite(clazz, mediaType));
    }

    /**
     * 지원하는 미디어 타입 목록을 반환합니다.
     *
     * @return 지원하는 {@link MediaType} 리스트
     */
    @Override
    public @NonNull List<MediaType> getSupportedMediaTypes() {
        return delegates.stream()
                .flatMap(d -> d.getSupportedMediaTypes().stream())
                .distinct()
                .toList();
    }

    /**
     * 입력 메시지를 읽어 변환합니다.
     *
     * @param clazz        변환할 클래스 타입
     * @param inputMessage 입력 HTTP 메시지
     * @return 변환된 객체
     * @throws IOException                     변환 실패 시 발생
     * @throws HttpMessageNotReadableException 읽을 수 없는 경우 발생
     */
    @Override
    public @NonNull T read(@NonNull Class<? extends T> clazz, @NonNull HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        MediaType mediaType = inputMessage.getHeaders().getContentType();

        Optional<HttpMessageConverter<T>> delegate = delegates.stream()
                .filter(d -> d.canRead(clazz, mediaType))
                .findFirst();

        if (delegate.isEmpty()) {
            throw new HttpMessageNotReadableException("No suitable HttpMessageConverter for " + mediaType,
                    inputMessage);
        }

        // T body = delegate.get().read(clazz, inputMessage);
        return delegate.get().read(clazz, inputMessage);
    }

    /**
     * 객체를 출력 메시지로 변환하여 씁니다.
     *
     * @param t             출력할 객체
     * @param contentType   응답 Content-Type
     * @param outputMessage 출력 HTTP 메시지
     * @throws IOException                     변환 실패 시 발생
     * @throws HttpMessageNotWritableException 쓸 수 없는 경우 발생
     */
    @Override
    public void write(@NonNull T t, @Nullable MediaType contentType, @NonNull HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        Class<?> clazz = t.getClass();

        Optional<HttpMessageConverter<T>> delegate = delegates.stream()
                .filter(d -> d.canWrite(clazz, contentType))
                .findFirst();

        if (delegate.isEmpty()) {
            throw new HttpMessageNotWritableException("No suitable HttpMessageConverter for " + contentType);
        }

        delegate.get().write(t, contentType, outputMessage);
    }
}
