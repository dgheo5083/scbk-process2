package com.scbank.process.api.svc.common.service.securities.dto.blocking;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PRC 서비스 응답 정보 클래스
 * 해외IP차단서비스 화면
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IntegrationMessage(id = "SecBlkSetBlockingForeignIpResponse", description = "해외IP차단서비스 화면 응답 DTO", type = Type.RESPONSE)
public class SecBlkSetBlockingForeignIpResponse implements IMessageObject {

    @MessageField(id = "ipBlockYn", name = "ip차단여부")
    private String ipBlockYn;

    @MessageField(id = "ipProtectYn", name = "해외IP차단여부")
    private String ipProtectYn;

    @MessageField(id = "custName", name = "고객명")
    private String custName;

    @MessageField(id = "connectType", name = "접속방법")
    private String connectType;

    @MessageField(id = "connectedKoreaYn", name = "한국접속여부")
    private String connectedKoreaYn;

    @MessageField(id = "userType", name = "고객 타입(메뉴접근권한)")
    private String userType;

    @MessageField(id = "userWebType", name = "IB_WEB(조회가능 상태)")
    private String userWebType;

}
