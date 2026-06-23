package com.scbank.process.api.fw.base.shared;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.dao.errorcode.ScbkChnnlErrMgtDao;
import com.scbank.process.api.fw.base.dao.errorcode.dto.ScbkChnnlErrMsgInDto;
import com.scbank.process.api.fw.base.dao.errorcode.dto.ScbkChnnlErrMsgOutDto;

/**
 * Generated unit test for {@link ScbkChnnlErrMgtComponent}.
 */
class ScbkChnnlErrMgtComponentTest {

    private final ScbkChnnlErrMgtDao dao = mock(ScbkChnnlErrMgtDao.class);
    private final ScbkChnnlErrMgtComponent component = new ScbkChnnlErrMgtComponent(dao);

    private ScbkChnnlErrMsgOutDto out(String flag, String message) {
        ScbkChnnlErrMsgOutDto out = new ScbkChnnlErrMsgOutDto();
        out.setErrInfoSlctFlg(flag);
        out.setErrMsg(message);
        return out;
    }

    @Test
    void getScbkChnnlErrMsgDelegatesToDao() {
        ScbkChnnlErrMsgOutDto output = new ScbkChnnlErrMsgOutDto();
        when(dao.selectChnnlErrMgt(any(ScbkChnnlErrMsgInDto.class))).thenReturn(output);

        assertThat(component.getScbkChnnlErrMsg("SB", "E1", "KR", "M")).isSameAs(output);
    }

    @Test
    void getErrorMessageReturnsEmptyWhenNoResult() {
        when(dao.selectChnnlErrMgt(any())).thenReturn(null);
        assertThat(component.getErrorMessage("SB", "E1", "KR", "M")).isEmpty();
    }

    @Test
    void getErrorMessageReturnsDbMessageWhenFlagIsOne() {
        when(dao.selectChnnlErrMgt(any())).thenReturn(out("1", "db-message"));
        assertThat(component.getErrorMessage("SB", "E1", "KR", "M")).isEqualTo("db-message");
    }

    @Test
    void getErrorMessageReturnsEmptyWhenMessageBlankAndFlagNotOne() {
        when(dao.selectChnnlErrMgt(any())).thenReturn(out("2", ""));
        assertThat(component.getErrorMessage("SB", "E1", "KR", "M")).isEmpty();
    }

    @Test
    void getErrorMessageReturnsEmptyWhenFlagNotOneAndMessagePresent() {
        when(dao.selectChnnlErrMgt(any())).thenReturn(out("2", "present"));
        assertThat(component.getErrorMessage("SB", "E1", "KR", "M")).isEmpty();
    }
}
