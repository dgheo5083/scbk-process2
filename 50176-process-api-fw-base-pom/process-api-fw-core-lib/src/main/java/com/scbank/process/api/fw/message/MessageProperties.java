package com.scbank.process.api.fw.message;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 프레임워크 전문 처리 Properties
 *
 * @author sungdon.choi
 */
@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "csl.message")
public class MessageProperties {

    /**
     * 프레임워크 전문 처리 활성화 여부
     */
    private boolean enabled;

    /**
     * 전문 처리 기본 인코딩
     */
    private String defaultEncoding = StandardCharsets.UTF_8.toString();

    /**
     * 프레임워크 전문 메시지 Object 스캔 base package
     */
    private List<String> basePackages;

    /**
     * 프레임워크 전문 메시지 Object 스캔 어노테이션
     */
    private Class<? extends IntegrationMessage> annotationClass = IntegrationMessage.class;

    /**
     * 프레임워크 전문 처리 디버깅 로그 출력여부
     */
    private boolean useDebugLog = false;
}
