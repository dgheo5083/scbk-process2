package com.scbank.process.api.fw.base.integration.system.oltp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Generated unit test for {@link OltpRequestOptions}.
 */
class OltpRequestOptionsTest {

    @Test
    void instantiatesAndIsSelfConsistent() {
        OltpRequestOptions a = new OltpRequestOptions();
        assertThat(a).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a.toString()).isNotNull();
        a.hashCode();
    }
}
