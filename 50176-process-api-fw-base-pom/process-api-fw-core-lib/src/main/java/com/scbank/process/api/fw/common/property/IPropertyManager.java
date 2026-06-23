package com.scbank.process.api.fw.common.property;

import org.springframework.beans.factory.InitializingBean;

import com.scbank.process.api.fw.core.lifecycle.IReloadable;

/**
 * 프레임워크 공통 설정 정보 관리 인터페이스.
 * 
 * 설정 초기화 및 런타임 중 설정 값 조회를 지원하며,
 * 재로딩 인터페이스(IReloadable)와 Spring 초기화 인터페이스(InitializingBean)를 함께 구현한다.
 * 
 */
public interface IPropertyManager extends IReloadable, InitializingBean {

    /**
     * Spring Bean 초기화 이후 호출됨.
     * 설정 파일 로딩 또는 초기화 작업은 이 메서드 내에서 처리된다.
     */
    @Override
    default void afterPropertiesSet() throws Exception {
        init();
    }

    /**
     * 설정 초기화 메서드.
     * 파일 또는 외부 소스로부터 설정 정보를 로딩한다.
     */
    void init();

    /**
     * 문자열 설정값 조회
     * 값이 존재하지 않거나 공백 문자열 반환
     * 
     * @param key 설정 키
     * @return 설정값 또는 기본값
     */
    default String getString(String key) {
        return this.getString(key, "");
    }

    /**
     * 문자열 설정값 조회 (기본값 포함).
     * 
     * @param key          설정 키
     * @param defaultValue 설정 값이 존재하지 않을 경우 반환할 기본값
     * @return 설정 값 또는 기본값
     */
    String getString(String key, String defaultValue);

    /**
     * 정수형 설정값 조회
     * 값이 존재하지 않거나 0 반환
     * 
     * @param key 설정 키
     * @return 설정 값 또는 기본값
     */
    default int getInt(String key) {
        return this.getInt(key, 0);
    }

    /**
     * 정수형 설정값 조회 (기본값 포함).
     *
     * @param key          설정 키
     * @param defaultValue 설정 값이 존재하지 않을 경우 반환할 기본값
     * @return 설정 값 또는 기본값
     */
    int getInt(String key, int defaultValue);

    /**
     * boolean 설정값 조회.
     * 값이 존재하지 않거나 true가 아닌 경우 false 반환.
     *
     * @param key 설정 키
     * @return true/false
     */
    boolean getBoolean(String key);

    /**
     * boolean 설정값 조회 (기본값 포함).
     *
     * @param key          설정 키
     * @param defaultValue 설정 값이 존재하지 않을 경우 반환할 기본값
     * @return 설정 값 또는 기본값
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * 제네릭 방식으로 설정값 조회.
     *
     * @param key          설정 키
     * @param defaultValue 설정 값이 존재하지 않을 경우 반환할 기본값
     * @param <T>          반환 타입
     * @return 설정 값 또는 기본값
     */
    <T> T getProperty(String key, T defaultValue);
}
