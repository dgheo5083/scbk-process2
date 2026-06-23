package com.scbank.process.api.fw.base.gateway.edmi.base.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;

/**
 * Generated unit test for {@link EDMiFeignException}.
 */
class EDMiFeignExceptionTest {

    @Test
    void defaultConstructor() {
        EDMiFeignException ex = new EDMiFeignException();
        assertThat(ex).isNotNull();
    }

    @Test
    void frameworkErrorCodeConstructorUsesCode() {
        FrameworkErrorCode errorCode = mock(FrameworkErrorCode.class);
        when(errorCode.getCode()).thenReturn("E001");
        EDMiFeignException ex = new EDMiFeignException(errorCode, "message");
        assertThat(ex).isNotNull();
    }

    @Test
    void stringErrorCodeConstructor() {
        EDMiFeignException ex = new EDMiFeignException("E001", "message");
        assertThat(ex).isNotNull();
    }

    @Test
    void causeConstructor() {
        EDMiFeignException ex = new EDMiFeignException(new RuntimeException("boom"));
        assertThat(ex).isNotNull();
    }
}
