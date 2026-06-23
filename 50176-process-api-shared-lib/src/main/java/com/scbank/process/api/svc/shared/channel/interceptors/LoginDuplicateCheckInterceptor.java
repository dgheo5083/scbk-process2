package com.scbank.process.api.svc.shared.channel.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.dao.LoginSsoHisDao;
import com.scbank.process.api.svc.shared.dao.dto.DuplicationLoginCheckInfoParameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 중복 로그인 체크 인터셉터 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
public class LoginDuplicateCheckInterceptor implements HandlerInterceptor {

	/**
	 * 프레임워크 세션 매니저 컴포넌트
	 */
	private final ISessionContextManager sessionContextManager;
	
	/**
	 * 로그인 이력 조회 Dao 컴포넌트
	 */
	private final LoginSsoHisDao loginSsoHisDao;
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("LoginDuplicateCheckInterceptor > preHandle");
        
        //중복로그인 허용여부 체크
        String allowedDuplicateLogin = PropertiesUtils.getString("ALLOW_DUPLICATE_LOGIN");
        
        log.debug("LoginDuplicateCheckInterceptor > preHandle - ALLOW_DUPLICATE_LOGIN: [{}], URL: [{}]",  allowedDuplicateLogin, request.getRequestURI());
        
        if (!"N".equals(allowedDuplicateLogin)) {
        	return true;
        }
        log.debug("LoginDuplicateCheckInterceptor > preHandle - isLogin : [{}]",  this.sessionContextManager.isLogin());
        
        // 로그인상태 체크
        if (!this.sessionContextManager.isLogin()) {
        	return true;
        }
        
        //중복로그인 테이블 조회
        String userId = this.sessionContextManager.getLoginValue("UserID", String.class);
        String loginDatetime = StringUtils.defaultIfEmpty(this.sessionContextManager.getLoginValue("LoginDateTime", String.class), "0");
        
        log.debug("LoginDuplicateCheckInterceptor > preHandle - (session)userId: [{}], (session)loginDatetime : [{}]", userId, allowedDuplicateLogin);
        
        DuplicationLoginCheckInfoParameter parameter = DuplicationLoginCheckInfoParameter.builder()
        		.userId(userId)
        		.loginDt(Long.valueOf(loginDatetime))
        		.build();
        
        log.debug("LoginDuplicateCheckInterceptor > preHandle - parameter: [{}]", parameter.toString());
        
        int result = this.loginSsoHisDao.selectDuplicationLoginCheckInfo(parameter);
        
        log.debug("LoginDuplicateCheckInterceptor > preHandle - result: [{}]", result);
        
        if (result == 0) {
        	//로그아웃 처리
        	this.sessionContextManager.logout();
        	
        	throw new PRCServiceException("PRCLGN0014", "타 PC/Mobile에서 로그인되어 로그아웃 처리되었습니다. 정상적인 거래가 아니시면 고객센터(1588-1599)로 문의하여 주시기 바랍니다.");
        }
        
        return true;
    }
}
