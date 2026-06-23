package com.scbank.process.api.svc.shared.channel.interceptors;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.servlet.HandlerInterceptor;

import com.scbank.process.api.fw.base.store.ThreadLocalStoreDelegator;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.accesscontrol.MenuAuthorityCheckComponent;
import com.scbank.process.api.svc.shared.components.accesscontrol.dto.MenuAuthorityCheckRequest;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 요청 서비스 권한 체크 인터셉터 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
public class ServiceAuthCheckInterceptor implements HandlerInterceptor {
	
	private final MenuAuthorityCheckComponent authorityCheckComponent;
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.debug("ServiceAuthCheckInterceptor preHandle");
        
        if (isCheckIgnoreMenuId()) {
        	return true;
        }
        
        //권한체크 무시 uri 확인
        if (isCheckIgnoreRequestUri(request)) {
        	log.debug("# 권한체크 무시 요청 uri: {}", request.getRequestURI());
        	return true;
        }
        
        //**************************************************
        // 헤더로 넘어온 메뉴ID/권한정보 등을 이용하여
        // 이용시간/권한체크를 진행한다.
        //**************************************************
        String menuId = ThreadLocalStoreDelegator.getMenuId();
        String acType = ThreadLocalStoreDelegator.getAcType();
        String forceCheckCode = ThreadLocalStoreDelegator.getForceCheckCode();
        
        //TODO 접근권한 체크 무시 API URI 처리
        
        //메뉴ID가 없는경우
        if (!StringUtils.hasText(menuId)) {
        	log.debug("# 메뉴식별자 정보가 없음");
        	return true;
        }
        
        this.authorityCheckComponent.checkAuthority(MenuAuthorityCheckRequest.builder()
        		.menuId(menuId)
        		.acType(acType)
        		.forceCheckCode(forceCheckCode)
        		.build());
        
        return true;
    }
    
    /**
     * 이용가능시간 체크 무시 메뉴ID 여부 확인
     * @param request
     * @return
     */
    private boolean isCheckIgnoreMenuId() {
    	String menuId = StringUtils.defaultIfEmpty(PRCSharedUtils.getMenuId(), StringUtils.EMPTY);
    	if (StringUtils.isEmpty(menuId)) {
    		return false;
    	}
    	
    	List<String> checkIgnoreMenuIdList = Arrays.asList(PropertiesUtils.getString("PAGECHECK_IGNORE_MENU_ID", "").split("\\|"));
    	return checkIgnoreMenuIdList.stream().anyMatch(v -> menuId.equals(v));
    }
    
    /**
	 * 이용가능시간 체크 무시 API URI 여부 확인
	 * @param uri 현재 요청 URI
	 * @return
	 */
	private boolean isCheckIgnoreRequestUri(HttpServletRequest request) {
		String uri = request.getRequestURI();
		List<String> checkIgnoreUriList = Arrays.asList(PropertiesUtils.getString("PAGECHECK_IGNORE_URI_LIST", "").split("\\|"));
		return checkIgnoreUriList.stream().anyMatch(v -> uri.equals(v));
	}
}
