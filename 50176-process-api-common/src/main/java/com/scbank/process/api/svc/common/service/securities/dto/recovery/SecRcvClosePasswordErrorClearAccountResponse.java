package com.scbank.process.api.svc.common.service.securities.dto.recovery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 계좌 비밀번호 오류건수 해제
 */
@Data
@IntegrationMessage(id = "SecRcvClosePasswordErrorClearAccountResponse", type = Type.RESPONSE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecRcvClosePasswordErrorClearAccountResponse implements IMessageObject {

    @MessageField(id = "userID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String userID;

}
