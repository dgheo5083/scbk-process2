package com.scbank.process.api.fw.channel.interceptor;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties;
import com.scbank.process.api.fw.channel.constants.ChannelConstants;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 서비스별 요청 인터셉터 등록 처리 클래스입니다.
 * 
 * 설정된 서비스에 따라 {@link IRequestInterceptor} 구현체를 동적으로 Spring Bean으로 등록합니다.
 * 주입 시 생성자 기반으로 index와 URL 패턴 목록을 전달합니다.
 *
 * <p>
 * 등록 대상은 {@code framework.channel.service[*].interceptors} 설정에 정의된 클래스입니다.
 * </p>
 *
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
public class RequestInterceptorRegistrar implements IRequestInterceptorRegistrar {

    private Environment environment;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        Binder binder = Binder.get(environment);
        ChannelProperties properties = binder.bind(ChannelConstants.FRAMEWORK_CHANNEL_PREFIX, ChannelProperties.class)
                .get();

        if (log.isInfoEnabled()) {
            log.info("# 프레임워크 서비스 인터셉터 빈 등록 시작");
        }

        this.registerBean(registry, properties);
    }

    /**
     * 프레임워크에 정의된 모든 서비스에 대해 인터셉터를 Bean으로 등록합니다.
     * <p>
     * 서비스 단위로 URL 매핑을 결합하여, 동적으로 {@link AbstractRequestInterceptor} 또는
     * {@link IRequestInterceptor} 구현체를 등록합니다.
     * </p>
     */
    private void registerBean(BeanDefinitionRegistry registry, ChannelProperties channelProperties) {
        // 채널 프로퍼티가 없는경우
        if (channelProperties == null || channelProperties.getService() == null) {
            return;
        }
        channelProperties.getService().stream()
                .filter(ServiceProperties::enabled)
                .forEach(s -> {
                    String basePath = StringUtils.defaultIfBlank(s.basePath(), "");
                    String serviceId = s.serviceId();
                    List<Class<?>> interceptors = s.interceptors();

                    if (CollectionUtils.isEmpty(interceptors)) {
                        return;
                    }

                    // basePath + allowedUrlPatterns 조합
                    List<String> allowedUrlPatterns = s.serviceMapping().allowedUrlPatterns()
                            .stream()
                            .map(url -> basePath + url)
                            .toList();

                    // index 순서로 Bean 등록
                    IntStream.range(0, interceptors.size()).forEach(i -> {
                        Class<?> interceptorClass = interceptors.get(i);
                        this.registerServiceInterceptor(registry, serviceId, interceptorClass, i, allowedUrlPatterns);
                    });
                });
    }

    /**
     * 단일 서비스 인터셉터를 Spring Bean으로 동적으로 등록합니다.
     *
     * @param serviceId        서비스 ID
     * @param interceptorClass 인터셉터 클래스
     * @param index            실행 순서 인덱스
     * @param urlPatterns      URL 패턴 목록
     */
    private void registerServiceInterceptor(BeanDefinitionRegistry registry, String serviceId,
            Class<?> interceptorClass, int index,
            List<String> urlPatterns) {

        // 생성자 파라미터(index, urlPatterns) 전달
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .genericBeanDefinition(interceptorClass)
                .addPropertyValue("index", index)
                .addPropertyValue("urlPatterns", urlPatterns);

        String beanName = this.getInterceptorBeanName(serviceId, interceptorClass, index);

        if (registry.containsBeanDefinition(beanName)) {
            if (log.isWarnEnabled()) {
                log.warn("# 프레임워크 서비스 인터셉터 [{}] already registered as bean: {}", interceptorClass.getSimpleName(),
                        beanName);
            }
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("# 프레임워크 서비스 인터셉터 [{}] 빈 등록: {}", interceptorClass.getSimpleName(), beanName);
        }

        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    /**
     * 인터셉터 Bean 이름 생성 전략
     *
     * @param serviceId        서비스 ID
     * @param interceptorClass 클래스
     * @param index            순서
     * @return Bean 이름
     */
    private String getInterceptorBeanName(String serviceId, Class<?> interceptorClass, int index) {
        return serviceId + "#" + index + "#" + interceptorClass.getSimpleName();
    }
}
