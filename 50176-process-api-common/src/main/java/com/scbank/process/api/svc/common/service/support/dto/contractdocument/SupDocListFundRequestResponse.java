package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;
// import com.scbank.process.api.svc.common.dao.dto.DocumentResult;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 여신, 펀드 계약서류제공 신청내역 조회
 */
@Data
@IntegrationMessage(id = "SupDocListFundRequestResponse", type = Type.RESPONSE)
public class SupDocListFundRequestResponse implements IMessageObject {

    @MessageField(id = "dummyTemp1", name = "")
    private String dummyTemp1;

    @MessageField(id = "resCode", name = "정상코드")
    private String resCode;

    @MessageField(id = "bodyLength", name = "BODY부LENGTH")
    private int bodyLength;

    @MessageField(id = "aomacroNm", name = "마크로명으로 보임")
    private String aomacroNm;

    @MessageField(id = "rowCount", name = "조립건수")
    private int rowCount;

    @MessageField(id = "docuList", name = "계약서류리스트")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "SupDocListFundRequestResponse/rowCount")
    private List<DOCU> docuList;

    @Data
    public static class DOCU implements IMessageObject {

        @MessageField(id = "elementId", name = "elementId")
        private String elementId;

        @MessageField(id = "tranDt", name = "tranDt")
        private String tranDt;

        @MessageField(id = "loanReqNo", name = "loanReqNo")
        private String loanReqNo;

        @MessageField(id = "acctNo", name = "acctNo")
        private String acctNo;

        @MessageField(id = "custSsn", name = "custSsn")
        private String custSsn;

        @MessageField(id = "docCode", name = "docCode")
        private String docCode;

        @MessageField(id = "docName", name = "docName")
        private String docName;

        @MessageField(id = "brchNo", name = "brchNo")
        private String brchNo;

        @MessageField(id = "empNo", name = "empNo")
        private String empNo;

        @MessageField(id = "chnnlMk", name = "chnnlMk")
        private String chnnlMk;

        @MessageField(id = "jobMk", name = "bjobMkrchNo")
        private String jobMk;

        @MessageField(id = "createDocDt", name = "createDocDt")
        private String createDocDt;
    }

    @MessageField(id = "dummy", name = "더미")
    private String dummy;

    @MessageField(id = "errorCode")
    private String errorCode;

    @MessageField(id = "errorMessage")
    private String errorMessage;

    @MessageField(id = "errorModule")
    private String errorModule;

    @MessageField(id = "perBusNo", name = "주민등록번호")
    private String perBusNo;
}