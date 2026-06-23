package com.scbank.process.api.fw.base.channel.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import com.scbank.process.api.fw.base.channel.dto.PRCResponseHeader;

/**
 * Generated unit test for {@link PRCResponseMessage}.
 */
class PRCResponseMessageTest {

    @Test
    void valueObjectContract() {
        PRCResponseMessage<PRCResponseHeader> a = PRCResponseMessage.<PRCResponseHeader>builder().build();
        PRCResponseMessage<PRCResponseHeader> b = PRCResponseMessage.<PRCResponseHeader>builder().build();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
