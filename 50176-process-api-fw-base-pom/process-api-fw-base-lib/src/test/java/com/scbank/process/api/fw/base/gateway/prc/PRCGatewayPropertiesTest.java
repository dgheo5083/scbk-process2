package com.scbank.process.api.fw.base.gateway.prc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link PRCGatewayProperties}.
 */
class PRCGatewayPropertiesTest {

    @Test
    void valueObjectContract() {
        PRCGatewayProperties a = new PRCGatewayProperties();
        PRCGatewayProperties b = new PRCGatewayProperties();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
