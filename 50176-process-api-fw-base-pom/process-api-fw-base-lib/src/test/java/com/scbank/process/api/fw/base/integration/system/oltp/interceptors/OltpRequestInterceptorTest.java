package com.scbank.process.api.fw.base.integration.system.oltp.interceptors;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequest;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpReqHeader;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.session.ISessionContextManager;

/**
 * Generated unit test for {@link OltpRequestInterceptor}.
 */
class OltpRequestInterceptorTest {

    public static class RequestDto implements IMessageObject {
        public String UserID = "";
        public String CustName = "";
        public String TSPassword = "";
        public String PerBusNo = "";
        public String PerBusNo1 = "";
        public String TransPassword = "";
        public String SafeCardBranchNum = "";
        public String SafeCardINDEX = "";
        public String SafeCardINDEX2 = "";
    }

    private final ISessionContextManager sessionContextManager = mock(ISessionContextManager.class);
    private final OltpRequestInterceptor interceptor = new OltpRequestInterceptor(sessionContextManager);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private OltpRequest request(RequestDto dto) {
        OltpRequest request = mock(OltpRequest.class);
        OltpReqHeader header = OltpReqHeader.builder()
        		.oltpCommon(new OltpCommon())
        		.build();
        lenient().when(request.getHeader()).thenReturn(header);
        lenient().when(request.getRequestMessage()).thenReturn(dto);
        return request;
    }

    private IntegrationContext context(boolean isRealTran, boolean isPreTran) {
        IntegrationContext context = mock(IntegrationContext.class);
        lenient().when(context.getSystemId()).thenReturn("host");
        lenient().when(context.getAttribute(eq("isRealTran"), eq(Boolean.class))).thenReturn(isRealTran);
        lenient().when(context.getAttribute(eq("isPreTran"), eq(Boolean.class))).thenReturn(isPreTran);
        return context;
    }

    @Test
    void skipsWhenNotHostSystem() {
        IntegrationContext context = mock(IntegrationContext.class);
        when(context.getSystemId()).thenReturn("mci");

        assertThatCode(() -> interceptor.before(context, request(new RequestDto())))
                .doesNotThrowAnyException();
    }

    @Test
    void processesLoggedInRequest() {
        when(sessionContextManager.isLogin()).thenReturn(true);
        lenient().when(sessionContextManager.getLoginValue(anyString(), eq(String.class))).thenReturn("VAL");
        lenient().when(sessionContextManager.getGlobalValue(anyString(), eq(String.class))).thenReturn("VAL");

        assertThatCode(() -> interceptor.before(context(true, true), request(new RequestDto())))
                .doesNotThrowAnyException();
    }

    @Test
    void processesAnonymousRequest() {
        when(sessionContextManager.isLogin()).thenReturn(false);
        lenient().when(sessionContextManager.getLoginValue(anyString(), eq(String.class))).thenReturn("VAL");
        lenient().when(sessionContextManager.getGlobalValue(anyString(), eq(String.class))).thenReturn("VAL");

        assertThatCode(() -> interceptor.before(context(false, false), request(new RequestDto())))
                .doesNotThrowAnyException();
    }
}
