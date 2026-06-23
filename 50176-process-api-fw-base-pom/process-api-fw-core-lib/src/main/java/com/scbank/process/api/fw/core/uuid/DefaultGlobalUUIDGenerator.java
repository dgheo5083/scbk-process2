package com.scbank.process.api.fw.core.uuid;

import java.util.UUID;

/**
 * 프레임워크 기본 UUID 식별자 생성기 구현체
 * <p>
 * {@link UUID#randomUUID()}를 기반으로 고유한 문자열 식별자를 생성합니다.
 * </p>
 *
 * <p>
 * 주로 트랜잭션 ID, 세션 키, 요청 추적 ID(traceId) 등에 사용되며,
 * {@link IIdentifyGenerator} 인터페이스의 기본 구현체 역할을 합니다.
 * </p>
 *
 * @see IIdentifyGenerator
 * @see UUID
 * @author sungdon.choi
 * @version 1.0
 * @since 2022.01.16
 */
public class DefaultGlobalUUIDGenerator implements IIdentifyGenerator {

    /**
     * UUID 기반의 고유 식별자 생성
     *
     * @return UUID 문자열 (예: "550e8400-e29b-41d4-a716-446655440000")
     */
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
