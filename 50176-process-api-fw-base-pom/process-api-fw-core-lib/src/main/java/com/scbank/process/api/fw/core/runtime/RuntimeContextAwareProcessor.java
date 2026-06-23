package com.scbank.process.api.fw.core.runtime;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 런타임 컨텍스트 주입 처리기 (BeanPostProcessor)
 * <p>
 * Spring Bean 생성 시점에 {@link RuntimeContextAware} 인터페이스를 구현한 Bean을 자동 탐지하여,
 * {@link RuntimeContext}를 주입해주는 역할을 합니다.
 * </p>
 *
 * <p>
 * 주로 런타임 실행 모드(PRD, DEV, LOCAL 등)나 컨텍스트 기반 설정이 필요한 컴포넌트에 활용됩니다.
 * </p>
 *
 * @see RuntimeContext
 * @see RuntimeContextAware
 * @see BeanPostProcessor
 * 
 * @author sungdon.choi
 */
@RequiredArgsConstructor
@Component
public class RuntimeContextAwareProcessor implements BeanPostProcessor {

    /**
     * 런타임 컨텍스트 (전역 실행환경 정보)
     */
    private final RuntimeContext runtimeContext;

    /**
     * Bean 초기화 전에 RuntimeContext를 주입
     *
     * @param bean     초기화 대상 Bean
     * @param beanName Bean 이름
     * @return 주입된 Bean (변경 없으면 원본 그대로 반환)
     * @throws BeansException 처리 중 예외 발생 시
     */
    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName)
            throws BeansException {
        if (bean instanceof RuntimeContextAware aware) {
            aware.setRuntimeContext(runtimeContext);
        }
        return bean;
    }
}
