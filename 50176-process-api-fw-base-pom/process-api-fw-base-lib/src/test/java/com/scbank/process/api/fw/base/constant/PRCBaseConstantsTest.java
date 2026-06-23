package com.scbank.process.api.fw.base.constant;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Generated unit test for {@link PRCBaseConstants}.
 */
class PRCBaseConstantsTest {

    @Test
    void constantsAreExposed() {
        assertThat(new PRCBaseConstants()).isNotNull();
        assertThat(PRCBaseConstants.SUCCESS_RES_CODE).isEqualTo("00");
        assertThat(PRCBaseConstants.FAILED_RES_CODE).isEqualTo("99");
        assertThat(PRCBaseConstants.CSL_SESSION_ID_NAME).isEqualTo("X-Auth-Token");
        assertThat(PRCBaseConstants.CHANNEL_NAME).isEqualTo("channel");
        assertThat(PRCBaseConstants.LANGUAGE_HEADER_NAME).isEqualTo("Accept-Language");
        assertThat(PRCBaseConstants.X_DEVICE_UUID).isEqualTo("X-DEVICE-UUID");
    }
}
