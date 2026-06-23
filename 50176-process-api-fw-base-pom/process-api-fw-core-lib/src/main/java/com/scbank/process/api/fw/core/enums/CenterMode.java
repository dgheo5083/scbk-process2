package com.scbank.process.api.fw.core.enums;

/**
 * 센터 운영 모드 (CenterMode)
 * <p>
 * 프레임워크의 실행 환경 또는 시스템 운용 모드를 지정하는 열거형입니다.
 * 주로 데이터센터의 운영/백업 분기나 부하 테스트 구분에 사용됩니다.
 * </p>
 *
 * <ul>
 * <li>{@code MAIN} – 주 센터 (메인 프로덕션 환경)</li>
 * <li>{@code DR} – 재해 복구 센터 (Disaster Recovery)</li>
 * <li>{@code STRESS} – 부하 테스트 또는 성능 검증 환경</li>
 * </ul>
 *
 * <p>
 * 예: {@code RuntimeContext.getCenterMode()} 호출 결과와 매칭하여 분기 처리
 * </p>
 *
 * @author sungdon.choi
 */
public enum CenterMode {
    /** 메인 센터 */
    MAIN,

    /** 재해복구 센터 */
    DR,

    /** 부하 테스트 전용 센터 */
    STRESS,

    NONE;

    public static CenterMode of(String centerMode) {
        switch (centerMode.toLowerCase()) {
            case "main":
                return MAIN;
            case "dr":
                return DR;
            case "stress":
                return STRESS;
            default:
                return NONE;
        }
    }
}
