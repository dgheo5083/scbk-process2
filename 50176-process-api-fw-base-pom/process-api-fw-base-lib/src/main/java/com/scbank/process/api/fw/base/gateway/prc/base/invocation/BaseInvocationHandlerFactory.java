package com.scbank.process.api.fw.base.gateway.prc.base.invocation;

import java.lang.reflect.Method;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;
import com.scbank.process.api.fw.core.log.trace.TraceSection;

/**
 * 
 */
public abstract class BaseInvocationHandlerFactory implements feign.InvocationHandlerFactory {

	protected feign.InvocationHandlerFactory defaultFactory = new feign.InvocationHandlerFactory.Default();
	
	/**
     * 프로세스 API 게이트웨이 송/수신 시작 트레이스 로그 처리
     * 
     * @param ctx {@link IntegrationContext}
     */
	protected void beginTrace(TraceSection section, String url) {
		TraceContextHolder.get()
			.ifPresent((traceContext) -> traceContext.begin(url, section));
	}
	
	/**
     * 프로세스 API 게이트웨이 송/수신 완료 트레이스 로그 처리
     */
    protected void endTrace() {
        TraceContextHolder.get()
        	.ifPresent((traceContext) -> traceContext.end());
    }

    /**
     * 프로세스 API 게이트웨이 송/수신 실패 트레이스 로그 처리
     * 
     * @param ex CSL 게이트웨이 송/수신 중 발생한 예외 객체
     */
    protected void failTrace(Throwable ex) {
        TraceContextHolder.get()
        	.ifPresent((traceContext) -> traceContext.getCurrent().fail(ex.getMessage()));
    }
	
	/**
	 * 
	 * @param m
	 * @return
	 */
	protected String path(Method m) {
		if (m.isAnnotationPresent(GetMapping.class)) {
			return first(m.getAnnotation(GetMapping.class).value(), m.getAnnotation(GetMapping.class).path());
		}
		
		if (m.isAnnotationPresent(PostMapping.class)) {
			return first(m.getAnnotation(PostMapping.class).value(), m.getAnnotation(PostMapping.class).path());
		}
		
		if (m.isAnnotationPresent(PutMapping.class)) {
			return first(m.getAnnotation(PutMapping.class).value(), m.getAnnotation(PutMapping.class).path());
		}
		
		if (m.isAnnotationPresent(DeleteMapping.class)) {
			return first(m.getAnnotation(DeleteMapping.class).value(), m.getAnnotation(DeleteMapping.class).path());
		}
		
		if (m.isAnnotationPresent(PatchMapping.class)) {
			return first(m.getAnnotation(PatchMapping.class).value(), m.getAnnotation(PatchMapping.class).path());
		}
		return "/";
	}
	
	private static String first(String [] a, String[] b) {
		if (a != null && a.length > 0 && !a[0].isBlank()) {
			return a[0];
		}
		
		if (b != null && b.length > 0 && !b[0].isBlank()) {
			return b[0];
		}
		
		return "/";
	}
}
