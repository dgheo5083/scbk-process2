package com.scbank.process.api.fw.base.integration.system.oltp.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpError;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;

/**
 * Generated unit test for {@link OltpSystemException}.
 */
class OltpSystemExceptionTest {

    @Test
    void constructorStoresHeaderAndAccessors() {
        OltpResHeader header = new OltpResHeader();
        OltpSystemException ex = new OltpSystemException(header, "E001", "errorMessage");

        assertThat(ex.getHeader()).isSameAs(header);

        OltpResHeader otherHeader = new OltpResHeader();
        ex.setHeader(otherHeader);
        assertThat(ex.getHeader()).isSameAs(otherHeader);

        OltpError error = new OltpError();
        ex.setError(error);
        assertThat(ex.getError()).isSameAs(error);
    }
}
