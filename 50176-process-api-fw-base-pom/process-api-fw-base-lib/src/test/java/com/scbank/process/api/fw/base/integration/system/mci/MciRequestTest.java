package com.scbank.process.api.fw.base.integration.system.mci;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo;

/**
 * Generated unit test for {@link MciRequest}.
 */
class MciRequestTest {

    @Test
    void valueObjectContract() {
        MciRequest<MciMsgInfo> a = new MciRequest<>();
        MciRequest<MciMsgInfo> b = new MciRequest<>();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
