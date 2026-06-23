package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@IntegrationMessage(id = "CbTbs03H20100Req", type = Type.REQUEST, captureSystem = "OLTP", description = "보안매체 검증처리 요청부")
public class CbTbs03H20100Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransPassword;

    @MessageField(id = "SafeCardKind", name = "보안카드종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SafeCardKind;

    @MessageField(id = "SafeCardState", name = "안전카드 상태", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SafeCardState;

    @MessageField(id = "SafeCardIssueNum", name = "안전카드 발급번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SafeCardIssueNum;

    @MessageField(id = "SafeCardBranchNum", name = "안전카드 점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SafeCardBranchNum;

    @MessageField(id = "SafeCardINDEX", name = "안전카드index", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SafeCardINDEX;

    @MessageField(id = "SafeCardNum", name = "안전카드 번호", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SafeCardNum;

    @MessageField(id = "TelNo1", name = "전화번호1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo1;

    @MessageField(id = "TelNo2", name = "전화번호2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo2;

    @MessageField(id = "TelNo3", name = "전화번호3", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo3;

    @MessageField(id = "TransPasswordconfirm", name = "이체비밀번호확인", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransPasswordconfirm;

    @MessageField(id = "SafeCardconfirm", name = "보안카드번호확인", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SafeCardconfirm;

    @MessageField(id = "SafeCardINDEX2", name = "안전카드index", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SafeCardINDEX2;

    @MessageField(id = "SafeCardNum2", name = "안전카드 번호2", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SafeCardNum2;

    @MessageField(id = "LogSkip", name = "LogSkip", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String LogSkip;

    @MessageField(id = "LogDummy", name = "LogDummy", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String LogDummy;

    @MessageField(id = "FidoSkip", name = "FidoSkip", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FidoSkip;

    @MessageField(id = "YIPINCH", name = "PIN NO CHECK", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPINCH;

    @MessageField(id = "YIPINPASS", name = "간편로그인핀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPINPASS;

    @MessageField(id = "YIKEYBOARDYN", name = "키보드뱅킹여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKEYBOARDYN;

    @MessageField(id = "YIMBOTP", name = "모바일OTP사용여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMBOTP;

    @MessageField(id = "Dummy", name = "더미", length = 305, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "칩번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardIssueDate;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "전화국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleThree;

    @MessageField(id = "YIJIL", name = "U정보화마을 주문일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJIL;

    @MessageField(id = "YIJUMUN", name = "U정보화마을 주문번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMUN;

    @MessageField(id = "Dummy1", name = "더미1", length = 33, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy1;

    @MessageField(id = "YIIPN", name = "IP정보", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC정보", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

    @MessageField(id = "Dummy2", name = "더미2", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy2;

}