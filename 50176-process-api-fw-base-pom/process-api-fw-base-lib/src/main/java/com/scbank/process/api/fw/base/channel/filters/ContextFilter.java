package com.scbank.process.api.fw.base.channel.filters;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.scbank.process.api.fw.base.constant.PRCBaseConstants;
import com.scbank.process.api.fw.base.store.ThreadLocalStore;
import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.core.utils.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <pre>
 * 요청정보 처리 필터 클래스
 * 요청헤더에서 단말정보/앱정보/IPINSIDE 정보를 획득하여, ThreadLocal에 저장한다.
 * </pre>
 * @author sungdon.choi
 */
public class ContextFilter extends OncePerRequestFilter {

    /**
     * 요청 정보 저장소
     */
     private final ThreadLocalStore threadLocalStore = ThreadLocalStore.getInstance();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	try {
    		this.populateRequest(request);

            filterChain.doFilter(request, response);
    	} finally {
    		//ThreadLocal Context release
    		threadLocalStore.clearThreadLocalStore();
    		SecureContextStore.clearContext();
    	}
    }

    /**
     * 요청헤더에서 단말정보/앱정보/IPINSIDE 정보를 획득하여, ThreadLocal에 저장한다.
     * @param request {@link HttpServletRequest}
     */
    private void populateRequest(HttpServletRequest request) {
    	//Accept-Language
    	String acceptLanguage = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.LANGUAGE_HEADER_NAME), StringUtils.EMPTY);
    	threadLocalStore.setLanguageHeader(acceptLanguage);
    	
    	//세션ID (x-auth-token)
    	String sessionId = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.CSL_SESSION_ID_NAME), StringUtils.EMPTY);
    	threadLocalStore.setJSessionId(sessionId);
    	
    	//채널(channel) IB/MB/MW
    	String channel = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.CHANNEL_NAME), StringUtils.EMPTY);
    	threadLocalStore.setChannelId(channel);
    	
    	//앱 디바이스 정보
    	String deviceId = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_DEVICE_ID_NAME), StringUtils.EMPTY);
    	threadLocalStore.setDeviceId(deviceId);
    	
    	//앱 버전
    	String appVersion = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_APP_VERSION_NAME), StringUtils.EMPTY);
    	threadLocalStore.setAppVersion(appVersion);
    	
    	//OS 버전
    	String osVersion = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_OS_VERSION_NAME), StringUtils.EMPTY);
    	threadLocalStore.setOsVersion(osVersion);
    	
    	//OS 타입
    	String osType = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_OS_TYPE_NAME), StringUtils.EMPTY);
    	threadLocalStore.setOsType(osType);
    	
    	//TODO 처리 확인 필요
    	//클라이언트 IP
    	//String clientIp = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.TRUE_CLIENT_IP), StringUtils.EMPTY);
    	//threadLocalStore.set
    	
    	//***********************************************
    	//트래킹/화면 정보
    	//***********************************************
    	
    	//트래킹ID
    	String trackingId = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_TRACKING_ID_NAME), StringUtils.EMPTY);
    	threadLocalStore.setTrackingId(trackingId);
    	
    	//화면ID
    	String screenId = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_SCREEN_ID_NAME), StringUtils.EMPTY);
    	threadLocalStore.setScreenId(screenId);
    	
    	//메뉴ID
    	String menuId = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_MENU_ID_NAME), StringUtils.EMPTY);
    	threadLocalStore.setMenuId(menuId);
    	
    	//***********************************************
    	//IPINSIDE 정보
    	//***********************************************
    	
    	String ipinsideIp = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.IPINSIDE_IP), StringUtils.EMPTY);
    	threadLocalStore.setIpinsideIp(ipinsideIp);
    	
    	String ipinsideAx = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.IPINSIDE_AX), StringUtils.EMPTY);
    	threadLocalStore.setIpinsideAx(ipinsideAx);
    	
    	String ipinsideMac = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.IPINSIDE_MAC), StringUtils.EMPTY);
    	threadLocalStore.setIpinsideMac(ipinsideMac);
    	
    	String ipinsideHdd = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.IPINSIDE_HDD), StringUtils.EMPTY);
    	threadLocalStore.setIpinsideHdd(ipinsideHdd);
    	
    	//디바이스 UUID
    	String deviceUUID = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_DEVICE_UUID), StringUtils.EMPTY);
    	threadLocalStore.setDeviceUUID(deviceUUID);
    	
    	//유심 시리얼 번호
    	String simSerial = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_SIM_SERIAL), StringUtils.EMPTY);
    	threadLocalStore.setSimSerial(simSerial);
    	
    	//메뉴접근제어타입
    	String acType = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_AC_TYPE), StringUtils.EMPTY);
    	threadLocalStore.setAcType(acType);
    	
    	//서비스 이용시간 체크 우선순위 코드문자열
    	String forceCheckCode = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_FORCE_CHECK_CODE), StringUtils.EMPTY);
    	threadLocalStore.setForceCheckCode(forceCheckCode);
    }
}
