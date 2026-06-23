package com.scbank.process.api.fw.channel.interceptor;

import java.util.List;

import org.slf4j.MDC;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.channel.context.handler.IServiceContextHandler;
import com.scbank.process.api.fw.core.log.trace.TraceContext;
import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;
import com.scbank.process.api.fw.core.log.trace.TraceLogger;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContext;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * 프레임워크 서비스 요청 인터셉터 클래스
 *
 * <p>
 * 모든 서비스 요청에 대해 다음과 같은 공통 작업을 수행합니다:
 * <ul>
 * <li>{@link IServiceContext} 생성 및 스레드 로컬 저장</li>
 * <li>TraceContext 초기화 및 Trace 트리 로깅</li>
 * <li>MDC 설정 (request-uuid, device)</li>
 * <li>요청 처리 체인(requestInterceptorComposite) 실행</li>
 * </ul>
 *
 * <p>
 * Spring WebMvc HandlerInterceptor 인터페이스 구현
 * 
 * @see org.springframework.web.servlet.HandlerInterceptor
 */
@RequiredArgsConstructor
public class ServiceRequestInterceptor implements HandlerInterceptor {

    /**
     * 서비스 컨텍스트 핸들러 - HTTP 요청에서 {@link IServiceContext} 객체 생성
     */
    private final IServiceContextHandler serviceContextHandler;

    private final ObjectProvider<IRequestInterceptor> interceptorsProvider;

    /**
     * 등록된 개별 {@link IRequestInterceptor} 를 체인으로 실행하는 컴포지트 객체
     */
    private RequestInterceptorComposite requestInterceptorComposite;

    @PostConstruct
    public void init() {
        List<IRequestInterceptor> interceptors = interceptorsProvider.stream().toList();
        this.requestInterceptorComposite = new RequestInterceptorComposite(interceptors);
    }

    /**
     * 요청 전 처리 (context, trace 설정 포함)
     */
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {
        IServiceContext serviceContext = this.serviceContextHandler.createServiceContext(request, response);
        ServiceContextHolder.setContext(serviceContext);

        // TraceContext 초기화
        TraceContext traceContext = new TraceContext(serviceContext.request().getRequestURI());
        TraceContextHolder.set(traceContext);

        this.putMDC(serviceContext);
        return this.requestInterceptorComposite.preHandle(request, response, handler);
    }

    /**
     * DispatcherServlet 처리 이후 호출됨 (ModelAndView 처리 시점)
     */
    @Override
    public void postHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        this.requestInterceptorComposite.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 요청 완료 후 처리 (리소스 정리, 트레이스 종료 등)
     */
    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            @Nullable Exception ex) throws Exception {
        try {
            this.requestInterceptorComposite.afterCompletion(request, response, handler, ex);
        } finally {
            // Trace 종료 및 로깅
            TraceContextHolder.get().ifPresent((context) -> {
                context.end();
                TraceLogger.logTraceTree(context.getRoot());
            });

            // ThreadLocal, MDC 정리
            TraceContextHolder.clear();
            MDC.clear();
            ServiceContextHolder.clear();
        }
    }

    /**
     * MDC(Mapped Diagnostic Context) 설정
     *
     * @param serviceContext 서비스 요청 컨텍스트
     */
    private void putMDC(IServiceContext serviceContext) {
    	
    	String requestId = "";
    	String sessionId = "";
    	String userId = "";
    	
    	if (StringUtils.hasText(serviceContext.requestUId())) {
    		requestId = serviceContext.requestUId();
    		MDC.put("request-uuid", requestId);
    	}
    	
    	//널방어 로직 추가
    	if (serviceContext.session() != null) {
    		ISessionContext sessionContext = serviceContext.session();
    		sessionId = sessionContext.getSessionId();
    		MDC.put("session-id", sessionId);
    		
    		boolean isLogined = sessionContext.isLogined();
    		userId = isLogined ? sessionContext.getLoginSession().getUserId() : "";
    		MDC.put("user-id", userId);
    	} else {
    		HttpServletRequest request = serviceContext.request();
    		if (request != null) {
    			HttpSession httpSession = request.getSession();
    			sessionId = httpSession != null ? httpSession.getId() : "";
    			userId = "guest";
        		MDC.put("session-id", sessionId);
        		MDC.put("user-id", userId);
    		}
    	}
    	
    	String logContext = this.buildLogContext(requestId, sessionId, userId);
    	if (StringUtils.hasText(logContext)) {
    		MDC.put("logContext", logContext);
    	}
    }
    
    private String buildLogContext(String requestId, String sessionId, String userId) {
    	
    	StringBuffer sb = new StringBuffer();
    	
    	if (StringUtils.hasText(requestId)) {
    		sb.append("[req=").append(requestId).append("]");
    	}
    	
    	if (StringUtils.hasText(sessionId)) {
    		sb.append("[sessionId=").append(sessionId).append("]");
    	}
    	
    	if (StringUtils.hasText(userId) && !"guest".equalsIgnoreCase(userId)) {
    		sb.append("[userId=").append(userId).append("]");
    	}
    	
    	return sb.toString();
    }
}
