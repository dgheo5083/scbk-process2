package com.scbank.process.api.fw.base.integration.system.oltp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;

/**
 * Generated unit test for {@link OltpRequest}.
 */
class OltpRequestTest {

    @Test
    void valueObjectContract() {
        OltpRequest<OltpCommon> a = OltpRequest.<OltpCommon>builder().build();
        OltpRequest<OltpCommon> b = OltpRequest.<OltpCommon>builder().build();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
