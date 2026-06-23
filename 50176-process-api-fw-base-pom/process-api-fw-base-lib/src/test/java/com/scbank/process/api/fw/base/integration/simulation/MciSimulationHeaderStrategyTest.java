package com.scbank.process.api.fw.base.integration.simulation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciResHeader;

/**
 * Generated unit test for {@link MciSimulationHeaderStrategy}.
 */
class MciSimulationHeaderStrategyTest {

    private final MciSimulationHeaderStrategy strategy = new MciSimulationHeaderStrategy(new XmlMapper());

    @Test
    void supportedMatchesMciSystemId() {
        assertThat(strategy.supported("mci")).isTrue();
        assertThat(strategy.supported("MCI")).isTrue();
        assertThat(strategy.supported("oltp")).isFalse();
    }

    @Test
    void getErrorMsgReturnsNull() {
        assertThat(strategy.getErrorMsg("anything")).isNull();
    }

    @Test
    void getHeaderParsesValidXml() {
        String xml = "<MCI>"
                + "<MCICommon><CRYP_DV_CD>A</CRYP_DV_CD><MCI_ND_NO>5</MCI_ND_NO></MCICommon>"
                + "<MCIMsgInfo><MSG_CD>E1</MSG_CD></MCIMsgInfo>"
                + "<MCIContTran><CONT_DATA_CD>C</CONT_DATA_CD></MCIContTran>"
                + "</MCI>";

        MciResHeader header = strategy.getHeader(xml);

        assertThat(header.getMciSystemHeader().getCrypDvCd()).isEqualTo("A");
        assertThat(header.getMciSystemHeader().getMciNdNo()).isEqualTo(5);
        assertThat(header.getMciMsgInfo().getMsgInfo().getMsgCd()).isEqualTo("E1");
    }

    @Test
    void getHeaderReturnsDefaultOnParsingError() {
        MciResHeader header = strategy.getHeader("not-valid-xml <<");

        assertThat(header).isNotNull();
        assertThat(header.getMciSystemHeader()).isNotNull();
        assertThat(header.getMciContTran()).isNotNull();
    }
}
