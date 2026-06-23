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
@IntegrationMessage(id = "Ti1ibk01H981Req", type = Type.REQUEST, captureSystem = "OLTP", description = "세금계산서 여부 및 메일발송 요청 전문")
public class Ti1ibk01H981Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "RegNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
    private String RegNo;

    @MessageField(id = "CAGubun", name = "발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증)", length = 1)
    private String CAGubun;

    @MessageField(id = "CustGubun", name = "발급자구분(1:개인,2:기업)", length = 1)
    private String CustGubun;

    @MessageField(id = "RAGubun", name = "인증서종류(RA)(1:금융용,2:범용,8:사설,9:타기관(RA틀림))", length = 1)
    private String RAGubun;

    @MessageField(id = "SSRGubun", name = "수수료납부구분(1:발급,2:갱신)", length = 1)
    private String SSRGubun;

    @MessageField(id = "SafeCardKind", name = "보안카드종류", length = 1)
    private String SafeCardKind;

    @MessageField(id = "SecurityIndex", name = "안전카드위치", length = 6)
    private String SecurityIndex;

    @MessageField(id = "SecurityValue", name = "안전카드값", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SecurityValue;

    @MessageField(id = "SSRAcctNum", name = "수수료출금계좌", length = 11)
    private String SSRAcctNum;

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
    @MessageField(id = "AcctPasswd", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03")
    private String AcctPasswd;

    @MessageField(id = "SSRFee", name = "수수료금액", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SSRFee;

    @MessageField(id = "SSRVat", name = "부가가치세", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SSRVat;

    @MessageField(id = "TelCode1", name = "전화번호1", length = 4)
    private String TelCode1;

    @MessageField(id = "TelCode2", name = "전화번호2", length = 4)
    private String TelCode2;

    @MessageField(id = "TelCode3", name = "전화번호3", length = 4, masking = true)
    private String TelCode3;

    @MessageField(id = "Dummy", name = "dummy -반영처리이용", length = 32)
    private String Dummy;

    @MessageField(id = "IssueReceipt", name = "세금계산서 발급구분", length = 1)
    private String IssueReceipt;

    @MessageField(id = "TypesOfIndustry", name = "업종", length = 18)
    private String TypesOfIndustry;

    @MessageField(id = "Item", name = "종목", length = 18)
    private String Item;

    @MessageField(id = "IssueDate", name = "인증서유효기간시작일", length = 8)
    private String IssueDate;

    @MessageField(id = "ExpireDate", name = "인증서유효기간종료일", length = 8)
    private String ExpireDate;

    @MessageField(id = "JuminSaupjaNo", name = "주민사업자번호", length = 13, masking = true, maskingType = "01")
    private String JuminSaupjaNo;

    @MessageField(id = "SimplifyGB", name = "간소화구분값", length = 1)
    private String SimplifyGB;

    @MessageField(id = "Dummy0", name = "dummy0", length = 222)
    private String Dummy0;

    @MessageField(id = "InputJumin", name = "주민번호", length = 13, masking = true, maskingType = "01")
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "칩번호", length = 16)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardIssueDate;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "국번", length = 4)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, masking = true)
    private String TeleThree;

    @MessageField(id = "YIJIL", name = "주문일", length = 8)
    private String YIJIL;

    @MessageField(id = "YIJUMUN", name = "주문번호", length = 10)
    private String YIJUMUN;

    @MessageField(id = "Dummy1", name = "dummy1", length = 33)
    private String Dummy1;

    @MessageField(id = "YIIPN", name = "IP정보", length = 40)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC정보", length = 20)
    private String YIMAC;

    @MessageField(id = "Dummy2", name = "dummy2", length = 10)
    private String Dummy2;

}
