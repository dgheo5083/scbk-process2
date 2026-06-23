package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 계약서류제공 여신 계약서 상세조회
 */
@Data
@IntegrationMessage(id = "SupDocGetLoanDetailRequest", type = Type.REQUEST)
public class SupDocGetLoanDetailRequest implements IMessageObject {

    @MessageField(id = "yoJYIYL", name = "yoJYIYL")
    private String yoJYIYL;

    @MessageField(id = "yoRVKYN", name = "yoRVKYN")
    private String yoRVKYN;

    @MessageField(id = "yoSHGBN", name = "yoSHGBN")
    private String yoSHGBN;

    @MessageField(id = "yoINSU", name = "yoINSU")
    private String yoINSU;

    @MessageField(id = "yoBJSYU", name = "yoBJSYU")
    private String yoBJSYU;

    @MessageField(id = "yoYDYN", name = "yoYDYN")
    private String yoYDYN;

    @MessageField(id = "yoSPCOD", name = "yoSPCOD")
    private String yoSPCOD;

    @MessageField(id = "yoUDIY02", name = "yoUDIY02")
    private String yoUDIY02;

    @MessageField(id = "yoSJAMT", name = "yoSJAMT")
    private String yoSJAMT;

    @MessageField(id = "yoUDIY01", name = "yoUDIY01")
    private String yoUDIY01;

    @MessageField(id = "yoILBAN", name = "yoILBAN")
    private String yoILBAN;

    @MessageField(id = "yoSNAMT", name = "yoSNAMT")
    private String yoSNAMT;

    @MessageField(id = "yoUDIY04", name = "yoUDIY04")
    private String yoUDIY04;

    @MessageField(id = "yoSUIY2", name = "yoSUIY2")
    private String yoSUIY2;

    @MessageField(id = "yoUDIY03", name = "yoUDIY03")
    private String yoUDIY03;

    @MessageField(id = "yoSUIY3", name = "yoSUIY3")
    private String yoSUIY3;

    @MessageField(id = "yoUDIY06", name = "yoUDIY06")
    private String yoUDIY06;

    @MessageField(id = "yoUDIY05", name = "yoUDIY05")
    private String yoUDIY05;

    @MessageField(id = "yoSUIY1", name = "yoSUIY1")
    private String yoSUIY1;

    @MessageField(id = "yoUDIY08", name = "yoUDIY08")
    private String yoUDIY08;

    @MessageField(id = "yoUDIY07", name = "yoUDIY07")
    private String yoUDIY07;

    @MessageField(id = "yoUDIY09", name = "yoUDIY09")
    private String yoUDIY09;

    @MessageField(id = "yoRPWOL", name = "yoRPWOL")
    private String yoRPWOL;

    @MessageField(id = "yoSUGIJ", name = "yoSUGIJ")
    private String yoSUGIJ;

    @MessageField(id = "yoSUAMT", name = "yoSUAMT")
    private String yoSUAMT;

    @MessageField(id = "yoJUMIN", name = "yoJUMIN")
    private String yoJUMIN;

    @MessageField(id = "yoPAYJR", name = "yoPAYJR")
    private String yoPAYJR;

    @MessageField(id = "yoLASGI", name = "yoLASGI")
    private String yoLASGI;

    @MessageField(id = "yoGASAN", name = "yoGASAN")
    private String yoGASAN;

    @MessageField(id = "yoICHRI", name = "yoICHRI")
    private String yoICHRI;

    @MessageField(id = "yoISUCD", name = "yoISUCD")
    private String yoISUCD;

    @MessageField(id = "yoUDIY11", name = "yoUDIY11")
    private String yoUDIY11;

    @MessageField(id = "yoGYECD", name = "yoGYECD")
    private String yoGYECD;

    @MessageField(id = "yoUDIY10", name = "yoUDIY10")
    private String yoUDIY10;

    @MessageField(id = "yoICHIY", name = "yoICHIY")
    private String yoICHIY;

    @MessageField(id = "yoBUGUM", name = "yoBUGUM")
    private String yoBUGUM;

    @MessageField(id = "yoUDIY13", name = "yoUDIY13")
    private String yoUDIY13;

    @MessageField(id = "yoUDIY12", name = "yoUDIY12")
    private String yoUDIY12;

    @MessageField(id = "yoJAGUM", name = "yoJAGUM")
    private String yoJAGUM;

    @MessageField(id = "yoUDIY15", name = "yoUDIY15")
    private String yoUDIY15;

    @MessageField(id = "yoUDIY14", name = "yoUDIY14")
    private String yoUDIY14;

    @MessageField(id = "yoUDIY16", name = "yoUDIY16")
    private String yoUDIY16;

    @MessageField(id = "yoISUGK", name = "yoISUGK")
    private String yoISUGK;

    @MessageField(id = "yoGUBUN", name = "yoGUBUN")
    private String yoGUBUN;

    @MessageField(id = "yoMJEYN", name = "yoMJEYN")
    private String yoMJEYN;

    @MessageField(id = "yoBKIYL", name = "yoBKIYL")
    private String yoBKIYL;

    @MessageField(id = "yoGEOHT", name = "yoGEOHT")
    private String yoGEOHT;

    @MessageField(id = "yoHYUIL", name = "yoHYUIL")
    private String yoHYUIL;

    @MessageField(id = "yoSDJNM", name = "yoSDJNM")
    private String yoSDJNM;

    @MessageField(id = "yoIYGBN", name = "yoIYGBN")
    private String yoIYGBN;

    @MessageField(id = "yoHDUDY3", name = "yoHDUDY3")
    private String yoHDUDY3;

    @MessageField(id = "yoHDUDY2", name = "yoHDUDY2")
    private String yoHDUDY2;

    @MessageField(id = "yoHDUDY1", name = "yoHDUDY1")
    private String yoHDUDY1;

    @MessageField(id = "yoSPGB", name = "yoSPGB")
    private String yoSPGB;

    @MessageField(id = "yoCENIL", name = "yoCENIL")
    private String yoCENIL;

    @MessageField(id = "yoSHCD", name = "yoSHCD")
    private String yoSHCD;

    @MessageField(id = "yoSHGG", name = "yoSHGG")
    private String yoSHGG;

    @MessageField(id = "yoICBUN", name = "yoICBUN")
    private String yoICBUN;

    @MessageField(id = "yoGYENM", name = "yoGYENM")
    private String yoGYENM;

    @MessageField(id = "yoDAEGB", name = "yoDAEGB")
    private String yoDAEGB;

    @MessageField(id = "yoBHIYL", name = "yoBHIYL")
    private String yoBHIYL;

    @MessageField(id = "yoJNDAE", name = "yoJNDAE")
    private String yoJNDAE;

    @MessageField(id = "yoISUYI", name = "yoISUYI")
    private String yoISUYI;

    @MessageField(id = "yoCHAIL", name = "yoCHAIL")
    private String yoCHAIL;

    @MessageField(id = "yoGEOCI", name = "yoGEOCI")
    private String yoGEOCI;

    @MessageField(id = "yoSUIL1", name = "yoSUIL1")
    private String yoSUIL1;

    @MessageField(id = "yoSUIL2", name = "yoSUIL2")
    private String yoSUIL2;

    @MessageField(id = "yoSSUDY", name = "yoSSUDY")
    private String yoSSUDY;

    @MessageField(id = "yoGJIYL", name = "yoGJIYL")
    private String yoGJIYL;

    @MessageField(id = "yoIYFIX", name = "yoIYFIX")
    private String yoIYFIX;

    @MessageField(id = "yoTOAMT", name = "yoTOAMT")
    private String yoTOAMT;

    @MessageField(id = "yoSUIL3", name = "yoSUIL3")
    private String yoSUIL3;

    @MessageField(id = "yoYUNIY", name = "yoYUNIY")
    private String yoYUNIY;

    @MessageField(id = "yoFSTIL", name = "yoFSTIL")
    private String yoFSTIL;

    @MessageField(id = "yoIYDIS", name = "yoIYDIS")
    private String yoIYDIS;

    @MessageField(id = "yoSMSGB", name = "yoSMSGB")
    private String yoSMSGB;

    @MessageField(id = "yoDCJAN", name = "yoDCJAN")
    private String yoDCJAN;

    @MessageField(id = "yoNAME", name = "yoNAME")
    private String yoNAME;

    @MessageField(id = "yoBPYN", name = "yoBPYN")
    private String yoBPYN;

    @MessageField(id = "acctNum", name = "계좌번호")
    private String acctNum;

    @MessageField(id = "loanAcctKmCD", name = "계정과목코드")
    private String loanAcctKmCD;

    @MessageField(id = "loanKind", name = "대출종류")
    private String loanKind;

    @MessageField(id = "assort", name = "종별코드")
    private String assort;

    @MessageField(id = "kwamok", name = "과목코드")
    private String kwamok;

    @MessageField(id = "acctName", name = "상품명")
    private String acctName;

    @MessageField(id = "lmsPerBusNo", name = "lsmPerBusNo")
    private String lmsPerBusNo;

    @MessageField(id = "yoSUCNT", name = "yoSUCNT")
    private String yoSUCNT;

    @MessageField(id = "yoSUTBL", name = "계약서류리스트")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "SupDocGetLoanDetailRequest/yoSUCNT")
    private List<Collateral> yoSUTBL;

    @Data
    public static class Collateral implements IMessageObject {

        @MessageField(id = "yoSUGEJ", name = "계좌번호")
        private String yoSUGEJ;

        @MessageField(id = "yoSUNAM", name = "담보예금계좌번호")
        private String yoSUNAM;

    }
}
