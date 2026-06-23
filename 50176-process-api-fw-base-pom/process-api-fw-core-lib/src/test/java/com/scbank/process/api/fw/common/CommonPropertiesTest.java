package com.scbank.process.api.fw.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.CommonProperties.Config;
import com.scbank.process.api.fw.common.CommonProperties.PropertyConfig;

/**
 * {@link CommonProperties} 및 중첩 레코드 단위 테스트.
 */
class CommonPropertiesTest {

    @Test
    @DisplayName("각 설정 그룹 getter/setter 동작")
    void gettersAndSetters() {
        CommonProperties props = new CommonProperties();
        Config code = new Config(true, "classpath:code/*.xml");
        Config i18n = new Config(false, "classpath:i18n/*.xml");
        Config errorCode = new Config(true, "classpath:error/*.xml");
        Config holiday = new Config(true, "classpath:holiday.xml");
        Config serviceTime = new Config(false, "classpath:st.xml");
        PropertyConfig property = new PropertyConfig(true, List.of("classpath:a.properties"));

        props.setCode(code);
        props.setI18n(i18n);
        props.setErrorCode(errorCode);
        props.setHoliday(holiday);
        props.setServiceTime(serviceTime);
        props.setProperty(property);

        assertThat(props.getCode()).isSameAs(code);
        assertThat(props.getI18n()).isSameAs(i18n);
        assertThat(props.getErrorCode()).isSameAs(errorCode);
        assertThat(props.getHoliday()).isSameAs(holiday);
        assertThat(props.getServiceTime()).isSameAs(serviceTime);
        assertThat(props.getProperty()).isSameAs(property);
    }

    @Test
    @DisplayName("Config 레코드 접근자")
    void configRecord() {
        Config config = new Config(true, "classpath:x.xml");

        assertThat(config.enabled()).isTrue();
        assertThat(config.configLocation()).isEqualTo("classpath:x.xml");
    }

    @Test
    @DisplayName("PropertyConfig 레코드 접근자")
    void propertyConfigRecord() {
        PropertyConfig config = new PropertyConfig(false, List.of("a", "b"));

        assertThat(config.enabled()).isFalse();
        assertThat(config.configLocations()).containsExactly("a", "b");
    }

    @Test
    @DisplayName("BEAN_ID 상수 값 검증")
    void beanIdConstant() {
        assertThat(CommonProperties.BEAN_ID).isEqualTo("csl.common.properties");
    }
}
