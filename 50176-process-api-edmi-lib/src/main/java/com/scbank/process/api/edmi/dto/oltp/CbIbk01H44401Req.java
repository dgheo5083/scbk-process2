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
@IntegrationMessage(id = "CbIbk01H44401Req", type = Type.REQUEST, captureSystem = "OLTP", description = "펀드계산서 내역조회")
public class CbIbk01H44401Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TranGubun", name = "거래구분 (0간략조회,1상세)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TranGubun;

    @MessageField(id = "CloseAcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String CloseAcctNum;

    @MessageField(id = "ReferGubun", name = "조회구분(1원가,3지급,4해지,7배당)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReferGubun;

    @MessageField(id = "Index", name = "상세조회 회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Index;

    @MessageField(id = "TranDate", name = "상세조회 거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TranDate;

    @MessageField(id = "ReferStartDate", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferStartDate;

    @MessageField(id = "ReferEndDate", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferEndDate;

}