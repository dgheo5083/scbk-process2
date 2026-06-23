package com.scbank.process.api.fw.integration.connector.initializer;

import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.context.IntegrationContext;

import io.netty.channel.Channel;

/**
 * 연계 시스템 클라이언트 채널 초기화 인터페이스
 *
 * <p>
 * Netty 기반 연계 통신에서 채널(Channel)을 초기화할 때 파이프라인에
 * 사용자 정의 인코더, 디코더, 타임아웃 핸들러 등을 삽입할 수 있도록 제공하는 전략 인터페이스입니다.
 *
 * <p>
 * 예를 들어 시스템별 전문 포맷이나 전송 정책에 따라 서로 다른 ChannelPipeline 구성이 필요할 경우
 * 본 인터페이스 구현체를 통해 유연하게 대응 가능합니다.
 *
 * <p>
 * 스프링에서 {@code @Component} 혹은 수동 주입 방식으로 시스템별 initializer를 구성하여 사용합니다.
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
@FunctionalInterface
public interface IntegrationConnectorChannelInitializer {

    /**
     * Netty Channel 초기화 메서드
     *
     * @param channel      Netty 채널 객체
     * @param context      연계 컨텍스트 (시스템 ID, 인터페이스 ID, 트레이스 등 포함)
     * @param systemConfig 시스템별 커스텀 설정 (타임아웃, 포맷 등 포함)
     */
    void initialize(Channel channel, IntegrationContext context, IntegrationSystemConfig systemConfig);
}
