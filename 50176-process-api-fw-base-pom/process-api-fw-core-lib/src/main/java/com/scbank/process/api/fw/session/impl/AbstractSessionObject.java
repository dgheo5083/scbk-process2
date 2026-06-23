package com.scbank.process.api.fw.session.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.session.ISessionKeyValidator;
import com.scbank.process.api.fw.session.ISessionObject;
import com.scbank.process.api.fw.session.SessionKeyInvalidException;
import com.scbank.process.api.fw.session.SessionScope;

import lombok.RequiredArgsConstructor;

/**
 * 세션 저장소 공통 추상 클래스
 * <p>
 * {@link ISessionObject}의 기본 구현으로,
 * 내부 저장소는 Thread-safe한 {@link ConcurrentHashMap} 기반입니다.
 * <br>
 * 세션 키 유효성 검사 로직을 공통으로 제공하며,
 * 세션 범위({@link SessionScope})에 따라 적절한 키만 등록할 수 있도록 제한합니다.
 * </p>
 *
 * <ul>
 * <li>로그인 세션: {@link LoginSessionObject}</li>
 * <li>전역 세션: {@link GlobalSessionObject}</li>
 * </ul>
 *
 * @see ISessionObject
 * @see SessionScope
 * @see ISessionKeyValidator
 * @see SessionKeyInvalidException
 * @see RuntimeContext
 * 
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
@RequiredArgsConstructor
public abstract class AbstractSessionObject extends ConcurrentHashMap<Object, Object> implements ISessionObject {

    private static final long serialVersionUID = 1L;

    /**
     * 현재 세션 범위 (LOGIN 또는 GLOBAL)
     */
    protected final SessionScope sessionScope;

    /**
     * 단일 세션 키 저장 (세션 키 유효성 검사 수행)
     *
     * @param key   저장할 세션 키
     * @param value 저장할 값
     * @return 이전 값 (있다면)
     */
    @Override
    public Object put(Object key, Object value) {
        this.checkSessionAttributeKey(key);
        return super.put(key, value);
    }

    /**
     * 여러 세션 키-값 저장 (각 키에 대해 유효성 검사 수행)
     *
     * @param map 저장할 키-값 맵
     */
    @Override
    public void putAll(Map<? extends Object, ? extends Object> map) {
        if (map == null) {
            return;
        }

        for (Object key : map.keySet()) {
            if (key != null) {
                this.checkSessionAttributeKey(key);
            }
        }

        super.putAll(map);
    }

    /**
     * 세션 키 유효성 검사 수행
     * <p>
     * 프로덕션 모드(PRD)에서는 검사하지 않음.
     * 그 외 모드에서는 {@link ISessionKeyValidator}를 통해 키 유효성 검증 수행.
     * </p>
     *
     * @param sessionKey 검사할 세션 키
     * @throws SessionKeyInvalidException 등록되지 않은 키일 경우
     */
    private void checkSessionAttributeKey(Object sessionKey) {
        // 운영(PRD) 모드에서는 키 검사 생략
        // if (RunMode.PRD.name().equalsIgnoreCase(RuntimeContext.getRunMode())) {
        // return;
        // }
        //
        // ISessionKeyValidator validator =
        // RuntimeContext.getBean(ISessionKeyValidator.class);
        // if (validator == null) return;
        //
        // switch (sessionScope) {
        // case GLOBAL -> {
        // if (!validator.isAllowedGlobalKey(sessionKey)) {
        // throw new SessionKeyInvalidException("등록 되지 않는 세션키[" + sessionScope + "][" +
        // sessionKey + "]입니다. 등록 후 사용 하세요.");
        // }
        // }
        // case LOGIN -> {
        // if (!validator.isAllowedLoginKey(sessionKey)) {
        // throw new SessionKeyInvalidException("등록 되지 않는 세션키[" + sessionScope + "][" +
        // sessionKey + "]입니다. 등록 후 사용 하세요.");
        // }
        // }
        // }
    }
}
