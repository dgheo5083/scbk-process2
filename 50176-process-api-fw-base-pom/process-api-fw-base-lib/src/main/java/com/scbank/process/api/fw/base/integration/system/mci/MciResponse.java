package com.scbank.process.api.fw.base.integration.system.mci;

import com.scbank.process.api.fw.base.integration.system.mci.vo.MciError;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciResHeader;
import com.scbank.process.api.fw.integration.response.IntegrationResponse;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.Data;

/**
 * MCI 응답 메시지
 * 
 * @param <O>
 * @author sungdon.choi
 */
@Data
public class MciResponse<O extends IMessageObject> implements IntegrationResponse<MciResHeader, O, MciError> {

    /**
     * MCI 응답 헤더 메시지부
     */
    private MciResHeader header;

    /**
     * MCI 응답 바디 메시지부
     */
    private O response;

    /**
     * MCI 응답 에러 메시지
     */
    private MciError errorResponse;

    /**
     * MCI 응답 에러여부
     */
    private boolean isError;
}
