package com.scbank.process.api.fw.base.integration.system.mci;

import com.scbank.process.api.fw.base.integration.system.mci.vo.MciReqHeader;
import com.scbank.process.api.fw.integration.request.IntegrationRequest;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.Data;

/**
 * MCI 요청 전문 메시지 Wrapper
 * 
 * @author sungdon.choi
 * @param <B>
 */
@Data
public class MciRequest<B extends IMessageObject> implements IntegrationRequest<MciReqHeader, B> {

    /**
     * MCI 요청 헤더 메시지부
     */
    private MciReqHeader header;

    /**
     * MCI 요청 바디 메시지부
     */
    private B requestMessage;
}
