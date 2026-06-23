package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs07D93d00Req", type = Type.REQUEST, captureSystem = "OLTP", description = "직불현금카드사고신고 요청부")
public class CbTbs07D93d00Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, masking = true, maskingType = "03", padding = StringUtils.SPACE)
    private String TransPassword; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

    @MessageField(id = "YI_GUBN", name = "거래구분(1:발신 2:수신)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_GUBN;

    @MessageField(id = "YI_BKCD", name = "분실등록은행코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YI_BKCD;

    @MessageField(id = "YI_TEL1", name = "전화번호1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_TEL1;

    @MessageField(id = "YI_TEL2", name = "전화번호2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_TEL2;

    @MessageField(id = "YI_TEL3", name = "전화번호3", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_TEL3;

    @MessageField(id = "YI_BSGB", name = "일괄분실구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_BSGB;
}