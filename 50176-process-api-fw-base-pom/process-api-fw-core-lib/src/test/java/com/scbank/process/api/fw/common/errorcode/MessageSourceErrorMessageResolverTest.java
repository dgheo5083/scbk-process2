package com.scbank.process.api.fw.common.errorcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;

import com.scbank.process.api.fw.core.error.IErrorMessage;

/**
 * {@link MessageSourceErrorMessageResolver} 단위 테스트.
 */
class MessageSourceErrorMessageResolverTest {

    @Test
    @DisplayName("MessageSource 로 해석한 메시지를 담은 IErrorMessage 를 반환")
    void resolvesMessage() {
        MessageSource messageSource = mock(MessageSource.class);
        Object[] args = { "arg1" };
        when(messageSource.getMessage(eq("E001"), any(), eq(Locale.KOREAN))).thenReturn("해석된 메시지");

        MessageSourceErrorMessageResolver resolver = new MessageSourceErrorMessageResolver(messageSource);

        IErrorMessage result = resolver.resolveMessage("E001", args, Locale.KOREAN);

        assertThat(result.getErrorCode()).isEqualTo("E001");
        assertThat(result.getErrorMessage()).isEqualTo("해석된 메시지");
    }
}
