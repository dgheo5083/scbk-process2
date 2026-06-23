package com.scbank.process.api.fw.base.integration.interceptors.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.enums.MessageFormat;

/**
 * Generated unit test for {@link IntegrationJacksonLoggingInterceptor}.
 */
class IntegrationJacksonLoggingInterceptorTest {

    private final IntegrationJacksonLoggingInterceptor interceptor =
            new IntegrationJacksonLoggingInterceptor(mock(MessageContextFactory.class));

    @Test
    void beforeAndAfterAreNoOps() {
        IntegrationContext context = mock(IntegrationContext.class);
        assertThatCode(() -> {
            interceptor.before(context, new Object());
            interceptor.after(context, new Object(), new Object());
        }).doesNotThrowAnyException();
    }

    @Test
    void isSupportedForJsonFormat() {
        IntegrationContext context = mock(IntegrationContext.class);
        MessageContext messageContext = mock(MessageContext.class);
        lenient().when(context.getAttribute(eq("_MESSAGE_CTX_"), eq(MessageContext.class))).thenReturn(messageContext);
        lenient().when(messageContext.getFormat()).thenReturn(MessageFormat.JSON);

        assertThat(interceptor.isSupported(context)).isTrue();
    }
}
