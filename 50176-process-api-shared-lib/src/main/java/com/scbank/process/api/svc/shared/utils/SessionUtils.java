package com.scbank.process.api.svc.shared.utils;

import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class SessionUtils {

    /*
     * 세션 컨텍스트 매니저
     */
    private ISessionContextManager sessionManager;

    // <T> T getGlobalValue(String sessionKey, Class<T> requiredType);

    /**
     * 세션값 조회
     * 로그인상태에 따른 LoginSession / globalSession 에서 세션값 조회
     * 로그인 상태일 경우 LoginSession 우선조회
     * 
     * @title 세션값 조회
     * @param String   : 세션 키
     * @param Class<T> : 세션 값의 타겟 클래스
     * @return T : 세션값
     */
    public <T> T getSessionValue(String sessionKey, Class<T> requiredType) {

        sessionManager = RuntimeContext.getBean(ISessionContextManager.class);

        Object result = null;
        
        log.debug("getSessionValue > sessionManager.isLogin() : [{}]", sessionManager.isLogin());
        if (sessionManager.isLogin()) {
            //result = sessionManager.getLoginValue(sessionKey, requiredType);
        	result = sessionManager.getLoginValue(sessionKey, requiredType) != null ? 
        			sessionManager.getLoginValue(sessionKey, requiredType) : sessionManager.getGlobalValue(sessionKey, requiredType);
        } else {
            switch (sessionKey) {
                case "UserID":
                    result = CommonBizConstants.DEFAULT_USER_ID;
                    break;
                case "TSPassword":
                    result = CommonBizConstants.DEFAULT_TS_PASS_WORD;
                    break;
                default:
                    if (isAuthenticated()) {
                    	result = sessionManager.getLoginValue(sessionKey, requiredType) != null ? 
                    			sessionManager.getLoginValue(sessionKey, requiredType) : sessionManager.getGlobalValue(sessionKey, requiredType);
                    } else {
                    	log.debug("getSessionValue > sessionKey : [{}]", sessionKey);
                        result = sessionManager.getGlobalValue(sessionKey, requiredType);
                    }
            }
        }

        if (requiredType == String.class) {
            return requiredType.cast(StringUtils.defaultString((String) result));
        }

        return requiredType.cast(result);
    }

    /**
     * 
     * @param sessionKey
     * @return
     */
    public String getSessionValue(String sessionKey) {

        return getSessionValue(sessionKey, String.class);
    }

    public static boolean isLoginOrAuth(String type) {

        sessionManager = RuntimeContext.getBean(ISessionContextManager.class);

        boolean isloginAuth = false;
        switch (type) {
            case "LOGIN":
                isloginAuth = sessionManager.isLogin();
                break;
            case "AUTH":
                isloginAuth = isAuthenticated();
                break;
            default:
                isloginAuth = (sessionManager.isLogin() || isAuthenticated());
                break;
        }
        return isloginAuth;
    }

    public boolean isAuthenticated() {
    	sessionManager = RuntimeContext.getBean(ISessionContextManager.class);
        return "Y".equals(StringUtils.defaultString(sessionManager.getGlobalValue("BIZ_AUTH_FLAG", String.class)));
    }
}
