package com.scbank.process.api.fw.channel.config;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.constants.ChannelConstants;
import com.scbank.process.api.fw.channel.context.handler.DefaultServiceContextHandler;
import com.scbank.process.api.fw.channel.context.handler.IServiceContextHandler;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.channel.interceptor.IRequestInterceptor;
import com.scbank.process.api.fw.channel.interceptor.IRequestInterceptorRegistrar;
import com.scbank.process.api.fw.channel.interceptor.RequestInterceptorRegistrar;
import com.scbank.process.api.fw.channel.interceptor.ServiceRequestInterceptor;
import com.scbank.process.api.fw.channel.mapping.ServiceRequestMappingRegistrar;
import com.scbank.process.api.fw.channel.service.ServiceEndpointRequests;
import com.scbank.process.api.fw.channel.service.ServiceIdResolver;
import com.scbank.process.api.fw.channel.service.argument.ServiceMethodArgumentResolverComposite;
import com.scbank.process.api.fw.channel.service.argument.impl.InputObjectArgumentResolver;
import com.scbank.process.api.fw.channel.service.argument.impl.RequestHeaderArgumentResolver;
import com.scbank.process.api.fw.channel.service.argument.impl.ServiceContextArgumentResolver;
import com.scbank.process.api.fw.channel.service.executor.IServiceComponentExecutorFactory;
import com.scbank.process.api.fw.channel.service.executor.impl.DefaultServiceComponentExecutorFactory;
import com.scbank.process.api.fw.channel.service.interceptor.IServiceInterceptor;
import com.scbank.process.api.fw.channel.service.interceptor.ServiceInterceptorComposite;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.channel.service.registry.impl.ServiceRegistrar;
import com.scbank.process.api.fw.core.uuid.IIdentifyGenerator;
import com.scbank.process.api.fw.session.ISessionContextResolver;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 프레임워크 채널 Dispatcher 서비스 흐름 설정 클래스입니다.
 *
 * 'framework.channel.enabled=true'일 때 활성화되며,
 * 서비스 매핑, 컴포넌트 등록, 인터셉터 설정, Executor 구성 등
 * Dispatcher 흐름에 필요한 기본 인프라 Bean을 등록합니다.
 * </pre>
 * <p>
 * 주요 구성:
 * <ul>
 * <li>서비스 Context 핸들러 등록</li>
 * <li>서비스 정의 XML 등록기</li>
 * <li>요청 매핑(RequestMapping) 등록기</li>
 * <li>인터셉터 등록기 및 서비스 전용 인터셉터</li>
 * <li>ServiceComponent 스캐너 및 등록기</li>
 * <li>메서드 메타데이터 등록기</li>
 * <li>ArgumentResolver 및 Executor Factory 등록</li>
 * </ul>
 *
 * @author sungdon
 * @since 2025.04.26
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = ChannelConstants.FRAMEWORK_CHANNEL_ENABLED, havingValue = "true")
public class ChannelServiceConfigurations {

    /**
     * 서비스 ID를 식별하는 Resolver 등록
     */
    @Bean
    @ConditionalOnMissingBean(ServiceIdResolver.class)
    ServiceIdResolver serviceIdResolver(ChannelProperties properties) {
        return new ServiceIdResolver(properties);
    }

    /**
     * Dispatcher 흐름용 서비스 컨텍스트 핸들러 등록
     */
    @Bean
    IServiceContextHandler serviceContextHandler(
            @Qualifier("requestUUIdGenerator") IIdentifyGenerator identifyGenerator,
            IDeviceResolver deviceResolver,
            IServiceRegistrar serviceRegistrar,
            LocaleResolver localeResolver,
            ServiceIdResolver serviceIdResolver,
            ISessionContextResolver sessionContextResolver) {
        return new DefaultServiceContextHandler(identifyGenerator, deviceResolver, serviceRegistrar,
                localeResolver, serviceIdResolver, sessionContextResolver);
    }

    /**
     * 서비스 요청 전용 인터셉터 등록
     */
    @Order(Ordered.HIGHEST_PRECEDENCE + 10)
    @Bean("serviceRequestInterceptor")
    @ConditionalOnMissingBean(ServiceRequestInterceptor.class)
    MappedInterceptor serviceRequestInterceptor(
            ServiceEndpointRequests serviceEndpointRequests,
            IServiceContextHandler serviceContextHandler,
            ObjectProvider<IRequestInterceptor> interceptorsProvider) {

        ServiceRequestInterceptor serviceRequestInterceptor = new ServiceRequestInterceptor(serviceContextHandler,
                interceptorsProvider);
        serviceRequestInterceptor.init();
        return new MappedInterceptor(serviceEndpointRequests.enabledServiceEndpointUrls().toArray(String[]::new),
                serviceRequestInterceptor);
    }

    // ************************************************************************
    // 서비스 정의 및 매핑 관련
    // ************************************************************************

    /**
     * 서비스 정의 XML 등록기
     */
    @Bean("serviceRegistrar")
    @ConditionalOnMissingBean(IServiceRegistrar.class)
    IServiceRegistrar serviceRegistrar() {
        return new ServiceRegistrar();
    }

    /**
     * Spring RequestMapping 기반 요청-엔드포인트 매핑 등록기
     */
    @Bean
    @ConditionalOnMissingBean(ServiceRequestMappingRegistrar.class)
    @DependsOn("channelProperties")
    ServiceRequestMappingRegistrar serviceRequestMappingRegistrar(
            @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping requestMappingHandlerMapping,
            ChannelProperties channelProperties) {
        return new ServiceRequestMappingRegistrar(requestMappingHandlerMapping, channelProperties);
    }

    /**
     * 요청 인터셉터 등록기 (Interceptor 목록 등록 및 적용)
     */
    @Bean
    @ConditionalOnMissingBean(IRequestInterceptorRegistrar.class)
    IRequestInterceptorRegistrar requestInterceptorRegistrar() {
        return new RequestInterceptorRegistrar();
    }

    // ************************************************************************
    // 엔드포인트 관리
    // ************************************************************************

    /**
     * 서비스 엔드포인트 목록 집계 Bean (Swagger, RestDocs 등에 사용 가능)
     */
    @Bean("serviceEndpointRequests")
    @ConditionalOnMissingBean(ServiceEndpointRequests.class)
    @DependsOn({ "channelProperties" })
    ServiceEndpointRequests serviceEndpointRequests(ChannelProperties channelProperties) {
        return new ServiceEndpointRequests(channelProperties);
    }

    // ************************************************************************
    // 서비스 실행/호출 관련
    // ************************************************************************

    /**
     * 서비스 실행 전/후 처리 인터셉터 컴포지트
     * 
     * @param interceptors 인터셉터 빈 목록
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ServiceInterceptorComposite.class)
    ServiceInterceptorComposite serviceInterceptorComposite(List<IServiceInterceptor> interceptors) {
        return new ServiceInterceptorComposite(interceptors);
    }

    /**
     * 서비스 메서드 Argument Resolver 조합 (InputObject, ServiceContext 지원)
     */
    @Bean
    @ConditionalOnMissingBean(ServiceMethodArgumentResolverComposite.class)
    ServiceMethodArgumentResolverComposite serviceMethodArgumentResolverComposite() {
        return new ServiceMethodArgumentResolverComposite(
                List.of(new InputObjectArgumentResolver(),
                        new ServiceContextArgumentResolver(),
                        new RequestHeaderArgumentResolver()));
    }

    /**
     * 서비스 실행기(Executor) 생성 팩토리 등록
     */
    @Bean
    @ConditionalOnMissingBean(IServiceComponentExecutorFactory.class)
    @DependsOn({ "serviceMethodArgumentResolverComposite", "serviceInterceptorComposite" })
    IServiceComponentExecutorFactory serviceComponentExecutorFactory(
            ServiceMethodArgumentResolverComposite serviceMethodArgumentResolverComposite,
            ServiceInterceptorComposite serviceInterceptorComposite) {
        return new DefaultServiceComponentExecutorFactory(
                serviceMethodArgumentResolverComposite,
                serviceInterceptorComposite);
    }

    // @Bean
    // @DependsOn({ "serviceRequestInterceptor", "serviceEndpointRequests" })
    // WebMvcConfigurer interceptorConfigurer(
    // ServiceRequestInterceptor serviceRequestInterceptor,
    // ServiceEndpointRequests serviceEndpointRequests) {
    // return new WebMvcConfigurer() {
    // @Override
    // public void addInterceptors(@NonNull InterceptorRegistry registry) {
    // log.info("# 프레임워크 serviceRequestInterceptor 등록");
    // registry.addInterceptor(serviceRequestInterceptor)
    // .addPathPatterns(serviceEndpointRequests.enabledServiceEndpointUrls().toArray(String[]::new));
    // }
    // };
    // }
}
