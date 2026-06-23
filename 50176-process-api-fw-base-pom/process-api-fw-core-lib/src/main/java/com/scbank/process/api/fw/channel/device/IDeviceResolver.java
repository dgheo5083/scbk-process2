package com.scbank.process.api.fw.channel.device;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 프레임워크 서비스 요청 디바이스 정보 확인
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 2022. 1. 4.
 */
public interface IDeviceResolver {

    String BEAN_ID = "deviceResolver";

    default void setDeviceManager(IDeviceManager deviceManager) {

    }

    /**
     * 프레임워크 서비스 요청 디바이스 정보를 가져온다.
     *
     * @param request {@link HttpServletRequest}
     * @return 프레임워크 서비스 요청 디바이스 정보
     */
    default IDevice resolve(HttpServletRequest request) {
        return null;
    }
}
