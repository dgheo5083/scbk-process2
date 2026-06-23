package com.scbank.process.api.fw.message.metadata;

import java.io.Serializable;

/**
 * 프레임워크 서비스 I/O 필드 메타데이터를 정의하는 인터페이스입니다.
 * 
 * <p>
 * 각 전문 필드의 ID, 이름, 타입, 필수 여부, 경로, 데이터 타입 등의 메타정보를 제공합니다.
 * 또한 필드 정렬 순서를 위한 {@link Comparable} 기능을 지원합니다.
 * </p>
 * 
 * @see MessageType
 * @since 2022. 1. 14.
 * @version 1.0
 *          작성자: sungdon.choi
 */
public interface IMessageMetadata extends Serializable, Comparable<IMessageMetadata> {

    /**
     * 전문 필드의 ID를 반환합니다.
     *
     * @return 전문 필드 ID
     */
    String getId();

    /**
     * 전문 필드의 한글명을 반환합니다.
     *
     * @return 전문 필드 한글명
     */
    String getName();

    /**
     * 전문 필드의 정렬 순서를 반환합니다.
     *
     * @return 정렬 순서 (기본값: 0)
     */
    default int getOrder() {
        return 0;
    }

    /**
     * 전문 필드의 타입을 반환합니다.
     *
     * @return 전문 필드 타입 ({@link MessageType})
     */
    MessageType getType();

    /**
     * 전문 필드의 필수 여부를 반환합니다.
     *
     * @return 필수 여부 (true = 필수, false = 선택)
     */
    boolean isRequired();

    /**
     * 전문 필드의 전체 경로를 반환합니다.
     * 
     * <p>
     * 필드 계층 구조에 따라 경로를 표현할 수 있습니다.
     * </p>
     *
     * @return 전문 필드 경로
     */
    String getPath();

    /**
     * 전문 필드의 자바 타입을 반환합니다.
     *
     * @return 필드 타입 클래스
     */
    Class<?> getFieldType();

    /**
     * 전문 필드의 자바 필드명을 반환합니다.
     * 
     * @return
     */
    String getFieldName();

    /**
     * 전문 필드의 정렬 순서 기준으로 비교합니다.
     *
     * @param o 비교 대상 메타데이터
     * @return 정렬 기준 (this.order vs o.order)
     */
    @Override
    default int compareTo(IMessageMetadata o) {
        return Integer.compare(this.getOrder(), o.getOrder());
    }

    /**
     * I/O 필드 메타데이터 타입을 나타내는 열거형입니다.
     * 
     * <p>
     * 필드 타입으로 문자열, 숫자, 참조 객체, 반복 리스트 등 다양한 타입을 지원합니다.
     * </p>
     *
     * @author sungdon.choi
     * @version 1.0
     * @since 2022. 1. 14.
     */
    public static enum MessageType {
        BOOLEAN, VARIABLE_LENGTH, BYTE, CHAR, SHORT, INTEGER, LONG, DOUBLE, FLOAT, BIGDECIMAL, STRING, REFERENCE, OBJECT,
        REPEATED,
        NONE,
        MULTIPART_FILE;

        /**
         * 문자열로부터 {@link MessageType}을 파싱하여 반환합니다.
         *
         * @param typeString 필드 타입을 나타내는 문자열
         * @return 대응하는 {@link MessageType}, 매칭 실패 시 {@code NONE}
         */
        public static MessageType parse(String typeString) {
            if ("reference".equalsIgnoreCase(typeString)) {
                return MessageType.REFERENCE;
            } else if ("object".equalsIgnoreCase(typeString)) {
                return MessageType.OBJECT;
            } else if ("repeated".equalsIgnoreCase(typeString)) {
                return MessageType.REPEATED;
            } else if ("string".equalsIgnoreCase(typeString)) {
                return MessageType.STRING;
            } else if ("short".equalsIgnoreCase(typeString)) {
                return MessageType.SHORT;
            } else if ("integer".equalsIgnoreCase(typeString)) {
                return MessageType.INTEGER;
            } else if ("long".equalsIgnoreCase(typeString)) {
                return MessageType.LONG;
            } else if ("double".equalsIgnoreCase(typeString)) {
                return MessageType.DOUBLE;
            } else if ("float".equalsIgnoreCase(typeString)) {
                return MessageType.FLOAT;
            } else if ("bigdecimal".equalsIgnoreCase(typeString)) {
                return MessageType.BIGDECIMAL;
            } else if ("variable_length".equalsIgnoreCase(typeString)) {
                return MessageType.VARIABLE_LENGTH;
            } else if ("multipart_file".equalsIgnoreCase(typeString)) {
                return MessageType.MULTIPART_FILE;
            } else if ("byte".equalsIgnoreCase(typeString)) {
                return MessageType.BYTE;
            } else if ("char".equalsIgnoreCase(typeString)) {
                return MessageType.CHAR;
            }
            return MessageType.NONE;
        }
    }
}
