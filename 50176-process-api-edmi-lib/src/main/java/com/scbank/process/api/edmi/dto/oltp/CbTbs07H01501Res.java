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
@IntegrationMessage(id = "CbTbs07H01501Res", type = Type.RESPONSE, description = "신용카드분실사고/모바일카드_IN(개인/예비처리) 응답부")
public class CbTbs07H01501Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "HYSName", name = "회원고객명", length = 22, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HYSName;

    @MessageField(id = "GJAcctNum", name = "신용카드결제계좌", length = 16, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GJAcctNum;

    @MessageField(id = "GJDate", name = "신용카드결제일자", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GJDate;

    @MessageField(id = "CardInputYN", name = "카드입력여부", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardInputYN;

    @MessageField(id = "RCount", name = "요구(출력)명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "CardInfoData", name = "반복문1")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbTbs07H01501Res/RCount")
    private List<CardInfoData> CardInfoData;

    @Data
    public static class CardInfoData implements IMessageObject {
        @MessageField(id = "CardNo", name = "카드번호", length = 16, masking = true, maskingType = "08", align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CardNo;

        @MessageField(id = "CardName", name = "카드명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CardName;

        @MessageField(id = "CardGB", name = "카드구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CardGB;

        @MessageField(id = "CardOwnerGB", name = "카드소유구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CardOwnerGB;

        @MessageField(id = "BCCardYN", name = "신용카드구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BCCardYN;

        @MessageField(id = "MChipType", name = "모바일칩기능", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String MChipType;

        @MessageField(id = "MChipState", name = "모바일칩상태", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String MChipState;

        @MessageField(id = "MChipIssueDate", name = "모바일칩발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String MChipIssueDate;

        @MessageField(id = "YOBSGB", name = "분실해제구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOBSGB;

    }
}