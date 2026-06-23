package com.scbank.process.api.fw.base.store;

import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.scbank.process.api.fw.base.constant.PRCBaseConstants;

/**
 * Thread Local 기반 요청 정보 저장소
 * 
 * @author sungdon.choi
 */
public class ThreadLocalStore {

    /**
     * 싱글톤 인스턴스
     */
    private static ThreadLocalStore threadLocalStore = new ThreadLocalStore();

    /**
     * 스레드로컬 저장소
     */
    private static ThreadLocal<Map<String, Object>> threadLocalMap = new InheritableThreadLocal<>();

    /**
     * 
     * @return
     */
    public static ThreadLocalStore getInstance() {
        return threadLocalStore;
    }

    /**
     * 저장소에 저장한다.
     * 
     * @param key   키 문자열
     * @param value 값 데이터
     */
    public void setValue(String key, Object value) {
        if (value != null)
            getMap().put(key, value);
    }

    /**
     * 저장소에 저장된 정보를 key를 이용하여 값을 가져온다.
     * 
     * @param key 키 문자열
     * @return 키 문자열을 이용하여 획득한 값
     */
    public Object getValue(String key) {
        return getMap().get(key);
    }

    /**
     * 저장소 Map 객체를 설정한다.
     * @param map 저장소 map
     */
    public void setMap(Map<String, Object> map) {
    	threadLocalMap.set(map);
    }
    /**
     * 저장소 Map 객체를 가져온다.
     * 
     * @return 저장소 Map
     */
    public Map<String, Object> getMap() {
        Map<String, Object> objMap = threadLocalMap.get();
        if (objMap == null) {
            objMap = new HashMap<>();
            threadLocalMap.set(objMap);
        }
        return objMap;
    }

    /**
     * 프로세스API Access Token 문자열을 획득한다.
     * 
     * @return 프로세스API Access Token 문자열
     */
    public String getCSLInternalAccessTokenString() {
        return (String) getValue(PRCBaseConstants.CSL_INTERNAL_ACCESS_TOKEN_STR_NAME);
    }

    /**
     * 프로세스API Access Token 문자열을 저장한다.
     * 
     * @param iat 프로세스API Access Token 문자열
     */
    public void setCSLInternalAccessTokenString(String iat) {
        setValue(PRCBaseConstants.CSL_INTERNAL_ACCESS_TOKEN_STR_NAME, iat);
    }

    /**
     * CSL Access Token DecodedJWT 객체를 가져온다.
     * 
     * @return {@link DecodedJWT}
     */
    public DecodedJWT getCSLInternalAccessToken() {
        return (DecodedJWT) getValue(PRCBaseConstants.CSL_INTERNAL_ACCESS_TOKEN_NAME);
    }

    /**
     * CSL Access Token DecodedJWT 객체를 저장한다.
     * 
     * @param iat
     */
    public void setCSLInternalAccessToken(DecodedJWT iat) {
        setValue(PRCBaseConstants.CSL_INTERNAL_ACCESS_TOKEN_NAME, iat);
    }

    /**
     * 요청 tracking id를 획득한다.
     * 
     * @return 요청 tracking id
     */
    public String getTrackingId() {
        return (String) getValue(PRCBaseConstants.X_TRACKING_ID_NAME);
    }

    /**
     * 요청 tracking id를 저장한다.
     * 
     * @param trackingId 요청 tracking id
     */
    public void setTrackingId(String trackingId) {
        setValue(PRCBaseConstants.X_TRACKING_ID_NAME, trackingId);
    }

    /**
     * CSL 세션ID를 가져온다.
     * 
     * @return CSL 세션ID 문자열
     */
    public String getJSessionId() {
        return (String) getValue(PRCBaseConstants.CSL_SESSION_ID_NAME);
    }

    /**
     * CSL 세션ID를 저장한다.
     * 
     * @param jSessionId CSL 세션ID 문자열
     */
    public void setJSessionId(String jSessionId) {
        setValue(PRCBaseConstants.CSL_SESSION_ID_NAME, jSessionId);
    }

    /**
     * Language Header 문자열을 가져온다.
     * 
     * @return Language Header 문자열
     */
    public String getLanguageHeader() {
        return (String) getValue(PRCBaseConstants.LANGUAGE_HEADER_NAME);
    }

    /**
     * Language Header 문자열을 저장한다.
     * 
     * @param languageHeader Language Header 문자열
     */
    public void setLanguageHeader(String languageHeader) {
        setValue(PRCBaseConstants.LANGUAGE_HEADER_NAME, languageHeader);
    }

    /**
     * 요청 채널ID 문자열을 가져온다.
     * 
     * @return 요청 채널ID 문자열
     */
    public String getChannelId() {
        return (String) getValue(PRCBaseConstants.CHANNEL_NAME);
    }

    /**
     * 요청 채널ID 문자열을 저장한다.
     * 
     * @param channelId 요청 채널ID 문자열
     */
    public void setChannelId(String channelId) {
        setValue(PRCBaseConstants.CHANNEL_NAME, channelId);
    }

    /**
     * 요청 디바이스ID를 가져온다.
     * 
     * @return 요청 디바이스ID 문자열
     */
    public String getDeviceId() {
        return (String) getValue(PRCBaseConstants.X_DEVICE_ID_NAME);
    }

    /**
     * 요청 디바이스ID를 저장한다.
     * 
     * @param deviceId 요청 디바이스ID 문자열
     */
    public void setDeviceId(String deviceId) {
        setValue(PRCBaseConstants.X_DEVICE_ID_NAME, deviceId);
    }

    /**
     * 앱 버전 문자열을 가져온다.
     * 
     * @return 앱 버전 문자열
     */
    public String getAppVersion() {
        return (String) getValue(PRCBaseConstants.X_APP_VERSION_NAME);
    }

    /**
     * 앱 버전 문자열을 저장한다.
     * 
     * @param appVersion 앱 버전 문자열
     */
    public void setAppVersion(String appVersion) {
        this.setValue(PRCBaseConstants.X_APP_VERSION_NAME, appVersion);
    }

    /**
     * OS 타입 문자열을 가져온다.
     * 
     * @return OS 타입 문자열
     */
    public String getOsType() {
        return (String) this.getValue(PRCBaseConstants.X_OS_TYPE_NAME);
    }

    /**
     * OS 타입 문자열을 저장한다.
     * 
     * @param osType OS 타입
     */
    public void setOsType(String osType) {
        this.setValue(PRCBaseConstants.X_OS_TYPE_NAME, osType);
    }

    /**
     * OS Version 문자열을 가져온다.
     * 
     * @return OS Version 문자열
     */
    public String getOsVersion() {
        return (String) this.getValue(PRCBaseConstants.X_OS_VERSION_NAME);
    }

    /**
     * OS Version 문자열을 저장한다.
     * 
     * @param osVersion OS Version 문자열
     */
    public void setOsVersion(String osVersion) {
        this.setValue(PRCBaseConstants.X_OS_VERSION_NAME, osVersion);
    }

    /**
     * 요청 화면ID를 가져온다.
     * 
     * @return 요청 화면ID
     */
    public String getScreenId() {
        return (String) this.getValue(PRCBaseConstants.X_SCREEN_ID_NAME);
    }

    /**
     * 요청 화면ID를 저장한다.
     * 
     * @param screenId 요청 화면ID
     */
    public void setScreenId(String screenId) {
        this.setValue(PRCBaseConstants.X_SCREEN_ID_NAME, screenId);
    }

    /**
     * 요청 메뉴ID를 가져온다.
     * 
     * @return 요청 메뉴ID
     */
    public String getMenuId() {
        return (String) this.getValue(PRCBaseConstants.X_MENU_ID_NAME);
    }

    /**
     * 요청 메뉴ID를 저장한다.
     * 
     * @param menuId 요청 메뉴ID
     */
    public void setMenuId(String menuId) {
        this.setValue(PRCBaseConstants.X_MENU_ID_NAME, menuId);
    }
    
    /**
     * IPINSIDE IP 문자열을 저장한다.
     * @param ip IPINSIDE IP 문자열
     */
    public void setIpinsideIp(String ip) {
    	this.setValue(PRCBaseConstants.IPINSIDE_IP, ip);
    }
    
    /**
     * IPINSIDE IP 문자열을 획득한다.
     * 
     * @return IPINSIDE IP 문자열
     */
    public String getIpinsideIp() {
    	return (String) this.getValue(PRCBaseConstants.IPINSIDE_IP);
    }
    
    /**
     * IPINSIDE AX 문자열을 저장한다.
     * @param wdata IPINSIDE AX 문자열
     */
    public void setIpinsideAx(String wdata) {
    	this.setValue(PRCBaseConstants.IPINSIDE_AX, wdata);
    }
    
    /**
     * IPINSIDE AX 문자열을 획득한다.
     * 
     * @return IPINSIDE AX 문자열
     */
    public String getIpinsideAx() {
    	return (String) this.getValue(PRCBaseConstants.IPINSIDE_AX);
    }
    
    /**
     * IPINSIDE MAC 문자열을 저장한다.
     * @param mac IPINSIDE MAC 문자열
     */
    public void setIpinsideMac(String mac) {
    	this.setValue(PRCBaseConstants.IPINSIDE_MAC, mac);
    }
    
    /**
     * IPINSIDE MAC 문자열을 획득한다.
     * 
     * @return IPINSIDE MAC 문자열
     */
    public String getIpinsideMac() {
    	return (String) this.getValue(PRCBaseConstants.IPINSIDE_MAC);
    }
    
    /**
     * IPINSIDE HDD 문자열을 저장한다.
     * @param mac IPINSIDE HDD 문자열
     */
    public void setIpinsideHdd(String hdd) {
    	this.setValue(PRCBaseConstants.IPINSIDE_HDD, hdd);
    }
    
    /**
     * IPINSIDE HDD 문자열을 획득한다.
     * 
     * @return IPINSIDE HDD 문자열
     */
    public String getIpinsideHdd() {
    	return (String) this.getValue(PRCBaseConstants.IPINSIDE_HDD);
    }
    
    /**
     * 
     * @param deviceUUID
     */
    public void setDeviceUUID(String deviceUUID) {
    	this.setValue(PRCBaseConstants.X_DEVICE_UUID, deviceUUID);
    }
    
    public String getDeviceUUID() {
    	return (String)this.getValue(PRCBaseConstants.X_DEVICE_UUID);
    }
    
    public void setSimSerial(String simSerial) {
    	this.setValue(PRCBaseConstants.X_SIM_SERIAL, simSerial);
    }
    
    public String getSimSerial() {
    	return (String)this.getValue(PRCBaseConstants.X_SIM_SERIAL);
    }
    
    public void setAcType(String acType) {
    	this.setValue(PRCBaseConstants.X_AC_TYPE, acType);
    }
    
    public String getAcType() {
    	return (String) this.getValue(PRCBaseConstants.X_AC_TYPE);
    }
    
    public void setForceCheckCode(String forceCheckCode) {
    	this.setValue(PRCBaseConstants.X_FORCE_CHECK_CODE, forceCheckCode);
    }
    
    public String getForceCheckCode() {
    	return (String) this.getValue(PRCBaseConstants.X_FORCE_CHECK_CODE);
    }

    /**
     * 키를 이용하여 저장소에 저장된 값을 삭제한다.
     * 
     * @param key 저장소에 저장된 값의 키 문자열
     */
    public void clearValue(String key) {
        getMap().remove(key);
    }

    /**
     * 저장소 초기화 수행
     */
    public void clearThreadLocalStore() {
        threadLocalMap.remove();
        // MDC.clear();
    }
}
