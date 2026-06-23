package com.scbank.process.api.fw.base.dao.errorcode.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


/**
 * Generated unit test for {@link ScbkChnnlErrMsgInDto}.
 */
class ScbkChnnlErrMsgInDtoTest {

    @Test
    void valueObjectContract() {
        ScbkChnnlErrMsgInDto a = new ScbkChnnlErrMsgInDto();
        ScbkChnnlErrMsgInDto b = new ScbkChnnlErrMsgInDto();
        assertThat(a).isNotNull();
        assertThat(a.toString()).isNotNull();
        assertThat(a).isEqualTo(a);
        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
