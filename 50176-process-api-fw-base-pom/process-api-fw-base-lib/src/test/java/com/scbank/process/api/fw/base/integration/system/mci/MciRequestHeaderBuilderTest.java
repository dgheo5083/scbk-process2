package com.scbank.process.api.fw.base.integration.system.mci;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.integration.system.mci.vo.MciContTran;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciReqHeader;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciSystemHeader;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciTranCommHeader;
import com.scbank.process.api.fw.integration.exception.IntegrationException;

/**
 * Generated unit test for {@link MciRequestHeaderBuilder}.
 */
class MciRequestHeaderBuilderTest {

    private final MciRequestHeaderBuilder builder = new MciRequestHeaderBuilder();

    @Test
    void throwsWhenOptionsNull() {
        assertThatThrownBy(() -> builder.build(new HashMap<>(), null))
                .isInstanceOf(IntegrationException.class);
    }

    @Test
    void buildUsesDefaultsWhenOptionsEmpty() {
        MciReqHeader header = builder.build(null, new MciRequestOptions());

        assertThat(header).isNotNull();
        assertThat(header.getMciSystemHeader().getChnlTypCd()).isEqualTo("HUB");
        assertThat(header.getMciContTran()).isNotNull();
    }

    @Test
    void buildHonoursProvidedHeaders() {
        MciRequestOptions options = new MciRequestOptions();
        options.setMciSystemHeader(new MciSystemHeader());
        options.setChnlTypCd("WEB");
        options.setTmsgCreSysNm("SYS");

        MciTranCommHeader tranCommHeader = new MciTranCommHeader();
        tranCommHeader.setBlngBrNo("B001");
        tranCommHeader.setTxnBrNo("T001");
        options.setMciTranCommHeader(tranCommHeader);
        options.setMciContTran(new MciContTran());

        MciReqHeader header = builder.build(new HashMap<>(), options);

        assertThat(header.getMciSystemHeader().getChnlTypCd()).isEqualTo("WEB");
        assertThat(header.getMciSystemHeader().getTmsgCreSysNm()).isEqualTo("SYS");
        assertThat(header.getMciTranCommHeader().getBlngBrNo()).isEqualTo("B001");
        assertThat(header.getMciTranCommHeader().getTxnBrNo()).isEqualTo("T001");
    }
}
