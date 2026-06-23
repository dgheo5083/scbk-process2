package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;
import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H03100Res", type = Type.RESPONSE, description = "전계좌조회 응답 전문")
public class CbIbk01H03100Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "ContinueInfo", name = "연속정보", length = 90)
    private String ContinueInfo;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "DrawAcctNumListRecord", name = "출력계좌건수", length = 3)
    private Integer DrawAcctNumListRecord;

    @MessageField(id = "LoyPoint", name = "포인트", length = 10)
    private String LoyPoint;

    @MessageField(id = "Dummy1", name = "더미", length = 10)
    private String Dummy1;

    @MessageField(id = "REC_01", name = "출력계좌반복부", length = 3)
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H03100Res/DrawAcctNumListRecord")
    private List<DrawAcctNumListRecord> REC_01;

    @Getter
    @Setter
    public static class DrawAcctNumListRecord implements IMessageObject {

        @MessageField(id = "DrawAcctNum", name = "계좌번호", length = 14, masking = true, maskingType = "02")
        private String DrawAcctNum;

        @MessageField(id = "Assort", name = "계좌종별", length = 2)
        private String Assort;

        @MessageField(id = "DepKind", name = "계좌종류(1:입출금, 2:예금, 3:BC카드, H:현대카드)", length = 1)
        private String DepKind;

        @MessageField(id = "BalSign", name = "잔액부호", length = 1)
        private String BalSign;

        @MessageField(id = "Balance", name = "잔액", length = 13)
        private BigDecimal Balance;

        @MessageField(id = "Curcy", name = "통화코드", length = 3, defaultValue = "KRW")
        private String Curcy;

        @MessageField(id = "SavingStartDate", name = "예금신규일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String SavingStartDate;

        @MessageField(id = "SavingEndDate", name = "예금만기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String SavingEndDate;

        @MessageField(id = "DrawAcctNameAlias", name = "계좌별명/제휴카드명", length = 22, multiBytes = true)
        private String DrawAcctNameAlias;
    }
}
