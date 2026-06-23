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
@IntegrationMessage(id = "Ti1tbs07Cs924Req", type = Type.REQUEST, description = "이체비밀번호 등록/변경 요청 전문")
public class Ti1tbs07Cs924Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "PartCode", name = "구분코드", length = 2)
    private String PartCode;

    @MessageField(id = "PerBusNo", name = "주민/사업자번호", length = 13, masking = true, maskingType = "01")
    private String PerBusNo;

    /**
     * TOBE 암호화타입 어떻게?
     * <ext-ignore-condition> <!-- APP 이중암호화 대응 -->
     * <ext-start>_DNFE2E_</ext-start>
     * <ext-end>_/DNFE2E_</ext-end>
     * </ext-ignore-condition>
     * <ext-ignore-condition> <!-- WEB 이중암호화 대응 -->
     * <ext-start>_DVKEY_</ext-start>
     * <ext-end>_/DVKEY_</ext-end>
     * </ext-ignore-condition>
     */
    @MessageField(id = "HJPassword", name = "현재비밀번호", length = 8)
    private String HJPassword;

    @MessageField(id = "BGPassword", name = "변경비밀번호", length = 8)
    private String BGPassword;

    @MessageField(id = "ConfInNum", name = "확인입력번호", length = 8)
    private String ConfInNum;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02")
    private String AcctNum;

    @MessageField(id = "AcctPassword", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AcctPassword;

    @MessageField(id = "SafeCardKind", name = "보안카드종류", length = 1)
    private String SafeCardKind;

    @MessageField(id = "SecurityIndex", name = "안전카드 INDEX", length = 6)
    private String SecurityIndex;

    @MessageField(id = "SecurityValue", name = "안전카드값", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SecurityValue;

    @MessageField(id = "InforGubun", name = "정보구분", length = 1)
    private String InforGubun;

    @MessageField(id = "SecurityIndex2", name = "안전카드 INDEX 2", length = 6)
    private String SecurityIndex2;

    @MessageField(id = "SecurityValue2", name = "안전카드값 2", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SecurityValue2;

    @MessageField(id = "SafeCardState", name = "안전카드 상태", length = 1)
    private String SafeCardState;

    @MessageField(id = "SafeCardIssueNum", name = "안전카드 발급번호", length = 12)
    private String SafeCardIssueNum;

    @MessageField(id = "SafeCardBranchNum", name = "안전카드 점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SafeCardBranchNum;

    @MessageField(id = "SafeCardINDEX", name = "안전카드index", length = 6)
    private String SafeCardINDEX;

    @MessageField(id = "SafeCardNum", name = "안전카드 번호", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SafeCardNum;

    @MessageField(id = "TelNo1", name = "전화번호1", length = 4)
    private String TelNo1;

    @MessageField(id = "TelNo2", name = "전화번호2", length = 4)
    private String TelNo2;

    @MessageField(id = "TelNo3", name = "전화번호3", length = 4)
    private String TelNo3;

    @MessageField(id = "TransPasswordconfirm", name = "이체비밀번호확인", length = 1)
    private String TransPasswordconfirm;

    @MessageField(id = "SafeCardconfirm", name = "보안카드번호확인", length = 1)
    private String SafeCardconfirm;

    @MessageField(id = "SafeCardINDEX2", name = "안전카드index", length = 6)
    private String SafeCardINDEX2;

    @MessageField(id = "SafeCardNum2", name = "안전카드 번호", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SafeCardNum2;

    @MessageField(id = "Dummy", name = "dummy", length = 372)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "for Mobile", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "칩번호", length = 16)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardIssueDate;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "전화국번", length = 4)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4)
    private String TeleThree;

}
