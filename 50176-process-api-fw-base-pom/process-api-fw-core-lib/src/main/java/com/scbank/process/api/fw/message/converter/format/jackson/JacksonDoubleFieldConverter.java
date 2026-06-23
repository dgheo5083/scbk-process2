package com.scbank.process.api.fw.message.converter.format.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.format.common.DoubleFieldConverter;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;

import lombok.RequiredArgsConstructor;

/**
 * Jackson 기반 JSON 포맷에서 Double 타입 필드를 읽고 쓰기 위한 변환기 클래스.
 *
 * <p>
 * 이 클래스는 내부적으로 {@link DoubleFieldConverter}를 이용하여
 * JSON 문자열 데이터를 고정 길이 포맷으로 변환하거나,
 * 고정 길이 포맷 데이터를 JSON 숫자(Double) 타입으로 변환하는 역할을 한다.
 * </p>
 *
 * 주요 특징:
 * <ul>
 * <li>읽기 시: JsonNode 문자열 → 고정 길이 바이트 배열 변환 → Double 변환</li>
 * <li>쓰기 시: Double → 고정 길이 바이트 배열 변환 → JsonNode 숫자(Double) 변환</li>
 * <li>고정 길이 포맷 규칙(패딩, 길이 검증 등)을 준수</li>
 * </ul>
 *
 * 사용 예시:
 * <ul>
 * <li>환율, 금액, 퍼센트 등 소수점 실수형 JSON 필드 변환</li>
 * </ul>
 *
 * @see DoubleFieldConverter
 * @see AbstractJacksonMessageFieldConverter
 */
@RequiredArgsConstructor
public class JacksonDoubleFieldConverter extends AbstractJacksonMessageFieldConverter<Double> {

    /**
     * 내부 위임용 고정 길이 Double 변환기
     */
    private final DoubleFieldConverter fieldConverter;

    /**
     * JSON Node를 Double 타입으로 변환한다. (역직렬화)
     *
     * @param source         입력 JSON Node
     * @param fieldMetadata  필드 메타데이터
     * @param messageContext 변환 컨텍스트
     * @return 변환된 Double 값
     * @throws Exception 변환 실패 시
     */
    @Override
    public Double read(JsonNode source,
            IMessageFieldMetadata fieldMetadata,
            MessageContext messageContext) throws Exception {
    	if (source == null || source instanceof NullNode) {
    		return null;
    	}
        // JSON 문자열을 바이트 배열로 변환 후 고정 길이 Double 변환
        byte[] rawBytes = source.asText()
                .getBytes(this.getEncoding(fieldMetadata, messageContext.getDefaultEncoding()));
        return fieldConverter.read(rawBytes, fieldMetadata, messageContext);
    }

    /**
     * Double 값을 JSON Node로 변환한다. (직렬화)
     *
     * @param source         변환할 Double 값
     * @param fieldMetadata  필드 메타데이터
     * @param messageContext 변환 컨텍스트
     * @return 변환된 JSON 숫자 노드
     * @throws Exception 변환 실패 시
     */
    @Override
    public JsonNode write(Double source,
            IMessageFieldMetadata fieldMetadata,
            MessageContext messageContext) throws Exception {
    	if (source == null) {
    		return null;
    	}
        // 고정 길이 바이트 배열로 변환 후 다시 문자열로 변환하여 JSON 숫자로 파싱
        byte[] rawBytes = fieldConverter.write(source, fieldMetadata, messageContext);
        String value = new String(rawBytes, this.getEncoding(fieldMetadata, messageContext.getDefaultEncoding()));
        return JsonNodeFactory.instance.numberNode(Double.parseDouble(value));
    }
}
