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
@IntegrationMessage(id = "CbTbs07D42100Req", type = Type.REQUEST, captureSystem = "OLTP", description = "자동이체 신청 요청부")
public class CbTbs07D42100Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TransPassword;

    @MessageField(id = "ScPart", name = "신청구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ScPart;

    @MessageField(id = "CgAcctNum", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CgAcctNum;

    @MessageField(id = "CgAcctPassword", name = "출금계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String CgAcctPassword;

    @MessageField(id = "IgBankCode", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgBankCode;

    @MessageField(id = "IgAcctNum", name = "입금은행계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgAcctNum;

    @MessageField(id = "TransIAmt", name = "이체금액(입력)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TransIAmt;

    @MessageField(id = "TransDate", name = "이체일", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransDate;

    @MessageField(id = "TransSMon", name = "이체시작월", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransSMon;

    @MessageField(id = "TransEMon", name = "이체종료월", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransEMon;

    // @MessageField(id = "HgSNameSO1", name = "송금인명한글시작1", length = 1, align =
    // AlignType.LEFT, padding = StringUtils.SPACE)
    // private String HgSNameSO1;

    @MessageField(id = "HgSName", name = "송금인명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, sosi = true)
    private String HgSName;

    // @MessageField(id = "HgSNameSI1", name = "송금인명한글끝1", length = 1, align =
    // AlignType.LEFT, padding = StringUtils.SPACE)
    // private String HgSNameSI1;

    // @MessageField(id = "HgRNameSO2", name = "수취인명한글시작2", length = 1, align =
    // AlignType.LEFT, padding = StringUtils.SPACE)
    // private String HgRNameSO2;

    @MessageField(id = "HgRName", name = "수취인명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, sosi = true)
    private String HgRName;

    // @MessageField(id = "HgRNameSI2", name = "수취인명한글끝2", length = 1, align =
    // AlignType.LEFT, padding = StringUtils.SPACE)
    // private String HgRNameSI2;

    // @MessageField(id = "YIJUKYOSO3", name = "출금통장표시내역한글시작3", length = 1, align =
    // AlignType.LEFT, padding = StringUtils.SPACE)
    // private String YIJUKYOSO3;

    @MessageField(id = "YIJUKYO", name = "출금통장표시내역", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, sosi = true)
    private String YIJUKYO;

    // @MessageField(id = "YIJUKYOSI3", name = "출금통장표시내역한글끝3", length = 1, align =
    // AlignType.LEFT, padding = StringUtils.SPACE)
    // private String YIJUKYOSI3;

    // @MessageField(id = "YIJUKIPSO4", name = "입금통장표시내역한글시작4", length = 1, align =
    // AlignType.LEFT, padding = StringUtils.SPACE)
    // private String YIJUKIPSO4;

    @MessageField(id = "YIJUKIP", name = "입금통장표시내역", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, sosi = true)
    private String YIJUKIP;

    // @MessageField(id = "YIJUKIPSI4", name = "입금통장표시내역한글끝4", length = 1, align =
    // AlignType.LEFT, padding = StringUtils.SPACE)
    // private String YIJUKIPSI4;

}
