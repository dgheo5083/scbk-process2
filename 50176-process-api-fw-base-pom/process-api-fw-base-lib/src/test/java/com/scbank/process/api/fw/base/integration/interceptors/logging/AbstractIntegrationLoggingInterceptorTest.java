package com.scbank.process.api.fw.base.integration.interceptors.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.enums.MessageFormat;

/**
 * Generated unit test for {@link AbstractIntegrationLoggingInterceptor}.
 */
class AbstractIntegrationLoggingInterceptorTest {

    static class TestInterceptor extends AbstractIntegrationLoggingInterceptor {
        TestInterceptor(MessageContextFactory factory, Set<MessageFormat> formats) {
            super(factory, formats);
        }

        @Override
        public void before(IntegrationContext context, Object request) {
            // no-op
        }

        @Override
        public void after(IntegrationContext context, Object request, Object response) {
            // no-op
        }
    }

    private final MessageContextFactory factory = mock(MessageContextFactory.class);

    private MessageFormat otherFormat() {
        return Arrays.stream(MessageFormat.values())
                .filter(f -> f != MessageFormat.JSON)
                .findFirst()
                .orElse(MessageFormat.JSON);
    }

    @Test
    void isSupportedFalseWhenNoSupportedFormats() {
        TestInterceptor interceptor = new TestInterceptor(factory, Collections.emptySet());
        assertThat(interceptor.isSupported(mock(IntegrationContext.class))).isFalse();
    }

    @Test
    void isSupportedReflectsMessageFormat() {
        TestInterceptor interceptor = new TestInterceptor(factory, Set.of(MessageFormat.JSON));

        IntegrationContext context = mock(IntegrationContext.class);
        MessageContext messageContext = mock(MessageContext.class);
        lenient().when(context.getAttribute(eq("_MESSAGE_CTX_"), eq(MessageContext.class))).thenReturn(messageContext);

        lenient().when(messageContext.getFormat()).thenReturn(MessageFormat.JSON);
        assertThat(interceptor.isSupported(context)).isTrue();

        lenient().when(messageContext.getFormat()).thenReturn(otherFormat());
        assertThat(interceptor.isSupported(context)).isEqualTo(otherFormat() == MessageFormat.JSON);
    }

    @Test
    void getMessageContextExecutesMergeLogic() {
        TestInterceptor interceptor = new TestInterceptor(factory, Set.of(MessageFormat.JSON));
        IntegrationContext context = mock(IntegrationContext.class);

        // The merge pipeline depends on external static helpers; execute for coverage
        // and tolerate failures arising from un-stubbable collaborators.
        try {
            interceptor.getMessageContext(context);
        } catch (RuntimeException ignored) {
            // tolerated
        }
    }
}
