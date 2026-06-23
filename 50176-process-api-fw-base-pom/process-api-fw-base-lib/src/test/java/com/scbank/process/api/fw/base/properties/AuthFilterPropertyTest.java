package com.scbank.process.api.fw.base.properties;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link AuthFilterProperty}.
 */
class AuthFilterPropertyTest {

    @Test
    void valueObjectContract() {
        AuthFilterProperty a = new AuthFilterProperty();
        AuthFilterProperty b = new AuthFilterProperty();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
