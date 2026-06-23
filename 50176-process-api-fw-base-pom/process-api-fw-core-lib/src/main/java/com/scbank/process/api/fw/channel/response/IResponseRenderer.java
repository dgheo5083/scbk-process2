package com.scbank.process.api.fw.channel.response;

import org.springframework.http.ResponseEntity;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.message.IResponseMessage;

/**
 * <pre>
 * 프레임워크 채널 응답 메시지 전용 렌더러 SPI 인터페이스입니다.
 *
 * {@link IResponseMessage} 기반의 응답 객체를 HTTP {@link ResponseEntity}로 변환하는 전략을 정의합니다.
 * 
 * 렌더러 구현체는 특정 메시지 유형과 포맷(JSON, XML, 파일 등)에 대해 supports()를 통해 처리 가능 여부를 판단하고,
 * render()를 통해 실제 HTTP 응답 객체를 생성합니다.
 * </pre>
 *
 * 제네릭 타입 설명
 * <ul>
 * <li><b>T</b> - 처리 대상 {@link IResponseMessage}의 타입 (예: 파일 다운로드, JSON DTO
 * 등)</li>
 * <li><b>B</b> - 실제 HTTP 바디에 전송될 타입 (예: {@code byte[]}, DTO, 문자열 등)</li>
 * </ul>
 *
 * {@link ResponseRendererComposite} 내부에서 등록된 렌더러 중 supports()가 true인 구현체를 선택하여
 * render()를 수행합니다.
 *
 * @param <T> 처리할 메시지 타입
 * @param <B> HTTP 응답 바디 타입
 * @author
 */
public interface IResponseRenderer<T extends IResponseMessage<?, ?>, B> {

    /**
     * 해당 렌더러가 주어진 응답 메시지를 처리할 수 있는지 여부를 반환합니다.
     *
     * 메시지의 바디 타입, 컨텍스트 정보(예: Accept 헤더 등)를 기반으로 처리 가능 여부를 판단합니다.
     *
     * @param data    렌더링 대상 응답 메시지
     * @param context 요청 컨텍스트 (Accept 헤더, 서비스 메타정보 포함)
     * @return true이면 render() 대상이 됨
     */
    boolean supports(T data, IServiceContext context);

    /**
     * 주어진 응답 메시지를 HTTP {@link ResponseEntity}로 변환합니다.
     *
     * 변환된 응답은 Spring MVC가
     * {@link org.springframework.http.converter.HttpMessageConverter}를 통해 최종 직렬화하게
     * 됩니다.
     *
     * @param data    렌더링 대상 응답 메시지
     * @param context 요청 컨텍스트
     * @return HTTP 응답 엔티티 (Content-Type, 헤더 등 포함)
     */
    ResponseEntity<B> render(T data, IServiceContext context);
}
