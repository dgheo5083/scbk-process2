package com.scbank.process.api.fw.base.integration.system.edmi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiMsgInfo;

/**
 * Generated unit test for {@link EdmiResponse}.
 */
class EdmiResponseTest {

    @Test
    void valueObjectContract() {
        EdmiResponse<EdmiMsgInfo> a = new EdmiResponse<>();
        EdmiResponse<EdmiMsgInfo> b = new EdmiResponse<>();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
