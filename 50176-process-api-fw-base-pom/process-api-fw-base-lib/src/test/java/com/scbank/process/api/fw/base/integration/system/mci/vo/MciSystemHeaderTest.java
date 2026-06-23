package com.scbank.process.api.fw.base.integration.system.mci.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link MciSystemHeader}.
 */
class MciSystemHeaderTest {

    @Test
    void valueObjectContract() {
        MciSystemHeader a = new MciSystemHeader();
        MciSystemHeader b = new MciSystemHeader();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
