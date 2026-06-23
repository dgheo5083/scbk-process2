package com.scbank.process.api.svc.common.service.support.dto.customercenter;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * PROCESS 서비스 응답 정보 클래스
 * 마케팅 동의/변경 페이지
 */
@Data
@IntegrationMessage(id = "SupCscSetMarketingTermsPageResponse", type = Type.RESPONSE)
public class SupCscSetMarketingTermsPageResponse implements IMessageObject {

    @MessageField(id = "yoUSID", name = "이용자번호")
    private String yoUSID;

    @MessageField(id = "yoGRGJIL", name = "거래기준일자")
    private String yoGRGJIL;

    @MessageField(id = "yoGRJN", name = "거래점번호")
    private String yoGRJN;

    @MessageField(id = "yoPHONE01", name = "자택전화(Y/N)")
    private String yoPHONE01;

    @MessageField(id = "yoPHONE02", name = "직장전화(Y/N)")
    private String yoPHONE02;

    @MessageField(id = "yoPHONE03", name = "기타전화")
    private String yoPHONE03;

    @MessageField(id = "yoPHONE04", name = "휴대전화(Y/N)")
    private String yoPHONE04;

    @MessageField(id = "yoPHONE05", name = "SMS(Y/N)")
    private String yoPHONE05;

    @MessageField(id = "yoPHONE06", name = "제휴신용카드(Y/N)")
    private String yoPHONE06;

    @MessageField(id = "yoPHONE07", name = "당행제휴(Y/N)")
    private String yoPHONE07;

    @MessageField(id = "yoPHONE08", name = "Y/N")
    private String yoPHONE08;

    @MessageField(id = "yoPHONE09", name = "Y/N")
    private String yoPHONE09;

    @MessageField(id = "yoPHONE10", name = "Y/N")
    private String yoPHONE10;

    @MessageField(id = "yoMAIL01", name = "자택우편")
    private String yoMAIL01;

    @MessageField(id = "yoMAIL02", name = "직장전화")
    private String yoMAIL02;

    @MessageField(id = "yoMAIL03", name = "기타우편")
    private String yoMAIL03;

    @MessageField(id = "yoMAIL04", name = "이메일")
    private String yoMAIL04;

    @MessageField(id = "yoMAIL05", name = "Y/N")
    private String yoMAIL05;

    @MessageField(id = "yoMAIL06", name = "Y/N")
    private String yoMAIL06;

    @MessageField(id = "yoMAIL07", name = "Y/N")
    private String yoMAIL07;

    @MessageField(id = "yoMAIL08", name = "Y/N")
    private String yoMAIL08;

    @MessageField(id = "yoMAIL09", name = "Y/N")
    private String yoMAIL09;

    @MessageField(id = "yoMAIL10", name = "Y/N")
    private String yoMAIL10;

    @MessageField(id = "yoMUSE3", name = "개인정보수집이용동의 Y/N/SPACE")
    private String yoMUSE3;

    @MessageField(id = "yoMUSEM3", name = "광고성정보수신동의 Y/N/SPACE")
    private String yoMUSEM3;

    @MessageField(id = "yoLUSE22S", name = "개인(민감)정보분리 Y/N/SPACE")
    private String yoLUSE22S;

    @MessageField(id = "yoDUMMY", name = "더미")
    private String yoDUMMY;
    // -- CbIbk01H754 res

    @MessageField(id = "evtData", name = "evtData")
    private String evtData;

    @MessageField(id = "tranGb", name = "tranGb")
    private String tranGb;

    @MessageField(id = "evtId", name = "evtId")
    private String evtId;

    @MessageField(id = "callPageType", name = "callPageType")
    private String callPageType;

    @MessageField(id = "eventNo", name = "eventNo")
    private String eventNo;

    @MessageField(id = "isRas", name = "isRas")
    private String isRas;

}
