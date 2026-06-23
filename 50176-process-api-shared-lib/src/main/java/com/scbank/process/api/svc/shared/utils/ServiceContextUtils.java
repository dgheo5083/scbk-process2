package com.scbank.process.api.svc.shared.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ParameterInfo;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceContextUtils {

    /**
     * 공통 서비스 파리미터 조회
     * 
     * @title @ServiceEndpoint 어노테이션의 파라미터 조회
     * @param IServiceContext : 서비스 컨텍스트
     * @param String...       : 조회할 파라미터 이름
     * @return Map<String, String> : 서비스파라미터키 = 서비스파라미터값
     */
    public Map<String, String> getServiceParameterMap(IServiceContext sCtx, String... parameterName) {

        if (parameterName == null || parameterName.length < 1) {
            return Map.of();
        }

        Map<String, String> serviceParameters = getServiceParameterMap(sCtx);

        if (serviceParameters.isEmpty()) {
            return Map.of();
        }

        // 전체 서비스 파라미터 중 parameterName 대상 파라미터만 반환
        return serviceParameters.entrySet().stream()
                .filter(p -> Set.of(parameterName).contains(p.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 공통 서비스 파리미터 전체 조회
     * 
     * @title @ServiceEndpoint 어노테이션의 파라미터 전체 조회
     * @param IServiceContext - 서비스 컨텍스트
     * @return Map<String, String> - 서비스파라미터키 : 서비스파라미터값
     */
    public Map<String, String> getServiceParameterMap(IServiceContext sCtx) {
        if (sCtx.serviceDefinition() == null) {
            return Map.of();
        }

        List<ParameterInfo> serviceParameters = sCtx.serviceDefinition().getParameters();

        if (CollectionUtils.isEmpty(serviceParameters)) {
            return Map.of();
        }

        // 서비스 파라미터 Map 반환
        return serviceParameters.stream()
                .collect(Collectors.toMap(
                        p -> p.parameterName(),
                        p -> p.parameterValue()));
    }

}
