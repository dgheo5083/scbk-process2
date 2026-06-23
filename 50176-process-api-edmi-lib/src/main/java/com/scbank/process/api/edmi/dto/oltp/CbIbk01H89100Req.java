package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@IntegrationMessage(id = "CbIbk01H89100Req", type = Type.REQUEST, captureSystem = "OLTP", description = "비밀번호 변경 요청 전문")
public class CbIbk01H89100Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "로그인비밀번호", length = 8, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "CustJumin1", name = "주민번호 앞6자리", length = 6)
    private String CustJumin1;

    @MessageField(id = "CustJumin2", name = "주민번호 뒤7자리", length = 7, masking = true, maskingType = "01")
    private String CustJumin2;

    @MessageField(id = "HPASS", name = "현재비밀번호", length = 8, masking = true, maskingType = "03")
    private String HPASS;

    @MessageField(id = "LPASS", name = "변경비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String LPASS;

    @MessageField(id = "CPASS", name = "확인입력번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String CPASS;

    @MessageField(id = "DrawAcctNum", name = "계좌번호", length = 11)
    private String DrawAcctNum;

    @MessageField(id = "AcctPassword", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03")
    private String AcctPassword;

    @MessageField(id = "ActType", name = "처리구분. 1:등록,2:변경,3:재설정,4:해지,0:최초등록", length = 1)
    private String ActType;

    @MessageField(id = "Yidaech", name = "대출조회고객", length = 1)
    private String Yidaech;

    @MessageField(id = "Yisinyo", name = "신용카드조회고객", length = 1)
    private String Yisinyo;

    @MessageField(id = "CardNum", name = "신용카드번호", length = 16)
    private String CardNum;

    @MessageField(id = "E2EPwdNum", name = "신용카드비밀번호", length = 4, masking = true, maskingType = "03")
    private String E2EPwdNum;

    @MessageField(id = "E2EPwdCvvNum", name = "신용카드CVV2비밀번호", length = 3, masking = true, maskingType = "03")
    private String E2EPwdCvvNum;

    @MessageField(id = "ExpriyNum1", name = "유효기간월", length = 2)
    private String ExpriyNum1;

    @MessageField(id = "ExpriyNum2", name = "유효기간년", length = 2)
    private String ExpriyNum2;

    @MessageField(id = "YiPreChk", name = "사전확인", length = 1)
    private String YiPreChk;

    @MessageField(id = "YiBidaem", name = "비대면본인확인", length = 1)
    private String YiBidaem;

}
