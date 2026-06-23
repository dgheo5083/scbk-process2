package com.scbank.process.api.fw.session;

import java.io.Serializable;
import java.util.Map;

/**
 * 세션 데이터 저장 객체 인터페이스
 * <p>
 * {@link java.util.Map} 인터페이스를 확장하여, 키-값 기반으로 세션 데이터를 저장합니다.
 * 이 인터페이스는 글로벌 세션, 로그인 세션 등 다양한 세션 컨텍스트에 사용될 수 있으며,
 * 값은 반드시 {@link Serializable}을 구현한 객체만 허용합니다.
 * </p>
 * <p>
 * 구현 예: {@code GlobalSessionObject}, {@code LoginSessionObject}
 * </p>
 * 
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
public interface ISessionObject extends Map<Object, Object> {
}
