package com.scbank.process.api.svc.shared.components.auth;

import org.apache.commons.lang3.StringUtils;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.sign.SignComponent;
import com.scbank.process.api.svc.shared.components.sign.dto.SignVerifyInfo;
import com.scbank.process.api.svc.shared.components.verification.VerificationComponent;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationVerifyInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class SecureDataComponent {

    /**
     * 전자서명 검증 컴포넌트
     */
    private final SignComponent signComponent;

    private final VerificationComponent verificationComponent;

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    @ComponentOperation(name = "(추가인증)/보안매체/전자서명 검증")
    @Deprecated
    public void verifyAll(IMessageObject serviceRequest, VerificationVerifyInfo verificationVerifyInfo,
            SignVerifyInfo signVerifyInfo) {

        throw new PRCServiceException("SIGNERROR", "공통 전자서명 검증 직접호출 공통기능이 변경되었습니다.");

        // verificationComponent.verifyAdditional();

        // verificationComponent.verifyTokens(verificationVerifyInfo);

        // // 전자서명 검증
        // signComponent.verifySign(serviceRequest, signVerifyInfo);
    }

    @ComponentOperation(name = "(추가인증)/보안매체/전자서명 검증")
    public void verifyAll(VerificationVerifyInfo verificationVerifyInfo, SignVerifyInfo signVerifyInfo) {
        verificationComponent.verifyAdditional();

        verificationComponent.verifyTokens(verificationVerifyInfo);

        // 전자서명 검증
        signComponent.verifySign(signVerifyInfo);
    }

    @ComponentOperation(name = "(추가인증)/보안매체/전자서명 검증")
    @Deprecated
    public void verifyAll(IMessageObject serviceRequest, SignVerifyInfo signVerifyInfo) {
        throw new PRCServiceException("SIGNERROR", "공통 전자서명 검증 직접호출 공통기능이 변경되었습니다.");
        // verificationComponent.verifyAdditional();

        // VerificationVerifyInfo verificationVerifyInfo = new VerificationVerifyInfo();
        // verificationComponent.verifyTokens(verificationVerifyInfo);

        // // 전자서명 검증
        // signComponent.verifySign(serviceRequest, signVerifyInfo);
    }

    @ComponentOperation(name = "(추가인증)/보안매체/전자서명 검증")
    public void verifyAll(SignVerifyInfo signVerifyInfo) {
        verificationComponent.verifyAdditional();

        VerificationVerifyInfo verificationVerifyInfo = new VerificationVerifyInfo();
        verificationComponent.verifyTokens(verificationVerifyInfo);

        // 전자서명 검증
        signComponent.verifySign(signVerifyInfo);
    }

    @ComponentOperation(name = "추가인증/보안매체 검증")
    public void verifyVerification() {

        VerificationVerifyInfo verificationVerifyInfo = new VerificationVerifyInfo();

        String myAcctSkipYn = sessionManager.getGlobalValue("myAcctSkipYn", String.class);
        String motpSignSkipYn = sessionManager.getGlobalValue("motpSignSkipYn", String.class);
        String logSkipYn = sessionManager.getGlobalValue("LogSkip", String.class);
        String transPwUseYn = "";

        if (sessionManager.isLogin()) {
            transPwUseYn = StringUtils.defaultString(sessionManager.getLoginValue("TransPWUseYN", String.class));
        } else {
            transPwUseYn = StringUtils.defaultString(sessionManager.getGlobalValue("TransPWUseYN", String.class));
        }

        log.debug("HDG Debug myAcctSkipYn [{}], motpSignSkipYn [{}]", myAcctSkipYn, motpSignSkipYn);

        String fidoSkip = "Y".equals(myAcctSkipYn) ? "M" : "";
        fidoSkip = "Y".equals(motpSignSkipYn) ? "M" : fidoSkip;

        verificationVerifyInfo.setFidoSkip(fidoSkip);

        verificationVerifyInfo.setLogSkip(logSkipYn);

        verificationVerifyInfo.setTransPasswordYn("1".equals(transPwUseYn) ? "N" : "Y");

        verificationComponent.verifyAdditional();

        verificationComponent.verifyTokens(verificationVerifyInfo);
    }

    @ComponentOperation(name = "추가인증/보안매체 검증")
    public void verifyVerification(VerificationVerifyInfo verificationVerifyInfo) {
        verificationComponent.verifyAdditional();
        verificationComponent.verifyTokens(verificationVerifyInfo);
    }

    @ComponentOperation(name = "전자서명 검증")
    @Deprecated
    public void verifySign(IMessageObject serviceRequest, SignVerifyInfo signVerifyInfo) {
        // signComponent.verifySign(serviceRequest, signVerifyInfo);
        throw new PRCServiceException("SIGNERROR", "공통 전자서명 검증 직접호출 공통기능이 변경되었습니다.");
    }

    @ComponentOperation(name = "전자서명 검증")
    public void verifySign(SignVerifyInfo signVerifyInfo) {
        signComponent.verifySign(signVerifyInfo);
    }

}
