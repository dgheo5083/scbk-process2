package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs07H01501Req", type = Type.REQUEST, captureSystem = "OLTP", description = "신용카드분실사고/모바일카드_IN(개인/예비처리) 요청부")
public class CbTbs07H01501Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "JHGubun", name = "조회구분(1신용카드 2모바일칩)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JHGubun;

    @MessageField(id = "RCount", name = "요구(출력)명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "PerBusNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerBusNo;

    @MessageField(id = "YIJHGB", name = "화면조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJHGB;

    @MessageField(id = "Dummy", name = "dummy", length = 434, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "주민번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "칩번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardIssueDate;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleThree;

}