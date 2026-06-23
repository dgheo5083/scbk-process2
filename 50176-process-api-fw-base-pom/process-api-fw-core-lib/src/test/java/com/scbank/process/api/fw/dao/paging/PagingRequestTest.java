package com.scbank.process.api.fw.dao.paging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link PagingRequest} 단위 테스트.
 */
class PagingRequestTest {

    @Test
    @DisplayName("offset/limit getter/setter 동작 검증")
    void gettersAndSetters() {
        PagingRequest request = new PagingRequest();
        request.setOffset(10);
        request.setLimit(20);

        assertThat(request.getOffset()).isEqualTo(10);
        assertThat(request.getLimit()).isEqualTo(20);
    }

    @Test
    @DisplayName("초기값은 0")
    void defaultValues() {
        PagingRequest request = new PagingRequest();

        assertThat(request.getOffset()).isZero();
        assertThat(request.getLimit()).isZero();
    }
}
