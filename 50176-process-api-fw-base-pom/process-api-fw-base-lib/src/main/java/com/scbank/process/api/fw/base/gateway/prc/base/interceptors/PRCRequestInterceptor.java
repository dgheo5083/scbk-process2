package com.scbank.process.api.fw.base.gateway.prc.base.interceptors;

import java.util.Locale;

import com.scbank.process.api.fw.base.constant.PRCBaseConstants;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.LocaleUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContext;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 프로세스 API 엔드포인트 요청 인터셉터 구현 클래스
 * 
 * @author sungdon.choi
 */
public class PRCRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        IServiceContext serviceContext = ServiceContextHolder.getContext();
        HttpServletRequest request = serviceContext.request();

        String jsessionId = this.getJsessionId(serviceContext);
        String cookieString = this.getCookieString(request);
        String userAgentString = "";
        String cslAccessTokenString = "";
        String channelCode = this.getChannelCode(serviceContext); // 채널정보
        String deviceId = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_DEVICE_ID_NAME), StringUtils.EMPTY);
        String appVersion = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_APP_VERSION_NAME), StringUtils.EMPTY);
        String language = this.getLanguage(serviceContext); // 언어코드
        String osType = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_OS_TYPE_NAME), StringUtils.EMPTY);
        String osVersion = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_OS_VERSION_NAME), StringUtils.EMPTY);
        String clientIp = ""; // 단말 IP
        String trackingId = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_TRACKING_ID_NAME), StringUtils.EMPTY);
        String screenId = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_SCREEN_ID_NAME), StringUtils.EMPTY);
        String menuId = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_MENU_ID_NAME), StringUtils.EMPTY);
        
        String ipinsideIp = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.IPINSIDE_IP), StringUtils.EMPTY);
        String ipinsideAx = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.IPINSIDE_AX), StringUtils.EMPTY);
        String ipinsideMac = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.IPINSIDE_MAC), StringUtils.EMPTY);
        String ipinsideHdd = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.IPINSIDE_HDD), StringUtils.EMPTY);
        
        String deviceUUID = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_DEVICE_UUID), StringUtils.EMPTY);
        String simSerial = StringUtils.defaultIfEmpty(request.getHeader(PRCBaseConstants.X_SIM_SERIAL), StringUtils.EMPTY);

        template.header("Authorization", new String[] { "Bearer " + cslAccessTokenString })
                .header("cookie", cookieString)
                .header("User-Agent", userAgentString)
                .header("Accept-Language", language)
                .header(PRCBaseConstants.CSL_SESSION_ID_NAME, new String[] { jsessionId })
                .header(PRCBaseConstants.CHANNEL_NAME, channelCode)
                .header(PRCBaseConstants.X_DEVICE_ID_NAME, deviceId)
                .header(PRCBaseConstants.X_APP_VERSION_NAME, appVersion)
                .header(PRCBaseConstants.X_OS_VERSION_NAME, osVersion)
                .header(PRCBaseConstants.X_OS_TYPE_NAME, osType)
                .header(PRCBaseConstants.X_OS_VERSION_NAME, osVersion)
                .header(PRCBaseConstants.TRUE_CLIENT_IP, clientIp)
                .header(PRCBaseConstants.X_SCREEN_ID_NAME, screenId)
                .header(PRCBaseConstants.X_MENU_ID_NAME, menuId)
                .header(PRCBaseConstants.X_TRACKING_ID_NAME, trackingId)
                .header(PRCBaseConstants.IPINSIDE_IP, ipinsideIp)
                .header(PRCBaseConstants.IPINSIDE_AX, ipinsideAx)
                .header(PRCBaseConstants.IPINSIDE_MAC, ipinsideMac)
                .header(PRCBaseConstants.IPINSIDE_HDD, ipinsideHdd)
                .header(PRCBaseConstants.X_DEVICE_UUID, deviceUUID)
                .header(PRCBaseConstants.X_SIM_SERIAL, simSerial);
    }

    /**
     * CSL 세션ID를 획득한다.
     * 
     * @param serviceContext {@link IServiceContext}
     * @return CSL 세션ID
     */
    private String getJsessionId(IServiceContext serviceContext) {
        if (serviceContext == null) {
            return StringUtils.EMPTY;
        }

        String jsessionId = "";
        ISessionContext sessionContext = serviceContext.session();
        if (sessionContext != null) {
            jsessionId = sessionContext.getSessionId();
        }
        return jsessionId;
    }

    /**
     * 채널코드를 획득한다.
     * 
     * @param serviceContext {@link IServiceContext}
     * @return 채널코드
     */
    private String getChannelCode(IServiceContext serviceContext) {
        if (serviceContext == null) {
            return StringUtils.EMPTY;
        }

        String channelCode = serviceContext.channelId();
        if (StringUtils.isEmpty(channelCode)) {
            HttpServletRequest request = serviceContext.request();
            if (request != null && request.getHeader(PRCBaseConstants.CHANNEL_NAME) != null) {
                channelCode = StringUtils.defaultString(request.getHeader(PRCBaseConstants.CHANNEL_NAME)).trim();
            }
        }
        return channelCode;
    }

    /**
     * 언어정보를 가져온다.
     * 
     * @param serviceContext {@link IServiceContext}
     * @return 언어정보
     */
    private String getLanguage(IServiceContext serviceContext) {
        if (serviceContext == null) {
            return StringUtils.EMPTY;
        }

        Locale locale = serviceContext.locale();
        if (locale == null) {
            locale = LocaleUtils.toLocale(RuntimeContext.getDefaultLocale());
        }

        return locale.getLanguage();
    }
    
    /**
     * 요청헤더 쿠키 문자열을 가져온다.
     * @param request {@link HttpServletRequest}
     * @return
     */
    private String getCookieString(HttpServletRequest request) {
    	if (request == null) {
            return StringUtils.EMPTY;
        }
    	
    	String cookie = request.getHeader("cookie");
    	return cookie;
    }
}
