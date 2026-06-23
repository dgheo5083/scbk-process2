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
@IntegrationMessage(id = "CbIbk01H44l00Req", type = Type.REQUEST, captureSystem = "OLTP", description = "MTS_계좌해지거래 신청 입력")
public class CbIbk01H44l00Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YI_GRGB", name = "업무구분", length = 1)
    private String YI_GRGB;

    @MessageField(id = "CgAcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CgAcctNum;

    @MessageField(id = "CgAcctPassword", name = "계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "03")
    private Integer CgAcctPassword;

    @MessageField(id = "YI_DUMMY", name = "여분", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_DUMMY;

}