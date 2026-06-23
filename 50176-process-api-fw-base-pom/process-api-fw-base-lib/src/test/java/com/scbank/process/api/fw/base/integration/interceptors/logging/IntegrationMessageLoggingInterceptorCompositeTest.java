package com.scbank.process.api.fw.base.integration.interceptors.logging;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.integration.context.IntegrationContext;

/**
 * Generated unit test for {@link IntegrationMessageLoggingInterceptorComposite}.
 */
class IntegrationMessageLoggingInterceptorCompositeTest {

    private final IntegrationContext context = mock(IntegrationContext.class);
    private final AbstractIntegrationLoggingInterceptor supported =
            mock(AbstractIntegrationLoggingInterceptor.class);
    private final AbstractIntegrationLoggingInterceptor unsupported =
            mock(AbstractIntegrationLoggingInterceptor.class);

    private IntegrationMessageLoggingInterceptorComposite composite() {
        when(supported.isSupported(context)).thenReturn(true);
        when(unsupported.isSupported(context)).thenReturn(false);
        return new IntegrationMessageLoggingInterceptorComposite(
                Arrays.asList(null, unsupported, supported));
    }

    @Test
    void beforeInvokesOnlySupportedInterceptors() {
        Object request = new Object();
        composite().before(context, request);

        verify(supported).before(context, request);
        verify(unsupported, never()).before(context, request);
    }

    @Test
    void afterInvokesOnlySupportedInterceptors() {
        Object request = new Object();
        Object response = new Object();
        composite().after(context, request, response);

        verify(supported).after(context, request, response);
        verify(unsupported, never()).after(context, request, response);
    }
}
