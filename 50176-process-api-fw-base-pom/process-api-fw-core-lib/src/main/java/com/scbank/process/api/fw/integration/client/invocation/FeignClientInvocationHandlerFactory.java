package com.scbank.process.api.fw.integration.client.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext.FeignFilterContextHolder;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.context.IntegrationContextHolder;

import feign.InvocationHandlerFactory;
import feign.Target;

/**
 * Feign Client InvocationHandler Factory 구현 클래스
 * 
 * @author sungdon.choi
 */
public class FeignClientInvocationHandlerFactory implements InvocationHandlerFactory {

    @SuppressWarnings("rawtypes")
    @Override
    public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
        return (proxy, method, args) -> {
            IntegrationContext integrationContext = IntegrationContextHolder.get();
            FeignFilterContext ctx = new FeignFilterContext();
            ctx.setSystemId(integrationContext.getSystemId());
            ctx.setInterfaceId(ctx.getInterfaceId());

            FeignFilterContextHolder.set(ctx);
            try {
                return dispatch.get(method).invoke(args);
            } finally {
                FeignFilterContextHolder.clear();
            }
        };
    }

}
