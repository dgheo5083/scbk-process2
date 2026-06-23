package com.scbank.process.api.fw.base.gateway.edmi.base.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link EDMiRequestMessage}.
 */
class EDMiRequestMessageTest {

    @Test
    void valueObjectContract() {
        EDMiRequestMessage a = EDMiRequestMessage.builder().build();
        EDMiRequestMessage b = EDMiRequestMessage.builder().build();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
