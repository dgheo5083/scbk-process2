package com.scbank.process.api.fw.session.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.session.ISessionContext;
import com.scbank.process.api.fw.session.ISessionContextAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 세션 어댑터 기본 구현체
 * 다양한 형태의 외부 세션 데이터를 {@link ISessionContext} 타입으로 변환(adapt)합니다.
 * <ul>
 * <li>{@link ISessionContext} 타입은 그대로 반환</li>
 * <li>{@link Map} 형태는 {@link DefaultSessionContext}로 변환</li>
 * <li>{@code JSON String}은 Jackson으로 역직렬화하여 변환</li>
 * </ul>
 * 
 * <p>
 * 예외 상황이나 지원되지 않는 타입일 경우 {@link IllegalArgumentException}을 발생시킵니다.
 * </p>
 * 
 * @see ISessionContextAdapter
 * @see DefaultSessionContext
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultSessionContextAdapter implements ISessionContextAdapter {

    /**
     * Jackson ObjectMapper (JSON ↔ 객체 변환용)
     */
    private final ObjectMapper objectMapper;

    /**
     * 주어진 raw 객체를 {@link ISessionContext}로 변환
     *
     * @param raw 변환할 원본 객체 (Map, JSON String, ISessionContext 등)
     * @return 변환된 ISessionContext 인스턴스
     * @throws IllegalArgumentException 지원하지 않는 타입이거나 역직렬화 실패 시
     */
    @Override
    public ISessionContext adapt(Object raw) {

        if (log.isDebugEnabled()) {
            log.debug("# 프레임워크 SessionContext 어댑터 처리 시작.. 세션 데이터 type: {}", raw.getClass());
        }

        // 이미 ISessionContext 타입인 경우 그대로 반환
        if (raw instanceof ISessionContext context) {
            return context;
        }

        // Map 구조인 경우 ObjectMapper로 변환
        if (raw instanceof Map) {
            return objectMapper.convertValue(raw, DefaultSessionContext.class);
        }

        // JSON 문자열인 경우 역직렬화 처리
        if (raw instanceof String json) {
            try {
                return objectMapper.readValue(json, DefaultSessionContext.class);
            } catch (Exception e) {
                throw new IllegalArgumentException("프레임워크 세션 JSON 역직렬화 실패", e);
            }
        }

        // 지원하지 않는 타입
        throw new IllegalArgumentException("지원하지 않는 타입: " + raw.getClass());
    }
}
