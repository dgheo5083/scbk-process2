package com.scbank.process.api.fw.base.integration.constant;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Generated unit test for {@link IntegrationConstant}.
 */
class IntegrationConstantTest {

    @Test
    void constantsAreExposed() {
        assertThat(new IntegrationConstant()).isNotNull();
        assertThat(IntegrationConstant.SYSTEM_ID_HOST).isEqualTo("host");
        assertThat(IntegrationConstant.SYSTEM_ID_MCI).isEqualTo("mci");
        assertThat(IntegrationConstant.SYSTEM_ID_EDMI).isEqualTo("edmi");
        assertThat(IntegrationConstant.DEFAULT_MCI_MAX_LOOP_CNT).isEqualTo(10);
        assertThat(IntegrationConstant.DEFAULT_MCI_LIST_FIELD_NAME).isEqualTo("FO_PAY");
        assertThat(IntegrationConstant.KR_SMB_VAN_TYPE).isEqualTo("57");
    }
}
