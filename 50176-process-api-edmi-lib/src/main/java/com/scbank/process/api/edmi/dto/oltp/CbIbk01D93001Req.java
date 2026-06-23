package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01D93001Req", type = Type.REQUEST, captureSystem = "OLTP", description = "사고신고등록 요청부")
public class CbIbk01D93001Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "AcctPassword", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String AcctPassword; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

    @MessageField(id = "ETELnum1", name = "연락처 지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ETELnum1;

    @MessageField(id = "ETELnum2", name = "연락처 국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ETELnum2;

    @MessageField(id = "ETELnum3", name = "연락처 전화번호", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ETELnum3;

    @MessageField(id = "StateGubun", name = "사고신고구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String StateGubun;

    @MessageField(id = "PerBusNo", name = "주민 사업자 번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerBusNo;

}