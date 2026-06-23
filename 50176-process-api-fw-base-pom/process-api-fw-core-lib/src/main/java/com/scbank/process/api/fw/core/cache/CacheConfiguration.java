package com.scbank.process.api.fw.core.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 프레임워크 캐시 설정
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 16.
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfiguration {

    @Bean(ICacheManager.BEAN_ID)
    @ConditionalOnMissingBean(ICacheManager.class)
    ICacheManager cacheManager(CacheManager cacheManager) {
        return new DefaultCacheManager(cacheManager);
    }
}
