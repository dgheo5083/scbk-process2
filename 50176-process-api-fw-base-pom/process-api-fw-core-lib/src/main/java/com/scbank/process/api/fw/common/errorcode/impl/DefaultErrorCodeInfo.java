package com.scbank.process.api.fw.common.errorcode.impl;

import java.util.List;

import com.scbank.process.api.fw.common.errorcode.IErrorButtonActionInfo;
import com.scbank.process.api.fw.common.errorcode.IErrorCodeInfo;
import com.scbank.process.api.fw.common.errorcode.IErrorGuideMessageInfo;

import lombok.Builder;
import lombok.Data;

/**
 * 프레임워크 에러코드 정보 구현 클래스
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
@Data
@Builder
public class DefaultErrorCodeInfo implements IErrorCodeInfo {

    private static final long serialVersionUID = 1L;

    private String code;

    private String langCode;

    private String message;

    private List<IErrorGuideMessageInfo> errorGuideMessageInfos;

    private String displayType;

    private IErrorButtonActionInfo errorButtonActionInfo;
}
