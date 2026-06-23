package com.scbank.process.api.fw.base.gateway.prc.base.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.scbank.process.api.fw.core.log.trace.TraceSection;

import feign.Target;

/**
 * 프로세스 API Feign Client InvocationHandler Factory 구현 클래스
 */
public class DefaultInvocationHandlerFactory extends BaseInvocationHandlerFactory {

    feign.InvocationHandlerFactory defaultFactory = new feign.InvocationHandlerFactory.Default();

    @SuppressWarnings("rawtypes")
    @Override
    public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
        InvocationHandler baseHandler = defaultFactory.create(target, dispatch);
        return (proxy, method, args) -> {
        	String url = target.url() + path(method);

            this.beginTrace(TraceSection.PRC_CALL, url);
            try {
                return baseHandler.invoke(proxy, method, args);
            } catch (Exception ex) {
            	this.failTrace(ex);
                throw ex;
            } finally {
            	this.endTrace();
            }
        };
    }

}
