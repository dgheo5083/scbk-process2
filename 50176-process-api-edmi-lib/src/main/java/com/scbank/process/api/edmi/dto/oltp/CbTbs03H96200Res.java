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
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbTbs03H96200Res", type = Type.RESPONSE, description = "입금계좌유효성검증 응답부")
public class CbTbs03H96200Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "IgBankCode", name = "은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgBankCode;

    @MessageField(id = "IgAcctNum", name = "계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgAcctNum;

    @MessageField(id = "InAccTowner", name = "수취인명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InAccTowner;

}
