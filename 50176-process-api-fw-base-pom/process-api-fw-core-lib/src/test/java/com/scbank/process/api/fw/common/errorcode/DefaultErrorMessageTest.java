package com.scbank.process.api.fw.common.errorcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link DefaultErrorMessage} 단위 테스트.
 */
class DefaultErrorMessageTest {

    @Test
    @DisplayName("builder 및 getter 동작")
    void builderAndAccessors() {
        DefaultErrorMessage message = DefaultErrorMessage.builder()
                .errorCode("E001")
                .errorMessage("오류 발생")
                .errorGuideMessages(List.of("guide1", "guide2"))
                .build();

        assertThat(message.getErrorCode()).isEqualTo("E001");
        assertThat(message.getErrorMessage()).isEqualTo("오류 발생");
        assertThat(message.getErrorGuideMessages()).containsExactly("guide1", "guide2");
    }

    @Test
    @DisplayName("setter 및 equals/hashCode 동작")
    void settersAndEquality() {
        DefaultErrorMessage a = DefaultErrorMessage.builder().build();
        a.setErrorCode("E");
        a.setErrorMessage("m");
        DefaultErrorMessage b = DefaultErrorMessage.builder().errorCode("E").errorMessage("m").build();

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a.toString()).contains("DefaultErrorMessage");
    }
}
