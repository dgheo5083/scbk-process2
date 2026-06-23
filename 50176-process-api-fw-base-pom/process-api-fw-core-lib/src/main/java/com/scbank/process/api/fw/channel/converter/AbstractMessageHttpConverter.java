package com.scbank.process.api.fw.channel.converter;

import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

import com.scbank.process.api.fw.channel.support.ChannelMessageContextCreator;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.MessageFormat;

/**
 * 메시지 기반 HTTP 컨버터 추상 클래스
 *
 * <p>
 * 프레임워크 전용 메시지 직렬화/역직렬화 처리를 위한 기반 클래스입니다.
 * MIME 타입에 따른 메시지 변환을 구현하기 위해 확장되며, charset 처리 유틸도 포함합니다.
 *
 * @param <T> 변환 대상 타입
 */
public abstract class AbstractMessageHttpConverter<T> extends AbstractHttpMessageConverter<T> {

    /**
     * 단일 미디어 타입을 지원하는 생성자
     *
     * @param supportedMediaType 지원하는 MediaType
     */
    protected AbstractMessageHttpConverter(MediaType supportedMediaType) {
        super(supportedMediaType);
    }

    /**
     * 다중 미디어 타입을 지원하는 생성자
     *
     * @param supportedMediaTypes 지원하는 MediaType 배열
     */
    protected AbstractMessageHttpConverter(MediaType... supportedMediaTypes) {
        super(supportedMediaTypes);
    }

    /**
     * 기본 문자셋과 함께 다중 미디어 타입을 지원하는 생성자
     *
     * @param defaultCharset      기본 문자셋
     * @param supportedMediaTypes 지원하는 MediaType 배열
     */
    protected AbstractMessageHttpConverter(Charset defaultCharset, MediaType... supportedMediaTypes) {
        super(defaultCharset, supportedMediaTypes);
    }

    /**
     * 입력 메시지에서 charset을 추출하거나, 없을 경우 기본값을 반환합니다.
     *
     * @param inputMessage   HTTP 입력 메시지
     * @param defaultCharset 기본 문자셋
     * @return 유효한 문자셋
     */
    protected Charset resolveCharset(HttpInputMessage inputMessage, Charset defaultCharset) {
        MediaType contentType = inputMessage.getHeaders().getContentType();
        if (contentType != null && contentType.getCharset() != null) {
            return contentType.getCharset();
        }
        return defaultCharset;
    }

    /**
     * 출력 메시지에서 charset을 추출하거나, 없을 경우 기본값을 반환합니다.
     *
     * @param outputMessage  HTTP 출력 메시지
     * @param defaultCharset 기본 문자셋
     * @return 유효한 문자셋
     */
    protected Charset resolveCharset(HttpOutputMessage outputMessage, Charset defaultCharset) {
        MediaType contentType = outputMessage.getHeaders().getContentType();
        if (contentType != null && contentType.getCharset() != null) {
            return contentType.getCharset();
        }
        return defaultCharset;
    }

    /**
     * 메시지 변환 컨텍스트를 생성한다.
     * 
     * @param format   메시지 포맷 (FIXEDLEGTH, JSON, XML 등)
     * @param encoding 인코딩 문자셋
     * @return 메시지 변환 컨텍스트
     */
    protected MessageContext createMessageContext(MessageFormat format, String encoding) {
        ChannelMessageContextCreator messageContextCreator = RuntimeContext.getBean(ChannelMessageContextCreator.class);

        MessageContext messageContext = messageContextCreator.create(format, encoding);
        return messageContext;
    }
}
