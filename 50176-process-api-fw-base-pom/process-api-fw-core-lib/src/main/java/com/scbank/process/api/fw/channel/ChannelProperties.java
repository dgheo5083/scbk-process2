package com.scbank.process.api.fw.channel;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.channel.constants.ChannelConstants;
import com.scbank.process.api.fw.message.MessageFormatOptionConfig;
import com.scbank.process.api.fw.message.enums.MessageFormat;

import lombok.Data;

/**
 * <pre>
 * 프레임워크 채널 설정 클래스입니다.
 * application.yml 또는 application.properties에서 "csl.channel" prefix로 시작하는 설정을 바인딩합니다.
 *
 * Dispatcher, Validator, Interceptor, 메시지 변환기 등
 * 프레임워크 전체 흐름에 영향을 주는 주요 설정을 포함합니다.
 * </pre>
 *
 * 예시:
 * 
 * <pre>
 * csl:
 *   channel:
 *     enabled: true
 *     validation:
 *       enabled: true
 *     service:
 *       - service-id: COMMON
 *         base-path: /api/common
 *         config-locations:
 *           - classpath:/services/common/*.xml
 * </pre>
 *
 * @author sungdon.choi
 * @since 2025.04.16
 */
@Data
@Component
@ConfigurationProperties(prefix = ChannelConstants.FRAMEWORK_CHANNEL_PREFIX)
public class ChannelProperties {

        /** 프레임워크 채널 기능 활성화 여부 */
        private boolean enabled;

        /** 기본 Dispatcher Controller 클래스 */
        private Class<?> defaultControllerClass;

        /** 기본 Dispatcher Controller 내 실행할 메서드명 */
        private String defaultControllerMethod;

        /** 서비스별 설정 목록 */
        private List<ServiceProperties> service;

        /** 예외 처리 정책 설정 */
        private ExceptionConfig exception;

        /** 입력 DTO 유효성 검증 설정 */
        private ValidationConfig validation;

        /** 지원하는 디바이스 목록 */
        private List<DeviceConfig> devices;

        /** 메시지 소스 설정 (MessageSource) */
        private MessageSourceConfig messageSource;

        private Map<MessageFormat, MessageFormatOptionConfig> messageFormatOptions;

        /**
         * 예외 처리 정책 설정
         *
         * @param handleGlobalAdvice GlobalExceptionHandler 자동 등록 여부
         */
        public record ExceptionConfig(boolean handleGlobalAdvice) {
        }

        /**
         * 입력 DTO 유효성 검증 설정
         *
         * @param enabled true일 경우 Bean Validation(JSR-380)을 사용
         */
        public record ValidationConfig(boolean enabled) {
        }

        /**
         * 디바이스 정의
         *
         * @param id          디바이스 ID (예: PC, MOBILE)
         * @param name        디바이스 이름
         * @param description 설명
         * @param order       정렬 순서
         * @param regEx       User-Agent 판별 정규식
         * @param isDefault   기본 디바이스 여부
         */
        public record DeviceConfig(String id, String name, String description, int order, String regEx,
                        boolean isDefault) {
        }

        /**
         * 서비스별 채널 설정
         *
         * @param enabled          해당 서비스 사용 여부
         * @param serviceId        서비스 식별자 (예: COMMON, CMS 등)
         * @param basePath         서비스의 공통 URL prefix
         * @param controllerClass  Dispatcher 역할의 컨트롤러 클래스
         * @param controllerMethod Dispatcher 메서드명
         * @param configLocations  XML 서비스 정의 파일 경로 목록
         * @param interceptors     서비스 전용 인터셉터 클래스 목록
         * @param serviceMapping   요청 URL/Content-Type/Method 조건 설정
         * @param component        서비스 컴포넌트 스캔 대상 패키지 설정
         */
        public record ServiceProperties(
                        boolean enabled,
                        String serviceId,
                        String basePath,
                        Class<?> controllerClass,
                        String controllerMethod,
                        List<String> configLocations,
                        List<Class<?>> interceptors,
                        ServiceMappingConfig serviceMapping,
                        ServiceComponentConfig component) {

                /**
                 * 서비스 매핑 조건 정의
                 *
                 * @param description         설명
                 * @param allowedUrlPatterns  허용되는 요청 URI 패턴 (Ant 스타일)
                 * @param allowedContentTypes 허용되는 Content-Type
                 * @param allowedMethods      허용되는 HTTP 메서드(GET, POST 등)
                 */
                public record ServiceMappingConfig(
                                String description,
                                List<String> allowedUrlPatterns,
                                List<String> allowedContentTypes,
                                List<String> allowedMethods) {
                }
        }

        /**
         * 메시지소스 설정
         *
         * @param enabled                메시지 소스 기능 사용 여부
         * @param encoding               메시지 파일 인코딩
         * @param fallbackToSystemLocale 시스템 Locale fallback 여부
         * @param cacheSeconds           메시지 캐시 시간 (초)
         * @param locations              메시지 파일 위치 목록
         */
        public record MessageSourceConfig(
                        boolean enabled,
                        String encoding,
                        boolean fallbackToSystemLocale,
                        int cacheSeconds,
                        List<String> locations) {
        }

        /**
         * 서비스 컴포넌트 패키지 스캔 설정
         *
         * @param basePackages 컴포넌트 클래스 스캔 대상 base 패키지 목록
         */
        public record ServiceComponentConfig(List<String> basePackages) {
        }
}
