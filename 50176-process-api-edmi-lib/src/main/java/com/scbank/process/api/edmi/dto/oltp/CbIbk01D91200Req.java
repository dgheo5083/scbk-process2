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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@IntegrationMessage(id = "CbIbk01D91200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "조회회원가입 요청부")
public class CbIbk01D91200Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "CustName", name = "성명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "PerNo", name = "주민등록번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerNo;

    @MessageField(id = "AcctCardNum", name = "기본계좌", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctCardNum;

    @MessageField(id = "AcctCardPassword", name = "계좌비밀번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String AcctCardPassword;

    @MessageField(id = "Tele", name = "전화번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Tele;

    @MessageField(id = "ConfTSPassword", name = "확인용통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String ConfTSPassword;

    @MessageField(id = "CardNum", name = "신용카드번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CardNum;

    @MessageField(id = "E2EPwdNum", name = "신용카드비밀번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String E2EPwdNum;

    @MessageField(id = "ExpiryNum1", name = "유효기간월", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ExpiryNum1;

    @MessageField(id = "ExpiryNum2", name = "유효기간년", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ExpiryNum2;

}
