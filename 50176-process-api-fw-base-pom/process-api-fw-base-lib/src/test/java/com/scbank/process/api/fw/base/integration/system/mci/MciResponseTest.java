package com.scbank.process.api.fw.base.integration.system.mci;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo;

/**
 * Generated unit test for {@link MciResponse}.
 */
class MciResponseTest {

    @Test
    void valueObjectContract() {
        MciResponse<MciMsgInfo> a = new MciResponse<>();
        MciResponse<MciMsgInfo> b = new MciResponse<>();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
