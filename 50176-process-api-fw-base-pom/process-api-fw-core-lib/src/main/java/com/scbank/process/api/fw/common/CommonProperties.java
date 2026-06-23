package com.scbank.process.api.fw.common;

import lombok.Data;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 프레임워크 공통 설정 프로퍼티
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 16.
 */
@Data
@Component
@ConfigurationProperties(prefix = "csl.common")
public class CommonProperties {

    public static final String BEAN_ID = "csl.common.properties";

    /**
     * 공통코드
     */
    private Config code;

    /**
     * 공통 프로퍼티
     */
    private PropertyConfig property;

    /**
     * 다국어
     */
    private Config i18n;

    /**
     * 에러코드
     */
    private Config errorCode;

    /**
     * 공휴일 관리
     */
    private Config holiday;

    /**
     * 서비스 이용시간 관리
     */
    private Config serviceTime;

    /**
     * 공통기능 설정 정보
     *
     * @param enabled        사용여부
     * @param configLocation 설정 경로
     */
    public record Config(boolean enabled, String configLocation) {

    }

    public record PropertyConfig(boolean enabled, List<String> configLocations) {

    }
}
