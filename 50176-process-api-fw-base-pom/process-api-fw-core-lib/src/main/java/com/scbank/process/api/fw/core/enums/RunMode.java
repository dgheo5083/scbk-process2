package com.scbank.process.api.fw.core.enums;

/**
 * 실행 환경 모드 (RunMode)
 * <p>
 * 애플리케이션의 실행 목적에 따라 구분되는 런타임 실행 모드입니다.
 * 환경별 설정 분기, 로깅 정책, 외부 연동 대상 등을 구분하는 데 사용됩니다.
 * </p>
 *
 * <ul>
 * <li>{@code LOCAL} – 로컬 개발 환경</li>
 * <li>{@code UT} – 단위 테스트(Unit Test) 실행 환경</li>
 * <li>{@code SIT} – 시스템 통합 테스트(System Integration Test) 환경</li>
 * <li>{@code UAT} – 사용자 수용 테스트(User Acceptance Test) 환경</li>
 * <li>{@code PRD} – 실제 서비스 운영 환경</li>
 * </ul>
 *
 * <p>
 * 일반적으로 {@code RuntimeContext.getRunMode()} 값과 매칭하여 조건부 동작을 제어합니다.
 * </p>
 *
 * @author sungdon.choi
 */
public enum RunMode {
    /** 로컬 개발 환경 */
    LOCAL,

    /** 단위 테스트 환경 */
    UT,

    /** 시스템 통합 테스트 환경 */
    SIT,

    /** 사용자 수용 테스트 환경 */
    UAT,

    /** 프로덕션 운영 환경 */
    PRD,

    NONE;

    public static RunMode of(String runMode) {
        switch (runMode.toLowerCase()) {
            case "local":
                return LOCAL;
            case "ut":
                return UT;
            case "sit":
                return SIT;
            case "uat":
                return UAT;
            case "prd":
                return PRD;
            default:
                return NONE;
        }
    }
}
