package com.scbank.process.api.fw.core.log.trace;

/**
 * <pre>
 * Trace 구간(섹션)을 나타내는 열거형.
 * 각 섹션은 트레이스 로그 출력 시 구분되는 실행 영역을 의미합니다.
 * </pre>
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 17.
 */
public enum TraceSection {

    /**
     * 컨트롤러 계층 (예: /api 요청 진입점)
     */
    CTRL,

    /**
     * 서비스 계층 (비즈니스 로직 처리)
     */
    SVC,

    /**
     * 공유 컴포넌트 (서비스에서 사용하는 재사용 가능한 공유 컴포넌트)
     */
    SHARED,

    /**
     * 공통 컴포넌트
     */
    COM,

    /**
     * DAO 계층 (데이터베이스 접근 로직)
     */
    DAO,

    /**
     * 외부 시스템 호출 (예: 연계 시스템, REST API, TCP 등)
     */
    EXT_CALL,

    /**
     * 타 업무 프로세스API 엔드포인트 호출
     */
    PRC_CALL,
    
    /**
     * 타 업무 프로세스API 엔드포인트 호출(시뮬레이션 용)
     */
    PRC_SIMULATION,

    /**
     * 배치 작업 실행 구간
     */
    BATCH,

    /**
     * 역거래
     */
    REVERSE,

    /**
     * 배치 작업 (예: 쿼츠 스케줄러 잡)
     */
    JOB,

    /**
     * 명시되지 않은 구간
     */
    NONE
}
