package com.scbank.process.api.fw.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.CommonProperties.Config;
import com.scbank.process.api.fw.common.CommonProperties.PropertyConfig;
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
 * {@link CommonConfiguration} 의 {@code @Bean} 팩토리 메서드 단위 테스트.
 */
class CommonConfigurationTest {

    private final CommonConfiguration configuration = new CommonConfiguration();
    private final ICacheManager cacheManager = mock(ICacheManager.class);

    private CommonProperties properties() {
        CommonProperties props = new CommonProperties();
        props.setCode(new Config(true, "classpath:code.xml"));
        props.setErrorCode(new Config(true, "classpath:error.xml"));
        props.setHoliday(new Config(true, "classpath:holiday.xml"));
        props.setServiceTime(new Config(true, "classpath:st.xml"));
        props.setProperty(new PropertyConfig(true, List.of("classpath:a.properties")));
        return props;
    }

    @Test
    @DisplayName("codeManager 는 DefaultCodeManager 를 생성")
    void codeManagerBean() {
        ICodeManager manager = configuration.codeManager(properties(), cacheManager);
        assertThat(manager).isInstanceOf(DefaultCodeManager.class);
    }

    @Test
    @DisplayName("errorCodeManager 는 DefaultErrorCodeManager 를 생성")
    void errorCodeManagerBean() {
        IErrorCodeManager manager = configuration.errorCodeManager(properties(), cacheManager);
        assertThat(manager).isInstanceOf(DefaultErrorCodeManager.class);
    }

    @Test
    @DisplayName("holidayManager 는 DefaultHolidayManager 를 생성")
    void holidayManagerBean() {
        IHolidayManager manager = configuration.holidayManager(properties(), cacheManager);
        assertThat(manager).isInstanceOf(DefaultHolidayManager.class);
    }

    @Test
    @DisplayName("serviceTimeManager 는 DefaultServiceTimeManager 를 생성")
    void serviceTimeManagerBean() {
        IServiceTimeManager manager = configuration.serviceTimeManager(properties(), cacheManager);
        assertThat(manager).isInstanceOf(DefaultServiceTimeManager.class);
    }

    @Test
    @DisplayName("propertyManager 는 DefaultPropertyManager 를 생성")
    void propertyManagerBean() {
        IPropertyManager manager = configuration.propertyManager(properties());
        assertThat(manager).isInstanceOf(DefaultPropertyManager.class);
    }
}
