package com.scbank.process.api.fw.base.gateway.edmi.base.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link EDMiResponseMessage}.
 */
class EDMiResponseMessageTest {

    @Test
    void valueObjectContract() {
        EDMiResponseMessage a = EDMiResponseMessage.builder().build();
        EDMiResponseMessage b = EDMiResponseMessage.builder().build();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
