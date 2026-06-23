package com.scbank.process.api.fw.dao.paging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link PagingRequestContext} 단위 테스트.
 */
class PagingRequestContextTest {

    @AfterEach
    void tearDown() {
        // ThreadLocal 누수 방지
        PagingRequestContext.clear();
    }

    @Test
    @DisplayName("PagingRequest 직접 set 후 get 으로 동일 인스턴스 조회")
    void setAndGetInstance() {
        PagingRequest request = new PagingRequest();
        request.setOffset(5);
        request.setLimit(15);

        PagingRequestContext.set(request);

        assertThat(PagingRequestContext.get()).isSameAs(request);
    }

    @Test
    @DisplayName("offset/limit 로 set 시 PagingRequest 가 구성되어 저장")
    void setByOffsetAndLimit() {
        PagingRequestContext.set(3, 7);

        PagingRequest result = PagingRequestContext.get();
        assertThat(result).isNotNull();
        assertThat(result.getOffset()).isEqualTo(3);
        assertThat(result.getLimit()).isEqualTo(7);
    }

    @Test
    @DisplayName("clear 후 get 은 null 반환")
    void clearRemovesValue() {
        PagingRequestContext.set(1, 1);
        PagingRequestContext.clear();

        assertThat(PagingRequestContext.get()).isNull();
    }

    @Test
    @DisplayName("초기 상태에서 get 은 null")
    void getWithoutSetReturnsNull() {
        assertThat(PagingRequestContext.get()).isNull();
    }
}
