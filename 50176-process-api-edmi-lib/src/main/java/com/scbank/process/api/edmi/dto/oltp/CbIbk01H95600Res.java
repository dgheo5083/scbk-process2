package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IntegrationMessage(id = "CbIbk01H95600Res", type = Type.RESPONSE, description = "계좌별명관리 응답 전문")
public class CbIbk01H95600Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "Success", name = "완료여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Success;

    @MessageField(id = "NickName", name = "계좌별명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String NickName;

    @MessageField(id = "InBrnchNum", name = "계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String InBrnchNum;
}
