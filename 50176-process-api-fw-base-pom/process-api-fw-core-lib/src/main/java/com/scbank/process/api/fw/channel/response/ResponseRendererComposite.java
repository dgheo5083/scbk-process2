package com.scbank.process.api.fw.channel.response;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.message.IResponseMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 등록된 {@link IResponseRenderer} 리스트 중 supports()가 true인 렌더러를 찾아
 * 응답 메시지를 HTTP ResponseEntity로 변환하는 책임을 수행하는 조합 렌더러입니다.
 * </pre>
 *
 * <p>
 * 우선순위는 등록 순서를 따르며, 최초로 supports() = true인 렌더러가 선택됩니다.
 * </p>
 */
@Slf4j
public class ResponseRendererComposite {

    /**
     * 응답 메시지 랜더러 목록
     */
    private final List<IResponseRenderer<?, ?>> delegates;

    /**
     * 생성자
     * 
     * @param delegates 응답 메시지 랜더러 객체 목록
     */
    public ResponseRendererComposite(List<IResponseRenderer<?, ?>> delegates) {
        this.delegates = delegates;
    }

    /**
     * 주어진 응답 메시지를 처리할 수 있는 렌더러를 찾아 render()를 수행합니다.
     *
     * @param response IResponseMessage 응답 메시지
     * @param context  요청 컨텍스트
     * @return HTTP 응답 ResponseEntity
     * @throws IllegalStateException 지원하는 렌더러가 없는 경우
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ResponseEntity<?> render(IResponseMessage<?, ?> response, IServiceContext context) {
        for (IResponseRenderer renderer : delegates) {
            if (renderer.supports(response, context)) {
                if (log.isDebugEnabled()) {
                    log.debug("# 프레임워크 {}.render execute", renderer.getClass().getSimpleName());
                }

                ResponseEntity<?> responseEntity = renderer.render(response, context);

                return responseEntity;
            }
        }

        throw new IllegalStateException("지원하는 ResponseRenderer가 존재하지 않습니다: " + response.getClass());
    }
}
