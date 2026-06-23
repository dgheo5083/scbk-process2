package com.scbank.process.api.fw.dao.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.ibatis.plugin.Invocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.dao.paging.PagingRequestContext;
import com.scbank.process.api.fw.dao.paging.PagingResultContext;

/**
 * {@link DaoPagingInterceptor} 단위 테스트.
 */
class DaoPagingInterceptorTest {

    private DaoPagingInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new DaoPagingInterceptor();
    }

    @AfterEach
    void tearDown() {
        PagingRequestContext.clear();
        PagingResultContext.clear();
    }

    @Test
    @DisplayName("페이징 요청이 없으면 결과를 그대로 반환")
    void noPagingRequestReturnsResult() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        List<String> data = List.of("a", "b", "c");
        when(invocation.proceed()).thenReturn(data);

        Object result = interceptor.intercept(invocation);

        assertThat(result).isSameAs(data);
        assertThat(PagingResultContext.getHasNext()).isNull();
    }

    @Test
    @DisplayName("결과가 List 가 아니면 그대로 반환")
    void nonListResultReturnedAsIs() throws Throwable {
        PagingRequestContext.set(0, 2);
        Invocation invocation = mock(Invocation.class);
        Object data = "not-a-list";
        when(invocation.proceed()).thenReturn(data);

        Object result = interceptor.intercept(invocation);

        assertThat(result).isSameAs(data);
    }

    @Test
    @DisplayName("결과 크기가 limit 보다 크면 hasNext=true 이며 limit 까지 잘라서 반환")
    void resultExceedingLimitIsTruncated() throws Throwable {
        PagingRequestContext.set(0, 2);
        Invocation invocation = mock(Invocation.class);
        when(invocation.proceed()).thenReturn(List.of("a", "b", "c"));

        Object result = interceptor.intercept(invocation);

        assertThat(result).isInstanceOf(List.class);
        assertThat(PagingResultContext.getHasNext()).isTrue();
    }

    @Test
    @DisplayName("결과 크기가 limit 이하면 hasNext=false 이며 전체 반환")
    void resultWithinLimitReturnedFully() throws Throwable {
        PagingRequestContext.set(0, 5);
        Invocation invocation = mock(Invocation.class);
        List<String> data = List.of("a", "b");
        when(invocation.proceed()).thenReturn(data);

        Object result = interceptor.intercept(invocation);

        assertThat(result).isSameAs(data);
        assertThat(PagingResultContext.getHasNext()).isFalse();
    }
}
