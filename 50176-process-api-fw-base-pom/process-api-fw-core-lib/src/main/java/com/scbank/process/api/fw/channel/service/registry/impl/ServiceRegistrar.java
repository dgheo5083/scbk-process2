package com.scbank.process.api.fw.channel.service.registry.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceComponentConfig;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties;
import com.scbank.process.api.fw.channel.constants.ChannelConstants;
import com.scbank.process.api.fw.channel.service.ServiceId;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionAnnotationLoader;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link IServiceRegistrar} 구현체로,
 * 서비스 정의(XML) 로딩 및 서비스 컴포넌트(@ServiceComponent 클래스)의 스프링 빈 등록,
 * 메서드 메타데이터 수집 등을 담당한다.
 * 
 * 프레임워크 채널 설정(framework.channel.*) 기반으로 초기화되며,
 * 각 서비스 ID 및 컴포넌트 클래스를 기준으로 캐시를 구성한다.
 * 
 */
@Slf4j
@RequiredArgsConstructor
public class ServiceRegistrar implements IServiceRegistrar {

    /** Spring 환경 정보 (프로퍼티 바인딩 시 사용됨) */
    private Environment environment;

    /** 서비스 정의 캐시 - 키: 서비스 ID + URL 조합, 값: 서비스 정의 메타데이터 */
    private final Map<ServiceId, ServiceDefinitionMetadata> cache = new ConcurrentHashMap<>();

    /** 메서드 메타데이터 캐시 - 키: 클래스명@메소드명, 값: 메서드 메타데이터 */
    private final Map<String, ServiceMethodMetadata> metadataMap = new ConcurrentHashMap<>();

    private final BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

    private final ServiceDefinitionAnnotationLoader serviceDefinitionAnnotationLoader = new ServiceDefinitionAnnotationLoader();

    /** 서비스 정의 XML 파서 */

    /**
     * Spring BeanDefinition 등록 전 처리 로직
     *
     * @param registry BeanDefinition 등록용 registry
     * @throws BeansException 스프링 예외
     */
    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        Binder binder = Binder.get(environment);
        ChannelProperties properties = binder.bind(ChannelConstants.FRAMEWORK_CHANNEL_PREFIX, ChannelProperties.class)
                .get();

        registerServiceComponentBean(registry, properties);
    }

    /**
     * @ServiceComponent 클래스 스캔 및 Bean 등록 + 메서드 메타데이터 수집
     *
     * @param registry   빈 등록용 레지스트리
     * @param properties 채널 속성
     */
    private void registerServiceComponentBean(BeanDefinitionRegistry registry, ChannelProperties properties) {
        if (properties == null || properties.getService() == null) {
            return;
        }

        properties.getService().stream()
                .filter(ServiceProperties::enabled)
                .forEach(service -> {
                    ServiceComponentConfig config = service.component();
                    if (config == null) {
                        log.warn("# [{}] 서비스 컴포넌트 base-packages 설정 누락", service.serviceId());
                        return;
                    }

                    List<Class<?>> candidates = scanServiceComponentClasses(config.basePackages());

                    // 서비스정의 메타데이터 로드
                    this.registerServiceDefinition(service.serviceId(), candidates);

                    candidates.forEach(clazz -> {
                        log.debug("# [{}] 서비스 컴포넌트 Bean 등록: {}", service.serviceId(), clazz.getName());
                        registerServiceComponentBean(registry, clazz);
                        registerServiceMethodMetadata(clazz);
                    });

                    log.info("# [{}] 서비스 컴포넌트 {}건 등록 완료", service.serviceId(), candidates.size());
                });
    }

    /**
     * @ServiceComponent 어노테이션이 붙은 클래스를 지정된 base-package들에서 스캔
     *
     * @param basePackages base-package 목록
     * @return 어노테이션이 붙은 클래스 목록
     */
    private List<Class<?>> scanServiceComponentClasses(List<String> basePackages) {
        List<Class<?>> result = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ServiceComponent.class));

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition bd : candidates) {
                try {
                    result.add(Class.forName(bd.getBeanClassName()));
                } catch (Exception ignored) {
                }
            }
        }
        return result;
    }

    /**
     * 
     * @param componentClass
     */
    private void registerServiceDefinition(String serviceId, List<Class<?>> candidates) {
        List<ServiceDefinitionMetadata> definitionMetadataList = this.serviceDefinitionAnnotationLoader.load(serviceId,
                candidates);
        this.registerServiceDefinitions(definitionMetadataList.toArray(ServiceDefinitionMetadata[]::new));
    }

    /**
     * 서비스 정의 메타데이터를 캐시에 등록
     *
     * @param info 메타데이터 목록
     */
    private void registerServiceDefinitions(ServiceDefinitionMetadata... info) {
        Arrays.stream(info).forEach(definition -> cache.put(ServiceId.builder()
                .service(definition.getServiceId())
                .url(definition.getUrl())
                .build(),
                definition));
    }

    /**
     * 클래스에 대해 ProxyFactoryBean 기반 빈으로 등록한다.
     *
     * @param registry    Spring 빈 정의 레지스트리
     * @param targetClass 대상 컴포넌트 클래스
     */
    private void registerServiceComponentBean(BeanDefinitionRegistry registry, Class<?> targetClass) {
        AnnotatedBeanDefinition beanDef = new AnnotatedGenericBeanDefinition(targetClass);
        AnnotationConfigUtils.processCommonDefinitionAnnotations(beanDef);
        ((AbstractBeanDefinition) beanDef).setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        beanDef.setRole(BeanDefinition.ROLE_APPLICATION);
        beanDef.setPrimary(false);
        beanDef.setLazyInit(false);

        String beanName = this.getServiceComponentBeanName(targetClass, beanDef, registry);
        registry.registerBeanDefinition(beanName, beanDef);
    }

    /**
     * @ServiceComponent 어노테이션의 ID 또는 클래스 이름으로 Bean 이름을 결정
     *
     * @param componentClass 대상 클래스
     * @return Bean 이름
     */
    private String getServiceComponentBeanName(Class<?> componentClass, BeanDefinition beanDefinition,
            BeanDefinitionRegistry registry) {
        if (!componentClass.isAnnotationPresent(ServiceComponent.class)) {
            return componentClass.getSimpleName();
        }

        ServiceComponent annotation = componentClass.getAnnotation(ServiceComponent.class);
        String componentId = annotation.id();

        return StringUtils.isEmpty(componentId) ? this.beanNameGenerator.generateBeanName(beanDefinition, registry)
                : componentId;
    }

    /**
     * 클래스의 public 메서드를 기준으로 메서드 메타데이터를 수집한다.
     *
     * @param clazz 컴포넌트 클래스
     */
    private void registerServiceMethodMetadata(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            String description = method.isAnnotationPresent(ServiceEndpoint.class)
                    ? method.getAnnotation(ServiceEndpoint.class).description()
                    : "";

            List<ParameterMetadata> parameters = Arrays.stream(method.getParameters())
                    .map(p -> new ParameterMetadata(
                            p.getName(), p.getType(), p.getAnnotations(),
                            IMessageObject.class.isAssignableFrom(p.getType())))
                    .toList();

            ServiceMethodMetadata metadata = new ServiceMethodMetadata(
                    clazz, method, description, parameters, method.getReturnType());

            metadataMap.put(buildKey(clazz, method.getName()), metadata);
        }
    }

    /**
     * 메서드 메타데이터 캐시의 키를 생성
     *
     * @param clazz      클래스
     * @param methodName 메서드명
     * @return 키 (형식: 클래스명@메서드명)
     */
    private String buildKey(Class<?> clazz, String methodName) {
        return clazz.getName() + "@" + methodName;
    }

    // ===== IServiceRegistrar 구현부 =====

    /** {@inheritDoc} */
    @Override
    public ServiceDefinitionMetadata getServiceDefinition(ServiceId serviceId) {
        return this.cache.get(serviceId);
    }

    /** {@inheritDoc} */
    @Override
    public List<ServiceDefinitionMetadata> getServiceDefinitions() {
        return new ArrayList<>(this.cache.values());
    }

    /** {@inheritDoc} */
    @Override
    public ServiceMethodMetadata getServiceMethodMetadata(Class<?> serviceClass, String methodName) {
        return metadataMap.get(buildKey(serviceClass, methodName));
    }

    /** {@inheritDoc} */
    @Override
    public ServiceMethodMetadata getServiceMethodMetadata(String serviceMethodName) {
        return metadataMap.get(serviceMethodName);
    }

    /** {@inheritDoc} */
    @Override
    public List<ServiceMethodMetadata> getAllServiceMethodMetadata() {
        return new ArrayList<>(metadataMap.values());
    }

    /** {@inheritDoc} */
    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
