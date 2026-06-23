package com.scbank.process.api.fw.base.integration.system.edmi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Generated unit test for {@link EdmiRequestOptions}.
 */
class EdmiRequestOptionsTest {

    @Test
    void instantiatesAndIsSelfConsistent() {
        EdmiRequestOptions a = new EdmiRequestOptions();
        assertThat(a).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a.toString()).isNotNull();
        a.hashCode();
    }
}
