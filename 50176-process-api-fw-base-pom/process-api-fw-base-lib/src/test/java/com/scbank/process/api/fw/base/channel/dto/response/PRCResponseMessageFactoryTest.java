package com.scbank.process.api.fw.base.channel.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.core.error.IErrorMessageResolver;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;

import jakarta.validation.ConstraintViolationException;

/**
 * Generated unit test for {@link PRCResponseMessageFactory}.
 */
class PRCResponseMessageFactoryTest {

    private final IErrorMessageResolver errorMessageResolver = mock(IErrorMessageResolver.class);
    private final PRCResponseMessageFactory<IMessageObject> factory =
            new PRCResponseMessageFactory<>(errorMessageResolver);

    @Test
    void okBuildsSuccessResponse() {
        IMessageObject payload = mock(IMessageObject.class);

        try (MockedStatic<ServiceContextHolder> sch = mockStaticServiceContext()) {
            sch.when(ServiceContextHolder::getContext).thenReturn(null);

            PRCResponseMessage<IMessageObject> message = factory.ok(payload);

            assertThat(message.getHeader().getResCode()).isEqualTo("00");
            assertThat(message.getBody()).isSameAs(payload);
        }
    }

    @Test
    void failBuildsErrorResponse() {
        lenient().when(errorMessageResolver.resolveMessage(any(), any(), any())).thenReturn(null);

        try (MockedStatic<ServiceContextHolder> sch = mockStaticServiceContext();
                MockedStatic<RuntimeContext> rc = org.mockito.Mockito.mockStatic(RuntimeContext.class)) {
            sch.when(ServiceContextHolder::getContext).thenReturn(null);
            rc.when(RuntimeContext::getDefaultLocale).thenReturn("ko");
            rc.when(RuntimeContext::getDefaultErrorCode).thenReturn("E000");
            rc.when(RuntimeContext::getDefaultErrorMessage).thenReturn("default-error");

            PRCResponseMessage<IMessageObject> message = factory.fail(new RuntimeException("boom"));

            assertThat(message.getHeader().getResCode()).isEqualTo("99");
            assertThat(message.getHeader().getErrorCode()).isEqualTo("E000");
            assertThat(message.getHeader().getErrorMessage()).isEqualTo("boom");
        }
    }

    @Test
    void failWithConstraintViolationIsUnsupported() {
        assertThatThrownBy(() -> factory.fail(mock(ConstraintViolationException.class)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    private MockedStatic<ServiceContextHolder> mockStaticServiceContext() {
        return org.mockito.Mockito.mockStatic(ServiceContextHolder.class);
    }
}
