package com.scbank.process.api.svc.shared.service.interceptor;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;
import com.scbank.process.api.fw.base.store.secure.vo.SignInfo;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.interceptor.IServiceInterceptor;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.sign.constants.SignConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class SignVerifyServiceInterceptor implements IServiceInterceptor {

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    /**
     * SignVerifyIntegrationInterceptor에서
     * 서명 검증 및 저장 처리를 위해서
     * 
     * SecureContext의 서명데이터 존재여부 체크
     * 요청 request 데이터를 serviceContext에 저장
     */
    @Override
    public boolean preHandle(IServiceContext sCtx, IMessageObject request) {

        // SecureContext
        Optional<SecureContext> secureContext = SecureContextStore.getContext();

        if (secureContext.isEmpty() || secureContext.get() == null) {
            // 서명 데이터 미존재
            return true;
        }

        SignInfo signInfo = secureContext.get().getSign();

        if (signInfo == null) {
            // 서명 데이터 미존재
            return true;
        }

        sCtx.setAttribute(SignConstants.SIGN_VERIFY_SERVICE_REQUEST, request);
        log.debug("SignVerifyServiceInterceptor > preHandle(전자서명 context 셋팅) > request : [{}]",
                request != null ? request.toString() : null);

        return true;
    }

    @Override
    public void postHandle(IServiceContext sCtx, IMessageObject request) {

        // TODO : service Interceptor에서 session Clear 해도 될까?
        // 전자서명 정상응답코드
        // sessionManager.removeGlobalValue(PRCSharedConstants.SIGN_SUCCESS_CODE);
        // 전자서명데이터
        // sessionManager.removeGlobalValue(PRCSharedConstants.SIGN_DATA_NAME);

        // IMPORTANT: 디지털인증서 전자서명 원문은 한 service transaction 내에서 설정되지 않아서 clear 불가
        // 전자서명 원문
        // sessionManager.removeGlobalValue(PRCSharedConstants.SIGN_ORG_DATA_NAME);
    }
}