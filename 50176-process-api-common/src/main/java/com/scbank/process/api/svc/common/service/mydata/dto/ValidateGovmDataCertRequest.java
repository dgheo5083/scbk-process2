package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 공공데이터 전자서명 검증
 */
@Data
@IntegrationMessage(id = "ValidateGovmDataCertRequest", type = Type.REQUEST)
public class ValidateGovmDataCertRequest implements IMessageObject {

    @MessageField(id = "commonCertSignYn", name = "")
    private String commonCertSignYn;

    @MessageField(id = "devlopeCertVerifyFlag", name = "")
    private String devlopeCertVerifyFlag;

    @MessageField(id = "checkFinFlag", name = "")
    private String checkFinFlag;

    @MessageField(id = "pkcs7SignedData", name = "")
    private String pkcs7SignedData;

    @MessageField(id = "finTechSignYN", name = "")
    private String finTechSignYN;
    
    @MessageField(id = "vidRandom", name = "")
    private String vidRandom;

}
