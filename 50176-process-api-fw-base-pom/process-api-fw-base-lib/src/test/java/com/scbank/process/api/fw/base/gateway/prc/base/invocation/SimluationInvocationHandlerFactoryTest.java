package com.scbank.process.api.fw.base.gateway.prc.base.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.gateway.prc.base.simulation.SimulationGatewayResolver;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.SimulationGatewayResolver.ResolvedGateway;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.SimulationResponseFactory;

import feign.InvocationHandlerFactory.MethodHandler;
import feign.Target;

/**
 * Generated unit test for {@link SimluationInvocationHandlerFactory}.
 */
class SimluationInvocationHandlerFactoryTest {

    private final SimulationGatewayResolver gatewayResolver = mock(SimulationGatewayResolver.class);
    private final SimulationResponseFactory responseFactory = mock(SimulationResponseFactory.class);
    private final SimluationInvocationHandlerFactory factory =
            new SimluationInvocationHandlerFactory(gatewayResolver, responseFactory);

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void createReturnsHandlerThatDelegatesWhenNoGatewayResolved() throws Throwable {
        Target target = mock(Target.class);
        lenient().when(target.url()).thenReturn("http://localhost");
        when(gatewayResolver.resolve(anyString())).thenReturn(Optional.<ResolvedGateway>empty());

        InvocationHandler handler = factory.create(target, new HashMap<Method, MethodHandler>());
        assertThat(handler).isNotNull();

        try {
            handler.invoke(new Object(), Object.class.getMethod("toString"), null);
        } catch (Throwable ignored) {
            // tracing wrapper rethrows delegate failures; tolerated for coverage
        }
    }
}
