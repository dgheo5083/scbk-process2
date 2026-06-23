package com.scbank.process.api.fw.core.runtime;

/**
 * 런타임 컨텍스트 주입용 인터페이스
 * <p>
 * 해당 인터페이스를 구현한 클래스는 {@link RuntimeContext}를 주입받아
 * 런타임 실행환경에 따라 동적으로 동작할 수 있도록 구성됩니다.
 * </p>
 *
 * <p>
 * 예: 실행 모드(PRD/LOCAL/DEV), 사용자 정보, 트랜잭션 정보 등 참조 가능
 * </p>
 *
 * @see RuntimeContext
 * @author sungdon.choi
 */
public interface RuntimeContextAware {

    /**
     * 런타임 컨텍스트 설정
     *
     * @param runtimeContext 주입받을 런타임 컨텍스트
     */
    void setRuntimeContext(RuntimeContext runtimeContext);
}
