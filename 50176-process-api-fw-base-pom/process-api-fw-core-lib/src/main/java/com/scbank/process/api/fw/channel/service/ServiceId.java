package com.scbank.process.api.fw.channel.service;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * Dispatcher 요청 시 URI에 따라 매핑된 서비스 식별자(ServiceId)를 나타내는 모델입니다.
 *
 * 이 객체는 {@link ServiceIdResolver}에 의해 생성되며,
 * 서비스 실행 시 어떤 서비스 영역에 속하는 요청인지를 식별하는 데 사용됩니다.
 * </pre>
 *
 * <p>
 * 주로 다음과 같은 용도로 사용됩니다:
 * <ul>
 * <li>서비스 분기 처리 (MULTI 서비스 모드)</li>
 * <li>요청 URI 기반 Swagger 문서 그룹 결정</li>
 * <li>로깅 및 추적 시 서비스 영역 구분</li>
 * </ul>
 *
 * 예:
 * <code>
 * ServiceId(service="COMMON", url="/api/common/login/userLogin")
 * </code>
 *
 * @author gasigol
 * @version 1.0
 * @since 2025.04.16
 */
@Data
@EqualsAndHashCode
@Builder
public class ServiceId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 서비스 식별자 (예: COMMON, AUTH, ADMIN 등)
     */
    private String service;

    /**
     * 실제 요청된 URI 전체 경로
     */
    private String url;
}
