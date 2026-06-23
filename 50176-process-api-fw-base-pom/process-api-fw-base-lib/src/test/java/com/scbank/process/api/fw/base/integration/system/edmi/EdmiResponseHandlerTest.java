package com.scbank.process.api.fw.base.integration.system.edmi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.integration.system.edmi.exception.EdmiSystemException;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiMsgInfo;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiMsgInfo.HostMsg;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiResHeader;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiSystemHeader;
import com.scbank.process.api.fw.integration.context.IntegrationContext;

/**
 * Generated unit test for {@link EdmiResponseHandler}.
 */
class EdmiResponseHandlerTest {

    private final EdmiResponseHandler handler = new EdmiResponseHandler();

    private EdmiResHeader header(String procRsltDvCd, String msgCd, String msgCtt) {
        EdmiResHeader header = new EdmiResHeader();
        EdmiSystemHeader systemHeader = new EdmiSystemHeader();
        systemHeader.setProcRsltDvcd(procRsltDvCd);
        header.setSystemHeader(systemHeader);

        EdmiMsgInfo msgInfo = new EdmiMsgInfo();
        List<HostMsg> hostMsgs = new ArrayList<>();
        if (msgCd != null) {
            HostMsg hostMsg = new HostMsg();
            hostMsg.setMsgCd(msgCd);
            hostMsg.setMsgCtt(msgCtt);
            hostMsgs.add(hostMsg);
        }
        msgInfo.setHostMsg(hostMsgs);
        header.setMsgInfo(msgInfo);
        return header;
    }

    @Test
    void isErrorReflectsProcessResultCode() {
        assertThat(handler.isError(header("0", null, null))).isFalse();
        assertThat(handler.isError(header("1", null, null))).isTrue();
    }

    @Test
    void getResponseCodeReturnsMessageCode() {
        assertThat(handler.getResponseCode(header("1", "E01", "boom"))).isEqualTo("E01");
    }

    @Test
    void getResponseCodeReturnsEmptyWhenNoHostMessages() {
        assertThat(handler.getResponseCode(header("1", null, null))).isEmpty();
    }

    @Test
    void checkErrorAndThrowableDoesNothingForSuccess() {
        IntegrationContext context = mock(IntegrationContext.class);
        assertThatCode(() -> handler.checkErrorAndThrowable(context, null, header("0", null, null), null))
                .doesNotThrowAnyException();
    }

    @Test
    void checkErrorAndThrowableThrowsForError() {
        IntegrationContext context = mock(IntegrationContext.class);
        lenient().when(context.getCaptureSystem()).thenReturn("EDMI");

        assertThatThrownBy(() -> handler.checkErrorAndThrowable(
                context, null, header("1", "E01", "boom"), null))
                .isInstanceOf(EdmiSystemException.class);
    }
}
