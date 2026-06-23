package com.scbank.process.api.fw.dao.paging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link PagingResultContext} 단위 테스트.
 */
class PagingResultContextTest {

    @AfterEach
    void tearDown() {
        PagingResultContext.clear();
    }

    @Test
    @DisplayName("hasNext true 저장 후 조회")
    void setHasNextTrue() {
        PagingResultContext.setHasNext(true);

        assertThat(PagingResultContext.getHasNext()).isTrue();
    }

    @Test
    @DisplayName("hasNext false 저장 후 조회")
    void setHasNextFalse() {
        PagingResultContext.setHasNext(false);

        assertThat(PagingResultContext.getHasNext()).isFalse();
    }

    @Test
    @DisplayName("clear 후 조회 시 null")
    void clearRemovesValue() {
        PagingResultContext.setHasNext(true);
        PagingResultContext.clear();

        assertThat(PagingResultContext.getHasNext()).isNull();
    }

    @Test
    @DisplayName("초기 상태에서 조회 시 null")
    void getWithoutSetReturnsNull() {
        assertThat(PagingResultContext.getHasNext()).isNull();
    }
}
