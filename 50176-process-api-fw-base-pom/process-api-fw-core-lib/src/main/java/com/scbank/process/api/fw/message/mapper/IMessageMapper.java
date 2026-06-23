package com.scbank.process.api.fw.message.mapper;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;

/**
 * 메시지 객체를 직렬화(serialize)하거나 역직렬화(deserialize)하는 매퍼 인터페이스입니다.
 * 
 * <p>
 * 기본적으로 {@link IMessageObject}를 바이트 배열(byte[])로 변환하거나,
 * 반대로 바이트 배열을 {@link IMessageObject} 타입으로 변환합니다.
 * 변환 과정에서는 {@link MessageContext}를 통해 트랜스폼 관련 정보를 주고받습니다.
 * 필요에 따라 {@link IIntegrationMessageMetadata}를 기반으로 변환 메타정보를 세부 조정할 수 있습니다.
 * </p>
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 14.
 */
public interface IMessageMapper {

        /**
         * 주어진 메시지 객체를 직렬화하여 바이트 배열로 변환합니다.
         *
         * @param source 직렬화할 메시지 객체
         * @param ctx    변환 컨텍스트 (트랜스폼 관련 정보)
         * @param <T>    메시지 객체 타입
         * @return 직렬화된 바이트 배열
         * @throws Exception 직렬화 실패 시 발생할 수 있는 예외
         */
        <T extends IMessageObject> byte[] serialize(T source, MessageContext ctx) throws Exception;

        /**
         * 주어진 메시지 객체를 메타데이터를 기반으로 직렬화하여 바이트 배열로 변환합니다.
         *
         * @param source   직렬화할 메시지 객체
         * @param metadata 직렬화에 사용할 통합 메타데이터
         * @param ctx      변환 컨텍스트 (트랜스폼 관련 정보)
         * @param <T>      메시지 객체 타입
         * @return 직렬화된 바이트 배열
         * @throws Exception 직렬화 실패 시 발생할 수 있는 예외
         */
        <T extends IMessageObject> byte[] serialize(T source, IIntegrationMessageMetadata metadata,
                        MessageContext ctx) throws Exception;

        /**
         * 주어진 바이트 배열을 지정된 메시지 타입으로 역직렬화합니다.
         *
         * @param source     역직렬화할 바이트 배열
         * @param targetType 변환할 대상 메시지 클래스 타입
         * @param ctx        변환 컨텍스트 (트랜스폼 관련 정보)
         * @param <T>        메시지 객체 타입
         * @return 역직렬화된 메시지 객체
         * @throws Exception 역직렬화 실패 시 발생할 수 있는 예외
         */
        <T extends IMessageObject> T deserialize(byte[] source, Class<T> targetType, MessageContext ctx)
                        throws Exception;

        /**
         * 주어진 바이트 배열을 메타데이터를 기반으로 지정된 메시지 타입으로 역직렬화합니다.
         *
         * @param source   역직렬화할 바이트 배열
         * @param metadata 역직렬화에 사용할 통합 메타데이터
         * @param ctx      변환 컨텍스트 (트랜스폼 관련 정보)
         * @param <T>      메시지 객체 타입
         * @return 역직렬화된 메시지 객체
         * @throws Exception 역직렬화 실패 시 발생할 수 있는 예외
         */
        <T extends IMessageObject> T deserialize(byte[] source, IIntegrationMessageMetadata metadata,
                        MessageContext ctx) throws Exception;
}
