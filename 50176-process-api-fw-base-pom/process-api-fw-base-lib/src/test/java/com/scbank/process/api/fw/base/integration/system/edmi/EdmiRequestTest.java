package com.scbank.process.api.fw.base.integration.system.edmi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiMsgInfo;

/**
 * Generated unit test for {@link EdmiRequest}.
 */
class EdmiRequestTest {

    @Test
    void valueObjectContract() {
        EdmiRequest<EdmiMsgInfo> a = EdmiRequest.<EdmiMsgInfo>builder().build();
        EdmiRequest<EdmiMsgInfo> b = EdmiRequest.<EdmiMsgInfo>builder().build();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
