package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 계약서류제공 신탁 계약서 상세조회
 */
@Data
@IntegrationMessage(id = "SupDocGetTrustAccountDetailResponse", type = Type.RESPONSE)
public class SupDocGetTrustAccountDetailResponse implements IMessageObject {

    @MessageField(id = "acctNum", name = "acctNum")
    private String acctNum;

    @MessageField(id = "yoCNAME", name = "yoCNAME")
    private String yoCNAME;

    @MessageField(id = "yoTOCD", name = "yoTOCD")
    private String yoTOCD;

    @MessageField(id = "yoSINAK", name = "yoSINAK")
    private String yoSINAK;

    @MessageField(id = "yoPNAME", name = "yoPNAME")
    private String yoPNAME;

    @MessageField(id = "yoOPEIL", name = "yoOPEIL")
    private String yoOPEIL;

    @MessageField(id = "yoGIGAN", name = "yoGIGAN")
    private String yoGIGAN;

    @MessageField(id = "yoWBIL", name = "yoWBIL")
    private String yoWBIL;

    @MessageField(id = "yoPBOSU", name = "yoPBOSU")
    private String yoPBOSU;

    @MessageField(id = "yoGBOSU", name = "yoGBOSU")
    private String yoGBOSU;

    @MessageField(id = "yoJDSSR", name = "yoJDSSR")
    private String yoJDSSR;

    @MessageField(id = "yoUNGB", name = "yoUNGB")
    private String yoUNGB;

    @MessageField(id = "yoSUGB", name = "yoSUGB")
    private String yoSUGB;

    @MessageField(id = "yoALGB", name = "yoALGB")
    private String yoALGB;

    @MessageField(id = "yoCRISK", name = "yoCRISK")
    private String yoCRISK;

    @MessageField(id = "yoPRISK", name = "yoPRISK")
    private String yoPRISK;

    @MessageField(id = "yoTONM", name = "yoTONM")
    private String yoTONM;

    @MessageField(id = "lmsPerBusNo", name = "lmsPerBusNo")
    private String lmsPerBusNo;

    @MessageField(id = "jacctNum", name = "jacctNum")
    private String jacctNum;

    @MessageField(id = "chKCRISK", name = "chKCRISK")
    private String chKCRISK;

    @MessageField(id = "yoBD2DC", name = "yoBD2DC")
    private String yoBD2DC;
}