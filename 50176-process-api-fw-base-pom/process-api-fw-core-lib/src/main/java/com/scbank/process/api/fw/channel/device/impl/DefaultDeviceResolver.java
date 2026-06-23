package com.scbank.process.api.fw.channel.device.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

import com.scbank.process.api.fw.channel.device.DeviceUtils;
import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.device.IDeviceManager;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.core.utils.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 프레임워크 기본 디바이스 정보 획득 처리자
 * 프레임워크 서비스 요청 User-Agent 헤더 값을 이용하여 서비스요청 디바이스정보를 획득한다.
 * </pre>
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 2022. 1. 10.
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultDeviceResolver implements IDeviceResolver {

    public static final String DEFAULT_PARAMETER_NAME = "device";
    /**
     * 프레임워크 디바이스 매니저
     */
    private final IDeviceManager deviceManager;

    /**
     * <pre>
     * 디바이스 강제설정 파라미터명
     * 프레임워크 서비스 요청 시 지정된 파라미터명으로 디바이스 ID 값이 오는경우
     * User-Agent 헤더 값을 무시하고 파라미터값을 우선하여 디바이스정보를 검색 및 획득한다.
     * </pre>
     */
    @Setter
    private String parameterName = DEFAULT_PARAMETER_NAME;

    @Override
    public IDevice resolve(HttpServletRequest request) {
        Assert.notNull(this.deviceManager, "Framework DeviceManager is required");

        IDevice selected = null;
        // 기본 디바이스 정보로 설정
        IDevice defaultDevice = this.deviceManager.getDefaultDevice();

        if (StringUtils.isNotEmpty(request.getParameter(parameterName))) {
            String deviceId = StringUtils.defaultString(request.getParameter(parameterName));

            IDevice findDevice = this.deviceManager.find(deviceId);
            if (findDevice != null) {
                log.debug("Parameter Set Device selected [{}]", findDevice.getId());
                return findDevice;
            }
        }

        String userAgent = StringUtils
                .defaultString(StringUtils.getLetterOrDigitOtherSpace(request.getHeader("User-Agent")))
                .trim();
        log.debug("Request User-Agent [{}]", userAgent);

        if (StringUtils.isEmpty(userAgent)) {
            log.debug("Request User-Agent is Null Or Empty. Default Device resolved");
            return defaultDevice;
        }

        List<IDevice> deviceList = Collections.unmodifiableList(
                this.deviceManager.getDeviceList().stream().sorted().toList());
        IDevice device;
        for (IDevice d : deviceList) {
            device = d;

            String regExString = StringUtils.defaultIfEmpty(device.getRegEx(), "");
            log.debug("Device RegEx [{}][{}]", device.getId(), regExString);

            String[] regExArray = regExString.split("\\|");
            for (String regEx : regExArray) {
                log.debug("Device regEx [{}]", regEx);
                // 요청헤더 User-Agent 정규식 매칭 확인하여 디바이스 정보를 확정한다.
                if (userAgent.matches(regEx)) {
                    selected = device;

                    log.debug("Device selected [{}]", selected.getId());
                    break;
                }
            }

            if (selected != null) {
                break;
            }
        }

        if (selected == null) {
            log.debug("selected device is null resolve Default Device");
            selected = defaultDevice;
        }

        DeviceUtils.setCurrentDevice(request, selected);
        return selected;
    }
}
