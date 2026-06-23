package com.scbank.process.api.fw.channel.service.executor.impl;

import java.lang.reflect.Method;

import com.scbank.process.api.fw.channel.service.argument.ServiceMethodArgumentResolverComposite;
import com.scbank.process.api.fw.channel.service.executor.IServiceComponentExecutor;
import com.scbank.process.api.fw.channel.service.executor.IServiceComponentExecutorFactory;
import com.scbank.process.api.fw.channel.service.interceptor.ServiceInterceptorComposite;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;

import lombok.RequiredArgsConstructor;

/**
 * {@link IServiceComponentExecutorFactory}의 기본 구현체입니다.
 * 
 * 주어진 {@link ServiceInfo}, {@link ServiceMethodMetadata}를 기반으로
 * Spring Bean 및 Method 정보를 추출하여 {@link DefaultServiceComponentExecutor}를 생성합니다.
 * 
 * 이 구조는 추후 프록시 기반 실행기 또는 트레이싱 확장 등으로 대체 가능하도록 설계되어 있습니다.
 * 
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public class DefaultServiceComponentExecutorFactory implements IServiceComponentExecutorFactory {

    private final ServiceMethodArgumentResolverComposite argumentResolverComposite;

    private final ServiceInterceptorComposite serviceInterceptorComposite;

    /**
     * 실행 대상 서비스 메서드 정보를 기반으로 Executor를 생성합니다.
     *
     * @param serviceMethodMetadata 메서드 메타데이터 (반드시 필요)
     * @return 실행기
     * @throws Exception Bean 조회 또는 리플렉션 실패 시
     */
    @Override
    public IServiceComponentExecutor create(ServiceMethodMetadata serviceMethodMetadata) throws Exception {
        Class<?> targetClass = serviceMethodMetadata.getDeclaringClass();
        Method targetMethod = serviceMethodMetadata.getMethod();
        // Spring ApplicationContext에서 실제 빈 가져오기
        Object handlerBean = RuntimeContext.getBean(targetClass);

        return new DefaultServiceComponentExecutor(
                serviceMethodMetadata,
                argumentResolverComposite,
                serviceInterceptorComposite,
                handlerBean,
                targetMethod);
    }
}
