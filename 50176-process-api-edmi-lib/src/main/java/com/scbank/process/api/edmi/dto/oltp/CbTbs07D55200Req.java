package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs07D55200Req", type = Type.REQUEST, description = "국세이체 본처리 요청부", captureSystem = "OLTP")
public class CbTbs07D55200Req implements IMessageObject {

    @MessageField(id = "UserID", name = "사용자ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TransPassword;

    @MessageField(id = "YI_NBJong", name = "납부종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_NBJong;

    @MessageField(id = "CGAcctNum", name = "출금계좌번호", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String CGAcctNum;

    @MessageField(id = "CgAcctPassword", name = "출금계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "03")
    private String CgAcctPassword;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "전화국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "05")
    private String TeleThree;

    @MessageField(id = "PerBusNo", name = "주민등록번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerBusNo;

    @MessageField(id = "YI_CustReferNum", name = "고객조회번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_CustReferNum;

    @MessageField(id = "YI_PayYearMonth", name = "부과년월", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YI_PayYearMonth;

    @MessageField(id = "YIGOJITYPE", name = "납부구분", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGOJITYPE;

    @MessageField(id = "GRType", name = "발행형태", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GRType;

    @MessageField(id = "YIGITA", name = "기타", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGITA;

    @MessageField(id = "YI_OfficeNum", name = "세입징수관계좌번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_OfficeNum;

    @MessageField(id = "SmAccountTitle", name = "소계정", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SmAccountTitle;

    @MessageField(id = "YI_ElePayNum", name = "전자납부번호", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_ElePayNum;

    @MessageField(id = "YINGDATE", name = "납기일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YINGDATE;

    @MessageField(id = "YI_PayLimit", name = "납부기한", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YI_PayLimit;

    @MessageField(id = "YI_PayAmt", name = "납부금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YI_PayAmt;

    @MessageField(id = "AmtConfNum", name = "금액검증번호", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AmtConfNum;

    @MessageField(id = "SjYN", name = "금액수정허용유무", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SjYN;

    @MessageField(id = "YunDeYN", name = "연대납부대상유무", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YunDeYN;

    @MessageField(id = "NBType", name = "납부형대구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String NBType;

    @MessageField(id = "GIROType", name = "장표종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GIROType;

    @MessageField(id = "YI_AdminArea", name = "이용기관 발행기관 분류코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YI_AdminArea;

    @MessageField(id = "YI_UseLocalGiroNum", name = "이용기관 지로번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YI_UseLocalGiroNum;

    @MessageField(id = "YI_TaxTypeCode", name = "세목 코드", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YI_TaxTypeCode;

    @MessageField(id = "E2ERegNum1", name = "납부자번호", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String E2ERegNum1;

    @MessageField(id = "YI_GLYear", name = "회계 년도", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YI_GLYear;

    @MessageField(id = "YI_BaseTax", name = "본세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YI_BaseTax;

    @MessageField(id = "YI_EduTax", name = "교육세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YI_EduTax;

    @MessageField(id = "YI_FireTax", name = "농특세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YI_FireTax;

    @MessageField(id = "YI_DelayAmt", name = "가산금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YI_DelayAmt;

    @MessageField(id = "YINBNAME", name = "납부자명", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YINBNAME;

    @MessageField(id = "YINBCHKJB", name = "납부자확인정보", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YINBCHKJB;

    @MessageField(id = "CorpName", name = "수납기관명", length = 36, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CorpName;

    @MessageField(id = "YONBIDNO", name = "납부순번", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YONBIDNO;

    @MessageField(id = "YITRGB", name = "이체화면구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITRGB;

    @MessageField(id = "Dummy", name = "Dummy", length = 38, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "칩번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardIssueDate;

    @MessageField(id = "TeleOneChip", name = "TeleOneChip", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOneChip;

    @MessageField(id = "TeleTwoChip", name = "TeleTwoChip", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwoChip;

    @MessageField(id = "TeleThreeChip", name = "TeleThreeChip", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleThreeChip;

    @MessageField(id = "YIJIL", name = "주문일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJIL;

    @MessageField(id = "YIJUMUN", name = "주문번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMUN;

    @MessageField(id = "Dummy1", name = "Dummy1", length = 33, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy1;

    @MessageField(id = "YIIPN", name = "IP주소", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC주소", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

    @MessageField(id = "Dummy2", name = "Dummy2", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy2;
}