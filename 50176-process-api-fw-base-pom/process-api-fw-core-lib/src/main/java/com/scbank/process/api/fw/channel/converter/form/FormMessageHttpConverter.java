package com.scbank.process.api.fw.channel.converter.form;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;

import com.scbank.process.api.fw.channel.converter.AbstractMessageHttpConverter;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextHolder;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.mapper.form.FormMessageMapper;

/**
 * {@link IMessageObject} 기반 DTO 객체를 {@code application/x-www-form-urlencoded}
 * 형식으로 읽고 쓰기 위한
 * Spring {@link org.springframework.http.converter.HttpMessageConverter}
 * 구현체입니다.
 * 내부적으로 {@link FormMessageMapper}를 통해 직렬화/역직렬화를 수행하며,
 * {@link MessageContextHolder}를 사용해 변환 중 Context를 ThreadLocal에 유지합니다.
 *
 * @param <T> IMessageObject를 구현한 DTO 타입
 * @author sungdon.choi
 * @since 2025.04.30
 */
public class FormMessageHttpConverter<T extends IMessageObject> extends AbstractMessageHttpConverter<T> {

	private final FormMessageMapper messageMapper;

	/**
	 * 지정된 {@link FormMessageMapper}를 사용하여 컨버터를 생성합니다.
	 *
	 * @param messageMapper form 데이터 직렬화/역직렬화 매퍼
	 */
	public FormMessageHttpConverter(FormMessageMapper messageMapper) {
		super(Charset.forName(RuntimeContext.getDefaultEncoding()), MediaType.APPLICATION_FORM_URLENCODED);
		this.messageMapper = messageMapper;
	}

	/**
	 * 지원 여부를 판단합니다. 모든 {@link IMessageObject} 서브타입을 지원합니다.
	 *
	 * @param clazz 읽거나 쓸 수 있는지 확인할 클래스 타입
	 * @return true이면 변환 가능
	 */
	@Override
	protected boolean supports(@NonNull Class<?> clazz) {
		return IMessageObject.class.isAssignableFrom(clazz);
	}

	/**
	 * {@code application/x-www-form-urlencoded} 형식의 HTTP 요청을 읽어
	 * {@link IMessageObject} 타입으로 변환합니다.
	 *
	 * @param clazz        변환할 대상 클래스
	 * @param inputMessage HTTP 입력 메시지
	 * @return 변환된 DTO 객체
	 * @throws IOException                     변환 중 I/O 오류 발생 시
	 * @throws HttpMessageNotReadableException 변환 실패 시
	 */
	@Override
	protected @NonNull T readInternal(@NonNull Class<? extends T> clazz, @NonNull HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		long length = inputMessage.getHeaders().getContentLength();
		byte[] requestBytes = inputMessage.getBody().readNBytes((int) length);

		Charset charset = this.resolveCharset(inputMessage, Charset.forName(RuntimeContext.getDefaultEncoding()));
		MessageContext messageContext = this.createMessageContext(MessageFormat.FORM, charset.name());
		MessageContextHolder.set(messageContext);

		try {
			return this.messageMapper.deserialize(requestBytes, clazz, messageContext);
		} catch (Exception e) {
			throw new HttpMessageNotReadableException("Form 데이터 역직렬화 실패", e, inputMessage);
		} finally {
			MessageContextHolder.clear();
		}
	}

	/**
	 * {@link IMessageObject} 타입의 객체를 {@code application/x-www-form-urlencoded} 형식으로
	 * HTTP 응답에 씁니다.
	 *
	 * @param t             직렬화할 대상 객체
	 * @param outputMessage HTTP 응답 메시지
	 * @throws IOException                     쓰기 실패 시
	 * @throws HttpMessageNotWritableException 직렬화 실패 시
	 */
	@Override
	protected void writeInternal(@NonNull T t, @NonNull HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		Charset charset = this.resolveCharset(outputMessage, Charset.forName(RuntimeContext.getDefaultEncoding()));
		MessageContext messageContext = this.createMessageContext(MessageFormat.FORM, charset.name());
		MessageContextHolder.set(messageContext);

		try {
			byte[] responseBytes = messageMapper.serialize(t, messageContext);
			outputMessage.getHeaders().setContentLength(responseBytes.length);
			outputMessage.getBody().write(responseBytes);
		} catch (Exception e) {
			throw new HttpMessageNotWritableException("Form 데이터 직렬화 실패", e);
		} finally {
			MessageContextHolder.clear();
		}
	}
}
