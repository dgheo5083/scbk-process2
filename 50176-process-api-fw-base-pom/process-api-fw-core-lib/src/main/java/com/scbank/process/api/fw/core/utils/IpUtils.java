package com.scbank.process.api.fw.core.utils;

import java.net.InetAddress;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sungdon.choi
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpUtils {

    static final String PROP_NAME_POD_IP = "POD_IP";

    /**
     * 
     * @param ip
     * @return
     */
    public static boolean validIp(String ip) {
        return ip != null && ip.matches("^(25[0-5]|2[04]\\d|[0-1]?\\d?\\d(\\.|$)){4}$");
    }

    /**
     * 요청 클라이언트 IP를 가져온다.
     * @return
     */
    public static String getClientIp() {
    	ServletRequestAttributes attrs = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
    	if (attrs == null) {
    		return null;
    	}
		HttpServletRequest request = attrs.getRequest();
        return getClientIp(request);
    }
    
    /**
     * 요청 클라이언트 IP를 가져온다.
     * 
     * @param request {@link HttpServletRequest}
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forward-For");
        if (!StringUtils.hasText(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 현재 머신의 IP 주소를 가져온다.
     * 
     * @return 획득한 로컬 IP 주소
     */
    public static String getLocalIp() {
        try {
            String ip = System.getProperty(PROP_NAME_POD_IP);
            if (!StringUtils.hasText(ip)) {
                InetAddress localhost = InetAddress.getLocalHost();
                ip = localhost.getHostAddress();
            }
            return ip;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "127.0.0.1";
        }
    }

    /**
     * 현재 머신의 호스트명을 가져온다.
     * 
     * @return 현재 머신의 호스트명
     */
    public static String getHostName() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostName();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "unknwon";
        }
    }
}
