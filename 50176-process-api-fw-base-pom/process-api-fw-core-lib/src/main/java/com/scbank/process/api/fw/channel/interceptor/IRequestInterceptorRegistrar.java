package com.scbank.process.api.fw.channel.interceptor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;

/**
 * 프레임워크에서 정의한 {@link IRequestInterceptor} 구현체들을 등록/관리하는 인터페이스입니다.
 *
 * <p>
 * {@link InitializingBean}, {@link ApplicationContextAware}를 확장하여
 * Spring 초기화 시점에 자동 등록되는 구조를 제공합니다.
 *
 * <p>
 * 일반적으로 구현체에서는 {@link org.springframework.context.ApplicationContext}에서
 * 모든 {@link IRequestInterceptor} Bean을 수집하여, 실행 우선순위(index) 순으로 정렬하고,
 * Dispatcher 필터 체인 또는 HandlerInterceptor로 등록하는 역할을 수행합니다.
 *
 * <pre>
 * 사용 예시:
 * public class RequestInterceptorRegistrar implements IRequestInterceptorRegistrar {
 *     public void register() {
 *         // ApplicationContext에서 IRequestInterceptor Bean 수집 및 등록
 *     }
 * }
 * </pre>
 *
 * @author sungdon.choi
 */
public interface IRequestInterceptorRegistrar extends BeanDefinitionRegistryPostProcessor, EnvironmentAware {

}
