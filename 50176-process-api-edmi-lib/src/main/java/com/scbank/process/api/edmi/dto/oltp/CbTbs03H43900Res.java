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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbTbs03H43900Res", type = Type.RESPONSE, description = "은행연합회조회 응답부", captureSystem = "OLTP")
public class CbTbs03H43900Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "InquiryGubun", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InquiryGubun;

    @MessageField(id = "PerNo", name = "주민번호", length = 13, masking = true, maskingType = "01", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String PerNo;

    @MessageField(id = "TaxDuplicateYN", name = "세금우대중복여부", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TaxDuplicateYN;

    @MessageField(id = "TaxTotalLimit", name = "세금우대총한도", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TaxTotalLimit;

    @MessageField(id = "UseLimit", name = "사용한도", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal UseLimit;

    @MessageField(id = "NUseLimit", name = "미사용한도", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal NUseLimit;

    @MessageField(id = "NextDivision", name = "연속구분", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String NextDivision;

    @MessageField(id = "NextCountNo", name = "연속전문번호", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String NextCountNo;

    @MessageField(id = "RCount", name = "출력수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "TaxLimitRecord", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbTbs03H43900Res/RCount")
    private List<TaxLimitRecord> TaxLimitRecord;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaxLimitRecord implements IMessageObject {
        @MessageField(id = "KFBGubun", name = "당행구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String KFBGubun;

        @MessageField(id = "AcctNum", name = "계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AcctNum;

        @MessageField(id = "JoinAmt", name = "가입금액", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal JoinAmt;

        @MessageField(id = "OpenDate", name = "신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String OpenDate;

        @MessageField(id = "MartuDate", name = "만기일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String MartuDate;

        @MessageField(id = "CloseDate", name = "해지일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String CloseDate;

        @MessageField(id = "DRBrnchName", name = "등록점포명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DRBrnchName;

        @MessageField(id = "ItemCode", name = "상품코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String ItemCode;

        @MessageField(id = "ItemName", name = "상품명", length = 18, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String ItemName;

        @MessageField(id = "CloseCode", name = "해지코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CloseCode;

        @MessageField(id = "CloseGubunName", name = "해지구분명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CloseGubunName;

    }

}
