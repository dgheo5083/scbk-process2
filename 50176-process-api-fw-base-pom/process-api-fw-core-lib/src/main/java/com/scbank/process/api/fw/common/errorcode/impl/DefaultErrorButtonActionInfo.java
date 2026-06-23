package com.scbank.process.api.fw.common.errorcode.impl;

import com.scbank.process.api.fw.common.errorcode.IErrorButtonActionInfo;

import lombok.Builder;
import lombok.Data;

/**
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
@Data
@Builder
public class DefaultErrorButtonActionInfo implements IErrorButtonActionInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 버튼 액션 타입
     */
    private String type;

    /**
     * 버튼 라벨
     */
    private String label;

    /**
     * 버튼 클릭 시 이동 할 타겟
     */
    private String target;
}
