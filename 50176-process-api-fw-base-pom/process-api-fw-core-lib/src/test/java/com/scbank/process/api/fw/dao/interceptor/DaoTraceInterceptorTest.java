package com.scbank.process.api.fw.dao.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.log.trace.TraceContext;
import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;
import com.scbank.process.api.fw.core.log.trace.TraceNode;
import com.scbank.process.api.fw.core.log.trace.TraceSection;

/**
 * {@link DaoTraceInterceptor} 단위 테스트.
 */
class DaoTraceInterceptorTest {

    private DaoTraceInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new DaoTraceInterceptor();
    }

    @AfterEach
    void tearDown() {
        TraceContextHolder.clear();
    }

    private Invocation invocationFor(SqlCommandType type, String mapperId) {
        MappedStatement ms = mock(MappedStatement.class);
        when(ms.getId()).thenReturn(mapperId);
        when(ms.getSqlCommandType()).thenReturn(type);

        Invocation invocation = mock(Invocation.class);
        when(invocation.getArgs()).thenReturn(new Object[] { ms });
        return invocation;
    }

    @Test
    @DisplayName("TraceContext 존재 시 begin/end 가 호출되고 결과를 반환")
    void withContextSuccess() throws Throwable {
        TraceContext context = mock(TraceContext.class);
        TraceContextHolder.set(context);

        Invocation invocation = invocationFor(SqlCommandType.SELECT, "com.scbank.Mapper.find");
        when(invocation.proceed()).thenReturn("result");

        Object result = interceptor.intercept(invocation);

        assertThat(result).isEqualTo("result");
        verify(context).begin(contains("[SELECT] com.scbank.Mapper.find"), eq(TraceSection.DAO));
        verify(context).end();
    }

    @Test
    @DisplayName("TraceContext 존재 시 예외 발생하면 현재 노드를 실패 처리하고 end 후 재전파")
    void withContextFailure() throws Throwable {
        TraceContext context = mock(TraceContext.class);
        TraceNode current = mock(TraceNode.class);
        when(context.getCurrent()).thenReturn(current);
        TraceContextHolder.set(context);

        Invocation invocation = invocationFor(SqlCommandType.UPDATE, "com.scbank.Mapper.update");
        RuntimeException boom = new IllegalStateException("db error");
        when(invocation.proceed()).thenThrow(boom);

        assertThatThrownBy(() -> interceptor.intercept(invocation)).isSameAs(boom);

        verify(context).begin(contains("[UPDATE] com.scbank.Mapper.update"), eq(TraceSection.DAO));
        verify(current).fail(contains("IllegalStateException"));
        verify(context).end();
    }

    @Test
    @DisplayName("TraceContext 가 없으면 trace 처리 없이 결과를 반환")
    void withoutContext() throws Throwable {
        TraceContextHolder.clear();
        Invocation invocation = invocationFor(SqlCommandType.INSERT, "com.scbank.Mapper.insert");
        when(invocation.proceed()).thenReturn(42);

        Object result = interceptor.intercept(invocation);

        assertThat(result).isEqualTo(42);
    }

    @Test
    @DisplayName("TraceContext 가 없을 때 예외 발생 시 그대로 재전파")
    void withoutContextFailure() throws Throwable {
        TraceContextHolder.clear();
        Invocation invocation = invocationFor(SqlCommandType.DELETE, "com.scbank.Mapper.delete");
        RuntimeException boom = new RuntimeException("boom");
        when(invocation.proceed()).thenThrow(boom);

        assertThatThrownBy(() -> interceptor.intercept(invocation)).isSameAs(boom);
    }

    @Test
    @DisplayName("plugin 은 대상 객체를 래핑한 프록시를 반환")
    void pluginWrapsTarget() {
        Interface target = mock(Interface.class);

        Object wrapped = interceptor.plugin(target);

        assertThat(wrapped).isNotNull();
    }

    @Test
    @DisplayName("setProperties 는 아무 동작도 하지 않는다")
    void setPropertiesIsNoOp() {
        Properties properties = new Properties();
        properties.setProperty("k", "v");

        interceptor.setProperties(properties);
        // 예외 없이 종료되면 통과
    }

    /** Plugin.wrap 대상이 될 수 있도록 인터페이스 기반 더미 타입. */
    interface Interface {
        void doWork();
    }

    @Test
    @DisplayName("plugin 호출은 begin/end 와 무관하다(컨텍스트 상호작용 없음)")
    void pluginDoesNotTouchContext() {
        TraceContext context = mock(TraceContext.class);
        TraceContextHolder.set(context);

        interceptor.plugin(mock(Interface.class));

        verify(context, never()).begin(org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.any());
    }
}
