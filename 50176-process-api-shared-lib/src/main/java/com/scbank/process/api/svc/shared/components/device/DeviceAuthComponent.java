package com.scbank.process.api.svc.shared.components.device;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.device.dto.DeviceAuthInfo;
import com.scbank.process.api.svc.shared.components.device.enums.DeviceAuthStatus;
import com.scbank.process.api.svc.shared.dao.DeviceAuthUserDao;
import com.scbank.process.api.svc.shared.dao.dto.DeviceAuthUserParameter;
import com.scbank.process.api.svc.shared.dao.dto.DeviceAuthUserResult;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class DeviceAuthComponent {

    private final DeviceAuthUserDao deviceAuthUserDao;

    /**
     * 단말기지정 서비스에 등록된 단말수를 가져온다.
     * 
     * @param userId
     * @return
     * @throws PRCServiceException
     */
    @ComponentOperation(name = "getDeviceAuthCount", description = "단말기지정 서비스에 등록된 단말수 조회")
    public Integer getDeviceAuthCount(String userId) throws PRCServiceException {

        log.debug("@@@ 단말기지정 서비스에 등록된 단말수 조회 시작...... mode {}", RuntimeContext.getRunMode());

        Integer deviceCount = 0;

        DeviceAuthUserParameter params = DeviceAuthUserParameter.builder().serviceNo("1016020500").userId(userId)
                .build();

        if (!RunMode.LOCAL.equals(RuntimeContext.getRunMode())) {
            deviceCount = deviceAuthUserDao.selectDeviceAuthCount(params);
        }

        log.debug("@@@ 단밀기지정 서비스에 등록된 단말수 조회 종료......deviceCount[" + deviceCount + "]");

        return deviceCount;
    }

    /**
     * 단말기 지정 서비스 상태 및 다른 단말 접속 여부조회
     * 
     * @param pcFixValue (0, 1, 2)
     * @return
     * @throws PRCServiceException
     */
    @ComponentOperation(name = "getDeviceAuthInfoNOtherYn", description = "단말기지정서비스&다른단말기접속여부")
    public DeviceAuthInfo getDeviceAuthInfoNOtherYn(String pcFixValue) throws PRCServiceException {
        return getDeviceAuthInfoNOtherYn(pcFixValue, SessionUtils.getSessionValue("UserID"));
    }

    @ComponentOperation(name = "getDeviceAuthInfoNOtherYn", description = "단말기지정서비스&다른단말기접속여부")
    public DeviceAuthInfo getDeviceAuthInfoNOtherYn(String pcFixValue, String userId) throws PRCServiceException {

        pcFixValue = StringUtils.defaultString(pcFixValue);

        DeviceAuthInfo result = new DeviceAuthInfo();

        DeviceAuthStatus status = DeviceAuthStatus.From(pcFixValue);

        int count = this.getDeviceAuthCount(userId);

        status = resolveStatus(status, count);

        String otherDeviceYn = this.isOtherDeviceCheckRequired(status) ? this.getOtherDeviceYn(userId) : "N";

        result.setDeviceAuthValue(status.getCode());
        result.setOtherDeviceYn(otherDeviceYn);

        log.debug("@@@ ipinside getDeviceAuthInfoNOtherYn result[" + result.toString() + "]");

        return result;
    }

    /**
     * 단말기지정 서비스 가입 고객에 한해서 미지정 단말 거래 가능여부
     * 
     * @param userId
     * @return
     * @throws PRCServiceException
     */
    private String getOtherDeviceYn(String userId) throws PRCServiceException {

        log.debug("@@@ 단말기지정 서비스 가입 고객에 한해서 미지정 단말 거래 가능여부 시작 userId [" + userId + "]");

        String otherDeviceYn = "N";

        DeviceAuthUserParameter params = DeviceAuthUserParameter.builder().userId(userId).build();

        if (!RunMode.LOCAL.equals(RuntimeContext.getRunMode())) {
            DeviceAuthUserResult result = deviceAuthUserDao.selectDeviceAuthUserInfo(params);

            if (result != null) {
                otherDeviceYn = StringUtils.defaultIfBlank(result.getOtherpcYes(), "N").toUpperCase();
            }
        }

        log.debug("@@@ 단말기지정 서비스 가입 고객에 한해서 미지정 단말 거래 가능여부 종료 [" + otherDeviceYn + "]");

        return otherDeviceYn;
    }

    /**
     * 상태값 보정 로직
     * - 미등록 상태(0) 인데 실제 등록된 단말이 있으면 -> 등록 상태로 변경
     * - 특정 상태(기존 -1)는 접속 오류 케이스로 간주하여 미등록으로 초기화
     * 
     * @param status
     * @param count
     * @return
     */
    private DeviceAuthStatus resolveStatus(DeviceAuthStatus status, int count) {

        // 오류 상태는 미등록으로 초기화
        if (status == DeviceAuthStatus.ERROR) {
            return DeviceAuthStatus.NOT_REGISTERED;
        }

        // 미등록인데 실제 등록 단말이 있으면 등록 상태로 보정
        if (status == DeviceAuthStatus.NOT_REGISTERED && count > 0) {
            return DeviceAuthStatus.REGISTERED_BLOCK_OTHER;
        }

        // 그외 상태는 그대로 유지
        return status;
    }

    /**
     * 다른 단말 접속 여부 조회 필요여부
     * - 서비스 미등록(0) -> 조회 불필요
     * - 서비스 등록 상태(1, 2) -> 조회 필요
     * 
     * @param status
     * @return
     */
    private boolean isOtherDeviceCheckRequired(DeviceAuthStatus status) {
        return status.isRegistered();
    }
}
