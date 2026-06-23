package com.scbank.process.api.fw.base.dao.errorcode.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link ScbkChnnlErrMsgOutDto}.
 */
class ScbkChnnlErrMsgOutDtoTest {

    @Test
    void valueObjectContract() {
        ScbkChnnlErrMsgOutDto a = new ScbkChnnlErrMsgOutDto();
        ScbkChnnlErrMsgOutDto b = new ScbkChnnlErrMsgOutDto();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
