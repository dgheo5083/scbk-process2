package com.scbank.process.api.fw.dao.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.scbank.process.api.fw.core.log.trace.TraceContext;
import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;
import com.scbank.process.api.fw.core.log.trace.TraceSection;

/**
 * <pre>
 * MyBatis Mapper 실행 시 Trace 로그를 남기는 Interceptor
 * - SELECT, UPDATE, INSERT, DELETE 구분하여 로그 출력
 * - TraceContext 를 통해 트리 형태의 트레이스 로깅 지원
 * - Exception 발생 시 해당 노드를 실패로 마킹
 * </pre>
 * 
 * @author sungdon.choi
 * @since 25. 4. 25.
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
        }),
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class
        })
})
public class DaoTraceInterceptor implements Interceptor {

    /**
     * SQL 실행 시점마다 호출되는 메서드
     * 
     * @param invocation 현재 실행 중인 MyBatis 호출 정보
     * @return 실제 SQL 실행 결과
     * @throws Throwable 예외 발생 시
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];

        // Mapper 메서드 ID (예: co.kr.mapper.UserMapper.getUser)
        String mapperId = ms.getId();

        // SQL 타입 (예: SELECT, UPDATE, INSERT, DELETE 등)
        String sqlType = ms.getSqlCommandType().name();

        // Trace용 라벨 구성
        String label = TraceSection.DAO.name() + " - [" + sqlType + "] " + mapperId;

        TraceContext context = TraceContextHolder.get().orElse(null);
        if (context != null) {
            context.begin(label, TraceSection.DAO);
        }

        try {
            return invocation.proceed(); // 실제 SQL 실행
        } catch (Throwable e) {
            // 트레이스 실패 마킹
            if (context != null) {
                context.getCurrent().fail(e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            throw e;
        } finally {
            if (context != null) {
                context.end(); // 실행 종료 시간 기록
            }
        }
    }

    /**
     * 프록시 적용 메서드 (기본 구현)
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 플러그인 설정값 주입용 (현재 미사용)
     */
    @Override
    public void setProperties(Properties properties) {
        // no-op
    }
}
