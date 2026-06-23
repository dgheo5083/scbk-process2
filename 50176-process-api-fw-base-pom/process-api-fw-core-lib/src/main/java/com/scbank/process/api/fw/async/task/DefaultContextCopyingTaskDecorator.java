package com.scbank.process.api.fw.async.task;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;

/**
 * 프레임워크 ContextCopyingTaskDecorator 클래스
 */
public class DefaultContextCopyingTaskDecorator implements TaskDecorator {

	//TODO SecureContext 어쩔겨...
	
	@Override
	public Runnable decorate(Runnable runnable) {
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Map<String, String> contextMap = MDC.getCopyOfContextMap();
		IServiceContext serviceContext = ServiceContextHolder.getContext();
		
		return () -> {
			RequestAttributes previousRequestAttributes = RequestContextHolder.getRequestAttributes();
			Map<String, String> previousMDC = MDC.getCopyOfContextMap();
			IServiceContext previousServiceContext = ServiceContextHolder.getContext();
			
			try {
				if (requestAttributes != null) {
					RequestContextHolder.setRequestAttributes(requestAttributes);
				}
				
				if (contextMap != null) {
					MDC.setContextMap(contextMap);
				} else {
					MDC.clear();
				}
				
				ServiceContextHolder.setContext(serviceContext);
				
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
					ServiceContextHolder.clear();
				}
			}
		};
	}
}
