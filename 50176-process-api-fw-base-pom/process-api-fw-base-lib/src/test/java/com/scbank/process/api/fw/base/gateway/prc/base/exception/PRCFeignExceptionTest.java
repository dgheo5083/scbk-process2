package com.scbank.process.api.fw.base.gateway.prc.base.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;

/**
 * Generated unit test for {@link PRCFeignException}.
 */
class PRCFeignExceptionTest {

    @Test
    void defaultConstructor() {
        PRCFeignException ex = new PRCFeignException();
        assertThat(ex).isNotNull();
    }

    @Test
    void frameworkErrorCodeConstructorUsesCode() {
        FrameworkErrorCode errorCode = mock(FrameworkErrorCode.class);
        when(errorCode.getCode()).thenReturn("E001");
        PRCFeignException ex = new PRCFeignException(errorCode, "message");
        assertThat(ex).isNotNull();
    }

    @Test
    void stringErrorCodeConstructor() {
        PRCFeignException ex = new PRCFeignException("E001", "message");
        assertThat(ex).isNotNull();
    }

    @Test
    void causeConstructor() {
        PRCFeignException ex = new PRCFeignException(new RuntimeException("boom"));
        assertThat(ex).isNotNull();
    }
}
