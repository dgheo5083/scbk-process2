package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 계약서류제공 펀드 계약서 상세조회
 */
@Data
@IntegrationMessage(id = "SupDocGetFundDetailResponse", type = Type.RESPONSE)
public class SupDocGetFundDetailResponse implements IMessageObject {

    @MessageField(id = "acctNum", name = "계좌번호")
    private String acctNum;

    @MessageField(id = "yoCNAME", name = "고객명")
    private String yoCNAME;

    @MessageField(id = "yoTOCD", name = "통화코드")
    private String yoTOCD;

    @MessageField(id = "yoSINAK", name = "계좌개설금액")
    private String yoSINAK;

    @MessageField(id = "yoPNAME", name = "상품명")
    private String yoPNAME;

    @MessageField(id = "yoOPEIL", name = "계좌개설일자")
    private String yoOPEIL;

    @MessageField(id = "yoGIGAN", name = "만기일자")
    private String yoGIGAN;

    @MessageField(id = "yoWBIL", name = "yoWBIL")
    private String yoWBIL;

    @MessageField(id = "yoPBOSU", name = "선취보수")
    private String yoPBOSU;

    @MessageField(id = "yoGBOSU", name = "후취보수")
    private String yoGBOSU;

    @MessageField(id = "yoJDSSR", name = "중도해지수수료율")
    private String yoJDSSR;

    @MessageField(id = "yoUNGB", name = "운용보고서 통보")
    private String yoUNGB;

    @MessageField(id = "yoSUGB", name = "계산서 통보")
    private String yoSUGB;

    @MessageField(id = "yoALGB", name = "이벤트 통보")
    private String yoALGB;

    @MessageField(id = "yoCRISK", name = "고객투자등급")
    private String yoCRISK;

    @MessageField(id = "yoPRISK", name = "상품위험등급")
    private String yoPRISK;

    @MessageField(id = "yoJAJUM", name = "yoJAJUM")
    private String yoJAJUM;

    @MessageField(id = "yoJAKM", name = "yoJAKM")
    private String yoJAKM;

    @MessageField(id = "yoJABUN", name = "yoJABUN")
    private String yoJABUN;

    @MessageField(id = "yoTONM", name = "통화명")
    private String yoTONM;

    @MessageField(id = "yoZONG", name = "종별")
    private String yoZONG;

    @MessageField(id = "yoGRGB", name = "거래구분")
    private String yoGRGB;

    @MessageField(id = "yoHANDO", name = "세제한도금액")
    private String yoHANDO;

    @MessageField(id = "yoYSPCD", name = "세제유형")
    private String yoYSPCD;

    @MessageField(id = "yoGRBB", name = "거래방법")
    private String yoGRBB;

    @MessageField(id = "yoTOGB", name = "계약서류통보방법")
    private String yoTOGB;

    @MessageField(id = "yoWJAIL", name = "자동이체일")
    private String yoWJAIL;

    @MessageField(id = "yoWJAAMT", name = "자동이체금액")
    private String yoWJAAMT;

    @MessageField(id = "yoWSTIL", name = "자동이체시작일")
    private String yoWSTIL;

    @MessageField(id = "yoWLTIL", name = "자동이체종료일")
    private String yoWLTIL;

    @MessageField(id = "lmsPerBusNo", name = "lmsPerBusNo")
    private String lmsPerBusNo;

    @MessageField(id = "yoBD2DC", name = "yoBD2DC")
    private String yoBD2DC;

    @MessageField(id = "jacctNum", name = "jacctNum")
    private String jacctNum;

    @MessageField(id = "chKCRISK", name = "chKCRISK")
    private String chKCRISK;

}