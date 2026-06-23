package com.scbank.process.api.fw.message.serializer;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;

/**
 * 프레임워크 전문 역직렬화 처리 인터페이스
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 14.
 */
public interface IMessageDeserializer {

        /**
         * 역직렬화를 수행한다.
         * 
         * @param <T>        IMessageObject를 구현한 T 타입의 DTO
         * @param source     전문 메시지 source byte array
         * @param targetType 역직렬화 target class type
         * @param ctx        메시지 변환 컨텍스트
         * @return 역직렬화 대상 DTO 객체
         * @throws Exception 역직렬화 도중 발생한 예외
         */
        <T extends IMessageObject> T deserialize(byte[] source, Class<T> targetType, MessageContext ctx)
                        throws Exception;

        /**
         * 역직렬화를 수행한다.
         * 
         * @param <T>                        IMessageObject를 구현한 T 타입의 DTO
         * @param source                     전문 메시지 source byte array
         * @param integrationMessageMetadata 전문 메시지 메타데이터
         * @param ctx                        메시지 변환 컨텍스트
         * @return 역직렬화 대상 DTO 객체
         * @throws Exception 역직렬화 도중 발생한 예외
         */
        <T extends IMessageObject> T deserialize(byte[] source, IIntegrationMessageMetadata integrationMessageMetadata,
                        MessageContext ctx)
                        throws Exception;
}
