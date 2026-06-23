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
@IntegrationMessage(id = "Ti1tbs07Cs924Res", type = Type.RESPONSE, description = "이체비밀번호 등록/변경 응답 전문")
public class Ti1tbs07Cs924Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "ReferPart", name = "조회구분", length = 3)
    private String ReferPart;

    @MessageField(id = "JSDepErrCnt", name = "오류횟수(즉시이체)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JSDepErrCnt;

    @MessageField(id = "YJBKErrCnt", name = "오류횟수(외상매출채권담보대출)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YJBKErrCnt;

    @MessageField(id = "YHDepErrCnt", name = "오류횟수(외화이체)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YHDepErrCnt;

    @MessageField(id = "DepApprNumErrCnt", name = "오류횟수(이체승인번호)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DepApprNumErrCnt;

    @MessageField(id = "TSPasswordRenewReq", name = "비밀번호갱신요구(통신비밀번호)", length = 1)
    private String TSPasswordRenewReq;

    @MessageField(id = "JSDepRenewReq", name = "비밀번호갱신요구(즉시이체)", length = 1)
    private String JSDepRenewReq;

    @MessageField(id = "YJBKRenewReq", name = "비밀번호갱신요구(외상매출채권담보대출)", length = 1)
    private String YJBKRenewReq;

    @MessageField(id = "JHDepRenewReq", name = "비밀번호갱신요구(종합이체)", length = 1)
    private String JHDepRenewReq;

    @MessageField(id = "GYDepRenewReq", name = "비밀번호갱신요구(급여이체)", length = 1)
    private String GYDepRenewReq;

    @MessageField(id = "YHDepRenewReq", name = "비밀번호갱신요구(외화이체)", length = 1)
    private String YHDepRenewReq;

    @MessageField(id = "DepApprNumRenewReq", name = "비밀번호갱신요구(이체승인번호)", length = 1)
    private String DepApprNumRenewReq;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "JSDepPassword", name = "즉시이체비밀번호", length = 8)
    private String JSDepPassword;

    @MessageField(id = "YJBKPassword", name = "외상매출채권담보대출비밀번호", length = 8)
    private String YJBKPassword;

    @MessageField(id = "JHDepPassword", name = "종합이체비밀번호", length = 8)
    private String JHDepPassword;

    @MessageField(id = "GYDepPassword", name = "급여이체비밀번호", length = 8)
    private String GYDepPassword;

    @MessageField(id = "YHDepPassword", name = "외화이체비밀번호", length = 8)
    private String YHDepPassword;

    @MessageField(id = "DepApprNum", name = "이체승인번호", length = 8)
    private String DepApprNum;

    @MessageField(id = "HJPassword", name = "현재비밀번호", length = 8)
    private String HJPassword;

    @MessageField(id = "BGPassword", name = "변경비밀번호", length = 8)
    private String BGPassword;

    @MessageField(id = "ResultMsg", name = "결과메세지", length = 62)
    private String ResultMsg;

}
