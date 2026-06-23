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
@IntegrationMessage(id = "CbIbk01H11300Req", type = Type.REQUEST, captureSystem = "OLTP", description = "펀드 입출금명세조회 요청부")
public class CbIbk01H11300Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "AcctPassword", name = "계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "03")
    private String AcctPassword;

    @MessageField(id = "ReferStartDay", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferStartDay;

    @MessageField(id = "DealTime", name = "처리일", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DealTime;

    @MessageField(id = "ReferEndDay", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferEndDay;

    @MessageField(id = "BankbookPart", name = "유무통구분", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String BankbookPart;

    @MessageField(id = "TrxPart", name = "입출금구분", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TrxPart;

    @MessageField(id = "MSList", name = "명세서-한페이지당 출력할 레코드 수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MSList;

    @MessageField(id = "PrintPart", name = "출력구분", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String PrintPart;

    @MessageField(id = "K_Date", name = "연속정보일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_Date;

    @MessageField(id = "K_TrxNum", name = "연속정보처리통번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_TrxNum;

    @MessageField(id = "K_TrxBranch", name = "연속정보취급점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_TrxBranch;

    @MessageField(id = "K_AddCode", name = "연속정보증가코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_AddCode;

    @MessageField(id = "K_Time", name = "거래시간", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_Time;

    @MessageField(id = "PerNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerNo;

    @MessageField(id = "PwdSkip", name = "비밀번호체크유무", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PwdSkip;

    @MessageField(id = "Dummy", name = "dummy", length = 376, align = AlignType.LEFT, padding = StringUtils.SPACE)
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

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleThree;

}
