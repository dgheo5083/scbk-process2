package com.scbank.process.api.svc.common.components;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Res;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.dto.VerificationGetFindUserIdDto;
import com.scbank.process.api.svc.common.components.dto.VerificationGetTelNoDto;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.dao.DeviceAuthUserDao;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "인증 - 추가인증 컴포넌트")
public class VerificationAdditionalComponent {

    private final HostClient hostClient;

    private final ISessionContextManager sessionManager;

    private final IpinsideComponent ipinside;

    private final DeviceAuthUserDao deviceAuthUserDao;

    /**
     * Ipinside 모듈
     */
    private final IpinsideComponent ipinsideComponent;

    /* ASIS : SCBKAddAuthMdm.doTag */
    /**
     * 추가인증종류 ( default:A:B, A:간편인증, B:SMS명의인증, D:ARS인증, F:해외출국여부 조회 )
     * 
     * @param additionalType
     * @return
     */
    @ComponentOperation(name = "추가인증 가능대상목록 조회")
    public String getAdditionalType(String additionalType, String transType) {

        additionalType = StringUtils.defaultIfBlank(additionalType, "A:B");

        // 1. 앱이 아닐경우 간편인증 제외
        if (!PRCSharedUtils.isSB()) {
            additionalType = additionalType.replace("A", "");
        }

        // 2. 해외출국사실 여부 체크
        if (additionalType.indexOf("F") > -1) {
            String abraodYn = ipinside.getAbroadYn();

            if (!"Y".equals(abraodYn)) {
                // 해외가 아니기에 해외출국인증 제외!
                additionalType = additionalType.replace("F", "");

            }
        }

        return additionalType;
    }

    /**
     * 비밀번호 5회 오류 사용자가 사용자ID찾기를 접근하는 경우
     * 고객정보조회 전문 ( TI1IBK01_H128 ) 에서 에러가 발생하기 때문에 업무에서 세션에 올려준 전화번호로 처리 한다.
     * 
     * @return
     */
    @ComponentOperation(name = "추가인증 사용자ID찾기 전화번호 조회")
    public VerificationGetFindUserIdDto getFindUserIdInfo() {

        VerificationGetFindUserIdDto info = new VerificationGetFindUserIdDto();

        String findWasTranNo = sessionManager.getGlobalValue("FIND_USER_ID_TRCD", String.class);
        String findUserIdTel1 = sessionManager.getGlobalValue("FIND_USER_ID_TEL1", String.class);
        String findUserIdTel2 = sessionManager.getGlobalValue("FIND_USER_ID_TEL2", String.class);
        String findUserIdTel3 = sessionManager.getGlobalValue("FIND_USER_ID_TEL3", String.class);

        if (StringUtils.isNotBlank(findUserIdTel1) && StringUtils.isNotBlank(findUserIdTel2)
                && StringUtils.isNotBlank(findUserIdTel3)) {

            info.setWasTranNo(findWasTranNo);

            info.setFindUserIdYn("Y");
            info.setWasTranNo(findWasTranNo);
            info.setPhoneNo(findUserIdTel1 + findUserIdTel2 + findUserIdTel3);
            info.setMaskPhoneNo(findUserIdTel1 + "-" + findUserIdTel2 + "-****");
            info.setPhoneNo1(findUserIdTel1);
            info.setPhoneNo2(findUserIdTel2);
            info.setPhoneNo3(findUserIdTel3);
        }

        return info;
    }

    @ComponentOperation(name = "비로그인 전화번호 조회")
    public HashMap<String, String> getGuestUserInfo() {

        HashMap<String, String> guestUserInfo = new HashMap<String, String>();

        String guestUserTrCd = sessionManager.getGlobalValue("GUEST_USER_TRCD", String.class);
        String guestUserTel1 = sessionManager.getGlobalValue("GUEST_USER_TEL1", String.class);
        String guestUserTel2 = sessionManager.getGlobalValue("GUEST_USER_TEL2", String.class);
        String guestUserTel3 = sessionManager.getGlobalValue("GUEST_USER_TEL3", String.class);

        if (StringUtils.isNotBlank(guestUserTel1) && StringUtils.isNotBlank(guestUserTel2)
                && StringUtils.isNotBlank(guestUserTel3)) {

            guestUserInfo.put("trCd", guestUserTrCd);
            guestUserInfo.put("guestUserYn", "Y");
            guestUserInfo.put("phoneNo", guestUserTel1 + guestUserTel2 + guestUserTel3);
            guestUserInfo.put("maskPhoneNo", guestUserTel1 + "-" + guestUserTel2 + "-****");
            guestUserInfo.put("phoneNo1", guestUserTel1);
            guestUserInfo.put("phoneNo2", guestUserTel2);
            guestUserInfo.put("phoneNo3", guestUserTel3);
        } else {
            guestUserInfo.put("guestUserYn", "N");
        }

        return guestUserInfo;
    }

    @ComponentOperation(name = "추가인증 전화번호 조회")
    public VerificationGetTelNoDto getTelNo() {

        VerificationGetFindUserIdDto findUserIdInfo = this.getFindUserIdInfo();
        VerificationGetTelNoDto telNoInfo = new VerificationGetTelNoDto();
        HashMap<String, String> guestUserInfo = this.getGuestUserInfo();

        if ("Y".equals(findUserIdInfo.getFindUserIdYn())) {
            telNoInfo.setWasTranNo(findUserIdInfo.getWasTranNo());
            telNoInfo.setPhoneNo(findUserIdInfo.getPhoneNo());
            telNoInfo.setMaskPhoneNo(findUserIdInfo.getMaskPhoneNo());
            telNoInfo.setPhoneNo1(findUserIdInfo.getPhoneNo1());
            telNoInfo.setPhoneNo2(findUserIdInfo.getPhoneNo2());
            telNoInfo.setPhoneNo3(findUserIdInfo.getPhoneNo3());

        } else if ("Y".equals(guestUserInfo.get("guestUserYn"))) {
            telNoInfo.setWasTranNo(guestUserInfo.get("trCd"));
            telNoInfo.setPhoneNo(guestUserInfo.get("phoneNo"));
            telNoInfo.setMaskPhoneNo(guestUserInfo.get("maskPhoneNo"));
            telNoInfo.setPhoneNo1(guestUserInfo.get("phoneNo1"));
            telNoInfo.setPhoneNo2(guestUserInfo.get("phoneNo2"));
            telNoInfo.setPhoneNo3(guestUserInfo.get("phoneNo3"));
        } else {

            String userId = sessionManager.getGlobalValue("UserID", String.class);
            String tsPassword = sessionManager.getGlobalValue("TSPassword", String.class);
            String perBusNo = sessionManager.getGlobalValue("PerBusNo", String.class);

            if (sessionManager.isLogin() || (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(tsPassword)
                    && StringUtils.isNotEmpty(perBusNo))) {

                OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK01_H920");

                hostCfg.setImsTranCd("TI1IBK01");
                hostCfg.setInClassCd("H920");
                hostCfg.setSvcCd("920");

                CbIbk01H92000Req inputDto = new CbIbk01H92000Req();

                if (!sessionManager.isLogin()) {
                    inputDto.setUserID("FIRST999");
                    inputDto.setTSPassword("111111");
                    inputDto.setBKGuBun("2");
                    inputDto.setYISALES("Y");
                    inputDto.setInJuMinNo(perBusNo);
                } else {
                    inputDto.setUserID(SessionUtils.getSessionValue("UserID"));
                    inputDto.setTSPassword(SessionUtils.getSessionValue("TSPassword"));
                    inputDto.setBKGuBun("1");
                }

                OltpResponse<CbIbk01H92000Res> response = this.hostClient.sendOltp(hostCfg, inputDto,
                        CbIbk01H92000Res.class);

                OltpCommon oltpCommon = response.getHeader().getOltpCommon();
                String trCd = oltpCommon.getTrCd();

                CbIbk01H92000Res outputDto = response.getResponse();

                telNoInfo.setWasTranNo(trCd);

                telNoInfo.setPhoneNo(outputDto.getHandPhone1() + outputDto.getHandPhone2() + outputDto.getHandPhone3());
                telNoInfo.setMaskPhoneNo(outputDto.getHandPhone1() + "-" + outputDto.getHandPhone2() + "-****");
                telNoInfo.setPhoneNo1(outputDto.getHandPhone1());
                telNoInfo.setPhoneNo2(outputDto.getHandPhone2());
                telNoInfo.setPhoneNo3(outputDto.getHandPhone3());

                telNoInfo.setHomeTelNo(outputDto.getHomeTele1() + outputDto.getHomeTele2() + outputDto.getHomeTele3());
                telNoInfo.setMaskHomeTelNo(outputDto.getHomeTele1() + "-" + outputDto.getHomeTele2() + "-****");
                telNoInfo.setHomeTelNo1(outputDto.getHomeTele1());
                telNoInfo.setHomeTelNo2(outputDto.getHomeTele2());
                telNoInfo.setHomeTelNo3(outputDto.getHomeTele3());

                telNoInfo.setJobTelNo(outputDto.getJobTele1() + outputDto.getJobTele2() + outputDto.getJobTele3());
                telNoInfo.setMaskJobTelNo(outputDto.getJobTele1() + "-" + outputDto.getJobTele2() + "-****");
                telNoInfo.setJobTelNo1(outputDto.getJobTele1());
                telNoInfo.setJobTelNo2(outputDto.getJobTele2());
                telNoInfo.setJobTelNo3(outputDto.getJobTele3());
            }

        }

        return telNoInfo;

    }

}
