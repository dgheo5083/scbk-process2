 package com.scbank.process.api.fw.async.task;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.scbank.process.api.fw.channel.context.ServiceContextHolder;

class DefaultContextCopyingTaskDecoratorTest {

	@AfterEach
	void clear() {
		MDC.clear();
		RequestContextHolder.resetRequestAttributes();
		ServiceContextHolder.clear();
	}
	
	@Test
	void decorator_shouldRunRunnable() {
		DefaultContextCopyingTaskDecorator decorator =  new DefaultContextCopyingTaskDecorator();
		
		AtomicBoolean executed = new AtomicBoolean(false);
		
		Runnable decorated = decorator.decorate(() -> executed.set(true));
		decorated.run();
		
		assertTrue(executed.get());
	}
	
	@Test
	void decorate_whenNoMdc_thenClearMdc() {
		DefaultContextCopyingTaskDecorator decorator =  new DefaultContextCopyingTaskDecorator();
		
		Runnable decorated = decorator.decorate(() -> {
			//assertNull(MDC.getCopyOfContextMap());
			assertEquals(MDC.getCopyOfContextMap().size(), 0);
		});
		
		MDC.put("traceId", "OLD");
		
		decorated.run();
		
		assertEquals(MDC.getCopyOfContextMap().size(), 1);
	}
	
	@Test
	void decorate_shouldCopyRequestAttributes() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		
		RequestContextHolder.setRequestAttributes(attrs);
		
		DefaultContextCopyingTaskDecorator decorator = new DefaultContextCopyingTaskDecorator();
		
		Runnable decorated = decorator.decorate(() -> {
			assertSame(attrs, RequestContextHolder.getRequestAttributes());
		});
		
		RequestContextHolder.resetRequestAttributes();
		
		decorated.run();
	}

}
