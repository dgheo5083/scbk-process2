package com.scbank.process.api.fw.message.converter.format.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.format.common.NumberFieldConverter;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;

import lombok.RequiredArgsConstructor;

/**
 * Jackson 기반 JSON 포맷에서 정수(Number) 타입 필드를 읽고 쓰기 위한 변환기 클래스.
 *
 * <p>
 * 이 클래스는 내부적으로 {@link NumberFieldConverter}를 사용하여
 * JSON 문자열 데이터를 고정 길이 포맷으로 변환하거나,
 * 고정 길이 포맷 데이터를 JSON 숫자 타입으로 변환하는 역할을 한다.
 * </p>
 *
 * 주요 특징:
 * <ul>
 * <li>읽기 시: JsonNode 문자열 → 고정 길이 바이트 배열 변환 → Number 변환
 * (Integer/Long/Short)</li>
 * <li>쓰기 시: Number → 고정 길이 바이트 배열 변환 → JsonNode 숫자(Integer/Long/Short) 변환</li>
 * <li>고정 길이 포맷 규칙(패딩, 길이 검증 등)을 준수</li>
 * <li>필드 타입(Short/Integer/Long)에 따라 JsonNode 숫자 타입을 자동 결정</li>
 * </ul>
 *
 * 사용 예시:
 * <ul>
 * <li>계좌번호, 회원번호, 거래번호 등 정수형 JSON 필드 변환</li>
 * </ul>
 *
 * @see NumberFieldConverter
 * @see AbstractJacksonMessageFieldConverter
 */
@RequiredArgsConstructor
public class JacksonNumberFieldConverter<T extends Number> extends AbstractJacksonMessageFieldConverter<T> {

    /**
     * 내부 위임용 고정 길이 Number 변환기
     */
    @SuppressWarnings("rawtypes")
    private final NumberFieldConverter fieldConverter;

    /**
     * JSON Node를 Number 타입으로 변환한다. (역직렬화)
     *
     * @param source         입력 JSON Node
     * @param metadata       필드 메타데이터
     * @param messageContext 변환 컨텍스트
     * @return 변환된 Number 값 (Short, Integer, Long)
     * @throws Exception 변환 실패 시
     */
    @SuppressWarnings("unchecked")
    @Override
    public T read(JsonNode source,
            IMessageFieldMetadata metadata,
            MessageContext messageContext) throws Exception {
    	if (source == null || source.isNull()) {
    		return null;
    	}
        // JSON 문자열을 바이트 배열로 변환 후 고정 길이 Number 변환
        byte[] rawBytes = source.asText()
                .getBytes(this.getEncoding(metadata, messageContext.getDefaultEncoding()));
        return (T) fieldConverter.read(rawBytes, metadata, messageContext);
    }

    /**
     * Number 값을 JSON Node로 변환한다. (직렬화)
     *
     * @param source         변환할 Number 값
     * @param metadata       필드 메타데이터
     * @param messageContext 변환 컨텍스트
     * @return 변환된 JSON 숫자 노드
     * @throws Exception 변환 실패 시
     */
    @SuppressWarnings("unchecked")
    @Override
    public JsonNode write(T source,
            IMessageFieldMetadata metadata,
            MessageContext messageContext) throws Exception {
    	if (source == null) {
    		return null;
    	}
        // 고정 길이 바이트 배열로 변환 후 다시 문자열로 변환하여 JsonNode 숫자로 파싱
        byte[] rawBytes = fieldConverter.write(source, metadata, messageContext);
        String value = new String(rawBytes, this.getEncoding(metadata, messageContext.getDefaultEncoding()));
        return this.toNumberNode(value, metadata);
    }

    /**
     * 문자열을 필드 타입(Short/Long/Integer)에 맞는 JsonNode 숫자로 변환한다.
     *
     * @param source   변환할 문자열
     * @param metadata 필드 메타데이터
     * @return 변환된 JsonNode 숫자
     */
    private JsonNode toNumberNode(String source, IMessageFieldMetadata metadata) {
        try {
            return switch (metadata.getType()) {
                case SHORT -> JsonNodeFactory.instance.numberNode(Short.parseShort(source));
                case LONG -> JsonNodeFactory.instance.numberNode(Long.parseLong(source));
                default -> JsonNodeFactory.instance.numberNode(Integer.parseInt(source));
            };
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "필드 [" + metadata.getId() + "] 값 '" + source + "'는 " + metadata.getType() + "으로 변환할 수 없습니다.",
                    e);
        }
    }
}
