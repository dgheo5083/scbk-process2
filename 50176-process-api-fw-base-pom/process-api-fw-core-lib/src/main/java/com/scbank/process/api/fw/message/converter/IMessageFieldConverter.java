package com.scbank.process.api.fw.message.converter;

import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;

/**
 * 프레임워크 전문(Field) 변환을 담당하는 기본 인터페이스.
 *
 * <p>
 * 이 인터페이스는 고정 길이, JSON, XML 등 다양한 포맷에서
 * 개별 필드 단위 변환(직렬화/역직렬화)을 일관된 방식으로 처리하기 위해 정의된다.
 * </p>
 *
 * 변환 흐름:
 * <ul>
 * <li>{@code read} : 외부 포맷 데이터를 내부 객체로 변환 (역직렬화)</li>
 * <li>{@code write} : 내부 객체를 외부 포맷 데이터로 변환 (직렬화)</li>
 * </ul>
 *
 * 제네릭 타입 설명:
 * <ul>
 * <li>S : 변환 전 데이터 타입 (source 타입, 예: byte[], JsonNode 등)</li>
 * <li>T : 변환 후 데이터 타입 (target 타입, 예: Integer, BigDecimal, String 등)</li>
 * </ul>
 *
 * @param <S> 입력(source) 타입
 * @param <T> 출력(target) 타입
 *
 * @author sungdon.choi
 */
public interface IMessageFieldConverter<S, T> {

    /**
     * S 타입의 source를 읽어 T 타입으로 변환한다. (역직렬화)
     *
     * @param source         변환할 원본 데이터
     * @param metadata       변환 대상 필드 메타데이터
     * @param messageContext 변환 과정의 컨텍스트 정보
     * @return 변환된 T 타입 객체
     * @throws Exception 변환 실패 시 발생
     */
    T read(S source, IMessageFieldMetadata metadata, MessageContext messageContext) throws Exception;

    /**
     * T 타입의 값을 S 타입으로 변환한다. (직렬화)
     *
     * @param value          변환할 대상 값
     * @param metadata       변환 대상 필드 메타데이터
     * @param messageContext 변환 과정의 컨텍스트 정보
     * @return 변환된 S 타입 객체
     * @throws Exception 변환 실패 시 발생
     */
    S write(T value, IMessageFieldMetadata metadata, MessageContext messageContext) throws Exception;
}
