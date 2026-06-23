package com.scbank.process.api.fw.message.serializer;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;

/**
 * 프레임워크 전문 직렬화 처리 인터페이스
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 14.
 */
public interface IMessageSerializer {

    /**
     * 직렬화 처리를 수행한다.
     * 
     * @param <T>    IMessageObject를 구현한 T 타입
     * @param source T 타입의 DTO 클래스
     * @param ctx    메시지 변환 컨텍스트
     * @return 직렬화 처리 결과 byte array
     * @throws Exception 직렬화 처리 도중 발생한 예외
     */
    <T extends IMessageObject> byte[] serialize(T source, MessageContext ctx) throws Exception;

    /**
     * 직렬화 처리를 수행한다.
     * 
     * @param <T>                        IMessageObject를 구현한 T 타입
     * @param source                     T 타입의 DTO 클래스
     * @param integrationMessageMetadata 전문 메시지 메타데이터
     * @param ctx                        메시지 변환 컨텍스트
     * @return 직렬화 처리 결과 byte array
     * @throws Exception 직렬화 처리 도중 발생한 예외
     */
    <T extends IMessageObject> byte[] serialize(T source, IIntegrationMessageMetadata integrationMessageMetadata,
            MessageContext ctx)
            throws Exception;
}
