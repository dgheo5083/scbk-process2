package com.scbank.process.api.fw.base.async;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.scbank.process.api.fw.base.store.ThreadLocalStore;
import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.core.log.trace.TraceContext;
import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;

/**
 * 프레임워크 비동기 TaskDecorator 구현 클래스
 */
@Component
@Primary
public class SecureContextAwareTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Map<String, String> contextMap = MDC.getCopyOfContextMap();
		IServiceContext serviceContext = ServiceContextHolder.getContext();
		TraceContext traceContext = TraceContextHolder.get().orElse(null);
		//SecureContext secureContext = SecureContextStore.getContext().get();
		SecureContext secureContext = SecureContextStore.getContext().orElse(null);
		
		Map<String, Object> threadLocalStoreMap = ThreadLocalStore.getInstance().getMap();
		
		
		return () -> {
			RequestAttributes previousRequestAttributes = RequestContextHolder.getRequestAttributes();
			Map<String, String> previousMDC = MDC.getCopyOfContextMap();
			IServiceContext previousServiceContext = ServiceContextHolder.getContext();
			TraceContext previousTraceContext = TraceContextHolder.get().orElse(null);
			//SecureContext secureContext = SecureContextStore.getContext().get();
			SecureContext previousSecureContext = SecureContextStore.getContext().orElse(null);
			Map<String, Object> previousThreadLocalStoreMap = ThreadLocalStore.getInstance().getMap();
			
			
			try {
				if (requestAttributes != null) {
					RequestContextHolder.setRequestAttributes(requestAttributes);
				}
				
				if (contextMap != null) {
					MDC.setContextMap(contextMap);
				} else {
					MDC.clear();
				}
				
				if (serviceContext != null) {
					ServiceContextHolder.setContext(serviceContext);
				}
				
				if (traceContext != null) {
					TraceContextHolder.set(traceContext);
				}
				
				//secureContext
				if (secureContext != null) {
					SecureContextStore.setContext(secureContext);
				}
				
				if (threadLocalStoreMap != null) {
					ThreadLocalStore.getInstance().setMap(threadLocalStoreMap);
				}
				
				runnable.run();
			} finally {
				if (previousRequestAttributes != null) {
					RequestContextHolder.setRequestAttributes(previousRequestAttributes);
				} else {
					RequestContextHolder.resetRequestAttributes();
				}
				
				if (previousMDC != null) {
					MDC.setContextMap(previousMDC);
				} else {
					MDC.clear();
				}
				
				if (previousServiceContext != null) {
					ServiceContextHolder.setContext(previousServiceContext);
				} else {
					ServiceContextHolder.clear();
				}
				
				if (previousTraceContext != null) {
					TraceContextHolder.set(previousTraceContext);
				} else {
					TraceContextHolder.clear();
				}
				 
				if (previousSecureContext != null) {
					SecureContextStore.setContext(previousSecureContext);
				} else {
					SecureContextStore.clearContext();
				}
				
				if (previousThreadLocalStoreMap != null) {
					ThreadLocalStore.getInstance().setMap(previousThreadLocalStoreMap);
				} else {
					ThreadLocalStore.getInstance().clearThreadLocalStore();
				}
			}
		};
	}
}
