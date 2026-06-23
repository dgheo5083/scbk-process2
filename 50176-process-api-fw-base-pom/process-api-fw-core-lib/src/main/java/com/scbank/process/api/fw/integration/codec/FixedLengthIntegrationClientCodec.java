package com.scbank.process.api.fw.integration.codec;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.mapper.fixedlength.FixedLengthMessageMapper;

import lombok.RequiredArgsConstructor;

/**
 * 고정 길이 전문 포맷을 처리하는 연계 시스템용 Codec 구현체
 *
 * <p>
 * {@link FixedLengthMessageMapper}를 이용하여 요청 객체를 고정 길이 byte[]로 직렬화하거나,
 * 응답 byte[]를 지정된 DTO 타입으로 역직렬화합니다.
 *
 * <p>
 * 주로 MCI, HOST 계열 연계 시스템에서 사용되며, 프레임워크 내
 * {@link IntegrationClientCodecRegistry}에 등록되어 사용됩니다.
 *
 * <p>
 * {@code encode()}는 요청 DTO → 고정 길이 byte[], {@code decode()}는 응답 byte[] → DTO 로
 * 동작합니다.
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
@RequiredArgsConstructor
public class FixedLengthIntegrationClientCodec implements IntegrationClientCodec {

    /**
     * 고정 길이 전문 매핑기 (Serializer/Deserializer)
     */
    private final FixedLengthMessageMapper fixedLengthMessageMapper;

    /**
     * 요청 DTO를 고정 길이 byte 배열로 직렬화합니다.
     *
     * @param context 연계 컨텍스트 (포맷, 인코딩, 시스템 ID 등 포함)
     * @param request 직렬화 대상 DTO 객체
     * @return 고정 길이 직렬화 결과
     */
    @Override
    public <T extends IMessageObject> byte[] encode(MessageContext context, T request)
            throws Exception {
        return this.fixedLengthMessageMapper.serialize(request, context);
    }

    /**
     * 고정 길이 byte 배열을 DTO로 역직렬화합니다.
     *
     * @param context      연계 컨텍스트
     * @param response     응답 전문 바이트 배열
     * @param responseType 역직렬화 대상 DTO 타입
     * @param <T>          반환 타입
     * @return 역직렬화된 DTO
     */
    @Override
    public <T extends IMessageObject> T decode(MessageContext context, byte[] response,
            Class<T> responseType) throws Exception {
        return this.fixedLengthMessageMapper.deserialize(response, responseType, context);
    }
}
