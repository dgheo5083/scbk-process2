package com.scbank.process.api.fw.base.integration.log;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.integration.log.dao.MciLogDao;
import com.scbank.process.api.fw.base.integration.log.dao.dto.MciLogParameter;
import com.scbank.process.api.fw.session.ISessionContextManager;

/**
 * Generated unit test for {@link IntegrationLogEventListener}.
 */
class IntegrationLogEventListenerTest {

    private final MciLogDao mciLogDao = mock(MciLogDao.class);
    private final ISessionContextManager sessionContextManager = mock(ISessionContextManager.class);
    private final IntegrationLogEventListener listener =
            new IntegrationLogEventListener(mciLogDao, sessionContextManager);

    @Test
    void ignoresUnsupportedSystem() {
        IntegrationLogEvent event = IntegrationLogEvent.builder().systemId("host").build();
        listener.on(event);
        verify(mciLogDao, never()).insertMciLog(any());
    }

    @Test
    void ignoresWhenTxCodeEmpty() {
        IntegrationLogEvent event = IntegrationLogEvent.builder().systemId("mci").txCd("").build();
        listener.on(event);
        verify(mciLogDao, never()).insertMciLog(any());
    }

    @Test
    void insertsMciLog() {
        lenient().when(sessionContextManager.getLoginValue(eq("CUST_NO"), eq(String.class))).thenReturn("C1");
        IntegrationLogEvent event = IntegrationLogEvent.builder()
                .systemId("mci")
                .messageId("MSG01")
                .txCd("T1")
                .data("payload")
                .build();

        listener.on(event);

        verify(mciLogDao).insertMciLog(any(MciLogParameter.class));
    }

    @Test
    void insertsEdmiLogWithTransformedTxCode() {
        IntegrationLogEvent event = IntegrationLogEvent.builder()
                .systemId("edmi")
                .messageId("ABC_EDMI_X")
                .data("payload")
                .build();

        listener.on(event);

        verify(mciLogDao).insertMciLog(any(MciLogParameter.class));
    }

    @Test
    void derivesCustomerNumberFromResidentNumber() {
        when(sessionContextManager.getLoginValue(eq("CUST_NO"), eq(String.class))).thenReturn("");
        when(sessionContextManager.getGlobalValue(eq("PerBusNo"), eq(String.class))).thenReturn("9001011234567");

        IntegrationLogEvent event = IntegrationLogEvent.builder()
                .systemId("mci")
                .messageId("CB_IDENTIFY_MB")
                .txCd("T1")
                .build();

        listener.on(event);

        verify(mciLogDao).insertMciLog(any(MciLogParameter.class));
    }
}
