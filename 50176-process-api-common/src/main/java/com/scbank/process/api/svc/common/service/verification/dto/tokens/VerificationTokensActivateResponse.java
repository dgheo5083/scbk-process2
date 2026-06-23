package com.scbank.process.api.svc.common.service.verification.dto.tokens;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationTokensActivateResponse", description = "보안매체 - 초기화 응답 DTO", type = Type.RESPONSE)
public class VerificationTokensActivateResponse implements IMessageObject {

    @MessageField(id = "additionalType", name = "인증구분(A:간편인증, B:SMS명의인증, D:ARS인증, E:SMS인증(삭제), F:해외출국여부조회, G:공인인증서, P:신분증촬영)")
    private String additionalType;

    @MessageField(id = "additionalYn", name = "추가인증 사용여부")
    private String additionalYn;

    @MessageField(id = "tokensType", name = "보안매체 구분")
    private String tokensType;

    @MessageField(id = "tokensYn", name = "보안매체 사용여부")
    private String tokensYn;

    @MessageField(id = "rspActnMethCd", name = "FDS 상태코드")
    private String rspActnMethCd;

    @MessageField(id = "tranPwdYn", name = "이체비밀번호 확인여부")
    private String tranPwdYn;

    @MessageField(id = "safeCardSeq1", name = "보안카드 일련번호 지시번호1")
    private String safeCardSeq1;

    @MessageField(id = "safeCardSeq2", name = "보안카드 일련번호 지시번호2")
    private String safeCardSeq2;

    @MessageField(id = "safeCardSeq3", name = "보안카드 일련번호 지시번호3")
    private String safeCardSeq3;

    @MessageField(id = "safeCardIndex1", name = "보안카드 index 1")
    private String safeCardIndex1;

    @MessageField(id = "safeCardIndex2", name = "보안카드 index 2")
    private String safeCardIndex2;

    @MessageField(id = "safeCardKind", name = "보안카드종류 ( 1:보안카드, 2:(구)OTP, 3:일반OTP 및 스마트OTP )")
    private String safeCardKind;

    @MessageField(id = "smartOTP", name = "스마트OTP")
    private String smartOTP;

    @MessageField(id = "telNo1", name = "연락처 첫번째자리")
    private String telNo1;

    @MessageField(id = "telNo2", name = "연락처 두번째자리")
    private String telNo2;

    @MessageField(id = "telNo3", name = "연락처 세번째자리")
    private String telNo3;

    @MessageField(id = "fidoSignSkipYn", name = "금융인증서(생체), 디지털인증서 전자서명 스킵여부", defaultValue = "N")
    private String fidoSignSkipYn;

    @MessageField(id = "motpSignSkipYn", name = "MOTP일 경우 전자서명 스킵여부", defaultValue = "N")
    private String motpSignSkipYn;

    @MessageField(id = "branchNum", name = "영업점 (FDS 출금정지 )", defaultValue = "N")
    private String branchNum;

}