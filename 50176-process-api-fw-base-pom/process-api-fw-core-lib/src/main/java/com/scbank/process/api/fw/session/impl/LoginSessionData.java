package com.scbank.process.api.fw.session.impl;

import java.io.Serializable;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.session.SessionScope;

/**
 * 프레임워크 로그인 세션 데이터 객체
 * <p>
 * 로그인한 사용자에게만 적용되는 세션 저장소이며,
 * {@link AbstractSessionObject}를 기반으로 세션 범위는 {@link SessionScope#LOGIN}으로
 * 지정됩니다.
 * </p>
 * 
 * <ul>
 * <li>{@code userId}를 별도 필드로 관리하여 사용자 식별을 명확하게 함</li>
 * <li>생성 시 초기 데이터(Map)를 함께 주입 가능</li>
 * </ul>
 * 
 * <p>
 * <b>주의:</b> 이 클래스는 {@link LoginSessionObject}의 내부 필드 또는 전용 래퍼 용도로 설계된 것으로
 * 보입니다.
 * 일반 사용자 코드에서는 직접 사용하지 않고, {@code LoginSessionObject}를 통해 관리하는 것이 일반적입니다.
 * </p>
 *
 * @see AbstractSessionObject
 * @see SessionScope#LOGIN
 * @see LoginSessionObject
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
public class LoginSessionData extends AbstractSessionObject {

    private static final long serialVersionUID = 1L;

    /**
     * 기본 생성자 (세션 범위: LOGIN)
     */
    public LoginSessionData() {
        super(SessionScope.LOGIN);
    }

    /**
     * 사용자 ID 기반 생성자
     *
     * @param userId 로그인한 사용자 ID
     */
    public LoginSessionData(String userId) {
        this();
        this.put("userId", userId);
    }

    public void setUserId(String userId) {
        this.put("userId", userId);
    }

    public String getUserId() {
        Object userId = this.get("userId");
        if (userId == null) {
            return null;
        }
        return (String) userId;
    }

    /**
     * 초기 세션 데이터 기반 생성자
     *
     * @param data 초기 세션 데이터 (Map 형태)
     */
    public LoginSessionData(Map<String, ? extends Serializable> data) {
        this();
        if (!CollectionUtils.isEmpty(data)) {
            super.putAll(data);
        }
    }
}
