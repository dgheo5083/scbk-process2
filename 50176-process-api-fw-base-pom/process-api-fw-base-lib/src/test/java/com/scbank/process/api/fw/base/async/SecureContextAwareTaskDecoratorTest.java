package com.scbank.process.api.fw.base.async;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
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
 * Generated unit test for {@link SecureContextAwareTaskDecorator}.
 */
class SecureContextAwareTaskDecoratorTest {

    private final SecureContextAwareTaskDecorator decorator = new SecureContextAwareTaskDecorator();

    @AfterEach
    void tearDown() {
        ServiceContextHolder.clear();
        TraceContextHolder.clear();
        SecureContextStore.clearContext();
        RequestContextHolder.resetRequestAttributes();
        MDC.clear();
        ThreadLocalStore.getInstance().clearThreadLocalStore();
    }

    @Test
    void decoratesAndRunsWithEmptyContext() {
        Runnable runnable = mock(Runnable.class);

        Runnable decorated = decorator.decorate(runnable);
        assertThat(decorated).isNotNull();
        decorated.run();

        verify(runnable).run();
    }

    @Test
    void decoratesAndRunsWithPopulatedContext() {
        ServiceContextHolder.setContext(mock(IServiceContext.class));
        TraceContextHolder.set(mock(TraceContext.class));
        SecureContextStore.setContext(new SecureContext());
        RequestContextHolder.setRequestAttributes(mock(RequestAttributes.class));
        MDC.put("traceId", "abc");
        ThreadLocalStore.getInstance().setValue("k", "v");

        Runnable runnable = mock(Runnable.class);
        Runnable decorated = decorator.decorate(runnable);
        decorated.run();

        verify(runnable).run();
    }
}
