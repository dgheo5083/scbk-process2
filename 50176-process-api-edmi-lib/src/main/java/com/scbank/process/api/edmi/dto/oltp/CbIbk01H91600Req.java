package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbk01H91600Req", type = Type.REQUEST, captureSystem = "OLTP", description = "웹 회원가입 요청부")
public class CbIbk01H91600Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "E2ERegNum", name = "주민등록번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String E2ERegNum;

    @MessageField(id = "PerCode", name = "주민사업자구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerCode;

    @MessageField(id = "TranGubun", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TranGubun;

    @MessageField(id = "CustName", name = "성명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "CustGubun", name = "거래고객 여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustGubun;

    @MessageField(id = "SUserID", name = "찾은 이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SUserID;

    @MessageField(id = "Email", name = "이메일", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Email;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "AcctPasswd", name = "계좌비밀번호", length = 4, encoding = "cp500", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctPasswd;

    @MessageField(id = "ZipCode", name = "우편번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ZipCode;

    @MessageField(id = "Address", name = "주소", length = 62, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Address;

    @MessageField(id = "Tel1", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Tel1;

    @MessageField(id = "Tel2", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Tel2;

    @MessageField(id = "Tel3", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Tel3;

    @MessageField(id = "HTel1", name = "핸드폰번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HTel1;

    @MessageField(id = "HTel2", name = "핸드폰번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HTel2;

    @MessageField(id = "HTel3", name = "핸드폰번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HTel3;

    @MessageField(id = "CardNum", name = "카드번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CardNum;

    @MessageField(id = "E2EPwdNum", name = "카드비밀번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String E2EPwdNum;

    @MessageField(id = "E2EPwdCvvNum", name = "카드Cvv비밀번호", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String E2EPwdCvvNum;

    @MessageField(id = "ExpriyNum1", name = "유효기간1", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ExpriyNum1;

    @MessageField(id = "ExpriyNum2", name = "유효기간2", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ExpriyNum2;

}
