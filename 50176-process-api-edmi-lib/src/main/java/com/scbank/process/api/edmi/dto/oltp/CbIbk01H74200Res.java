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
@IntegrationMessage(id = "CbIbk01H74200Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "증명서>연말정산증명서-주택자금상환증명서")
public class CbIbk01H74200Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "PerBusNo", name = "주민/사업자번호", length = 13, masking = true, maskingType = "01")
    private String PerBusNo;

    @MessageField(id = "HNAME", name = "성명", length = 32, masking = true, maskingType = "03")
    private String HNAME;

    @MessageField(id = "HJUSO", name = "주소", length = 100, masking = true, maskingType = "05")
    private String HJUSO;

    @MessageField(id = "HBNKNM", name = "금융기관명", length = 32)
    private String HBNKNM;

    @MessageField(id = "HJAGUM", name = "상품명", length = 32)
    private String HJAGUM;

    @MessageField(id = "SULIL", name = "가입일", length = 8)
    private String SULIL;

    @MessageField(id = "CgAcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02")
    private String CgAcctNum;

    @MessageField(id = "HBOKSU", name = "외문구", length = 4)
    private String HBOKSU;

    @MessageField(id = "IJNIL", name = "주택취득임차일", length = 8)
    private String IJNIL;

    @MessageField(id = "FSTIL", name = "대출일", length = 8)
    private String FSTIL;

    @MessageField(id = "YGTOT", name = "연간합계액", length = 13)
    private BigDecimal YGTOT;

    @MessageField(id = "DSIJA", name = "소득공제대상액", length = 13)
    private BigDecimal DSIJA;

    @MessageField(id = "GRYY", name = "거래년도", length = 4)
    private String GRYY;

    @MessageField(id = "RCount", name = "출력명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "TR742Rec1", name = "접수번호별")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H74200Res/RCount")
    private List<TR742Rec1> TR742Rec1;

    @Getter
    @Setter
    public static class TR742Rec1 implements IMessageObject {
        @MessageField(id = "GRIL", name = "상환일자", length = 8)
        private String GRIL;

        @MessageField(id = "GRAMT", name = "원금", length = 13)
        private BigDecimal GRAMT;

        @MessageField(id = "IJAMT", name = "이자", length = 13)
        private BigDecimal IJAMT;

        @MessageField(id = "TOTAL", name = "합계", length = 13)
        private BigDecimal TOTAL;

    }
}