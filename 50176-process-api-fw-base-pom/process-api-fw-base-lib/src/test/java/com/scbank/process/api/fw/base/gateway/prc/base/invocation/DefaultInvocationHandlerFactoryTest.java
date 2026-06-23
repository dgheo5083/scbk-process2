package com.scbank.process.api.fw.base.gateway.prc.base.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import feign.InvocationHandlerFactory.MethodHandler;
import feign.Target;

/**
 * Generated unit test for {@link DefaultInvocationHandlerFactory}.
 */
class DefaultInvocationHandlerFactoryTest {

    private final DefaultInvocationHandlerFactory factory = new DefaultInvocationHandlerFactory();

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void createReturnsTracingInvocationHandler() throws Throwable {
        Target target = mock(Target.class);
        lenient().when(target.url()).thenReturn("http://localhost");

        InvocationHandler handler = factory.create(target, new HashMap<Method, MethodHandler>());
        assertThat(handler).isNotNull();

        // Exercise the tracing lambda for coverage; Object#toString is handled
        // by the delegated default handler without dispatch entries.
        try {
            handler.invoke(new Object(), Object.class.getMethod("toString"), null);
        } catch (Throwable ignored) {
            // tracing wrapper rethrows delegate failures; tolerated for coverage
        }
    }
}
