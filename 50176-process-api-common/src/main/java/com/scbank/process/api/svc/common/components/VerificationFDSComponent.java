package com.scbank.process.api.svc.common.components;

import org.apache.commons.lang3.StringUtils;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.components.ipinside.dto.DFinderApiInfo;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "인증 - FDS 컴포넌트")
public class VerificationFDSComponent {

    private boolean initialized = false;

    private String VFDS_SW_YN = "N"; // properties로 변경필요

    private String RSP_ACTN_METH_CD = "00"; // FDS 응답값 - 01:고객SMS통보, 02:FDS담당자SMS통보, 03:출금정지, 04:2FACT인증
    private String VFDS_RSP_ACTN_METH_CD = "00"; // VFDS 응답값 - 01:고객SMS통보, 02:FDS담당자SMS통보, 03:출금정지, 04:2FACT인증

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    private final IpinsideComponent ipinside;

    @PostConstruct
    public void init() {
        VFDS_SW_YN = PropertiesUtils.getString("VFDS_SW_YN");
        initialized = false;
        log.debug("HDG Debug VerificationFDSComponent init VFDS_SW_YN [{}]", VFDS_SW_YN);
    }

    private void initialized() {

        if (initialized) {
            return;
        }

        initialized = true;

        String userId = SessionUtils.getSessionValue("UserID");

        log.debug("HDG Debug VerificationFDSComponent initialized VFDS_SW_YN >> {}", VFDS_SW_YN);

        if ("Y".equals(VFDS_SW_YN)) {
            log.info("HDG Debug VFDS 조회 시작 #####");

            DFinderApiInfo dFinderInfo = ipinside.dFinderApi(userId, "R");

            if (dFinderInfo != null) {
                VFDS_RSP_ACTN_METH_CD = dFinderInfo.getRspActnMethCd();
                log.debug("HDG Debug VerificationFDSComponent initialized VFDS_RSP_ACTN_METH_CD >> {}",
                        VFDS_RSP_ACTN_METH_CD);
            }
        }
    }

    @ComponentOperation(name = "FDS 상태(03: 거래정지)에 따른 거래정지여부")
    public boolean isWdrawFreeze() {

        initialized();

        boolean isFreeze = false;

        if ("Y".equals(VFDS_SW_YN) && "03".equals(VFDS_RSP_ACTN_METH_CD)) {
            isFreeze = true;
        }

        log.debug("HDG Debug VerificationFDSComponent isWdrawFreeze FDS 상태에 따른 출금정지여부 > " + isFreeze);

        return isFreeze;
    }

    @ComponentOperation(name = "FDS 상태에 따른 거래차단여부")
    public boolean isTranBlock() {

        initialized();

        boolean isBlock = false;

        if ("Y".equals(VFDS_SW_YN) && "04".equals(VFDS_RSP_ACTN_METH_CD)) {
            isBlock = true;
        }

        log.debug("HDG Debug VerificationFDSComponent isTranBlock FDS 상태에 따른 거래차단여부 > " + isBlock);

        return isBlock;

    }

    @ComponentOperation(name = "FDS 상태(06: 출금정지)에 따른 신분증촬영여부")
    public boolean isIDCapture() {

        initialized();

        log.debug("HDG Debug VerificationFDSComponent isIDCapture 진입");

        boolean isCapture = false;

        // FDS 출금정지 체크여부(이체거래(예약, 즉시, 지연이체)에서만 적용)
        String fdsServiceCode = sessionManager.getGlobalValue("FDS_SERVICE_CODE", String.class);
        // 추가인증 비대면실명인증 여부
        String addAuthIdCaptureFlag = sessionManager.getGlobalValue("ADD_AUTH_ID_CAPTURE_FLAG", String.class);

        String idRecogResultYn = StringUtils.defaultIfBlank(SessionUtils.getSessionValue("idRecogResultYn"), "N");
        String idTruthResultYn = StringUtils.defaultIfBlank(SessionUtils.getSessionValue("idTruthResultYn"), "N");
        String faceResultYn = StringUtils.defaultIfBlank(SessionUtils.getSessionValue("faceResultYn"), "N");

        if ("Y".equals(VFDS_SW_YN) && "06".equals(VFDS_RSP_ACTN_METH_CD) && "211".equals(fdsServiceCode)) {

            // 출금정지 상태에서 비대면실명인증 완료일 경우
            if ("Y".equals(addAuthIdCaptureFlag) && "Y".equals(idRecogResultYn) && "Y".equals(idTruthResultYn)
                    && "Y".equals(faceResultYn)) {
                sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
                sessionManager.removeGlobalValue("ADD_AUTH_ID_CAPTURE_FLAG");
                isCapture = false;
            } else {
                isCapture = true;
            }

            sessionManager.removeGlobalValue("idRecogResultYn");
            sessionManager.removeGlobalValue("idTruthResultYn");
            sessionManager.removeGlobalValue("faceResultYn");
            sessionManager.removeGlobalValue("videoResultYn");

        }

        log.debug("HDG Debug VerificationFDSComponent isIDCapture FDS 상태에 따른 신분증촬영여부 > " + isCapture);

        return isCapture;
    }

    @ComponentOperation(name = "FDS 상태코드 반환")
    public String getRspActnMethCd() {
        initialized();

        return VFDS_RSP_ACTN_METH_CD;
    }

}
