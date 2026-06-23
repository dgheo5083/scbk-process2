package com.scbank.process.api.fw.base.integration.system.mci.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.integration.system.mci.vo.MciResHeader;

/**
 * Generated unit test for {@link MciSystemException}.
 */
class MciSystemExceptionTest {

    @Test
    void constructorStoresHeaderAndAccessors() {
        MciResHeader header = new MciResHeader();
        MciSystemException ex = new MciSystemException(header, "E001", "errorMessage");

        assertThat(ex.getHeader()).isSameAs(header);

        MciResHeader other = new MciResHeader();
        ex.setHeader(other);
        assertThat(ex.getHeader()).isSameAs(other);
    }
}
