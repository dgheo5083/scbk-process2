package com.scbank.process.api.fw.channel.device.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.device.IDeviceManager;

import lombok.Setter;

/**
 * <pre>
 * 디바이스(PC, MOBILE 등) 정보를 관리하는 기본 구현체입니다.
 * 
 * - 프레임워크 내에서 {@link IDeviceManager} 역할을 수행합니다.
 * - 디바이스 리스트를 저장하고 조회할 수 있습니다.
 * - 동시성 문제를 고려하여 {@link CopyOnWriteArrayList}를 사용합니다.
 * </pre>
 *
 * <p>
 * <b>주요 기능:</b>
 * </p>
 * <ul>
 * <li>디바이스 목록 추가 및 설정</li>
 * <li>디바이스 목록 조회 (초기화 포함)</li>
 * </ul>
 *
 * @see IDeviceManager
 * @see IDevice
 * 
 * @version 1.0
 * @since 2022. 1. 12.
 * @author sungdon.choi
 */
public class DefaultDeviceManager implements IDeviceManager {

    /**
     * 디바이스 목록
     * (초기화되지 않은 경우 {@link CopyOnWriteArrayList}로 초기화됨)
     */
    @Setter
    private List<IDevice> deviceList;

    /**
     * 현재 등록된 디바이스 리스트를 반환합니다.
     * 
     * 디바이스 리스트가 비어있거나 초기화되지 않은 경우
     * 내부적으로 {@link CopyOnWriteArrayList}로 초기화합니다.
     *
     * @return 등록된 디바이스 리스트
     */
    @Override
    public List<IDevice> getDeviceList() {
        if (this.deviceList == null) {
            this.deviceList = new CopyOnWriteArrayList<>();
        }
        return this.deviceList;
    }
}
