package com.scbank.process.api.fw.dao.interceptor;

import java.util.List;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.scbank.process.api.fw.dao.paging.PagingRequest;
import com.scbank.process.api.fw.dao.paging.PagingRequestContext;
import com.scbank.process.api.fw.dao.paging.PagingResultContext;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
        }),
})
public class DaoPagingInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object result = invocation.proceed();

        PagingRequest pagingRequest = PagingRequestContext.get();

        if (pagingRequest != null && result instanceof List<?>) {
            Integer limit = pagingRequest.getLimit();

            List<?> resultList = (List<?>) result;
            boolean hasNext = resultList.size() > limit;
            PagingResultContext.setHasNext(hasNext);

            if (hasNext) {
                return resultList.subList(0, limit);
            }
        }
        return result;
    }

}
