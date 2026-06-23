package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "GetJoinInfoResponse", type = Type.RESPONSE, description = "마이데이터 > 현대카드 가입여부조회 API")
public class GetJoinInfoResponse implements IMessageObject {

    @MessageField(id = "entrncFlg", name = "가입여부")
    private String entrncFlg;

    @MessageField(id = "hycardFlg", name = "SC 제휴 현대카드 보유여부")
    private String hycardFlg;

    @MessageField(id = "customerFlg", name = "버전체크")
    private String customerFlg;

    @MessageField(id = "rspCode", name = "응답코드")
    private String rspCode;

    @MessageField(id = "rspMsg", name = "응답메시지")
    private String rspMsg;

    @MessageField(id = "serviceAgrmntMkCd", name = "서비스동의구분코드")
    private String serviceAgrmntMkCd;

    @MessageField(id = "srvcUsePrvsn", name = "서비스이용약관동의여부")
    private String srvcUsePrvsn;

    @MessageField(id = "rqrdAgrmrDoc", name = "필수동의여부")
    private String rqrdAgrmrDoc;

    @MessageField(id = "slctAgrmrDoc", name = "선택동의여부")
    private String slctAgrmrDoc;

    @MessageField(id = "smsMk", name = "")
    private String smsMk;

    @MessageField(id = "slctAgrmrDocRoute", name = "")
    private String slctAgrmrDocRoute;

}
