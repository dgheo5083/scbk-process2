package com.scbank.process.api.fw.core.cache;

import java.io.Serializable;

/**
 * <pre>
 * 프레임워크 캐시 키 인터페이스입니다.
 * 
 * 모든 캐시 키 객체는 이 인터페이스를 구현하여,
 * 직렬화(Serialization)가 가능해야 합니다.
 * 
 * 주로 복합 키(Composite Key)나 식별 가능한 키 객체를 사용할 때
 * 타입 안전성과 구조화를 위해 사용합니다.
 * </pre>
 *
 * <p>
 * <b>주요 특징:</b>
 * </p>
 * <ul>
 * <li>모든 캐시 키는 {@link Serializable}을 구현해야 함</li>
 * <li>단일 String 키 대신 객체 기반 키 설계를 지원</li>
 * </ul>
 *
 * <p>
 * <b>사용 예시:</b>
 * </p>
 * 
 * <pre>{@code
 * public class UserCacheKey implements ICacheKey {
 *     private final Long userId;
 *     private final String region;
 *     // equals(), hashCode() 반드시 오버라이드 필요
 * }
 * }</pre>
 *
 * @see Serializable
 * @see ICacheManager
 * 
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 16.
 */
public interface ICacheKey extends Serializable {

}
