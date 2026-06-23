package com.scbank.process.api.fw.channel.context.handler;

import com.scbank.process.api.fw.channel.context.IServiceContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 서비스 컨텍스트 핸들러 인터페이스
 *
 * <p>
 * HTTP 요청(HttpServletRequest)을 기반으로 프레임워크 전용 컨텍스트 {@link IServiceContext}를
 * 생성합니다.
 * 요청 트레이스, 디바이스 정보, 세션 정보, 서비스 라우팅 정보 등을 담은 컨텍스트 객체를 구축하기 위한
 * 전략 인터페이스입니다.
 *
 * <p>
 * Spring 환경에서 요청 처리의 시작점으로 사용되며, 프레임워크 내부에서 요청 정보를 일관되게 전달하기 위한
 * 목적으로 활용됩니다.
 *
 * <p>
 * 구현체 예: {@link DefaultServiceContextHandler}
 *
 * @author sungdon.choi
 */
public interface IServiceContextHandler {

    /**
     * 서비스 컨텍스트 객체를 생성합니다.
     *
     * <p>
     * HTTP 요청/응답 정보를 기반으로 다음 항목들을 추출하여 서비스 컨텍스트를 생성합니다:
     * <ul>
     * <li>요청 UUID</li>
     * <li>디바이스 정보</li>
     * <li>세션 정보</li>
     * <li>요청 경로에 대한 서비스 정의 정보</li>
     * <li>요청 로케일</li>
     * </ul>
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @return 서비스 실행을 위한 {@link IServiceContext} 인스턴스
     */
    IServiceContext createServiceContext(HttpServletRequest request, HttpServletResponse response);
}
