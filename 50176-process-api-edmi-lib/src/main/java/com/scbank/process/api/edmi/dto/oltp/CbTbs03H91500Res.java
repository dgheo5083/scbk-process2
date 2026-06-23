package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbTbs03H91500Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "인터넷뱅킹해지 응답부")
public class CbTbs03H91500Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "CustName", name = "고객명", length = 22, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "PerNo", name = "주민번호", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerNo;

    @MessageField(id = "BasicAcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BasicAcctNum;

    @MessageField(id = "MSG", name = "MESSAGE", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MSG;

    @MessageField(id = "YODAECH", name = "대출회원구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODAECH;

    @MessageField(id = "CardMemberGubun", name = "신용카드회원구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CardMemberGubun;

    @MessageField(id = "CardNum", name = "신용카드번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CardNum;

}
