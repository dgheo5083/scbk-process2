package com.scbank.process.api.fw.integration.request;

import java.util.Map;

import com.scbank.process.api.fw.integration.cfg.IntegrationRequestOptions;
import com.scbank.process.api.fw.message.IMessageObject;

/**
 * 연계 요청 전문의 헤더를 생성하는 빌더 인터페이스
 *
 * <p>
 * 각 연계 시스템(MCI, HOST, FEP 등)에 맞는 헤더 형식을 생성하는 전략을 제공합니다.
 *
 * <ul>
 * <li>입력값으로는 공통 헤더 맵(defaultHeader)과 사용자 설정(cfg)을 전달받습니다.</li>
 * <li>출력값은 IMessageObject를 구현한 헤더 DTO입니다.</li>
 * </ul>
 *
 * <p>
 * 구현체는 시스템별로 정의하며, Spring Bean으로 등록되어 사용됩니다.
 *
 * @param <H> 생성할 헤더 객체 타입 (IMessageObject 구현체)
 * @param <C> 연계 설정 클래스 타입 (IntegrationConfig 구현체)
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public interface IntegrationRequestHeaderBuilder<H extends IMessageObject, CFG extends IntegrationRequestOptions> {

    /**
     * 연계 요청 전문의 헤더 객체를 생성합니다.
     *
     * @param defaultHeader 공통 헤더 키-값 (예: traceId, channelCode, userId 등)
     * @param cfg           사용자 정의 연계 설정
     * @return 생성된 헤더 객체
     */
    H build(Map<String, Object> defaultHeader, CFG cfg);
}
