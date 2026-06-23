package com.scbank.process.api.fw.integration.codec;

import com.scbank.process.api.fw.message.enums.MessageFormat;

/**
 * 연계 메시지 포맷별 Codec 제공 레지스트리 인터페이스
 *
 * <p>
 * {@link MessageFormat} 값을 기준으로 적절한 {@link IntegrationClientCodec} 구현체를 반환합니다.
 * <ul>
 * <li>예: JSON, XML, FIXED, DELIMITED, BINARY, CUSTOM 등</li>
 * <li>프레임워크 내부에서는 Codec 인스턴스를 직접 참조하지 않고, 본 레지스트리를 통해 조회</li>
 * </ul>
 *
 * <p>
 * 이 인터페이스의 구현체에서는 Spring Bean 또는 수동 등록된 Codec 인스턴스를 format 기준으로 매핑합니다.
 *
 * <p>
 * 사용 위치 예: {@code IntegrationManager#getIntegrationClientCodec()}
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public interface IntegrationClientCodecRegistry {

    /**
     * 주어진 메시지 포맷에 해당하는 Codec 인스턴스를 반환합니다.
     *
     * @param format 연계 메시지 포맷
     * @return 해당 포맷을 처리할 IntegrationClientCodec 구현체
     * @throws IllegalArgumentException 포맷에 대응하는 Codec이 없는 경우
     */
    IntegrationClientCodec getCodec(MessageFormat format);
}
