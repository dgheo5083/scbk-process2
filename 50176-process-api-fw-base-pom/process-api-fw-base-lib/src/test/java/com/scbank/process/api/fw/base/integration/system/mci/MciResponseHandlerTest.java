package com.scbank.process.api.fw.base.integration.system.mci;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.integration.system.mci.exception.MciSystemException;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo.AdtnMsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo.MsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo.NewErrMsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciResHeader;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciSystemHeader;
import com.scbank.process.api.fw.integration.context.IntegrationContext;

/**
 * Generated unit test for {@link MciResponseHandler}.
 */
class MciResponseHandlerTest {

    private final MciResponseHandler handler = new MciResponseHandler();
    private final IntegrationContext context = mock(IntegrationContext.class);

    private MciResHeader header(String procRsltDvcd, String newErrMsgCd) {
        MciResHeader header = new MciResHeader();
        MciSystemHeader systemHeader = new MciSystemHeader();
        systemHeader.setProcRsltDvcd(procRsltDvcd);
        header.setMciSystemHeader(systemHeader);

        MciMsgInfo msgInfo = new MciMsgInfo();
        MsgInfo info = new MsgInfo();
        info.setMsgCd("E1");
        info.setMsgCtt("content");
        msgInfo.setMsgInfo(info);

        AdtnMsgInfo adtnMsgInfo = new AdtnMsgInfo();
        adtnMsgInfo.setAdtnMsg("additional");
        msgInfo.setAdtnMsgInfo(adtnMsgInfo);

        NewErrMsgInfo newErrMsgInfo = new NewErrMsgInfo();
        newErrMsgInfo.setNewErrMsgCd(newErrMsgCd);
        newErrMsgInfo.setNewErrMsgCtt("new-content");
        msgInfo.setNewErrMsgInfo(newErrMsgInfo);

        header.setMciMsgInfo(msgInfo);
        return header;
    }

    @Test
    void isErrorHandlesNullAndProcessResult() {
        assertThat(handler.isError(null)).isTrue();
        assertThat(handler.isError(header("0", null))).isFalse();
        assertThat(handler.isError(header("1", null))).isTrue();
    }

    @Test
    void getResponseCodeHandlesScenarios() {
        assertThat(handler.getResponseCode(null)).isEmpty();
        assertThat(handler.getResponseCode(header("1", null))).isEqualTo("E1");
        assertThat(handler.getResponseCode(header("8", null))).isEqualTo("additional");
        assertThat(handler.getResponseCode(header("0", null))).isEmpty();
    }

    @Test
    void checkErrorAndThrowableReturnsForSuccess() {
        assertThatCode(() -> handler.checkErrorAndThrowable(context, null, header("0", null), null))
                .doesNotThrowAnyException();
    }

    @Test
    void checkErrorAndThrowableThrowsForProcessResultOneWithNewError() {
        assertThatThrownBy(() -> handler.checkErrorAndThrowable(context, null, header("1", "N1"), null))
                .isInstanceOf(MciSystemException.class);
    }

    @Test
    void checkErrorAndThrowableThrowsForProcessResultOneWithoutNewError() {
        assertThatThrownBy(() -> handler.checkErrorAndThrowable(context, null, header("1", null), null))
                .isInstanceOf(MciSystemException.class);
    }

    @Test
    void checkErrorAndThrowableThrowsForProcessResultNine() {
        assertThatThrownBy(() -> handler.checkErrorAndThrowable(context, null, header("9", null), null))
                .isInstanceOf(MciSystemException.class);
    }
}
