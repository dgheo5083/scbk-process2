package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "MciIdentifyDacomReq", type = Type.REQUEST, description = "데이콤 핸드폰 번호 인증 요청부")
public class MciIdentifyDacomReq implements IMessageObject {

    @MessageField(id = "DEPTCODE", name = "부서코드", length = 12)
    private String DEPTCODE;

    @MessageField(id = "OFFICECODE", name = "행번", length = 12)
    private String OFFICECODE;

    @MessageField(id = "RESPCODE", name = "LG유플러스 결과코드", length = 4)
    private String RESPCODE;

    @MessageField(id = "RESPMSG", name = "LG유플러스 결과메시지", length = 160)
    private String RESPMSG;

    @MessageField(id = "MOBILESTEP", name = "인증진행단계구분", length = 5)
    private String MOBILESTEP;

    @MessageField(id = "TID", name = "LG유플러스 거래번호", length = 24)
    private String TID;

    @MessageField(id = "MOBILECOM", name = "이동통신사 구분", length = 1)
    private String MOBILECOM;

    @MessageField(id = "MOBILENUM", name = "휴대폰 번호", length = 12)
    private String MOBILENUM;

    @MessageField(id = "E2ERegNum", name = "가입자 주민번호", length = 13)
    private String E2ERegNum;

    @MessageField(id = "AUTHMODE", name = "인증번호 SMS발송여부", length = 10)
    private String AUTHMODE;

    @MessageField(id = "AUTHNUMBER", name = "인증번호", length = 6)
    private String AUTHNUMBER;

    @MessageField(id = "CALLBACK", name = "CALLBACK 번호", length = 12)
    private String CALLBACK;

    @MessageField(id = "MERTNAME", name = "가맹점명", length = 20)
    private String MERTNAME;

    @MessageField(id = "NAMECHECKYN", name = "실명확인유무", length = 1)
    private String NAMECHECKYN;

    @MessageField(id = "BUYER", name = "성명(가입자명)", length = 15)
    private String BUYER;

    @MessageField(id = "BOHOSVCYN", name = "보호서비스 이용유무", length = 1)
    private String BOHOSVCYN;

    @MessageField(id = "ISSUERCODE", name = "이통사 응답코드", length = 4)
    private String ISSUERCODE;

    @MessageField(id = "ISSUERMSG", name = "이통사 응답메시지", length = 160)
    private String ISSUERMSG;

    @MessageField(id = "FLDdelimiter1", name = "1", length = 1, defaultValue = "31")
    private String FLDdelimiter1;

    @MessageField(id = "SEGdelimiter1", name = "2", length = 1, defaultValue = "30")
    private String SEGdelimiter1;

    @MessageField(id = "ENDdelimiter1", name = "3", length = 1, defaultValue = "255")
    private String ENDdelimiter1;

    @MessageField(id = "ENDdelimiter2", name = "4", length = 1, defaultValue = "255")
    private String ENDdelimiter2;
}
