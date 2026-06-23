package com.scbank.process.api.fw.base.integration.system.oltp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpError;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;

/**
 * Generated unit test for {@link OltpUtils}.
 */
class OltpUtilsTest {

    @SuppressWarnings("unchecked")
    private OltpResponse<OltpCommon> response() {
        return mock(OltpResponse.class);
    }

    @Test
    void errorAccessorsReturnEmptyForSuccessfulResponse() {
        OltpResponse<OltpCommon> response = response();
        when(response.isError()).thenReturn(false);

        assertThat(OltpUtils.getErrorCode(response)).isEmpty();
        assertThat(OltpUtils.getErrorModule(response)).isEmpty();
        assertThat(OltpUtils.getErrMsg1(response)).isEmpty();
        assertThat(OltpUtils.getErrMsg2(response)).isEmpty();
        assertThat(OltpUtils.getWrnMsg1(response)).isEmpty();
        assertThat(OltpUtils.getWrnMsg2(response)).isEmpty();
        assertThat(OltpUtils.getWrnMsg3(response)).isEmpty();
        assertThat(OltpUtils.isCapAbendError(response)).isFalse();
    }

    @Test
    void errorAccessorsReadFromErrorResponse() {
        OltpResponse<OltpCommon> response = response();
        when(response.isError()).thenReturn(true);

        OltpResHeader header = new OltpResHeader();
        OltpCommon common = new OltpCommon();
        common.setResponseCode("E01");
        header.setOltpCommon(common);
        lenient().when(response.getHeader()).thenReturn(header);

        OltpError error = new OltpError();
        error.setSHostErrorModule("MOD");
        error.setErrMsg1("m1");
        error.setErrMsg2("m2");
        error.setWrnMsg1("w1");
        error.setWrnMsg2("w2");
        error.setWrnMsg3("w3");
        when(response.getErrorResponse()).thenReturn(error);

        assertThat(OltpUtils.getErrorCode(response)).isEqualTo("E01");
        assertThat(OltpUtils.getErrorModule(response)).isEqualTo("MOD");
        assertThat(OltpUtils.getErrMsg1(response)).isEqualTo("m1");
        assertThat(OltpUtils.getErrMsg2(response)).isEqualTo("m2");
        assertThat(OltpUtils.getWrnMsg1(response)).isEqualTo("w1");
        assertThat(OltpUtils.getWrnMsg2(response)).isEqualTo("w2");
        assertThat(OltpUtils.getWrnMsg3(response)).isEqualTo("w3");
    }

    @Test
    void errorMessageAccessorsReturnEmptyWhenErrorResponseNull() {
        OltpResponse<OltpCommon> response = response();
        when(response.isError()).thenReturn(true);
        when(response.getErrorResponse()).thenReturn(null);

        assertThat(OltpUtils.getErrorModule(response)).isEmpty();
        assertThat(OltpUtils.getErrMsg1(response)).isEmpty();
        assertThat(OltpUtils.getWrnMsg1(response)).isEmpty();
    }

    @Test
    void isCapAbendErrorReturnsFalseWhenHeaderMissing() {
        OltpResponse<OltpCommon> response = response();
        when(response.isError()).thenReturn(true);
        when(response.getHeader()).thenReturn(null);

        assertThat(OltpUtils.isCapAbendError(response)).isFalse();
    }

    @Test
    void isCapAbendErrorReturnsFalseWhenCommonMissing() {
        OltpResponse<OltpCommon> response = response();
        when(response.isError()).thenReturn(true);
        OltpResHeader header = new OltpResHeader();
        when(response.getHeader()).thenReturn(header);

        assertThat(OltpUtils.isCapAbendError(response)).isFalse();
    }

    @Test
    void isCapAbendErrorReturnsFalseForNonCapFlags() {
        OltpResponse<OltpCommon> response = response();
        when(response.isError()).thenReturn(true);
        OltpResHeader header = new OltpResHeader();
        OltpCommon common = new OltpCommon();
        common.setStFlag1("A");
        common.setStFlag2("A");
        common.setStFlag3("A");
        common.setStFlag4("A");
        header.setOltpCommon(common);
        when(response.getHeader()).thenReturn(header);

        assertThat(OltpUtils.isCapAbendError(response)).isFalse();
    }

    @Test
    void getCapErrorStateConcatenatesFields() {
        OltpCommon common = new OltpCommon();
        common.setMsgInfoBlk(" a ");
        common.setModeInfo(" b ");
        common.setDesTp(" c ");

        assertThat(OltpUtils.getCapErrorState(common)).isEqualTo("abc");
    }
}
