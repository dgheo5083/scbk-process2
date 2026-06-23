package com.scbank.process.api.fw.common;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.scbank.process.api.fw.common.code.ICodeManager;
import com.scbank.process.api.fw.common.code.impl.DefaultCodeManager;
import com.scbank.process.api.fw.common.errorcode.IErrorCodeManager;
import com.scbank.process.api.fw.common.errorcode.impl.DefaultErrorCodeManager;
import com.scbank.process.api.fw.common.holiday.IHolidayManager;
import com.scbank.process.api.fw.common.holiday.impl.DefaultHolidayManager;
import com.scbank.process.api.fw.common.property.IPropertyManager;
import com.scbank.process.api.fw.common.property.impl.DefaultPropertyManager;
import com.scbank.process.api.fw.common.servicetime.IServiceTimeManager;
import com.scbank.process.api.fw.common.servicetime.impl.DefaultServiceTimeManager;
import com.scbank.process.api.fw.core.cache.ICacheManager;

/**
 * 프레임워크 공통 Configuration
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 16.
 */
@Configuration
public class CommonConfiguration {

    /**
     * 프레임워크 공통 코드 매니저 빈
     *
     * @param properties   공통 설정 프로퍼티
     * @param cacheManager 프레임워크 캐시 매니저
     * @return 프레임워크 공통 코드 매니저 빈
     */
    @Bean
    @DependsOn({"runtimeContextInitializer"})
    @ConditionalOnMissingBean(ICodeManager.class)
    @ConditionalOnProperty(prefix = "csl.common.code", name = "enabled", havingValue = "true")
    ICodeManager codeManager(
            CommonProperties properties,
            @Qualifier(ICacheManager.BEAN_ID) ICacheManager cacheManager) {
        return new DefaultCodeManager(cacheManager, properties.getCode());
    }

    /**
     * 프레임워크 공통 에러코드 매니저 빈
     *
     * @param properties   공통 설정 프로퍼티
     * @param cacheManager 프레임워크 캐시 매니저
     * @return 프레임워크 공통 에러코드 매니저 빈
     */
    @Bean
    @DependsOn({"runtimeContextInitializer"})
    @ConditionalOnMissingBean(IErrorCodeManager.class)
    @ConditionalOnProperty(prefix = "csl.common.error-code", name = "enabled", havingValue = "true")
    IErrorCodeManager errorCodeManager(
            CommonProperties properties,
            @Qualifier(ICacheManager.BEAN_ID) ICacheManager cacheManager) {
        return new DefaultErrorCodeManager(cacheManager, properties.getErrorCode());
    }

    @Bean
    @DependsOn({"runtimeContextInitializer"})
    @ConditionalOnMissingBean(IHolidayManager.class)
    @ConditionalOnProperty(prefix = "csl.common.holiday", name = "enabled", havingValue = "true")
    IHolidayManager holidayManager(
            CommonProperties properties,
            @Qualifier(ICacheManager.BEAN_ID) ICacheManager cacheManager) {
        return new DefaultHolidayManager(cacheManager, properties.getHoliday());
    }

    /**
     * 프레임워크 공통 서비스 이용시간 매니저 빈
     *
     * @param properties   공통 설정 프로퍼티
     * @param cacheManager 프레임워크 캐시 매니저
     * @return 프레임워크 공통 서비스 이용시간 매니저 빈
     */
    @Bean
    @DependsOn({"runtimeContextInitializer"})
    @ConditionalOnMissingBean(IServiceTimeManager.class)
    @ConditionalOnProperty(prefix = "csl.common.service-time", name = "enabled", havingValue = "true")
    IServiceTimeManager serviceTimeManager(
            CommonProperties properties,
            @Qualifier(ICacheManager.BEAN_ID) ICacheManager cacheManager) {
        return new DefaultServiceTimeManager(cacheManager, properties.getServiceTime());
    }

    /**
     * 프레임워크 공통 프로퍼티 매니저 빈
     * 
     * @param properties 공통 설정 프로퍼티
     * @return 프레임워크 공통 프로퍼티 매니저 빈
     */
    @Bean
    @DependsOn({"runtimeContextInitializer"})
    @ConditionalOnMissingBean(IPropertyManager.class)
    @ConditionalOnProperty(prefix = "csl.common.property", name = "enabled", havingValue = "true")
    IPropertyManager propertyManager(
            CommonProperties properties) {
        return new DefaultPropertyManager(properties.getProperty());
    }
}
