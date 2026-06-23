package com.scbank.process.api.fw.base.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * Generated unit test for {@link CSLServiceException} covering every constructor.
 */
@SuppressWarnings("deprecation")
class CSLServiceExceptionTest {

    @Test
    void causeConstructor() {
        CSLServiceException ex = new CSLServiceException(new IllegalStateException("boom"));
        assertThat(ex).isInstanceOf(FrameworkRuntimeException.class);
    }

    @Test
    void frameworkErrorCodeConstructor() {
        FrameworkErrorCode errorCode = mock(FrameworkErrorCode.class);
        lenient().when(errorCode.getCode()).thenReturn("E001");
        CSLServiceException ex = new CSLServiceException(errorCode);
        assertThat(ex).isNotNull();
    }

    @Test
    void errorCodeConstructor() {
        CSLServiceException ex = new CSLServiceException("E001");
        assertThat(ex).isNotNull();
    }

    @Test
    void errorCodeAndMessageConstructor() {
        CSLServiceException ex = new CSLServiceException("E001", "message");
        assertThat(ex).isNotNull();
    }

    @Test
    void errorCodeAndCauseConstructor() {
        CSLServiceException ex = new CSLServiceException("E001", new RuntimeException());
        assertThat(ex).isNotNull();
    }

    @Test
    void errorCodeMessageAndCauseConstructor() {
        CSLServiceException ex = new CSLServiceException("E001", "message", new RuntimeException());
        assertThat(ex).isNotNull();
    }

    @Test
    void errorCodeAndMessageArgsConstructor() {
        List<Object> args = new ArrayList<>();
        args.add("arg");
        CSLServiceException ex = new CSLServiceException("E001", args);
        assertThat(ex).isNotNull();
    }

    @Test
    void errorCodeMessageArgsAndCauseConstructor() {
        List<Object> args = new ArrayList<>();
        args.add("arg");
        CSLServiceException ex = new CSLServiceException("E001", args, new RuntimeException());
        assertThat(ex).isNotNull();
    }
}
