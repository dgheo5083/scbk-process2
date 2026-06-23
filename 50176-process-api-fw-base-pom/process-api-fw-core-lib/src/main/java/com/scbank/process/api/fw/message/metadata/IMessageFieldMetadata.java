package com.scbank.process.api.fw.message.metadata;

import java.util.ArrayList;
import java.util.List;

import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

/**
 * 메시지 필드에 대한 메타데이터를 정의하는 인터페이스입니다.
 * 
 * <p>
 * 필드 길이, 정렬, 패딩 문자, 인코딩, 마스킹, 반복부 정보 등
 * 직렬화/역직렬화에 필요한 상세 정보를 제공합니다.
 * </p>
 * 
 * <p>
 * 기본 구현에서는 일부 메서드에 기본값을 제공합니다.
 * </p>
 * 
 * @see IMessageMetadata
 * @see AlignType
 * @see RepeatType
 * @since 25. 4. 14.
 * @version 1.0
 * 
 *          작성자: sungdon.choi
 */
public interface IMessageFieldMetadata extends IMessageMetadata {

    /**
     * 필드의 전체 길이를 반환합니다.
     *
     * @return 필드 길이 (byte 단위)
     */
    int getLength();

    /**
     * 필드의 소수점 자리수를 반환합니다.
     *
     * @return 소수점 자리수
     */
    int getScale();

    /**
     * 필드의 패딩 문자를 반환합니다.
     * (예: 공백(0x20), 0 채우기(0x30))
     *
     * @return 패딩 문자 (기본값: 공백)
     */
    default String getPadding() {
        return " ";
    }

    /**
     * 필드의 정렬 타입을 반환합니다.
     *
     * @return 정렬 타입 (왼쪽 정렬, 오른쪽 정렬)
     */
    AlignType getAlign();

    /**
     * 필드 개별 인코딩 설정을 반환합니다.
     *
     * @return 인코딩 설정 문자열 (예: "UTF-8")
     */
    String getEncoding();

    /**
     * 필드 기본값을 반환합니다.
     * 
     * <p>
     * 처리 중 값이 없을 경우 이 기본값을 적용합니다.
     * </p>
     *
     * @return 기본값 문자열
     */
    String getDefaultValue();

    /**
     * 필드 로그 출력 시 마스킹 여부를 반환합니다.
     *
     * @return 마스킹 처리 여부
     */
    boolean isMasking();

    /**
     * 필드 마스킹 타입을 반환한다.
     * 
     * @return 필드 마스킹 타입 문자열
     */
    String getMaskingType();

    /**
     * 필드의 전각/반각 변환 처리 여부를 반환합니다.
     *
     * @return 전각 변환 처리 여부
     */
    boolean isMultiBytes();

    /**
     * SO/SI 제어문자 처리 여부를 반환합니다.
     *
     * @return SO/SI 처리 여부
     */
    boolean isSosi();

    /**
     * 필드 암호화 타입을 반환합니다.
     *
     * @return 암호화 타입 문자열
     */
    String getEncryptType();

    /**
     * 필드 타입 핸들러를 반환합니다.
     *
     * @return 타입 핸들러 클래스명 또는 식별자
     */
    String getTypeHandler();

    /**
     * 반복부의 반복 개수를 반환합니다.
     * 
     * <p>
     * 기본값은 빈 문자열("")입니다.
     * </p>
     *
     * @return 반복부 개수
     */
    default String getRepeatCount() {
        return "";
    }

    /**
     * 반복부의 반복 타입을 반환합니다.
     *
     * @return 반복 타입 ({@link RepeatType})
     */
    default RepeatType getRepeatType() {
        return RepeatType.NONE;
    }

    /**
     * 반복부의 제네릭 타입(Class)을 반환합니다.
     *
     * @return 반복부 제네릭 타입, 없으면 null
     */
    default Class<?> getRepeatedGenericType() {
        return null;
    }

    /**
     * 래퍼(wrapper) 이름을 반환합니다.
     * (XML/JSON 등에서 반복부를 감싸는 필드명)
     *
     * @return 래퍼 필드명
     */
    default String getWrapperName() {
        return "";
    }

    /**
     * 반복 요소(element) 이름을 반환합니다.
     *
     * @return 반복 요소명
     */
    default String getElementName() {
        return "";
    }

    /**
     * 현재 필드 하위에 등록된 메타데이터 목록을 반환합니다.
     * (중첩 객체나 반복 필드 등)
     *
     * @return 하위 메타데이터 목록
     */
    default List<IMessageMetadata> getChildren() {
        return new ArrayList<>();
    }

    /**
     * 테스트 데이터를 가져온다.
     * 
     * @return
     */
    default String getExample() {
        return "";
    }
}
