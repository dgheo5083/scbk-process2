package com.scbank.process.api.fw.base.channel.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.errorcode.IErrorCodeInfo;
import com.scbank.process.api.fw.common.errorcode.IErrorCodeManager;
import com.scbank.process.api.fw.common.errorcode.IErrorGuideMessageInfo;
import com.scbank.process.api.fw.core.error.IErrorMessage;

/**
 * Generated unit test for {@link DefaultErrorMessageResolver}.
 */
class DefaultErrorMessageResolverTest {

    private final IErrorCodeManager errorCodeManager = mock(IErrorCodeManager.class);
    private final DefaultErrorMessageResolver resolver = new DefaultErrorMessageResolver(errorCodeManager);

    @Test
    void returnsEmptyMessageWhenErrorCodeUnknown() {
        when(errorCodeManager.getErrorCodeInfo(eq("E000"), any())).thenReturn(null);

        IErrorMessage message = resolver.resolveMessage("E000", null, Locale.KOREAN);

        assertThat(message.getErrorCode()).isEqualTo("E000");
        assertThat(message.getErrorMessage()).isEmpty();
    }

    @Test
    void formatsMessageWithArgsAndGuides() {
        IErrorGuideMessageInfo guide = mock(IErrorGuideMessageInfo.class);
        lenient().when(guide.getMessage()).thenReturn("guide-1");

        IErrorCodeInfo info = mock(IErrorCodeInfo.class);
        when(info.getMessage()).thenReturn("Hello {0}");
        when(info.getErrorGuideMessages()).thenReturn(List.of(guide));
        when(errorCodeManager.getErrorCodeInfo(eq("E001"), any())).thenReturn(info);

        IErrorMessage message = resolver.resolveMessage("E001", new Object[] { "World" }, Locale.ENGLISH);

        assertThat(message.getErrorMessage()).isEqualTo("Hello World");
        assertThat(message.getErrorGuideMessages()).containsExactly("guide-1");
    }
}
