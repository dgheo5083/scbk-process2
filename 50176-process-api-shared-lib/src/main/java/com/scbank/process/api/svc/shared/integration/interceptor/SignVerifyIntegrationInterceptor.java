package com.scbank.process.api.svc.shared.integration.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequest;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequest;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpReqHeader;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptor;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.sign.SignComponent;
import com.scbank.process.api.svc.shared.components.sign.constants.SignConstants;
import com.scbank.process.api.svc.shared.components.sign.dto.SignVerifyInfo;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignActionType;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignVerifyType;
import com.scbank.process.api.svc.shared.constants.PRCSharedConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component("signVerifyIntegrationInterceptor")
public class SignVerifyIntegrationInterceptor implements IntegrationInterceptor {

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    /**
     * 전자서명 검증 공통 컴포넌트
     */
    private final SignComponent signComponent;

    @Override
    public void before(IntegrationContext iCtx, Object request) {
        // 서명 검증 대상 여부 체크
        SignVerifyType signVerifyType = iCtx.getAttribute(SignConstants.VERIFY_TYPE, SignVerifyType.class);
        SignActionType signActionType = iCtx.getAttribute(SignConstants.ACTION_TYPE, SignActionType.class);

        if (signVerifyType == null || signVerifyType == SignVerifyType.NONE) {
            if (log.isDebugEnabled()) {
                log.debug("SignVerifyIntegrationInterceptor > before(전자서명 비대상 거래)");
            }
            return;
        }

        // 서명 검증 요청 데이터 셋팅
        SignVerifyInfo signVerifyInfo = new SignVerifyInfo();
        signVerifyInfo.setSignVerifyType(signVerifyType);
        signVerifyInfo.setSignActionType(signActionType);

        IMessageObject requestMessage = null;

        // OLTP 요청 객체로부터 거래코드 및 전문요청 데이터 획득
        if (request instanceof OltpRequest oltpReqRequest) {
            OltpReqHeader header = oltpReqRequest.getHeader();

            String imsTranCd = StringUtils.defaultString(header.getOltpCommon().getImsTranCd()); // IMS_TRAN_CODE
            String inClassCd = StringUtils.defaultString(header.getOltpCommon().getInClassCd()); // IN_CLASS_CODE
            String svcCd = StringUtils.defaultString(header.getOltpCommon().getSvcCd()); // SERVICE_CODE
            String jobTp = StringUtils.defaultString(header.getOltpCommon().getJobTp()); // JOB_TYPE

            signVerifyInfo.setImsTranCd(imsTranCd);
            signVerifyInfo.setInClassCd(inClassCd);
            signVerifyInfo.setSvcCd(svcCd);
            signVerifyInfo.setJobTp(jobTp);

            requestMessage = oltpReqRequest.getRequestMessage();
        }
        // MCI 요청 IntegrationContext로 부터 거래코드 획득
        else if (request instanceof MciRequest mciReqRequest) {
            // MciRequest header = mciReqRequest.getHeader();

            String tranCd = StringUtils.defaultString(iCtx.getAttribute("tranCd")); // BUSINESS_FUNCTION_ID
            signVerifyInfo.setTranCd(tranCd);

            requestMessage = mciReqRequest.getRequestMessage();
        }

        if (log.isDebugEnabled()) {
            log.debug("SignVerifyIntegrationInterceptor > before(전자서명 대상 거래) - signVerifyInfo : [{}]",
                    signVerifyInfo.toString());
        }

        try {
            // 서명 검증/저장 실행
            signComponent.verifySign(requestMessage, signVerifyInfo);
        } catch (PRCServiceException e) {
            e.addErrorPageParameter("SECU_SERVICE_YN", "Y");
            throw e;
        } catch (Exception e) {
            PRCServiceException customException = new PRCServiceException("MA3CMM0001", "전달된 전자서명 처리 중 오류가 발생하였습니다.");
            customException.addErrorPageParameter("SECU_SERVICE_YN", "Y");
            throw customException;
        }

    }

    @Override
    public void after(IntegrationContext context, Object request, Object response) {
        sessionManager.removeGlobalValue(PRCSharedConstants.SIGN_SUCCESS_CODE);
    }
}