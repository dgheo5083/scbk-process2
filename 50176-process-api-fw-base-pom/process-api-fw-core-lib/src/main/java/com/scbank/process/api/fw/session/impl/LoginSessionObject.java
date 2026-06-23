package com.scbank.process.api.fw.session.impl;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.scbank.process.api.fw.core.exception.FrameworkException;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 프레임워크 공통 세션 - 로그인 세션 객체
 * <p>
 * 로그인한 사용자 전용 세션 데이터를 보관하는 컨테이너로,
 * 내부적으로 {@link LoginSessionData}를 포함하며 로그인 여부 상태도 함께 관리합니다.
 * </p>
 *
 * <ul>
 * <li>{@link LoginSessionData}는 키-값 저장소 역할을 수행</li>
 * <li>{@link AtomicBoolean}을 통해 로그인 상태를 스레드 안전하게 관리</li>
 * <li>직렬화 가능하며, Jackson 사용 시 알 수 없는 속성은 무시됨</li>
 * </ul>
 *
 * @see LoginSessionData
 * @see co.kr.scbank.framework.core.session.ISessionContext
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginSessionObject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 사용자 로그인 여부 플래그
     */
    private boolean logined = false;

    /**
     * 로그인 사용자 세션 데이터 (key-value 저장소)
     */
    @Getter
    @Setter
    private LoginSessionData loginData;

    @Getter
    @Setter
    private String userId;

    /**
     * 로그인 시 호출. 기존 세션을 초기화하고 userId를 설정.
     *
     * @param userId 사용자 ID
     * @throws FrameworkException userId가 비어있거나 오류 발생 시
     */
    public void initOnLogin(String userId) throws FrameworkException {
        if (StringUtils.isEmpty(userId)) {
            throw new FrameworkException("userId가 비어있습니다.");
        }
        if (this.loginData == null) {
            this.loginData = new LoginSessionData();
        }
        this.loginData.clear();
        this.setUserId(userId);
        this.setLogined(true);
    }

    /**
     * 로그아웃 시 호출. 로그인 여부 초기화 및 세션 클리어.
     *
     * @throws FrameworkException 로그인하지 않은 상태에서 호출된 경우
     */
    public void initOnLogout() throws FrameworkException {
        if (!this.isLogined()) {
            throw new FrameworkException("로그인하지 않은 상태에서 로그아웃 호출됨");
        }
        this.setLogined(false);
        if (this.loginData == null) {
            this.loginData = new LoginSessionData();
        }
        this.loginData.clear();
    }

    /**
     * 로그인 세션에 값 저장
     */
    @SuppressWarnings("unchecked")
    public <T> T put(String key, T value) {
        if (this.loginData == null) {
            this.loginData = new LoginSessionData();
        }
        return (T) this.loginData.put(key, value);
    }

    /**
     * 로그인 세션에서 값 조회
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        if (this.loginData == null) {
            return null;
        }
        return (T) this.loginData.get(key);
    }

    /**
     * 로그인 세션에서 값 제거
     */
    @SuppressWarnings("unchecked")
    public <T> T remove(String key) {
        if (this.loginData == null) {
            return null;
        }
        return (T) this.loginData.remove(key);
    }

    // /**
    // * 로그인 사용자 ID 조회
    // */
    // public String getUserId() {
    // return this.userId;
    // }

    /**
     * 로그인 여부 조회
     */
    public boolean isLogined() {
        return this.logined;
    }

    /**
     * 로그인 여부 설정
     */
    public void setLogined(boolean logined) {
        this.logined = logined;
    }

    /**
     * 로그인 상태 문자열 반환 ("Login" 또는 "Logout")
     */
    @JsonIgnore
    public String getLoginString() {
        return this.isLogined() ? "Login" : "Logout";
    }

    /**
     * 로그인 세션 초기화
     */
    public void clear() {
        if (this.loginData != null) {
            this.loginData.clear();
        }
    }
}
