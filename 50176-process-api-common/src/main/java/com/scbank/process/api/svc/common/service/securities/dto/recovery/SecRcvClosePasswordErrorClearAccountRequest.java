package com.scbank.process.api.svc.common.service.securities.dto.recovery;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 계좌 비밀번호 오류건수 해제
 */
@Data
@IntegrationMessage(id = "SecRcvClosePasswordErrorClearAccountRequest", type = Type.REQUEST)
public class SecRcvClosePasswordErrorClearAccountRequest implements IMessageObject {

    @MessageField(id = "yiUPGB", name = "업무구분")
    private String yiUPGB;

    @MessageField(id = "yiGJNO", name = "계좌번호")
    private String yiGJNO;

    @MessageField(id = "yiGJPASS", name = "계좌비밀번호")
    private String yiGJPASS;

    @MessageField(id = "member", name = "Client측 key일련번호")
    private String member;

    @MessageField(id = "userCode", name = "사용자 발신 코드")
    private String userCode;

    @MessageField(id = "username", name = "사용자명")
    private String username;

}
