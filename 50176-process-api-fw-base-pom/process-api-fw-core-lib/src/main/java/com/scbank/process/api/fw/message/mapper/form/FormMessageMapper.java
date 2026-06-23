package com.scbank.process.api.fw.message.mapper.form;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.mapper.IMessageMapper;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.serializer.form.FormMessageDeserializer;
import com.scbank.process.api.fw.message.serializer.form.FormMessageSerializer;

/**
 * {@link IMessageObject}를 application/x-www-form-urlencoded 형식으로 직렬화 및 역직렬화하기
 * 위한 메시지 매퍼입니다.
 * <p>
 * 내부적으로 {@link FormMessageSerializer}와 {@link FormMessageDeserializer}를 위임 받아
 * 사용하며,
 * 메타데이터 기반으로 원시 타입 및 리스트 필드를 처리합니다.
 * </p>
 *
 * @author
 * @since 2025.04.30
 */
public class FormMessageMapper implements IMessageMapper {

	private final FormMessageSerializer serializer;
	private final FormMessageDeserializer deserializer;

	/**
	 * 기본 생성자.
	 * <p>
	 * 기본 구현체 {@link FormMessageSerializer}, {@link FormMessageDeserializer}를 사용합니다.
	 * </p>
	 */
	public FormMessageMapper() {
		this.serializer = new FormMessageSerializer();
		this.deserializer = new FormMessageDeserializer();
	}

	/**
	 * 사용자 지정 직렬화기 및 역직렬화기를 주입받는 생성자.
	 *
	 * @param serializer   form 메시지 직렬화기
	 * @param deserializer form 메시지 역직렬화기
	 */
	public FormMessageMapper(FormMessageSerializer serializer, FormMessageDeserializer deserializer) {
		this.serializer = serializer;
		this.deserializer = deserializer;
	}

	/**
	 * DTO 객체를 application/x-www-form-urlencoded 바이트로 직렬화합니다.
	 *
	 * @param source 직렬화할 DTO 객체
	 * @param ctx    메시지 컨텍스트
	 * @return 직렬화된 바이트 배열
	 * @throws Exception 직렬화 실패 시
	 */
	@Override
	public <T extends IMessageObject> byte[] serialize(T source, MessageContext ctx) throws Exception {
		return this.serializer.serialize(source, ctx);
	}

	/**
	 * DTO 객체를 메타데이터를 기반으로 application/x-www-form-urlencoded 바이트로 직렬화합니다.
	 *
	 * @param source   직렬화할 DTO 객체
	 * @param metadata 메타데이터
	 * @param ctx      메시지 컨텍스트
	 * @return 직렬화된 바이트 배열
	 * @throws Exception 직렬화 실패 시
	 */
	@Override
	public <T extends IMessageObject> byte[] serialize(T source, IIntegrationMessageMetadata metadata,
			MessageContext ctx) throws Exception {
		return this.serializer.serialize(source, metadata, ctx);
	}

	/**
	 * application/x-www-form-urlencoded 형식의 바이트 데이터를 지정한 타입의 DTO로 역직렬화합니다.
	 *
	 * @param source     form 데이터
	 * @param targetType 역직렬화할 DTO 클래스
	 * @param ctx        메시지 컨텍스트
	 * @return 역직렬화된 DTO 객체
	 * @throws Exception 역직렬화 실패 시
	 */
	@Override
	public <T extends IMessageObject> T deserialize(byte[] source, Class<T> targetType, MessageContext ctx)
			throws Exception {
		return this.deserializer.deserialize(source, targetType, ctx);
	}

	/**
	 * 메타데이터 기반으로 form 데이터를 DTO 객체로 역직렬화합니다.
	 *
	 * @param source   form 데이터
	 * @param metadata 메타데이터
	 * @param ctx      메시지 컨텍스트
	 * @return 역직렬화된 DTO 객체
	 * @throws Exception 역직렬화 실패 시
	 */
	@Override
	public <T extends IMessageObject> T deserialize(byte[] source, IIntegrationMessageMetadata metadata,
			MessageContext ctx) throws Exception {
		return this.deserializer.deserialize(source, metadata, ctx);
	}
}
