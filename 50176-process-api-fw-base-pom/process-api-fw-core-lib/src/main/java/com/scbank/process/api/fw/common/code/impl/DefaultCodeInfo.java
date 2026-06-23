package com.scbank.process.api.fw.common.code.impl;

import java.util.List;

import com.scbank.process.api.fw.common.code.ICodeInfo;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 프레임워크 공통코드
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 16.
 */
@Data
@EqualsAndHashCode
@Builder
public class DefaultCodeInfo implements ICodeInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 공통코드 그룹 키
     */
    private String key;

    /**
     * 공통코드 로케일 문자열
     */
    private String locale;

    /**
     * 공통코드 하위 공통코드 아이템 목록
     */
    private List<ICodeItemInfo> codeItemList;
}
