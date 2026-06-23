package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H93101Req", type = Type.REQUEST, captureSystem = "OLTP", description = "직불현금카드사고신고 요청부")
public class CbIbk01D93101Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "PerBusNo", name = "주민 사업자 번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerBusNo;

    @MessageField(id = "TeleOne", name = "연락처 지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "연락처 국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "연락처 전화번호", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleThree;

    @MessageField(id = "YoguRecordsu", name = "요구명세수", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YoguRecordsu;

    @MessageField(id = "TranGubun", name = "거래구분코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TranGubun;

    @MessageField(id = "Giung", name = "카드기능", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Giung;

    @MessageField(id = "AcctNum2", name = "계좌번호", length = 13, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum2;

    @MessageField(id = "CaNo", name = "카드번호", length = 19, masking = true, maskingType = "08", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CaNo;

    @MessageField(id = "AcctNum1", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum1;

    @MessageField(id = "AcctPassword1", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String AcctPassword1; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

    @MessageField(id = "CardGubun", name = "1(MS) 2(IC)구분", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardGubun;

}