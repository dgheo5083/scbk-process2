package com.scbank.process.api.fw.base.integration.system.edmi.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiResHeader;

/**
 * Generated unit test for {@link EdmiSystemException}.
 */
class EdmiSystemExceptionTest {

    @Test
    void constructorStoresHeaderAndAccessors() {
        EdmiResHeader header = new EdmiResHeader();
        EdmiSystemException ex = new EdmiSystemException(header, "E001", "errorMessage");

        assertThat(ex.getHeader()).isSameAs(header);

        EdmiResHeader other = new EdmiResHeader();
        ex.setHeader(other);
        assertThat(ex.getHeader()).isSameAs(other);

        ex.setCaptureSystem("EDMI");
        assertThat(ex.getCaptureSystem()).isEqualTo("EDMI");
    }
}
