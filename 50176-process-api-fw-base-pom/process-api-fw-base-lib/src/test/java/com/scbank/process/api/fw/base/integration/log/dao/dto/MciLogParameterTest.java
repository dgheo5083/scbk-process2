package com.scbank.process.api.fw.base.integration.log.dao.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link MciLogParameter}.
 */
class MciLogParameterTest {

    @Test
    void valueObjectContract() {
        MciLogParameter a = MciLogParameter.builder().build();
        MciLogParameter b = MciLogParameter.builder().build();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
