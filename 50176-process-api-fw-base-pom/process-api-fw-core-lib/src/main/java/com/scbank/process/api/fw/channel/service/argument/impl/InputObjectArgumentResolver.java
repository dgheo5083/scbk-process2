package com.scbank.process.api.fw.channel.service.argument.impl;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.argument.IServiceMethodArgumentResolver;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;
import com.scbank.process.api.fw.message.IMessageObject;

/**
 * <pre>
 * {@link IMessageObject} 타입의 파라미터를 처리하는 기본 ArgumentResolver 구현체입니다.
 *
 * 이 Resolver는 서비스 메서드 파라미터가 {@code
 * IMessageObject
 * } 또는 그 하위 클래스인 경우에 동작하며,
 * Dispatcher로부터 전달된 입력 객체(input)를 그대로 메서드 인자로 사용합니다.
 * </pre>
 *
 * <p>
 * 예시 서비스 메서드:
 * 
 * <pre>{@code
 * &#64;ComponentOperation(description = "사용자 로그인")
 * public UserLoginOutput login(UserLoginInput input) {
 *     ...
 * }
 * }</pre>
 *
 * 위 예시에서 {@code UserLoginInput}은 {@code IMessageObject}를 구현한 입력 DTO이며,
 * 본 Resolver에 의해 자동으로 전달됩니다.
 *
 * 사용 조건:
 * <ul>
 * <li>파라미터 타입이 {@code IMessageObject} 또는 하위 클래스일 것</li>
 * <li>입력 객체(input)는 Dispatcher에서 전달된 JSON 바디 또는 외부 메시지 구조일 수 있음</li>
 * </ul>
 *
 * @author Min-jun
 * @version 1.0
 * @since 2025.04.25
 */
public class InputObjectArgumentResolver implements IServiceMethodArgumentResolver {

    /**
     * 주어진 파라미터 타입이 {@link IMessageObject} 타입이거나 그 하위 타입인지 확인합니다.
     *
     * @param parameter 파라미터 메타데이터 (타입, 이름, 애노테이션 포함)
     * @return 처리 가능 여부 (true = 해당 파라미터를 이 Resolver가 처리함)
     */
    @Override
    public boolean supports(ParameterMetadata parameter) {
        return parameter.isBody();
    }

    /**
     * Dispatcher로부터 전달된 입력 객체를 그대로 반환합니다.
     *
     * 이 메서드는 supports()가 true인 경우에만 호출되며, 일반적으로 JSON 메시지 바디에서 파싱된 DTO가 전달됩니다.
     *
     * @param parameter 바인딩 대상 파라미터 메타데이터
     * @param context   현재 서비스 실행 컨텍스트 (사용자 정보, 요청 등 포함)
     * @param input     Dispatcher에서 전달된 입력 객체 (nullable 가능)
     * @return 해당 파라미터에 전달할 인자 (보통 입력 DTO 객체)
     */
    @Override
    public Object resolveArgument(ParameterMetadata parameter, IServiceContext context, Object input) {
        return input;
    }
}
