package com.scbank.process.api.svc.shared.components.device.enums;

import java.util.Arrays;

/**
 * 단말기 기정 서비스 상태
 * 
 * 코드값 기준:
 * 0: 미등록
 * 1: 등록 + 다른 단말 미허용
 * 2: 등록 + 다른 단말 허용
 */
public enum DeviceAuthStatus {

    // 오류 상태
    ERROR("-1"),
    // 단말기 지정 서비스 미등록
    NOT_REGISTERED("0"),
    // 단말기 지정 서비스 등록 + 다른 단말 미허용
    REGISTERED_BLOCK_OTHER("1"),
    // 단말기 지정 서비스 등록 + 다른 단말 허용
    REGISTERED_ALLOW_OTHER("2");

    private final String code;

    DeviceAuthStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * 외부 전달 문자열 값을 enum으로 변환
     * - null 또는 정의되지 않은 값은 NOT_REGISTERED로 처리
     * 
     * @param value
     * @return
     */
    public static DeviceAuthStatus From(String value) {
        if (value == null)
            return NOT_REGISTERED;

        return Arrays.stream(values()).filter(v -> v.code.equals(value)).findFirst().orElse(NOT_REGISTERED);
    }

    /**
     * 서비스 등록 여부
     * (1, 2는 등록 상태로 간주)
     * 
     * @return
     */
    public boolean isRegistered() {
        return this != NOT_REGISTERED;
    }

    /**
     * 다른 단말 허용 여부
     * 
     * @return
     */
    public boolean isAllowOtherDevice() {
        return this == REGISTERED_ALLOW_OTHER;
    }

}
