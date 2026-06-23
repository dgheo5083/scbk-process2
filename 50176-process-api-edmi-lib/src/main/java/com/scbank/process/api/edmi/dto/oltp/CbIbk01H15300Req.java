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
@IntegrationMessage(id = "CbIbk01H15300Req", type = Type.REQUEST, captureSystem = "OLTP", description = "대출거래내역조회 요청부")
public class CbIbk01H15300Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String AcctNum;

    @MessageField(id = "ReferStartDate", name = "조회시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer ReferStartDate;

    @MessageField(id = "ReferEndDate", name = "조회종료일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer ReferEndDate;

    @MessageField(id = "MSS", name = "출력명세건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer MSS;

    @MessageField(id = "TotalPageCnt", name = "총페이지수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer TotalPageCnt;

    @MessageField(id = "NowPageCnt", name = "현재페이지", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer NowPageCnt;

}