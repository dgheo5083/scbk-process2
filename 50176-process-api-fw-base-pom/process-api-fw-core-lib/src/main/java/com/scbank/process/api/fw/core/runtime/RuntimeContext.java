package com.scbank.process.api.fw.core.runtime;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.utils.StringUtils;

/**
 * 프레임워크 런타임 컨텍스트 (전역 ApplicationContext 및 Environment 접근 도우미)
 * <p>
 * {@link ApplicationContextAware}, {@link EnvironmentAware}를 구현하여,
 * Spring의 Bean 및 프로퍼티 설정에 전역적으로 접근할 수 있도록 지원합니다.
 * </p>
 *
 * <ul>
 * <li>정적 메서드를 통해 Bean 인스턴스 또는 환경변수에 접근 가능</li>
 * <li>프레임워크 내부에서 동적 설정, 프로파일, 실행모드(run-mode) 등을 참조하는 데 사용</li>
 * </ul>
 *
 * <p>
 * <b>주의:</b> 유틸리티 성격으로 정적 사용을 전제로 설계됨
 * </p>
 *
 * @author sungdon.choi
 */
@Lazy
@Component
public class RuntimeContext {

    private static volatile ApplicationContext applicationContext;

    private static volatile Environment environment;

    /** ──────────────── Environment 관련 메서드 ──────────────── */

    /**
     * 환경 변수 조회
     */
    public static String getEnv(String propertyName) {
        return environment.getProperty(propertyName);
    }

    /**
     * 환경 변수 조회 (기본값 제공)
     */
    public static String getEnv(String propertyName, String defaultValue) {
        return environment.getProperty(propertyName, defaultValue);
    }

    /**
     * 환경 변수 조회 (타입 변환)
     */
    public static <T> T getEnv(String propertyName, Class<T> type) {
        return environment.getProperty(propertyName, type);
    }

    /**
     * 실행 모드 조회 (ex. PRD, DEV, LOCAL 등)
     */
    public static String getRunModeString() {
        return environment.getProperty("csl.runtime.run-mode");
    }

    public static RunMode getRunMode() {
        String runModeString = getRunModeString();
        if (runModeString == null || runModeString.isEmpty()) {
            return RunMode.LOCAL;
        }
        return RunMode.of(runModeString);
    }

    /**
     * 센터 모드 조회 (ex. main, dr 등)
     */
    public static String getCenterMode() {
        return environment.getProperty("csl.runtime.center-mode");
    }

    /**
     * 기본 인코딩 조회
     */
    public static String getDefaultEncoding() {
        return environment.getProperty("csl.runtime.default-encoding");
    }

    /**
     * 기본 로케일 조회
     */
    public static String getDefaultLocale() {
        return environment.getProperty("csl.runtime.default-locale");
    }

    public static String getDefaultErrorCode() {
        return environment.getProperty("csl.runtime.default-error-code");
    }

    public static String getDefaultErrorMessage() {
        return environment.getProperty("csl.runtime.default-error-message");
    }

    /** ──────────────── Bean 조회 관련 메서드 ──────────────── */

    /**
     * 
     * @param <T>
     * @param requiredType
     * @return
     */
    public static <T> T getBean(Class<T> requiredType) {
    	if (applicationContext == null) {
    		return null;
    	}
        return applicationContext.getBean(requiredType);
    }

    /**
     * 
     * @param <T>
     * @param beanName
     * @param requiredType
     * @return
     */
    public static <T> T getBean(String beanName, Class<T> requiredType) {
    	if (applicationContext == null) {
    		return null;
    	}
        return applicationContext.getBean(beanName, requiredType);
    }

    /**
     * 
     * @param <T>
     * @param requiredType
     * @param args
     * @return
     */
    public static <T> T getBean(Class<T> requiredType, Object... args) {
    	if (applicationContext == null) {
    		return null;
    	}
        return applicationContext.getBean(requiredType, args);
    }

    /**
     * 
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
    	if (applicationContext == null) {
    		return null;
    	}
        return applicationContext.getBean(beanName);
    }

    /**
     * 
     * @param beanName
     * @param args
     * @return
     */
    public static Object getBean(String beanName, Object... args) {
    	if (applicationContext == null) {
    		return null;
    	}
        return applicationContext.getBean(beanName, args);
    }

    /**
     * 
     * @param propertyName
     * @return
     */
    public static String getProperty(String propertyName) {
        return environment.getProperty(propertyName, StringUtils.EMPTY);
    }

    /**
     * 
     * @param propertyName
     * @param defaultValue
     * @return
     */
    public static String getProperty(String propertyName, String defaultValue) {
        return environment.getProperty(propertyName, defaultValue);
    }

    /**
     * 
     * @param <T>
     * @param propertyName
     * @param targetType
     * @return
     */
    public static <T> T getProperty(String propertyName, Class<T> targetType) {
        return environment.getProperty(propertyName, targetType);
    }

    /**
     * 
     * @param <T>
     * @param propertyName
     * @param targetType
     * @param defaultValue
     * @return
     */
    public static <T> T getProperty(String propertyName, Class<T> targetType, T defaultValue) {
        return environment.getProperty(propertyName, targetType, defaultValue);
    }

    /** ──────────────── Context 주입 (Spring 사용) ──────────────── */

    public static void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        RuntimeContext.applicationContext = applicationContext;
    }

    public static void setEnvironment(@NonNull Environment environment) {
        RuntimeContext.environment = environment;
    }
}
