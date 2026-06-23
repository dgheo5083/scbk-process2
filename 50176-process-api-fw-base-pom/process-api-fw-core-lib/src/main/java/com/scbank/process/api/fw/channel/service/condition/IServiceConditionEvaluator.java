package com.scbank.process.api.fw.channel.service.condition;

import com.scbank.process.api.fw.channel.context.IServiceContext;

/**
 * <pre>
 * 서비스 실행 조건을 평가하는 인터페이스입니다.
 *
 * Dispatcher는 {@code
 * service - definition.xml
 * } 또는 DB에 정의된 조건 문자열을 기반으로,
 * {@code
 * IServiceContext
 * } 정보를 바인딩하여 평가한 후, 해당 서비스 실행 여부를 결정합니다.
 *
 * 조건 예시:
 * - "device == 'PC'"
 * - "userType == 'BANKING' and locale == 'ko'"
 * </pre>
 *
 * 조건 표현 언어는 SpEL, MVEL, Groovy 등으로 자유롭게 확장 가능하며,
 * 구현체는
 * {@link co.kr.scbank.framework.channel.service.resolver.impl.ServiceComponentResolver}에
 * 의해 사용됩니다.
 *
 * @author gasigol
 * @version 1.0
 * @since 2025.04.15
 */
public interface IServiceConditionEvaluator {

    /**
     * 서비스 조건식을 평가합니다.
     *
     * @param ctx       현재 요청의 서비스 실행 컨텍스트
     * @param condition 평가할 조건 문자열 (SpEL, MVEL 등 지원)
     * @return true: 조건 만족 → 서비스 실행 대상 / false: 조건 불만족
     */
    boolean evaluate(IServiceContext ctx, String condition);
}
