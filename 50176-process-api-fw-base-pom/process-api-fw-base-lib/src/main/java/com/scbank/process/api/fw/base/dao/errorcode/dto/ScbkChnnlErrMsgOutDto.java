package com.scbank.process.api.fw.base.dao.errorcode.dto;

import lombok.Data;

/**
 * 채널 에러메시지 조회 결과 DTO
 */
@Data
public class ScbkChnnlErrMsgOutDto {

    private String chnnlMkCd;
    private String ntnlCd;
    private String inIdent;
    private String errrMode;
    private String errMsg;
    private String errMsg1;
    private String errMsg2;
    private String errMsg3;
    private String errMsg4;
    private String errMsg5;
    private String errMsg6;
    private String errMsg7;
    private String useFlg;
    private String errInfoSlctFlg;
}
