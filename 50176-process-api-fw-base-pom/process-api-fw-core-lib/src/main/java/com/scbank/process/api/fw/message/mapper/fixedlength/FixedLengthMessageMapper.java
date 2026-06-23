package com.scbank.process.api.fw.message.mapper.fixedlength;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.mapper.IMessageMapper;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.serializer.fixedlength.FixedLengthMessageDeserializer;
import com.scbank.process.api.fw.message.serializer.fixedlength.FixedLengthMessageSerializer;

/**
 * 프레임워크 DTO를 고정 길이(Fixed Length) 방식으로 직렬화/역직렬화하는 메시지 매퍼입니다.
 * 
 * <p>
 * {@link FixedLengthMessageSerializer}를 이용해 DTO를 고정 길이 바이트 배열로 직렬화하고,
 * {@link FixedLengthMessageDeserializer}를 이용해 바이트 배열을 DTO로 역직렬화합니다.
 * </p>
 * 
 * <p>
 * 메타데이터를 제공하는 경우와 제공하지 않는 경우 모두 지원합니다.
 * </p>
 * 
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 14.
 */
public class FixedLengthMessageMapper implements IMessageMapper {

    /** 고정 길이 메시지를 직렬화하는 컴포넌트 */
    protected final FixedLengthMessageSerializer serializer;

    /** 고정 길이 메시지를 역직렬화하는 컴포넌트 */
    protected final FixedLengthMessageDeserializer deserializer;

    /**
     * 
     */
    public FixedLengthMessageMapper() {
        this.serializer = new FixedLengthMessageSerializer();
        this.deserializer = new FixedLengthMessageDeserializer();
    }

    /**
     * 
     * @param serializer   고정 길이 메시지를 직렬화하는 컴포넌트
     * @param deserializer 고정 길이 메시지를 역직렬화하는 컴포넌트
     */
    public FixedLengthMessageMapper(FixedLengthMessageSerializer serializer,
            FixedLengthMessageDeserializer deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    /**
     * 메시지 객체를 고정 길이 바이트 배열로 직렬화합니다.
     *
     * @param source 직렬화할 메시지 객체
     * @param ctx    변환 컨텍스트
     * @param <T>    메시지 객체 타입
     * @return 직렬화된 바이트 배열
     * @throws Exception 직렬화 실패 시 발생할 수 있는 예외
     */
    @Override
    public <T extends IMessageObject> byte[] serialize(T source, MessageContext ctx) throws Exception {
        return serializer.serialize(source, ctx);
    }

    /**
     * 메타데이터를 기반으로 메시지 객체를 고정 길이 바이트 배열로 직렬화합니다.
     *
     * @param source   직렬화할 메시지 객체
     * @param metadata 직렬화에 사용할 통합 메타데이터
     * @param ctx      변환 컨텍스트
     * @param <T>      메시지 객체 타입
     * @return 직렬화된 바이트 배열
     * @throws Exception 직렬화 실패 시 발생할 수 있는 예외
     */
    @Override
    public <T extends IMessageObject> byte[] serialize(T source,
            IIntegrationMessageMetadata metadata,
            MessageContext ctx) throws Exception {
        return serializer.serialize(source, metadata, ctx);
    }

    /**
     * 고정 길이 바이트 배열을 지정한 타입의 메시지 객체로 역직렬화합니다.
     *
     * @param source     역직렬화할 바이트 배열
     * @param targetType 변환할 대상 메시지 타입
     * @param ctx        변환 컨텍스트
     * @param <T>        메시지 객체 타입
     * @return 역직렬화된 메시지 객체
     * @throws Exception 역직렬화 실패 시 발생할 수 있는 예외
     */
    @Override
    public <T extends IMessageObject> T deserialize(byte[] source,
            Class<T> targetType,
            MessageContext ctx) throws Exception {
        return deserializer.deserialize(source, targetType, ctx);
    }

    /**
     * 메타데이터를 기반으로 고정 길이 바이트 배열을 메시지 객체로 역직렬화합니다.
     *
     * @param source   역직렬화할 바이트 배열
     * @param metadata 역직렬화에 사용할 통합 메타데이터
     * @param ctx      변환 컨텍스트
     * @param <T>      메시지 객체 타입
     * @return 역직렬화된 메시지 객체
     * @throws Exception 역직렬화 실패 시 발생할 수 있는 예외
     */
    @Override
    public <T extends IMessageObject> T deserialize(byte[] source,
            IIntegrationMessageMetadata metadata,
            MessageContext ctx) throws Exception {
        return deserializer.deserialize(source, metadata, ctx);
    }
}
