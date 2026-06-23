package com.scbank.process.api.fw.base.integration.system.mci;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Generated unit test for {@link MciRequestOptions}.
 */
class MciRequestOptionsTest {

    @Test
    void instantiatesAndIsSelfConsistent() {
        MciRequestOptions a = new MciRequestOptions();
        assertThat(a).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a.toString()).isNotNull();
        a.hashCode();
    }
}
