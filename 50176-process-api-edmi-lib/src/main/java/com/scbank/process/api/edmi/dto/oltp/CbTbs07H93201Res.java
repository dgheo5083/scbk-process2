package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs07H93201Res", type = Type.RESPONSE, description = "신용카드사고신고등록 응답부")
public class CbTbs07H93201Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "HYNum", name = "카드번호", length = 16, masking = true, maskingType = "08", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HYNum;

    @MessageField(id = "ReportGubun", name = "사고신고구분", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReportGubun;

    @MessageField(id = "CustName", name = "고객명", length = 22, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "DateTime", name = "사고신고일시", length = 14, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DateTime;

    @MessageField(id = "TelNo1", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo1;

    @MessageField(id = "TelNo2", name = "국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo2;

    @MessageField(id = "TelNo3", name = "전화번호", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo3;

    @MessageField(id = "EndState", name = "처리상태", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String EndState;

}