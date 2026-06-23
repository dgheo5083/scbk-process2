package com.scbank.process.api.fw.channel.service.argument;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;

/**
 * <pre>
 * 서비스 메서드의 단일 파라미터에 대해 처리 가능한 Argument Resolver 인터페이스입니다.
 *
 * 이 인터페이스의 구현체는 특정 파라미터 타입, 이름, 또는 애노테이션을 기준으로
 * 해당 파라미터를 어떻게 바인딩할지 정의합니다.
 * </pre>
 *
 * <p>
 * 예: IMessageObject 입력 DTO, IServiceContext, HttpServletRequest, @Param("id") 등
 * </p>
 *
 * Spring MVC의 {@code HandlerMethodArgumentResolver} 구조와 유사하며,
 * {@link ServiceMethodArgumentResolverComposite} 내에서 다수의 Resolver 중 적절한
 * Resolver가 선택되어 사용됩니다.
 *
 * @author Min-jun
 * @version 1.0
 * @since 2025.04.25
 */
public interface IServiceMethodArgumentResolver {

    /**
     * 해당 파라미터가 이 Resolver에 의해 처리 가능한지 여부를 판단합니다.
     *
     * @param parameter 파라미터 메타데이터 (타입, 이름, 애노테이션 포함)
     * @return true일 경우 resolveArgument() 호출 대상이 됩니다
     */
    boolean supports(ParameterMetadata parameter);

    /**
     * 파라미터 값을 실제 인자로 변환하여 반환합니다.
     * context 및 input 객체를 조합하여 파라미터 타입에 맞는 값을 생성합니다.
     *
     * @param parameter 파라미터 메타데이터
     * @param context   서비스 실행 컨텍스트
     * @param input     입력 객체 (예: JSON DTO)
     * @return 실행 시 전달할 실제 인자 값
     * @throws Exception 바인딩 실패 시 예외 발생
     */
    Object resolveArgument(ParameterMetadata parameter, IServiceContext context, Object input) throws Exception;
}
