package com.scbank.process.api.fw.core.runtime;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.core.enums.CenterMode;
import com.scbank.process.api.fw.core.enums.RunMode;

import lombok.Data;

/**
 * 런타임 환경 프로퍼티 클래스
 * 
 * @author sungdon.choi
 */
@Data
@Component
@ConfigurationProperties(prefix = "csl.runtime")
public class RuntimeProperties {

    /**
     * 운영 모드
     */
    private RunMode runMode; // LOCAL/DEV/SIT/UAT/PRD

    /**
     * 센터 모드
     */
    private CenterMode centerMode; // main/dr

    /**
     * 기본 로케일
     */
    private String defaultLocale;

    /**
     * 기본 인코딩 문자셋
     */
    private String defaultEncoding;

    /**
     * 기본 에러코드
     */
    private String defaultErrorCode;

    /**
     * 기본 에러 메시지
     */
    private String defaultErrorMessage;
}
