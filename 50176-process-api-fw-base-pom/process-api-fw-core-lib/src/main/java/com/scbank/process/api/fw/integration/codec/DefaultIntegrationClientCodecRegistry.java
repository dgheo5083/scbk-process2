package com.scbank.process.api.fw.integration.codec;

import java.util.EnumMap;
import java.util.Map;

import com.scbank.process.api.fw.message.enums.MessageFormat;

/**
 * 기본 Codec 레지스트리 구현체
 *
 * <p>
 * {@link MessageFormat} 열거형을 기준으로 {@link IntegrationClientCodec} 구현체를 등록/조회합니다.
 * <ul>
 * <li>{@link EnumMap} 기반으로 성능 및 타입 안전성을 확보</li>
 * <li>외부에서 직접 {@code register()} 호출로 Codec 등록 가능</li>
 * </ul>
 *
 * <p>
 * 프레임워크에서는 이 레지스트리를 통해 연계 메시지 포맷(JSON, FIXED 등)에 맞는 Codec을 유연하게 교체/확장할 수 있습니다.
 * <p>
 * 예:
 * 
 * <pre>{@code
 * registry.register(MessageFormat.JSON, new JacksonJsonCodec());
 * registry.register(MessageFormat.FIXED, new FixedLengthCodec());
 * }</pre>
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public class DefaultIntegrationClientCodecRegistry implements IntegrationClientCodecRegistry {

    /**
     * 포맷별 Codec 매핑 테이블
     */
    private final Map<MessageFormat, IntegrationClientCodec> codecs = new EnumMap<>(MessageFormat.class);

    /**
     * Codec을 포맷 기준으로 등록합니다.
     *
     * @param format 등록할 메시지 포맷
     * @param codec  해당 포맷을 처리할 Codec 구현체
     */
    public void register(MessageFormat format, IntegrationClientCodec codec) {
        codecs.put(format, codec);
    }

    /**
     * 지정된 포맷에 해당하는 Codec을 반환합니다.
     *
     * @param format 메시지 포맷
     * @return 등록된 Codec 구현체
     * @throws IllegalArgumentException 등록되지 않은 포맷일 경우 예외
     */
    @Override
    public IntegrationClientCodec getCodec(MessageFormat format) {
        IntegrationClientCodec codec = codecs.get(format);
        if (codec == null) {
            throw new IllegalArgumentException("Codec not found for format: " + format);
        }
        return codec;
    }
}
