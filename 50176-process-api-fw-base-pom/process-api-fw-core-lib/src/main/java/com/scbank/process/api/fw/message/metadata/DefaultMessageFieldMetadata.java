package com.scbank.process.api.fw.message.metadata;

import java.util.List;

import org.springframework.lang.Nullable;

import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * 전문 필드 메타데이터의 기본 구현체입니다.
 * 
 * <p>
 * {@link DefaultMessageMetadata}를 확장하여,
 * 필드 길이, 정렬, 인코딩, 마스킹, 반복부 정보 등
 * 전문 필드별 세부 속성을 제공합니다.
 * </p>
 * 
 * <p>
 * {@link IMessageFieldMetadata} 인터페이스를 구현합니다.
 * </p>
 * 
 * @see IMessageFieldMetadata
 * @see AlignType
 * @see RepeatType
 * @since 25. 4. 22.
 * @version 1.0
 *          작성자: sungdon.choi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class DefaultMessageFieldMetadata extends DefaultMessageMetadata implements IMessageFieldMetadata {

    private static final long serialVersionUID = 1L;

    /** 필드 전체 길이 (byte 단위) */
    private int length;

    /** 필드 소수점 자리수 */
    private int scale;

    /** 필드 값 패딩 문자 */
    private String padding;

    /** 필드 값 정렬 방식 */
    private AlignType align;

    /** 필드 인코딩 타입 (예: "UTF-8") */
    @Nullable
    private String encoding;

    /** 필드 기본값 */
    @Nullable
    private String defaultValue;

    /** 필드 로그 마스킹 여부 */
    private boolean masking;

    /**
     * 필드 로그 마스킹 타입
     */
    private String maskingType;

    /** 필드 필수 여부 */
    private boolean required;

    /** 필드 전각/반각 처리 여부 */
    private boolean multiBytes;

    /** EBCDIC SO/SI 처리 여부 */
    private boolean sosi;

    /** 필드 암호화 알고리즘 타입 */
    @Nullable
    private String encryptType;

    /** 필드 타입 핸들러 식별자 */
    @Nullable
    private String typeHandler;

    /** 하위 필드 메타데이터 목록 (중첩 객체, 반복 필드 등) */
    private List<IMessageMetadata> children;

}
