package com.scbank.process.api.fw.channel.service.registry;

import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.PriorityOrdered;

import com.scbank.process.api.fw.channel.service.ServiceId;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;

public interface IServiceRegistrar extends BeanDefinitionRegistryPostProcessor, PriorityOrdered, EnvironmentAware {

    /**
     * 주어진 {@link ServiceId}에 해당하는 서비스 정의 메타데이터를 반환합니다.
     *
     * @param id 서비스 ID + URL 조합
     * @return 해당 요청에 매핑된 서비스 정의 정보 (null 가능)
     */
    ServiceDefinitionMetadata getServiceDefinition(ServiceId id);

    /**
     * 등록된 전체 서비스 정의 메타데이터 목록을 반환합니다.
     *
     * @return 전체 서비스 definition 리스트
     */
    List<ServiceDefinitionMetadata> getServiceDefinitions();

    /**
     * 클래스 및 메서드명 기반으로 메타데이터를 조회합니다.
     *
     * @param serviceClass 서비스 컴포넌트 클래스
     * @param methodName   메서드 이름
     * @return 해당 메서드에 대한 메타데이터, 없을 경우 null
     */
    ServiceMethodMetadata getServiceMethodMetadata(Class<?> serviceClass, String methodName);

    /**
     * 클래스명 메소드명 문자열을 조합으로 메타데이터를 조회합니다.
     * 
     * @param serviceMethodName 서비스클래스명 + '@' + 메서드명
     * @return 해당 메서드에 대한 메타데이터, 없을 경우 null
     */
    ServiceMethodMetadata getServiceMethodMetadata(String serviceMethodName);

    /**
     * 등록된 모든 서비스 메서드 메타데이터 목록을 반환합니다.
     *
     * @return 전체 메타데이터 리스트
     */
    List<ServiceMethodMetadata> getAllServiceMethodMetadata();
}
