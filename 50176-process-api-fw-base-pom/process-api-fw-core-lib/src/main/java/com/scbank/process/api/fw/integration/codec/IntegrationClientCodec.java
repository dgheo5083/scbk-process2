package com.scbank.process.api.fw.integration.codec;

import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;

/**
 * 연계 전문 송수신을 위한 Codec 인터페이스
 *
 * <p>
 * 요청 DTO를 바이트 배열로 직렬화하고,
 * 응답 바이트 배열을 DTO로 역직렬화하는 역할을 수행합니다.
 *
 * <p>
 * 포맷별(JSON, XML, FIXED, DELIMITED 등)로 구현체를 분리하여 사용하며,
 * {@code IntegrationClientCodecRegistry}를 통해 포맷별 Codec을 주입받습니다.
 *
 * <p>
 * 직렬화/역직렬화 시 {@link IntegrationContext}를 기반으로
 * 시스템 ID, 인터페이스 ID, 인코딩 정보 등을 함께 전달받아 동적 처리가 가능합니다.
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public interface IntegrationClientCodec {

    /**
     * 요청 객체를 바이트 배열로 직렬화합니다.
     *
     * @param context 연계 컨텍스트 (포맷, 인코딩, 시스템 정보 등 포함)
     * @param request 직렬화할 요청 DTO
     * @return 직렬화된 byte 배열
     */
    <T extends IMessageObject> byte[] encode(MessageContext context, T request) throws Exception;

    /**
     * 응답 바이트 배열을 지정된 타입의 DTO로 역직렬화합니다.
     *
     * @param context      연계 컨텍스트
     * @param response     응답 전문 바이트 배열
     * @param responseType 역직렬화 대상 DTO 클래스 타입
     * @param <T>          반환할 DTO 타입
     * @return 역직렬화된 DTO 객체
     */
    <T extends IMessageObject> T decode(MessageContext context, byte[] response,
            Class<T> responseType) throws Exception;
}
