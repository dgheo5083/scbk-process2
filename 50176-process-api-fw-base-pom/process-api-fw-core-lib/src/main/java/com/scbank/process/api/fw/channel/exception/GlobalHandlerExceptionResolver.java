package com.scbank.process.api.fw.channel.exception;

import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.scbank.process.api.fw.channel.converter.HttpMessageConverterComposite;
import com.scbank.process.api.fw.channel.message.IResponseMessage;
import com.scbank.process.api.fw.channel.message.IResponseMessageFactory;
import com.scbank.process.api.fw.message.IMessageObject;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 전역 예외 처리 핸들러
 * 
 * {@link HandlerExceptionResolver}를 구현하여 프레임워크 내에서 발생하는 예외를 포착하고,
 * 요청의 Accept 헤더에 따라 적절한 Content-Type으로 오류 응답을 출력합니다.
 *
 * <ul>
 * <li>Exception → {@link IResponseMessage} 변환: {@link IResponseMessageFactory}
 * 사용</li>
 * <li>Content-Type별 직렬화: {@link HttpMessageConverterComposite}</li>
 * <li>JSON, XML, Text 등 다양한 응답 포맷에 대응 가능</li>
 * </ul>
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 2025. 4. 23.
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(prefix = "csl.channel", name = "enabled", havingValue = "true")
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {

    /**
     * 메시지 포맷에 따라 직렬화를 수행하는 컨버터 컴포지트 (JSON/XML 등)
     */
    protected final HttpMessageConverterComposite<IMessageObject> messageConverterComposite;

    /**
     * 예외를 {@link IResponseMessage}로 변환하는 팩토리
     */
    @SuppressWarnings("rawtypes")
    protected final IResponseMessageFactory responseMessageFactory;

    /**
     * 컴포넌트 로드확인 메소드
     */
    @PostConstruct
    void init() {
        log.info("# 프레임워크 GlobalHandlerExceptionResolver 컴포넌트 초기화 완료");
    }

    /**
     * 예외 발생 시 처리 로직
     *
     * @param request  요청 객체
     * @param response 응답 객체
     * @param handler  예외 발생 시점의 핸들러 객체 (null 가능)
     * @param ex       실제 발생한 예외
     * @return 예외가 처리되었음을 나타내는 {@link ModelAndView} (내용 없음)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public ModelAndView resolveException(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @Nullable Object handler,
            @NonNull Exception ex) {

        // 요청 헤더로부터 Accept 타입 파싱
        MediaType mediaType = resolveAccept(request);

        // 기본 응답 상태 설정 (추후 커스터마이징 가능)
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType(mediaType.toString());

        try {
            // 예외 → 응답 메시지 변환
            IResponseMessage responseMessage = this.responseMessageFactory.fail(ex);

            // 메시지 변환기 통해 직렬화하여 응답 본문 출력
            this.messageConverterComposite.write(
                    responseMessage,
                    mediaType,
                    new ServletServerHttpResponse(response));
        } catch (Exception io) {
            log.warn("응답 변환 실패", io);
        }

        // DispatcherServlet에 예외 처리 완료됨을 알림
        return new ModelAndView();
    }

    /**
     * 요청의 Accept 헤더를 기반으로 응답에 사용할 MediaType 결정
     *
     * @param request HttpServletRequest
     * @return MediaType (기본값: application/json)
     */
    protected MediaType resolveAccept(HttpServletRequest request) {
        String accept = Optional.ofNullable(request.getHeader("Accept"))
                .orElse(MediaType.APPLICATION_JSON_VALUE);

        try {
            return MediaType.parseMediaType(accept);
        } catch (Exception e) {
            return MediaType.APPLICATION_JSON;
        }
    }
}
