package com.scbank.process.api.fw.base.store;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Thread Local Store 접근자 클래스
 */
public class ThreadLocalStoreDelegator {

    public static ThreadLocalStore threadLocalStore() {
        return ThreadLocalStore.getInstance();
    }

    /**
     * 프로세스API Access Token 문자열을 획득한다.
     * 
     * @return CSL Access Token 문자열
     */
    public static String getCSLInternalAccessTokenString() {
        return ThreadLocalStore.getInstance()
                .getCSLInternalAccessTokenString();
    }

    /**
     * 프로세스API Access Token 객체 획득
     * 
     * @return CSL Access Token 객체
     */
    public static DecodedJWT getCSLInternalAccessToken() {
        return threadLocalStore().getCSLInternalAccessToken();
    }

    /**
     * 요청 tracking ID 문자열을 획득한다.
     * 
     * @return 요청 tracking ID 문자열
     */
    public static String getTrackingId() {
        return threadLocalStore().getTrackingId();
    }

    /**
     * 프로세스API 세션ID 문자열을 획득한다.
     * 
     * @return CSL 세션ID 문자열
     */
    public static String getJSessionId() {
        return threadLocalStore().getJSessionId();
    }

    /**
     * Language Header 문자열을 가져온다.
     * 
     * @return Language Header 문자열
     */
    public static String getLanguageHeader() {
        return threadLocalStore().getLanguageHeader();
    }

    /**
     * 요청 채널ID 문자열을 가져온다.
     * 
     * @return 요청 채널ID 문자열
     */
    public static String getChannelId() {
        return threadLocalStore().getChannelId();
    }

    /**
     * <pre>
     * 요청 디바이스ID를 가져온다.
     * 모바일뱅킹인 경우에만 값이 존재한다.
     * </pre>
     * @return 요청 디바이스ID 문자열
     */
    public static String getDeviceId() {
        return threadLocalStore().getDeviceId();
    }

    /**
     * 앱 버전 문자열을 가져온다.
     * 
     * @return 앱 버전 문자열
     */
    public static String getAppVersion() {
        return threadLocalStore().getAppVersion();
    }

    /**
     * OS 타입 문자열을 가져온다.
     * 
     * @return OS 타입 문자열
     */
    public static String getOsType() {
        return threadLocalStore().getOsType();
    }

    /**
     * OS Version 문자열을 가져온다.
     * 
     * @return OS Version 문자열
     */
    public static String getOsVersion() {
        return threadLocalStore().getOsVersion();
    }

    /**
     * 요청 화면ID를 가져온다.
     * 
     * @return 요청 화면ID
     */
    public static String getScreenId() {
        return threadLocalStore().getScreenId();
    }

    /**
     * 요청 메뉴ID를 가져온다.
     * 
     * @return 요청 메뉴ID
     */
    public static String getMenuId() {
        return threadLocalStore().getMenuId();
    }
    
    /**
     * IPINSIDE IP 문자열을 획득한다.
     * 
     * @return IPINSIDE IP 문자열
     */
    public static String getIpinsideIp() {
    	return threadLocalStore().getIpinsideIp();
    }
    
    /**
     * IPINSIDE AX 문자열을 획득한다.
     * 
     * @return IPINSIDE AX 문자열
     */
    public static String getIpinsideAx() {
    	return threadLocalStore().getIpinsideAx();
    }
    
    /**
     * IPINSIDE MAC 문자열을 획득한다.
     * 
     * @return IPINSIDE MAC 문자열
     */
    public static String getIpinsideMac() {
    	return threadLocalStore().getIpinsideMac();
    }
    
    /**
     * IPINSIDE HDD 문자열을 획득한다.
     * 
     * @return IPINSIDE HDD 문자열
     */
    public static String getIpinsideHdd() {
    	return threadLocalStore().getIpinsideHdd();
    }
    
    /**
     * 
     * @return
     */
    public static String getDeviceUUID() {
    	return threadLocalStore().getDeviceUUID();
    }
    
    /**
     * 
     * @return
     */
    public static String getSimSerial() {
    	return threadLocalStore().getSimSerial();
    }
    
    public static String getAcType() {
    	return threadLocalStore().getAcType();
    }
    
    public static String getForceCheckCode() {
    	return threadLocalStore().getForceCheckCode();
    }
}
