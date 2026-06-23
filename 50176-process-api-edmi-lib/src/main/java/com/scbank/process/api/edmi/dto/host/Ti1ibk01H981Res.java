package com.scbank.process.api.edmi.dto.host;

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
@IntegrationMessage(id = "Ti1ibk01H981Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "세금계산서 여부 및 메일발송 응답 전문")
public class Ti1ibk01H981Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "RegNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
    private String RegNo;

    @MessageField(id = "DeptPersonName", name = "DeptPersonName", length = 32, masking = true, maskingType = "04")
    private String DeptPersonName;

    @MessageField(id = "SSRAcctNum", name = "수수료출금계좌", length = 11)
    private String SSRAcctNum;

    @MessageField(id = "SSRFee", name = "수수료금액", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SSRFee;

    @MessageField(id = "SSRVat", name = "부가가치세", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SSRVat;

    @MessageField(id = "SSRDate", name = "Date", length = 8)
    private String SSRDate;

    @MessageField(id = "SSRTime", name = "Time", length = 6)
    private String SSRTime;

    @MessageField(id = "SSRResult", name = "Result", length = 1)
    private String SSRResult;

    @MessageField(id = "SSRSecq", name = "Seq", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SSRSecq;

}
