package com.scbank.process.api.svc.common.service.verification.dto.tokens;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationTokensActivateRequest", description = "보안매체 - 초기화 요청 DTO", type = Type.REQUEST)
public class VerificationTokensActivateRequest implements IMessageObject {

    @MessageField(id = "additionalType", name = "인증구분(A:간편인증, B:SMS명의인증, D:ARS인증, F:해외출국여부조회, G:공인인증서, P:신분증촬영)", defaultValue = "A:D:F")
    private String additionalType;

    @MessageField(id = "additionalYn", name = "추가인증 사용여부 ( defaule:기본정책에 따름, Y:사용, N:미사용 )", example = "")
    private String additionalYn;

    @MessageField(id = "tokensYn", name = "보안매체 사용여부 ( default:로그인type에 따름, Y:사용(로그인Type무시하고 항상 출력), N:미사용(이체비밀번호 사용자는 비밀번호만 출력) )")
    private String tokensYn;

    @MessageField(id = "tranType", name = "거래구분", example = "")
    private String tranType;

    @MessageField(id = "todayTranAmt", name = "금일이체 누적금액", defaultValue = "0", example = "0")
    private String todayTranAmt;

    @MessageField(id = "secrCardSeqYn", name = "보안카드 지시번호 사용여부 ( default:미사용, Y:사용 )")
    private String safeCardSeqYn;

}
