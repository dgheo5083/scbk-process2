package com.scbank.process.api.fw.core.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 프레임워크 캐시 설정을 위한 프로퍼티 매핑 클래스입니다.
 * 
 * application.yml 또는 application.properties 파일의 
 * 'framework.cache' prefix 하위 설정을 매핑하여 사용합니다.
 *
 * 주요 역할:
 * - 캐시 이름별 TTL(Time-To-Live), 최대 크기(Max Size) 설정 관리
 * - {@link DefaultCacheManager} 초기화에 사용
 * </pre>
 *
 * <p>
 * <b>예시 yml 설정:</b>
 * </p>
 * 
 * <pre>
 * csl:
 *   cache:
 *     target:
 *       sampleCache:
 *         ttl: 600
 *         maxSize: 1000
 * </pre>
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 16.
 *
 * @see DefaultCacheManager
 */
@Component(CacheProperties.BEAN_ID)
@ConfigurationProperties(prefix = "csl.cache")
public class CacheProperties {

    /** Spring Bean ID */
    public static final String BEAN_ID = "csl.cache.properties";

    /**
     * <pre>
     * 캐시 이름별 설정 정보 Map입니다.
     * Key는 캐시 이름, Value는 TTL/최대크기를 정의한 {@link CacheConfig}.
     * </pre>
     */
    @Getter
    @Setter
    private Map<String, CacheConfig> target = new HashMap<>();

    /**
     * <pre>
     * 캐시 개별 설정 정보
     * - ttl: 캐시 데이터 생존 시간(초 단위)
     * - maxSize: 캐시 최대 항목 수
     * </pre>
     */
    public record CacheConfig(int ttl, int maxSize) {

    }
}
