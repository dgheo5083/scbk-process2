package com.scbank.process.api.fw.channel.service.argument.impl;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.argument.IServiceMethodArgumentResolver;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;

/**
 * <pre>
 * {@link IServiceContext} 타입의 파라미터를 처리하는 ArgumentResolver 구현체입니다.
 *
 * 서비스 메서드의 파라미터 중 {@code
 * IServiceContext
 * } 또는 하위 타입이 있을 경우,
 * 현재 실행 중인 컨텍스트 객체를 해당 파라미터에 자동 주입해줍니다.
 * </pre>
 *
 * <p>
 * 이 Resolver는 다음과 같은 메서드 정의를 처리할 수 있습니다:
 * 
 * <pre>{@code
 * public UserLoginOutput login(UserLoginInput input, IServiceContext context) {
 *     String traceId = context.getTraceId();
 *     ...
 * }
 * }</pre>
 *
 * Dispatcher 실행 시점에 {@code IServiceContext}는 이미 준비되어 있으며,
 * 요청 사용자, 트레이스 ID, 내부 attribute 등을 포함합니다.
 *
 * 사용 조건:
 * <ul>
 * <li>파라미터 타입이 {@code IServiceContext} 또는 그 하위 클래스일 것</li>
 * <li>입력 DTO 외에 실행 컨텍스트가 필요한 경우 사용</li>
 * </ul>
 *
 * @author sungdon.choi
 * @since 2025.04.25
 */
public class ServiceContextArgumentResolver implements IServiceMethodArgumentResolver {

    /**
     * 해당 파라미터가 {@link IServiceContext} 타입인지 여부를 반환합니다.
     *
     * @param parameter 파라미터 메타데이터
     * @return true: 이 Resolver가 처리 대상임
     */
    @Override
    public boolean supports(ParameterMetadata parameter) {
        return IServiceContext.class.isAssignableFrom(parameter.getType());
    }

    /**
     * 현재 요청의 {@link IServiceContext} 객체를 그대로 반환합니다.
     *
     * @param parameter 파라미터 메타데이터
     * @param context   현재 실행 컨텍스트
     * @param input     입력 DTO (사용하지 않음)
     * @return IServiceContext 객체
     */
    @Override
    public Object resolveArgument(ParameterMetadata parameter, IServiceContext context, Object input) {
        return context;
    }
}
