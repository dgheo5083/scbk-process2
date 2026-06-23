package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01D84600Req", type = Type.REQUEST, description = "비대면 및 금융거래 한도제한 계좌 관리", captureSystem = "OLTP")
public class CbIbk01D84600Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIHMCD", name = "항목코드(745:금융거래한도, 749:비대면거래한도)", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIHMCD;

    @MessageField(id = "YIDHGB", name = "등록해제구분(1:등록, 2:해제) 202412해제만가능", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDHGB;

    @MessageField(id = "YIJMNO", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJMNO;

    @MessageField(id = "YIGJNO", name = "계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJNO;

}
