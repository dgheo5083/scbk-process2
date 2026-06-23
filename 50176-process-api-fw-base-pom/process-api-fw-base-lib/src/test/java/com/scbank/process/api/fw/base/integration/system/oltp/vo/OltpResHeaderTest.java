package com.scbank.process.api.fw.base.integration.system.oltp.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link OltpResHeader}.
 */
class OltpResHeaderTest {

    @Test
    void valueObjectContract() {
        OltpResHeader a = new OltpResHeader();
        OltpResHeader b = new OltpResHeader();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
