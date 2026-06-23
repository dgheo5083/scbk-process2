package com.scbank.process.api.fw.base.integration.system.mci.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link MciReqHeader}.
 */
class MciReqHeaderTest {

    @Test
    void valueObjectContract() {
        MciReqHeader a = MciReqHeader.builder().build();
        MciReqHeader b = MciReqHeader.builder().build();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
