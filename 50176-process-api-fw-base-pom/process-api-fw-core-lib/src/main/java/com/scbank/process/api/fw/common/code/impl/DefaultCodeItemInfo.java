package com.scbank.process.api.fw.common.code.impl;

import com.scbank.process.api.fw.common.code.ICodeItemInfo;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 16.
 */
@Data
@EqualsAndHashCode
@Builder
public class DefaultCodeItemInfo implements ICodeItemInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 공통코드 아이템 키
     */
    private String key;

    /**
     * 공통코드 아이템 값
     */
    private String value;

    /**
     * 공통코드 아이템 sort order
     */
    private int order;
}
