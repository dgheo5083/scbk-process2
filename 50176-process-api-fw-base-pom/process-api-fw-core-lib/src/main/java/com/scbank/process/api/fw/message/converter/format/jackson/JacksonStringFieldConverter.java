package com.scbank.process.api.fw.message.converter.format.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.format.common.StringFieldConverter;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;

import lombok.RequiredArgsConstructor;

/**
 * Jackson 기반 JSON 포맷에서 String 타입 필드를 읽고 쓰기 위한 변환기 클래스.
 *
 * <p>
 * 이 클래스는 내부적으로 {@link StringFieldConverter}를 사용하여
 * JSON 문자열 데이터를 고정 길이 포맷으로 변환하거나,
 * 고정 길이 포맷 데이터를 JSON 문자열로 변환하는 역할을 한다.
 * </p>
 *
 * 주요 특징:
 * <ul>
 * <li>읽기 시: JsonNode 문자열 → 고정 길이 바이트 배열 변환 → String 변환</li>
 * <li>쓰기 시: String → 고정 길이 바이트 배열 변환 → JsonNode 문자열(TextNode) 변환</li>
 * <li>고정 길이 포맷 규칙(패딩, 공백 제거, 길이 검증 등)을 준수</li>
 * <li>TextNode 타입 여부를 구분하여 처리</li>
 * </ul>
 *
 * 사용 예시:
 * <ul>
 * <li>이름, 주소, 메시지 내용 등 일반 문자열 JSON 필드 변환</li>
 * </ul>
 *
 * @see StringFieldConverter
 * @see AbstractJacksonMessageFieldConverter
 */
@RequiredArgsConstructor
public class JacksonStringFieldConverter extends AbstractJacksonMessageFieldConverter<String> {

    /**
     * 내부 위임용 고정 길이 String 변환기
     */
    private final StringFieldConverter fieldConverter;

    /**
     * JSON Node를 String 타입으로 변환한다. (역직렬화)
     *
     * @param source         입력 JSON Node
     * @param metadata       필드 메타데이터
     * @param messageContext 변환 컨텍스트
     * @return 변환된 String 값
     * @throws Exception 변환 실패 시
     */
    @Override
    public String read(JsonNode source,
            IMessageFieldMetadata metadata,
            MessageContext messageContext) throws Exception {
    	String value = source.isNull() ? null : source.asText();
        byte[] sourceBytes = value == null ? new byte[] {} : value.getBytes(this.getEncoding(metadata, messageContext.getDefaultEncoding()));
        return fieldConverter.read(sourceBytes, metadata, messageContext);
    }

    /**
     * String 값을 JSON Node로 변환한다. (직렬화)
     *
     * @param value          변환할 String 값
     * @param metadata       필드 메타데이터
     * @param messageContext 변환 컨텍스트
     * @return 변환된 JSON 문자열 노드
     * @throws Exception 변환 실패 시
     */
    @Override
    public JsonNode write(String value,
            IMessageFieldMetadata metadata,
            MessageContext messageContext) throws Exception {
        // 고정 길이 바이트 배열로 변환 후 다시 문자열로 변환하여 TextNode 생성
        byte[] bytes = this.fieldConverter.write(value, metadata, messageContext);
        return JsonNodeFactory.instance.textNode(
                new String(bytes, this.getEncoding(metadata, messageContext.getDefaultEncoding())));
    }
}
