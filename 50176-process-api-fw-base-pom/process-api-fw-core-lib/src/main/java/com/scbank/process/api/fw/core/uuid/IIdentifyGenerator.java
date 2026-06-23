package com.scbank.process.api.fw.core.uuid;

/**
 * 프레임워크 식별자(Unique ID) 생성기 인터페이스
 * <p>
 * 고유한 문자열 식별자를 생성하는 기능을 정의합니다.
 * 트랜잭션 ID, 세션 ID, 요청 추적 ID(traceId) 등 다양한 식별자 생성기에 적용할 수 있습니다.
 * </p>
 *
 * <ul>
 * <li>UUID, NanoID, Snowflake 등 다양한 전략 구현체와 연동 가능</li>
 * <li>{@code @FunctionalInterface}로 선언되어 람다 표현식으로도 구현 가능</li>
 * </ul>
 *
 * <p>
 * <b>Spring 등록시 Bean 이름:</b> {@code ib.core.IIdentifyGenerator}
 * </p>
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 2022.01.16
 */
@FunctionalInterface
public interface IIdentifyGenerator {

    /**
     * 기본 Bean ID (Spring 등록용 이름)
     */
    String BEAN_ID = "ib.core.IIdentifyGenerator";

    /**
     * 고유한 ID를 생성하여 반환
     *
     * @return 고유 식별자 문자열
     */
    String generateId();
}
