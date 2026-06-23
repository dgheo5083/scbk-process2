package com.scbank.process.api.fw.security.xss.processor.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.security.xss.processor.IXssProtectionProcessor;

import jakarta.servlet.ServletRequest;

/**
 * {@link XssProtectionProcessorComposite} 단위 테스트.
 */
class XssProtectionProcessorCompositeTest {

    @Test
    @DisplayName("Content-Type 을 지원하는 첫 Processor 에 위임한다")
    void delegatesToSupportingProcessor() throws Exception {
        IXssProtectionProcessor notSupporting = mock(IXssProtectionProcessor.class);
        IXssProtectionProcessor supporting = mock(IXssProtectionProcessor.class);
        ServletRequest request = mock(ServletRequest.class);
        ServletRequest sanitized = mock(ServletRequest.class);

        when(request.getContentType()).thenReturn("application/json");
        when(notSupporting.supports("application/json")).thenReturn(false);
        when(supporting.supports("application/json")).thenReturn(true);
        when(supporting.sanitize(request)).thenReturn(sanitized);

        XssProtectionProcessorComposite composite =
                new XssProtectionProcessorComposite(List.of(notSupporting, supporting));

        assertThat(composite.sanitize(request)).isSameAs(sanitized);
        verify(notSupporting, never()).sanitize(request);
    }

    @Test
    @DisplayName("지원하는 Processor 가 없으면 IllegalStateException 을 던진다")
    void throwsWhenNoProcessorSupports() {
        IXssProtectionProcessor processor = mock(IXssProtectionProcessor.class);
        ServletRequest request = mock(ServletRequest.class);
        when(request.getContentType()).thenReturn("application/unknown");
        when(processor.supports("application/unknown")).thenReturn(false);

        XssProtectionProcessorComposite composite =
                new XssProtectionProcessorComposite(List.of(processor));

        assertThatThrownBy(() -> composite.sanitize(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("application/unknown");
    }
}
