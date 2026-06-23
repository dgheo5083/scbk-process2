package com.scbank.process.api.fw.base.integration.system.edmi.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link EdmiSystemHeader}.
 */
class EdmiSystemHeaderTest {

    @Test
    void valueObjectContract() {
        EdmiSystemHeader a = new EdmiSystemHeader();
        EdmiSystemHeader b = new EdmiSystemHeader();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
