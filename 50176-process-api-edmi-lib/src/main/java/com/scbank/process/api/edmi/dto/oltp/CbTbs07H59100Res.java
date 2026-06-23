package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

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
@IntegrationMessage(id = "CbTbs07H59100Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "국세이체 예비처리및관세조회")
public class CbTbs07H59100Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "YIO_K_GJNum", name = "연속-지정번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int YIO_K_GJNum;

    @MessageField(id = "YIO_K_Spare", name = "연속-예비", length = 47)
    private String YIO_K_Spare;

    @MessageField(id = "YI_CustMgmtNum", name = "고객관리번호 (주민등록번호 등)", length = 13)
    private String YI_CustMgmtNum;

    @MessageField(id = "PayerName", name = "납부자명", length = 42)
    private String PayerName;

    @MessageField(id = "JsName", name = "징수기관명", length = 42)
    private String JsName;

    @MessageField(id = "JsKmCode", name = "징수과목코드", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int JsKmCode;

    @MessageField(id = "JsKmName", name = "징수과목명", length = 42)
    private String JsKmName;

    @MessageField(id = "JsKGeja", name = "징수관계좌번호", length = 6)
    private String JsKGeja;

    @MessageField(id = "SoGeJung", name = "소계정", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int SoGeJung;

    @MessageField(id = "JsActYear", name = "징수결의회계연도", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JsActYear;

    @MessageField(id = "InPayAmt", name = "납기내금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal InPayAmt;

    @MessageField(id = "InPayDate", name = "납기내기한", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String InPayDate;

    @MessageField(id = "OutPayAmt", name = "납기후금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal OutPayAmt;

    @MessageField(id = "OutPayDate", name = "납기후기한", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OutPayDate;

    @MessageField(id = "gojitype", name = "고지서유형", length = 1)
    private String gojitype;

    @MessageField(id = "YI_BaseTax", name = "본세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YI_BaseTax;

    @MessageField(id = "YI_EduTax", name = "교육세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YI_EduTax;

    @MessageField(id = "YI_FireTax", name = "농특세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YI_FireTax;

    @MessageField(id = "YOEXCEL", name = "특소세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOEXCEL;

    @MessageField(id = "YOJUSE", name = "주세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOJUSE;

    @MessageField(id = "YOBUGA", name = "부가세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOBUGA;

    @MessageField(id = "YOCARSE", name = "교통세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOCARSE;

    @MessageField(id = "YOBANGWI", name = "방위세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOBANGWI;

    @MessageField(id = "YOCASAN", name = "가산금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOCASAN;

    @MessageField(id = "YOHGNM", name = "회계명", length = 42)
    private String YOHGNM;

    @MessageField(id = "YOSKNM", name = "소관명", length = 42)
    private String YOSKNM;

    @MessageField(id = "YOJUSO", name = "납부자주소", length = 82)
    private String YOJUSO;

    @MessageField(id = "YOSINGO", name = "수입신고번호", length = 15)
    private String YOSINGO;

    @MessageField(id = "KiilGubun", name = "납기내후구분", length = 1)
    private String KiilGubun;

    @MessageField(id = "YOSJYN", name = "금액수정허용유무", length = 1)
    private String YOSJYN;

    @MessageField(id = "YOYUNDE", name = "연대납부대상유무", length = 1)
    private String YOYUNDE;

    @MessageField(id = "PayDate", name = "납부일시", length = 14, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String PayDate;

    @MessageField(id = "GogiDate", name = "고지일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GogiDate;

    @MessageField(id = "DaeNapYN", name = "대리납부허용유무", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DaeNapYN;

    @MessageField(id = "TaxActionInfo", name = "청구기관수납인", length = 32)
    private String TaxActionInfo;

    @MessageField(id = "YIO_AdminArea", name = "지로분류코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int YIO_AdminArea;

    @MessageField(id = "YIO_UseLocalGiroNum", name = "지로기관지로번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int YIO_UseLocalGiroNum;

    @MessageField(id = "YONBIDNO", name = "납부순번", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int YONBIDNO;

    @MessageField(id = "YONASENO", name = "납세의무자번호", length = 13)
    private String YONASENO;

    @MessageField(id = "YOGNAGMK", name = "기납금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOGNAGMK;

    @MessageField(id = "YOJNHGMK", name = "잔여납부할금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOJNHGMK;

}
