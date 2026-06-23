package com.scbank.process.api.fw.message.converter.format.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.scbank.process.api.fw.message.converter.AbstractMessageFieldConverter;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverter;

/**
 * Jackson 기반 메시지 필드 변환기 추상 클래스.
 *
 * <p>
 * 이 클래스는 JSON 포맷(JsonNode) 기반의 필드 변환 처리를 위한
 * 공통 부모 클래스로 사용된다.
 * </p>
 *
 * 주요 특징:
 * <ul>
 * <li>{@link JsonNode} 타입의 입력(JSON)을 처리한다.</li>
 * <li>출력 타입은 제네릭 {@code T}로 자유롭게 지정할 수 있다.</li>
 * <li>Jackson 파싱/직렬화 공통 처리를 확장하기 위한 기본 구조를 제공한다.</li>
 * </ul>
 *
 * 상속 대상:
 * <ul>
 * <li>문자열 변환기 (예: JacksonStringFieldConverter)</li>
 * <li>숫자 변환기 (예: JacksonNumberFieldConverter)</li>
 * <li>날짜 변환기 등 다양한 JSON 필드 변환기 구현 가능</li>
 * </ul>
 *
 * @param <T> 변환 결과 타입 (예: String, Integer, LocalDateTime 등)
 * @see JsonNode
 * @see IMessageFieldConverter
 */
public abstract class AbstractJacksonMessageFieldConverter<T>
        extends AbstractMessageFieldConverter
        implements IMessageFieldConverter<JsonNode, T> {
}
