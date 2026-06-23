package com.scbank.process.api.fw.base.integration.system.oltp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.integration.system.oltp.exception.OltpSystemException;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpError;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;
import com.scbank.process.api.fw.integration.context.IntegrationContext;

/**
 * Generated unit test for {@link OltpResponseHandler}.
 */
class OltpResponseHandlerTest {

    private final OltpResponseHandler handler = new OltpResponseHandler();

    private OltpResHeader header(String errorCode) {
        OltpResHeader header = new OltpResHeader();
        OltpCommon common = new OltpCommon();
        common.setErrorCode(errorCode);
        header.setOltpCommon(common);
        return header;
    }

    private IntegrationContext context() {
        IntegrationContext context = mock(IntegrationContext.class);
        lenient().when(context.getLocale()).thenReturn(Locale.KOREA);
        return context;
    }

    @Test
    void isErrorAndResponseCode() {
        assertThat(handler.isError(header("0000"))).isFalse();
        assertThat(handler.isError(header("9999"))).isTrue();
        assertThat(handler.getResponseCode(header("9999"))).isEqualTo("9999");
    }

    @Test
    void checkErrorAndThrowableReturnsForSuccess() {
        assertThatCode(() -> handler.checkErrorAndThrowable(context(), null, header("0000"), new OltpError()))
                .doesNotThrowAnyException();
    }

    @Test
    void checkCapErrorReturnsForSuccess() {
        assertThatCode(() -> handler.checkCapError(context(), null, header("0000"), new OltpError()))
                .doesNotThrowAnyException();
    }

    @Test
    void checkCapErrorThrowsForRegionDown() {
        assertThatThrownBy(() -> handler.checkCapError(context(), null, header("9999"), new OltpError()))
                .isInstanceOf(OltpSystemException.class);
    }

    @Test
    void checkErrorAndThrowableThrowsForError() {
        assertThatThrownBy(() -> handler.checkErrorAndThrowable(context(), null, header("9999"), new OltpError()))
                .isInstanceOf(OltpSystemException.class);
    }
}
