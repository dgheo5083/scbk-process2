package com.scbank.process.api.fw.message.mapper.multipart;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.mapper.IMessageMapper;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.serializer.multipart.MultipartMessageDeserializer;

/**
 * {@code multipart/form-data} 요청을 처리하는 {@link IMessageMapper} 구현체입니다.
 * <p>
 * 직렬화는 지원하지 않으며, multipart 요청에 포함된 파라미터 및 파일 데이터를
 * {@link IMessageObject}로 역직렬화하는 데에만 사용됩니다.
 * </p>
 *
 * @author
 * @since 1.0
 */
public class MultipartMessageMapper implements IMessageMapper {

	private final MultipartMessageDeserializer deserializer;

	/**
	 * 생성자
	 *
	 * @param deserializer multipart 요청 데이터를 처리할 deserializer 구현체
	 */
	public MultipartMessageMapper(MultipartMessageDeserializer deserializer) {
		this.deserializer = deserializer;
	}

	/**
	 * multipart/form-data는 직렬화를 지원하지 않기 때문에 호출 시
	 * {@link UnsupportedOperationException}을 발생시킵니다.
	 *
	 * @throws UnsupportedOperationException 직렬화 미지원
	 */
	@Override
	public <T extends IMessageObject> byte[] serialize(T source, MessageContext ctx) throws Exception {
		throw new UnsupportedOperationException("multipart/form-data 직렬화는 지원되지 않습니다.");
	}

	/**
	 * multipart/form-data는 직렬화를 지원하지 않기 때문에 호출 시
	 * {@link UnsupportedOperationException}을 발생시킵니다.
	 *
	 * @throws UnsupportedOperationException 직렬화 미지원
	 */
	@Override
	public <T extends IMessageObject> byte[] serialize(T source, IIntegrationMessageMetadata metadata,
			MessageContext ctx) throws Exception {
		throw new UnsupportedOperationException("multipart/form-data 직렬화는 지원되지 않습니다.");
	}

	/**
	 * 주어진 클래스 타입 기반으로 multipart 요청을 역직렬화하여 {@link IMessageObject}를 반환합니다.
	 *
	 * @param source     multipart에서는 본문이 사용되지 않기 때문에 무시됩니다
	 * @param targetType 역직렬화할 DTO 클래스 타입
	 * @param ctx        메시지 컨텍스트
	 * @return 역직렬화된 {@link IMessageObject} 인스턴스
	 */
	@Override
	public <T extends IMessageObject> T deserialize(byte[] source, Class<T> targetType, MessageContext ctx)
			throws Exception {
		return this.deserializer.deserialize(source, targetType, ctx);
	}

	/**
	 * 주어진 메타데이터 기반으로 multipart 요청을 역직렬화하여 {@link IMessageObject}를 반환합니다.
	 *
	 * @param source   multipart에서는 본문이 사용되지 않기 때문에 무시됩니다
	 * @param metadata 메시지 메타데이터
	 * @param ctx      메시지 컨텍스트
	 * @return 역직렬화된 {@link IMessageObject} 인스턴스
	 */
	@Override
	public <T extends IMessageObject> T deserialize(byte[] source, IIntegrationMessageMetadata metadata,
			MessageContext ctx) throws Exception {
		return this.deserializer.deserialize(source, metadata, ctx);
	}
}
