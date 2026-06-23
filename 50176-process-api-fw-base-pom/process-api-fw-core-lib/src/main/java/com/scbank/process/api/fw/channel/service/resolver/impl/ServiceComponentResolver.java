package com.scbank.process.api.fw.channel.service.resolver.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.condition.IServiceConditionEvaluator;
import com.scbank.process.api.fw.channel.service.condition.impl.SpelServiceConditionEvaluator;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceInfo;
import com.scbank.process.api.fw.channel.service.resolver.IServiceComponentResolver;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 서비스 라우팅 결정기 구현체
 *
 * 요청된 서비스 정의(ServiceDefinitionMetadata) 내의 조건식들을 기반으로 
 * 실제로 실행할 서비스(ServiceInfo)를 결정합니다.
 * 
 * 조건은 Spring Expression Language(SpEL) 기반 평가기를 사용해 처리하며,
 * 조건에 맞는 컴포넌트가 없을 경우 fallback 처리를 수행합니다.
 * </pre>
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 15.
 */
@Slf4j
public class ServiceComponentResolver implements IServiceComponentResolver {

    /** SpEL 기반 조건 평가기 */
    private final IServiceConditionEvaluator serviceConditionEvaluator = new SpelServiceConditionEvaluator();

    /**
     * 서비스 정보 목록 중 조건에 맞는 컴포넌트를 결정합니다.
     *
     * @param ctx 현재 서비스 컨텍스트
     * @return 조건에 맞는 서비스 컴포넌트
     * @throws Exception 평가 실패 또는 fallback 실패 시 예외
     */
    @Override
    public ServiceInfo resolve(IServiceContext ctx) throws Exception {
        // 요청에 대한 전체 서비스 정의 조회
        ServiceDefinitionMetadata serviceDefinition = ctx.serviceDefinition();
        List<ServiceInfo> services = serviceDefinition.getServices();

        // 우선순위(priority) 오름차순 정렬 후 조건에 맞는 서비스 필터링
        Optional<ServiceInfo> matched = services.stream()
                .sorted(Comparator.comparingInt(ServiceInfo::getPriority))
                .filter(s -> !s.isFallback()) // fallback 아닌 서비스만 대상
                .filter(route -> {
                    String cond = route.getCondition();
                    return serviceConditionEvaluator.evaluate(ctx, cond); // SpEL 조건 평가
                })
                .findFirst();

        // 조건에 맞는 서비스가 없을 경우 fallback 처리
        return matched.orElseGet(() -> resolveFallback(serviceDefinition, services));
    }

    /**
     * fallback 컴포넌트를 선택합니다.
     * fallbackRef가 지정된 경우 해당 컴포넌트를 참조하며, 그렇지 않으면 fallback 자신을 사용합니다.
     *
     * @param serviceDefinition 서비스 정의 메타
     * @param services          전체 서비스 목록
     * @return fallback 서비스 정보
     */
    private ServiceInfo resolveFallback(ServiceDefinitionMetadata serviceDefinition, List<ServiceInfo> services) {
        if (log.isInfoEnabled()) {
            log.info("# 서비스 컴포넌트 fallback 처리, [{}][{}][{}] 조건에 맞는 서비스 컴포넌트가 없음",
                    serviceDefinition.getServiceId(), serviceDefinition.getDescription(), serviceDefinition.getUrl());
        }

        return services.stream()
                .filter(ServiceInfo::isFallback)
                .findFirst()
                .flatMap(fallback -> {
                    String ref = fallback.getFallbackRef();
                    if (ref != null) {
                        // fallback 참조가 있는 경우 해당 컴포넌트를 찾아 반환
                        return services.stream()
                                .filter(svc -> ref.equals(svc.getComponent()))
                                .findFirst();
                    }
                    // fallback 참조가 없으면 fallback 자신을 반환
                    return Optional.of(fallback);
                })
                .orElseThrow(() -> new IllegalStateException("조건에 맞는 서비스가 없습니다."));
    }
}
