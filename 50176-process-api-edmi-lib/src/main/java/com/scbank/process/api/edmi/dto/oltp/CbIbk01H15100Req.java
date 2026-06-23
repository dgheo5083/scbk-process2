package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H15100Req", type = Type.REQUEST, captureSystem = "OLTP", description = "대출현황(전체)조회 요청부")
public class CbIbk01H15100Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "InBrnchNum", name = "입력점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer InBrnchNum;

    @MessageField(id = "MSS", name = "명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer MSS;

    @MessageField(id = "YSCode", name = "연속구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YSCode;

    @MessageField(id = "JumNum", name = "점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer JumNum;

    @MessageField(id = "CustNum", name = "고객번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer CustNum;

    @MessageField(id = "Gamok", name = "과목", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Gamok;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "")
    private Integer AcctNum;

    @MessageField(id = "GubunCode", name = "구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GubunCode;

    @MessageField(id = "YIGUGU", name = "조회구분", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGUGU;

}