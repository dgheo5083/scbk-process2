package com.scbank.process.api.fw.integration.rebound;

import com.scbank.process.api.fw.integration.context.IntegrationContext;

/**
 * <pre>
 * 연속 거래(재요청/Rebound) 처리를 위한 전략 인터페이스입니다.
 *
 * 대용량 다건 조회나 페이징 방식의 연동 API 등에서,
 * 응답 결과를 기반으로 후속 요청을 구성하거나 반복 호출이 필요한 경우 이 전략을 적용합니다.
 *
 * 주요 기능:
 * 1. {@link #isContinue} - 연속 거래 수행 여부 판단
 * 2. {@link #handleData} - 다음 요청 메시지 조립
 * 3. {@link #getListFieldName} - 응답 내 결과 리스트 필드명 지정
 *
 * 실제 구현체는 연동 시스템의 응답 구조나 호출 방식에 맞게 이 인터페이스를 구현하면 됩니다.
 * </pre>
 *
 * @param <I> 요청(Request) 타입
 * @param <O> 응답(Response) 타입
 *
 * @author gasigol
 * @version 1.0
 * @since 2025.04.21
 */
public interface IntegrationReboundStrategy<I, O> {

    /**
     * 연속 거래를 계속 수행할지 여부를 판단합니다.
     *
     * @param context  연동 컨텍스트
     * @param response 현재 응답 객체
     * @return true: 후속 거래 필요, false: 종료
     */
    boolean isContinue(IntegrationContext context, O response);

    /**
     * 다음 연속 거래에 사용할 요청 객체를 생성합니다.
     *
     * @param request  이전 요청 객체
     * @param response 이전 응답 객체
     * @return 다음 요청 객체
     */
    I handleData(I request, O response);

    /**
     * 
     * @return
     */
    int getMaxLoopCnt();

    /**
     * 연속 거래 응답 결과를 저장할 필드명(예: "itemList")을 반환합니다.
     * 해당 필드는 통합 결과 병합 시 사용됩니다.
     *
     * @return 결과 리스트 필드명
     */
    String getListFieldName();
}
