package com.scbank.process.api.fw.base.integration.log;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link IntegrationLogCollectEvent}.
 */
class IntegrationLogCollectEventTest {

    @Test
    void valueObjectContract() {
        IntegrationLogCollectEvent a = IntegrationLogCollectEvent.builder().build();
        IntegrationLogCollectEvent b = IntegrationLogCollectEvent.builder().build();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
