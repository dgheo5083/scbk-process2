package com.scbank.process.api.fw.channel.device;

import java.util.Collections;
import java.util.List;
import org.springframework.util.CollectionUtils;

/**
 * <pre>
 * 프레임워크 디바이스 매니저 인터페이스
 * 프레임워크 서비스 요청시 요청 클라이언트의 디바이스 정보를 판단하기 위해 디바이스 목록 및 디바이스 정보 확정을 위한 resolver 인터페이스를 관리한다.
 * </pre>
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 2022. 1. 4.
 */
public interface IDeviceManager {

    String BEAN_ID = "ib.core.IDeviceManager";

    /**
     * 디바이스 매니저에 등록된 디바이스 목록을 가져온다.
     *
     * @return
     */
    default List<IDevice> getDeviceList() {
        return Collections.emptyList();
    }

    /**
     * 기본 디바이스 정보를 가져온다.
     *
     * @return
     */
    default IDevice getDefaultDevice() {
        List<IDevice> deviceList = this.getDeviceList();
        IDevice defaultDevice = null;
        if (deviceList != null && !deviceList.isEmpty()) {
            defaultDevice = deviceList.stream().filter(IDevice::isDefaultDevice)
                    .findFirst()
                    .orElse(null);
        }

        return defaultDevice;
    }

    /**
     * 인자로 받은 디바이스 ID로 디바이스 객체를 가져온다.
     *
     * @param id 디바이스 ID
     * @return 디바이스 ID로 검색된 디바이스 객체
     */
    default IDevice find(String id) {
        List<IDevice> deviceList = this.getDeviceList();
        return deviceList.stream()
                .filter(device -> id.equals(device.getId()))
                .findAny()
                .orElse(null);
    }

    /**
     * 디바이스정보를 등록한다.
     *
     * @param device 디바이스 매니저에 등록할 디바이스정보
     */
    default void add(IDevice device) {
        List<IDevice> deviceList = this.getDeviceList();
        deviceList.add(device);
    }

    default void add(List<IDevice> deviceList) {
        List<IDevice> original = this.getDeviceList();
        original.addAll(deviceList);
    }

    /**
     * 디바이스정보를 삭제한다.
     *
     * @param device 디바이스 매니저에서 삭제할 디바이스정보
     */
    default void remove(IDevice device) {
        if (device == null) {
            return;
        }

        List<IDevice> deviceList = this.getDeviceList();
        if (CollectionUtils.isEmpty(deviceList)) {
            return;
        }

        deviceList.remove(device);
    }

    /**
     * <pre>
     * 디바이스 정보를 삭제한다.
     * 프레임워크 디바이스 매니저에 등록된 디바이스 ID로 디바이스 정보를 검색하여 삭제한다.
     * </pre>
     *
     * @param id 디바이스 ID
     */
    default void remove(String id) {
        IDevice find = this.find(id);
        if (find == null) {
            return;
        }

        this.remove(find);
    }
}