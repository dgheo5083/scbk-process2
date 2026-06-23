package com.scbank.process.api.fw.channel.device;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 프레임워크 디바이스 정보 유틸리티 클래스
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 2022. 1. 10.
 */
public class DeviceUtils {

    public static final String CURRENT_DEVICE_ATTR = "ib.current.device";

    /**
     * 요청 채널 디바이스 정보를 세션에서 가져온다.
     *
     * @param request {@link HttpServletRequest}
     * @return 현재 요청 채널 디바이스 객체
     */
    public static IDevice getCurrentDevice(HttpServletRequest request) {
        return (IDevice) request.getAttribute(CURRENT_DEVICE_ATTR);
    }

    /**
     * 요청 채널 디바이스 정보를 세션에 저장한다.
     *
     * @param request       {@link HttpServletRequest}
     * @param currentDevice 현재 요청 채널 디바이스 객체
     */
    public static void setCurrentDevice(HttpServletRequest request, IDevice currentDevice) {
        request.setAttribute(CURRENT_DEVICE_ATTR, currentDevice);
    }
}
