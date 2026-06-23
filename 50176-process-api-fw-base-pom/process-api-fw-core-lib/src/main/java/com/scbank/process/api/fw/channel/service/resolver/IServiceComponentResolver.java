package com.scbank.process.api.fw.channel.service.resolver;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceInfo;

/**
 * 서비스 실행을 위한 컴포넌트를 동적으로 선택하는 전략 인터페이스입니다.
 * 
 * ServiceDefinitionMetadata 내에 정의된 여러 서비스 조건 중 하나를 선택할 때,
 * 호출 컨텍스트 ({@link IServiceContext})를 기반으로 실행 대상 {@link ServiceInfo}를 결정합니다.
 * 
 * 예를 들어 device, channel, userType 등의 컨텍스트 값에 따라 다른 서비스 구현을 선택할 수 있습니다.
 *
 * <pre>{@code
 * // 예시: 람다 방식 등록
 * resolver = ctx -> {
 *     if ("PC".equals(ctx.getParam("device"))) {
 *         return pcLoginServiceInfo;
 *     } else {
 *         return mobileLoginServiceInfo;
 *     }
 * };
 * }</pre>
 *
 * @author gasigol
 * @since 2025.04.15
 * @version 1.0
 */
@FunctionalInterface
public interface IServiceComponentResolver {

    /**
     * 주어진 서비스 컨텍스트를 기반으로 실행할 서비스 정보를 결정합니다.
     *
     * @param ctx 서비스 실행 컨텍스트 (입력값, 요청자 정보 등 포함)
     * @return 실행할 서비스 정보
     * @throws Exception 조건 판단 실패 또는 매핑 실패 시 예외
     */
    ServiceInfo resolve(IServiceContext ctx) throws Exception;
}
