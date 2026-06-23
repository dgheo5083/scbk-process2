package com.scbank.process.api.svc.shared.components.ipinside.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "IpinsideHddInfo", type = Type.RESPONSE, description = "IPInside HDD 정보")
public class DFinderApiInfo implements IMessageObject {

    /**
     * 결과코드
     */
    private String resultCode;

    /**
     * 조치방법코드
     */
    private String rspActnMethCd;

    /**
     * 조치상태코드
     */
    private String rspActnStatCd;

}
