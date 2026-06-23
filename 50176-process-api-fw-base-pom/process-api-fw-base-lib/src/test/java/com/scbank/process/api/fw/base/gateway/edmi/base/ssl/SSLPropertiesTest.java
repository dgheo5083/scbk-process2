package com.scbank.process.api.fw.base.gateway.edmi.base.ssl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link SSLProperties}.
 */
class SSLPropertiesTest {

    @Test
    void valueObjectContract() {
        SSLProperties a = new SSLProperties();
        SSLProperties b = new SSLProperties();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
