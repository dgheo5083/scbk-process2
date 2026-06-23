package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03H93300Res", type = Type.RESPONSE, description = "OTP안전카드사고신고 응답부")
public class CbTbs03H93300Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "CustName", name = "고객명", length = 22, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "SafeCardKind", name = "보안카드종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SafeCardKind;

    @MessageField(id = "FSCardNum", name = "안전카드번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FSCardNum;

    @MessageField(id = "DateTime", name = "사고신고일시", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DateTime;

    @MessageField(id = "TelNo1", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo1;

    @MessageField(id = "TelNo2", name = "국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo2;

    @MessageField(id = "TelNo3", name = "전화번호", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo3;

    @MessageField(id = "VenderErrCode", name = "기관장애여부(0:정상1:인증센터장애(금보연),2:발급기관장애)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String VenderErrCode;

    @MessageField(id = "VenderCode", name = "발급기관", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String VenderCode;

    @MessageField(id = "VenderName", name = "발급기관명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String VenderName;

    @MessageField(id = "YOCARDOTPCHECK", name = "신용카드OTP 체크 필드", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCARDOTPCHECK;

}