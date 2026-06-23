package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs07H93201Req", type = Type.REQUEST, description = "신용카드사고신고등록 요청부")
public class CbTbs07H93201Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "HYNum", name = "카드번호", length = 16, masking = true, maskingType = "08", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HYNum;

    @MessageField(id = "HYNumPassword", name = "카드비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String HYNumPassword; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _DVKEY

    @MessageField(id = "TelNo1", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo1;

    @MessageField(id = "TelNo2", name = "국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo2;

    @MessageField(id = "TelNo3", name = "전화번호", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo3;

    @MessageField(id = "PerBusNo", name = "주민 사업자 번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerBusNo;

    @MessageField(id = "YI_BSGB", name = "일괄분실구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_BSGB;

}