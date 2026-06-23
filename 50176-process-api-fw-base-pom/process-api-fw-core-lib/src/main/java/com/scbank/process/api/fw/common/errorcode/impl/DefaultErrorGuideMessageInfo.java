package com.scbank.process.api.fw.common.errorcode.impl;

import com.scbank.process.api.fw.common.errorcode.IErrorGuideMessageInfo;

import lombok.Builder;
import lombok.Data;

/**
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
@Data
@Builder
public class DefaultErrorGuideMessageInfo implements IErrorGuideMessageInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 가이드 메시지
     */
    private String message;

    /**
     * 노출 순서
     */
    private int order;
}
