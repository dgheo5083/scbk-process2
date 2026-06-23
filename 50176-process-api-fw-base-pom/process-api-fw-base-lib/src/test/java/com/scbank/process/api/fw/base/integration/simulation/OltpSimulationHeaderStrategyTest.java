package com.scbank.process.api.fw.base.integration.simulation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpError;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;

/**
 * Generated unit test for {@link OltpSimulationHeaderStrategy}.
 */
class OltpSimulationHeaderStrategyTest {

    private final OltpSimulationHeaderStrategy strategy = new OltpSimulationHeaderStrategy(new XmlMapper());

    @Test
    void supportedMatchesHostSystemId() {
        assertThat(strategy.supported("host")).isTrue();
        assertThat(strategy.supported("HOST")).isTrue();
        assertThat(strategy.supported("mci")).isFalse();
    }

    @Test
    void getHeaderParsesValidXml() {
        String xml = "<root><ScfbHeader>"
                + "<IMSTRANCD>X</IMSTRANCD><ERRORCODE>9999</ERRORCODE>"
                + "</ScfbHeader></root>";

        OltpResHeader header = strategy.getHeader(xml);

        assertThat(header.getOltpCommon().getImsTranCd()).isEqualTo("X");
        assertThat(header.getOltpCommon().getErrorCode()).isEqualTo("9999");
    }

    @Test
    void getHeaderReturnsDefaultOnError() {
        OltpResHeader header = strategy.getHeader("not-valid <<");

        assertThat(header).isNotNull();
        assertThat(header.getOltpCommon()).isNotNull();
    }

    @Test
    void getErrorMsgParsesHostErrorNode() {
        String xml = "<root><HostErrorMsg><errMsg1>boom</errMsg1></HostErrorMsg></root>";

        OltpError error = strategy.getErrorMsg(xml);

        assertThat(error).isNotNull();
        assertThat(error.getErrMsg1()).isEqualTo("boom");
    }

    @Test
    void getErrorMsgReturnsNullOnError() {
        assertThat(strategy.getErrorMsg("not-valid <<")).isNull();
    }
}
