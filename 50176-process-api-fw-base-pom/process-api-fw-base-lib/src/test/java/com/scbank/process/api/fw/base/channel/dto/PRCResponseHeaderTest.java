package com.scbank.process.api.fw.base.channel.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link PRCResponseHeader}.
 */
class PRCResponseHeaderTest {

    @Test
    void valueObjectContract() {
        PRCResponseHeader a = PRCResponseHeader.builder().build();
        PRCResponseHeader b = PRCResponseHeader.builder().build();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
