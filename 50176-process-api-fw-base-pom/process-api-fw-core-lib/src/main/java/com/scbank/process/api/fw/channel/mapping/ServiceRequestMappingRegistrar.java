package com.scbank.process.api.fw.channel.mapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties.ServiceMappingConfig;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 서비스 매핑 설정 정보를 기반으로 컨트롤러의 요청 URL을 스프링에 동적으로 등록합니다.
 * 
 * 각 서비스 설정에 포함된 URL 패턴, Content-Type, HTTP Method를 기준으로
 * {@link RequestMappingHandlerMapping}을 통해 Runtime 시점에 매핑을 수행합니다.
 * 
 * 등록 시점은 Spring ApplicationReadyEvent 발생 이후입니다.
 * 
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
public class ServiceRequestMappingRegistrar implements ApplicationContextAware, SmartInitializingSingleton {

    /** Spring MVC 요청 매핑 핸들러 */
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    /** 프레임워크 채널 설정 정보 */
    private final ChannelProperties channelProperties;

    /** Spring ApplicationContext */
    private ApplicationContext applicationContext;

    /** 기본 컨트롤러 클래스 */
    private Class<?> defaultControllerClass;

    /** 기본 컨트롤러 메서드명 */
    private String defaultControllerMethod;

    @Override
    public void afterSingletonsInstantiated() {
        init();
    }

    /**
     * Spring SmartInitializingSingleton 초기화 이후, 서비스별 요청 매핑 등록을 시작합니다.
     */
    // @EventListener(ApplicationReadyEvent.class)
    public void init() {

        if (channelProperties == null || channelProperties.getService() == null) {
            return;
        }

        this.defaultControllerClass = this.channelProperties.getDefaultControllerClass();
        this.defaultControllerMethod = this.channelProperties.getDefaultControllerMethod();

        List<ServiceProperties> service = this.channelProperties.getService();
        service.stream()
                .filter(ServiceProperties::enabled)
                .forEach(s -> {
                    try {
                        this.registerServiceRequestMapping(s.serviceId(), s);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
    }

    /**
     * 개별 서비스 설정에 따라 요청 URL을 매핑합니다.
     *
     * @param serviceId 서비스 식별자
     * @param service   서비스 설정
     * @throws Exception 매핑 실패 시 예외 발생
     */
    private void registerServiceRequestMapping(String serviceId, ServiceProperties service) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("# 프레임워크 서비스 [{}] 요청 URL 등록 시작..", serviceId);
        }

        String basePath = StringUtils.defaultIfBlank(service.basePath(), "");
        Class<?> controllerClass = service.controllerClass() == null ? this.defaultControllerClass
                : service.controllerClass();
        String controllerMethod = StringUtils.isEmpty(service.controllerMethod()) ? this.defaultControllerMethod
                : service.controllerMethod();

        // 컨트롤러 Bean 및 Method 조회
        Object handler = applicationContext.getBean(controllerClass);
        Method method = Arrays.stream(handler.getClass().getDeclaredMethods())
                .filter(m -> m.getName().equals(controllerMethod))
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException(controllerMethod));

        ServiceMappingConfig serviceMappingConfig = service.serviceMapping();
        List<String> allowedUrlPatterns = serviceMappingConfig.allowedUrlPatterns();
        List<String> allowedContentTypes = serviceMappingConfig.allowedContentTypes();
        List<String> allowedMethods = serviceMappingConfig.allowedMethods();

        // HTTP Method 배열 변환
        RequestMethod[] requestMethod = allowedMethods
                .stream()
                .map(m -> Optional.ofNullable(RequestMethod.resolve(m)).orElse(RequestMethod.POST))
                .toArray(RequestMethod[]::new);

        // URL 패턴별 매핑 등록
        allowedUrlPatterns.forEach(url -> {
            String fullPath = basePath + url;

            RequestMappingInfo mappingInfo = RequestMappingInfo
                    .paths(fullPath)
                    .methods(requestMethod)
                    .consumes(allowedContentTypes.toArray(String[]::new))
                    .build();

            requestMappingHandlerMapping.registerMapping(mappingInfo, handler, method);

            if (log.isInfoEnabled()) {
                log.info("# 프레임워크 서비스 [{}] 요청 URL 매핑 등록완료  - {}", serviceId, mappingInfo);
            }
        });
    }

    /**
     * ApplicationContext 주입 처리
     *
     * @param applicationContext Spring ApplicationContext
     * @throws BeansException 오류 발생 시
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
