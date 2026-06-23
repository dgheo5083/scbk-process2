package com.scbank.process.api.fw.message.converter;

import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;

/**
 * 프레임워크 전문(Field) 변환기 등록 및 관리 인터페이스.
 *
 * <p>
 * 각 전문 포맷별 필드 변환기를 {@link MessageType} 기준으로 등록하고,
 * 요청 시 타입에 맞는 {@link IMessageFieldConverter}를 제공하는 역할을 한다.
 * </p>
 *
 * 주요 기능:
 * <ul>
 * <li>MessageType 기반 필드 변환기 조회</li>
 * <li>등록된 변환기를 일관성 있게 관리</li>
 * </ul>
 *
 * 확장 시나리오:
 * <ul>
 * <li>고정 길이(Fixed-Length) 메시지용 변환기</li>
 * <li>JSON 포맷용 변환기</li>
 * <li>XML 포맷용 변환기</li>
 * <li>커스텀 포맷 전용 변환기 등록</li>
 * </ul>
 *
 * @see IMessageFieldConverter
 * @see MessageType
 * @see IMessageMetadata
 *
 * @author sungdon.choi
 */
public interface IMessageFieldConverterRegistry {

    /**
     * 주어진 {@link MessageType}에 해당하는 필드 변환기를 조회한다.
     *
     * @param type 변환 대상 전문 필드 타입
     * @return 등록된 {@link IMessageFieldConverter} 인스턴스
     * @throws IllegalArgumentException 등록된 변환기가 없거나 조회 실패 시
     */
    IMessageFieldConverter<?, ?> get(MessageType type);
}
