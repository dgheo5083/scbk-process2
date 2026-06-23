package com.scbank.process.api.fw.message.converter.format.common;

import com.scbank.process.api.fw.message.converter.AbstractMessageFieldConverter;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverter;

/**
 * 고정 길이 메시지 포맷(byte[]) 기반의 필드 변환 추상 클래스.
 *
 * <p>
 * 이 클래스는 고정 길이 바이트 배열을 입력으로 받아
 * 다양한 타입({@code T})으로 변환하거나,
 * 반대로 타입({@code T})을 고정 길이 바이트 배열로 변환하는
 * 공통 기반을 제공한다.
 * </p>
 *
 * 주요 특징:
 * <ul>
 * <li>입력 타입은 항상 {@code byte[]}이다.</li>
 * <li>출력 타입은 제네릭 {@code T}로 자유롭게 지정할 수 있다.</li>
 * <li>Fixed-Length 메시지 포맷(Field Align, Padding, Length 체크 등) 처리를 확장할 때
 * 사용한다.</li>
 * </ul>
 *
 * 상속 대상 예시:
 * <ul>
 * <li>{@link StringFieldConverter} - 문자열 변환</li>
 * <li>{@link NumberFieldConverter} - 정수형 변환</li>
 * <li>{@link BigDecimalFieldConverter} - 실수형(BigDecimal) 변환</li>
 * </ul>
 *
 * @param <T> 변환 대상 타입 (예: String, Integer, BigDecimal 등)
 * @see IMessageFieldConverter
 */
public abstract class AbstractByteArrayFieldConverter<T>
                extends AbstractMessageFieldConverter
                implements IMessageFieldConverter<byte[], T> {
}
