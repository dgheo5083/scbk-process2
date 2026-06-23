package com.scbank.process.api.fw.message.enums;

import com.scbank.process.api.fw.core.utils.StringUtils;

/**
 * 문자열 또는 숫자 필드의 정렬 방식을 나타내는 열거형입니다.
 *
 * <p>
 * 일반적으로 고정 길이 메시지 처리 시, 값이 필드에 맞춰 왼쪽 또는 오른쪽으로 정렬되어야 하는 경우에 사용됩니다.
 * </p>
 *
 * <ul>
 * <li>{@code LEFT} - 왼쪽 정렬 (빈 공간은 우측에 패딩 처리)</li>
 * <li>{@code RIGHT} - 오른쪽 정렬 (빈 공간은 좌측에 패딩 처리)</li>
 * <li>{@code NONE} - 정렬 없음 (기본값 또는 알 수 없는 값 처리)</li>
 * </ul>
 * 
 * @author sungdon.choi
 */
public enum AlignType {
    /** 왼쪽 정렬 */
    LEFT,
    /** 오른쪽 정렬 */
    RIGHT,
    /** 정렬 없음 */
    NONE;

    /**
     * 문자열 입력 값을 기반으로 정렬 타입을 반환합니다.
     * <p>
     * 입력 문자열이 "left"이면 {@link AlignType#LEFT}, "right"이면 {@link AlignType#RIGHT}를
     * 반환하며,
     * 그 외의 값은 {@link AlignType#NONE}으로 처리됩니다.
     * </p>
     *
     * @param typeString 정렬 타입 문자열 (예: "left", "right")
     * @return 해당하는 정렬 타입 {@link AlignType}, 없으면 {@link AlignType#NONE}
     */
    public static AlignType of(String typeString) {
        if (StringUtils.isEmpty(typeString)) {
            return AlignType.NONE;
        }
        if ("left".equalsIgnoreCase(typeString)) {
            return AlignType.LEFT;
        }
        if ("right".equalsIgnoreCase(typeString)) {
            return AlignType.RIGHT;
        }
        return AlignType.NONE;
    }
}
