package com.scbank.process.api.fw.base.integration.system.edmi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiReqHeader;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiSystemHeader;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiTranCommonHeader;
import com.scbank.process.api.fw.base.integration.uuid.IntegrationTranNoGenerator;

/**
 * Generated unit test for {@link EdmiRequestHeaderBuilder}.
 */
class EdmiRequestHeaderBuilderTest {

    private final IntegrationTranNoGenerator generator = mock(IntegrationTranNoGenerator.class);
    private final EdmiRequestHeaderBuilder builder = new EdmiRequestHeaderBuilder(generator);

    @Test
    void buildUsesDefaultsWhenOptionsEmpty() {
        when(generator.generateId()).thenReturn("000001");
        Map<String, Object> defaultHeader = new HashMap<>();

        EdmiReqHeader header = builder.build(defaultHeader, new EdmiRequestOptions());

        assertThat(header).isNotNull();
        assertThat(header.getSystemHeader().getChnlTypCd()).isEqualTo("API");
        assertThat(header.getTranCommonHeader().getGjmno()).isEqualTo("000001");
    }

    @Test
    void buildHonoursProvidedHeaders() {
        when(generator.generateId()).thenReturn("000002");

        EdmiRequestOptions options = new EdmiRequestOptions();
        options.setSystemHeader(new EdmiSystemHeader());
        options.setChnlTypCd("WEB");
        options.setTmsgCreSysNm("SYS");

        EdmiTranCommonHeader tranCommonHeader = new EdmiTranCommonHeader();
        tranCommonHeader.setBlngBrNo("B001");
        tranCommonHeader.setTxnBrNo("T001");
        options.setTranCommonHeader(tranCommonHeader);

        EdmiReqHeader header = builder.build(new HashMap<>(), options);

        assertThat(header.getSystemHeader().getChnlTypCd()).isEqualTo("WEB");
        assertThat(header.getSystemHeader().getTmsgCreSysNm()).isEqualTo("SYS");
        assertThat(header.getTranCommonHeader().getBlngBrNo()).isEqualTo("B001");
        assertThat(header.getTranCommonHeader().getTxnBrNo()).isEqualTo("T001");
    }
}
