package com.scbank.process.api.fw.base.dao.errorcode.dto;

import lombok.Data;

/**
 * 채널 에러메시지 정보 조회 입력 DTO
 */
@Data
public class ScbkChnnlErrMsgInDto {

    /**
     * 채널구분코드
     */
    private String chnnlMkCd;

    /**
     * 에러코드
     */
    String errCd;

    /**
     * 국가코드
     */
    String ntnlCd;

    /**
     * 에러모드
     */
    String errMode;
}
