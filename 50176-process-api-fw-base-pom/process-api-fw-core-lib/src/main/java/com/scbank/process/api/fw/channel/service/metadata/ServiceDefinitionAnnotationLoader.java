package com.scbank.process.api.fw.channel.service.metadata;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.core.annotation.AnnotationUtils;

import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint.AccessControl;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint.ServiceTime;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.AccessControlInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.InterceptorInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ParameterInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceTimeInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceTimeInfo.TimeRange;
import com.scbank.process.api.fw.channel.service.support.CamelCaseServiceUrlGenerator;
import com.scbank.process.api.fw.channel.service.support.IServiceUrlGenerator;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@RequiredArgsConstructor
public class ServiceDefinitionAnnotationLoader {

    private final IServiceUrlGenerator serviceUrlGenerator = new CamelCaseServiceUrlGenerator();

    /**
     * 
     * @param candidatesComponents
     * @return
     */
    public List<ServiceDefinitionMetadata> load(String serviceId, List<Class<?>> candidatesComponents) {

        List<ServiceDefinitionMetadata> definitionMetadataList = new ArrayList<>();
        candidatesComponents.forEach(clazz -> {
            log.debug("# [{}] 서비스정의 메타데이터 등록: {}", serviceId, clazz.getName());
            definitionMetadataList.addAll(this.registerServiceDefinition(serviceId, clazz));
        });
        return definitionMetadataList;
    }

    private List<ServiceDefinitionMetadata> registerServiceDefinition(String serviceId, Class<?> componentClass) {
        List<ServiceDefinitionMetadata> metadataList = new ArrayList<>();
        for (Method method : componentClass.getDeclaredMethods()) {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            ServiceEndpoint serviceEndpoint = AnnotationUtils.findAnnotation(method, ServiceEndpoint.class);
            if (serviceEndpoint == null) {
                continue;
            }

            ServiceDefinitionMetadata serviceDefinition = this.buildMetadata(serviceId, componentClass, method,
                    serviceEndpoint);
            if (serviceDefinition != null) {
                metadataList.add(serviceDefinition);
            }

        }

        return metadataList;
    }

    /**
     * 
     * @param serviceId 서비스ID
     * @param beanType  서비스컴포넌트 타입
     * @param method    메소드
     * @param endpoint  ServiceEndpoint
     * @return
     */
    private ServiceDefinitionMetadata buildMetadata(String serviceId, Class<?> beanType, Method method,
            ServiceEndpoint endpoint) {

        String description = endpoint.name();
        return ServiceDefinitionMetadata.builder()
                .serviceId(serviceId)
                .url(this.serviceUrlGenerator.generate(beanType, method))
                .description(description)
                .services(List.of(buildServiceInfo(beanType, method, endpoint)))
                .accessControl(this.buildAccessControlInfo(endpoint))
                .interceptors(this.buildServiceInterceptors(endpoint))
                .parameters(this.buildServiceParameters(endpoint))
                .serviceTime(this.buildServiceTimeInfo(endpoint))
                .build();
    }

    /**
     * 서비스 컴포넌트 정보 처리
     * 
     * @param beanType 서비스컴포넌트 타입
     * @param method   메소드
     * @param endpoint ServiceEndpoint
     * @return
     */
    private ServiceInfo buildServiceInfo(Class<?> beanType, Method method,
            ServiceEndpoint endpoint) {

        String description = endpoint.name();
        String component = beanType.getName() + "@" + method.getName();
        return ServiceInfo.builder()
                .priority(100)
                .description(description)
                .component(component)
                .build();
    }

    /**
     * 접근제어정보 처리
     * 
     * @param endpoint ServiceEndpoint
     * @return
     */
    private AccessControlInfo buildAccessControlInfo(ServiceEndpoint endpoint) {
        AccessControl accessControl = endpoint.accessControl();
        return new AccessControlInfo(accessControl.requiredLogin(), accessControl.channels());
    }

    /**
     * 서비스 파라미터 정보 빌드
     * 
     * @param endpoint
     * @return
     */
    private List<ParameterInfo> buildServiceParameters(ServiceEndpoint endpoint) {
        ServiceEndpoint.Parameter[] parameters = endpoint.parameters();
        if (parameters == null || parameters.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.stream(parameters).map(v -> new ParameterInfo(v.name(), v.value())).toList();
    }

    /**
     * 서비스 인터셉터정보 빌드
     * 
     * @param endpoint
     * @return
     */
    private List<InterceptorInfo> buildServiceInterceptors(ServiceEndpoint endpoint) {
        ServiceEndpoint.Interceptor[] interceptors = endpoint.interceptors();
        if (interceptors == null || interceptors.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.stream(interceptors).map(v -> new InterceptorInfo(v.value())).toList();
    }

    /**
     * 서비스 이용시간 정보 빌드
     * 
     * @param endpoint
     * @return
     */
    private ServiceTimeInfo buildServiceTimeInfo(ServiceEndpoint endpoint) {
        ServiceTime serviceTime = endpoint.serviceTime();
        if (serviceTime == null) {
            return null;
        }

        return new ServiceTimeInfo(
                serviceTime.enabled(),
                buildTimeRangeInfo(serviceTime.businessDay()),
                buildTimeRangeInfo(serviceTime.holiday()));
    }

    /**
     * 영업일/휴일 서비스이용 시작/종료시간 정보 빌드
     * 
     * @param timeRange
     * @return
     */
    private TimeRange buildTimeRangeInfo(ServiceEndpoint.ServiceTime.TimeRange timeRange) {
        String startTime = StringUtils.defaultIfEmpty(timeRange.startTime(), "");
        String endTime = StringUtils.defaultIfEmpty(timeRange.endTime(), "");
        return new TimeRange(startTime, endTime);
    }
}
