package com.scbank.process.api.edmi.dto.oltp;

import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01D93101Res", type = Type.RESPONSE, description = "직불현금카드사고신고 응답부")
public class CbIbk01D93101Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TranResult", name = "처리결과", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TranResult;

    @MessageField(id = "TranGubun", name = "거래구분코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TranGubun;

    @MessageField(id = "JuminNum", name = "주민 사업자 번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JuminNum;

    @MessageField(id = "CaNo", name = "카드번호", length = 19, masking = true, maskingType = "08", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CaNo;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 13, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "SaGoN", name = "사고등록수", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private Integer SaGoN;

    @MessageField(id = "Kyyumu", name = "겸용카드유무", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Kyyumu;

    @MessageField(id = "Bcyumu", name = "비씨신고유무", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Bcyumu;

    @MessageField(id = "TeleOne", name = "연락처 지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "연락처 국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "연락처 전화번호", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleThree;

    @MessageField(id = "RCount", name = "거래내역명세수", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private Integer RCount;

    @MessageField(id = "YOOTPER", name = "CD OTP", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOOTPER;

    @MessageField(id = "TranRecord", name = "반복부1")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01D93101Res/RCount")
    private List<TranRecord> TranRecord;

    @Data
    public static class TranRecord implements IMessageObject {
        @MessageField(id = "CardJongryu", name = "카드종류", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CardJongryu;

        @MessageField(id = "CardGiung", name = "카드기능종류", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CardGiung;

        @MessageField(id = "CardNum", name = "카드번호", length = 16, masking = true, maskingType = "08", align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CardNum;

        @MessageField(id = "CardAcctNum", name = "카드계좌번호", length = 13, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CardAcctNum;

        @MessageField(id = "RecCardGubun", name = "구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RecCardGubun;

        @MessageField(id = "CardSangtae", name = "카드상태", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CardSangtae;

    }
}