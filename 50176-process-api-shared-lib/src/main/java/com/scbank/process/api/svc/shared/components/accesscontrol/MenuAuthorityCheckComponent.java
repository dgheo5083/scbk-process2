package com.scbank.process.api.svc.shared.components.accesscontrol;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.accesscontrol.dto.MenuAuthorityCheckRequest;
import com.scbank.process.api.svc.shared.components.accesscontrol.dto.ServiceTimeCheckRequest;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 메뉴 접근제어 체크 컴포넌트
 */
@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "메뉴 권한 체크 컴포넌트", author = "sungdon.choi")
public class MenuAuthorityCheckComponent {

    /**
     * 세션 매니저 컴포넌트
     */
    private final ISessionContextManager sessionContextManager;

    /**
     * 서비스 이용시간 체크 컴포넌트
     */
    private final ServiceTimeCheckComponent serviceTimeCheckComponent;

    @ComponentOperation(name = "메뉴 접근제어 체크", author = "sungdon.choi")
    public void checkAuthority(MenuAuthorityCheckRequest request) {
        IServiceContext sc = ServiceContextHolder.getContext();
        String requestURI = sc == null ? StringUtils.EMPTY : sc.request().getRequestURI();
        String menuId = StringUtils.defaultIfEmpty(request.getMenuId(), "");
        String forceCheckCode = StringUtils.defaultIfEmpty(request.getForceCheckCode(), "");
        // 접근제어 타입
        String acType = StringUtils.defaultIfEmpty(request.getAcType(), "");
        List<String> acTypeList = Arrays.asList(acType.split("\\,"));

        log.debug("### {}", serviceTimeCheckComponent.isCheckIgnoreMenuId(request.getMenuId()));
        log.debug("### {}", serviceTimeCheckComponent.isCheckIgnoreRequestUri(requestURI));

        // 서비스 이용시간 체크
        if (!serviceTimeCheckComponent.isCheckIgnoreMenuId(request.getMenuId())
                || !serviceTimeCheckComponent.isCheckIgnoreRequestUri(requestURI)) {
            serviceTimeCheckComponent.checkServiceTime(
                    ServiceTimeCheckRequest.builder().type("menu").code(menuId).forceCheckCode(forceCheckCode).build());
        }
        

        boolean isLogined = this.sessionContextManager.isLogin();
        
        log.debug("### menuId [{}] : ", menuId);
        log.debug("### requestURI [{}] : ", requestURI);
        if(acTypeList != null) {
        	log.debug("### acTypeList toString [{}] : ", acTypeList.toString());
        	log.debug("### acTypeList.contains(\"00\") && !isLogined [{}] : ", acTypeList.contains("00") && !isLogined);
        }
        log.debug("### acTypeList.contains(\"00\") [{}] : ", acTypeList.contains("00"));
        log.debug("### isLogined [{}] : ", isLogined);
        
        // **************************************
        // 로그인여부 체크
        // **************************************
        if (acTypeList.contains("00") && !isLogined) {
            PRCServiceException ex = new PRCServiceException("PRCLGN0015", "로그인이 필요해요. 로그인을 진행할게요.");
//            ex.setNextPage("LGNADV000");

            throw ex;
        }

        // **************************************
        // 조회사용자가 이체성거래 접근 체크
        // **************************************
        if (acTypeList.contains("02") && isLogined) {
            String safeCardState = StringUtils
                    .defaultIfEmpty(this.sessionContextManager.getLoginValue("SafeCardState", String.class), "").trim();
            if ("0".equals(safeCardState)) {
                throw new PRCServiceException("PRCCMM0049", "조회 사용자는 사용할 수 없는 서비스입니다.");
            }
        }

        // **************************************
        // ID/PWD 로그인 사용자 상품 신규 메뉴 접근 통제
        // **************************************
        if (acTypeList.contains("05") && isLogined) {
            String deviceCertExistYn = StringUtils
                    .defaultIfEmpty(this.sessionContextManager.getGlobalValue("deviceCertExistYn", String.class), "")
                    .trim();
            String connectType = StringUtils
                    .defaultIfEmpty(this.sessionContextManager.getLoginValue("ConnectType", String.class), "").trim();

            if ("N".equals(deviceCertExistYn) && "2".equals(connectType)) {
                throw new PRCServiceException("PRCCMM0052", "인증서 로그인이 필요한 메뉴입니다.<br/>로그인 페이지로 이동하시겠습니까?");
            }
        }

        // **************************************
        // 이체성거래 단말지정서비스 체크(acCheckType = 04)
        // **************************************
        if (acTypeList.contains("04")) {
            String PcFixValue = "";// PC 지정 서비스 신청 상태 ( 0: 미신청 or 미지정PC , 2: 신청PC )
            String CustGubun = "";// 1 개인,2 기업
            String OtherPCYes = "";// PC미지정 단말 사용 가능여부. Y/N

            if (isLogined) {
                PcFixValue = StringUtils
                        .defaultIfEmpty(this.sessionContextManager.getLoginValue("PcFixValue", String.class), "");
                CustGubun = StringUtils
                        .defaultIfEmpty(this.sessionContextManager.getLoginValue("CustGubun", String.class), "");
                OtherPCYes = StringUtils
                        .defaultIfEmpty(this.sessionContextManager.getLoginValue("OtherPCYes", String.class), "");
            } else {
                PcFixValue = StringUtils
                        .defaultIfEmpty(this.sessionContextManager.getGlobalValue("PcFixValue", String.class), "");
                CustGubun = StringUtils
                        .defaultIfEmpty(this.sessionContextManager.getGlobalValue("CustGubun", String.class), "");
                OtherPCYes = StringUtils
                        .defaultIfEmpty(this.sessionContextManager.getGlobalValue("OtherPCYes", String.class), "");
            }

            if ("1".equals(PcFixValue) && !"2".equals(CustGubun) && !"Y".equals(OtherPCYes)) {
                String errorCode = "PRCCMM0045";
                String errorMsg = "단말기 지정 서비스에 가입되어 있어요.<br/>새로운 단말기에서 이용하려면 단말기 지정/해제를 해야 해요.<br/><br/>단말기 지정메뉴로 이동할까요?";
                if (PRCSharedUtils.isIB()) {
                    errorCode = "PRCCMM0046";
                    errorMsg = "단말기 지정 서비스에 가입되어 있어요. 새로운 단말기에서 이용하려면 단말기 지정/해제가 필요해요.<br/>모바일뱅킹 또는 영업점에서 단말기 지정/해제 후 이용해 주세요.";
                }
                PRCServiceException e = new PRCServiceException(errorCode, errorMsg);
                if ("PRCCMM0045".equals(errorCode)) {
                    e.setNextPage("MA3CRTDDD002");
                }
                throw e;
            }
        }

        // **************************************
        // 보안매체 상태(분실,폐기) 체크(acCheckType= 01)
        // 보안매체 필요한 서비스 진입 시 보안매체 비정상일(분실,폐기) 경우 접근 통제
        // **************************************
        if (acTypeList.contains("01") && isLogined) {
            String userTransStateMsg = StringUtils
                    .defaultIfEmpty(this.sessionContextManager.getLoginValue("UserTransStateMsg", String.class), "");
            if (StringUtils.hasText(userTransStateMsg)) {
                throw new PRCServiceException("PRCCMM0048", List.of(userTransStateMsg));
            }

            // 01:재설치, 02:기기변경
            String mOtpDeviceChageType = StringUtils.defaultIfEmpty(
                    this.sessionContextManager.getLoginValue("MOTP_DEVICE_CHANGE_TYPE", String.class), "");
            if ("02".equals(mOtpDeviceChageType) && PRCSharedUtils.isSB()) {
                throw new PRCServiceException("PRCCMM0057",
                        "변경된 기기에 모바일OTP 정보가 없습니다.<br/>편리하고 안전한 거래를 위하여 모바일OTP를 재발급 하시기 바랍니다.");
            }
        }

        // **************************************
        // ID/PWD 로그인 후 서명 필요한 서비스 접근 체크(acCheckType = 03)
        // **************************************
        if (acTypeList.contains("03") && isLogined && !PRCSharedUtils.isIB()) {
            String deviceCertExistYn = StringUtils
                    .defaultIfEmpty(this.sessionContextManager.getGlobalValue("deviceCertExistYn", String.class), "")
                    .trim();
            String finCertExistYn = StringUtils
                    .defaultIfEmpty(this.sessionContextManager.getGlobalValue("finCertExistYn", String.class), "")
                    .trim();
            String connectType = StringUtils
                    .defaultIfEmpty(this.sessionContextManager.getLoginValue("ConnectType", String.class), "").trim();

            if ("N".equals(deviceCertExistYn) && ("N".equals(finCertExistYn) || "E".equals(finCertExistYn))
                    && ("2".equals(connectType))) {
                PRCServiceException e = new PRCServiceException("PRCCMM0050",
                        "거래를 위해 금융인증서 발급이 필요해요.<br/> 인증센터로 이동할까요?");
                e.setNextPage("CRTHOM000");// 인증센터

                throw e;
            }

            // **************************************
            // 카카오 , 토스는 업무페이지를 판단하여 처리하도록 한다.
            // **************************************
            if ("N".equals(deviceCertExistYn) && ("N".equals(finCertExistYn) || "E".equals(finCertExistYn))
                    && ("D".equals(connectType) || "F".equals(connectType))) {
                // 핀테크인증서 연계메뉴 여부
                if (!this.isFintechMenu(menuId)) {
                    PRCServiceException e = new PRCServiceException("PRCCMM0050",
                            "단말기에 저장되어 있는 인증서가 없습니다.<br/>인증센터로 이동하시겠습니까?");
                    e.setNextPage("CRTHOM000");// 인증센터

                    throw e;
                }
            }
        }

        // **************************************
        // START 개인정보 노출자 접근금지 (로그인사용자) 2022.12 정기
        // **************************************
        String YOLRSOTGB = ""; // 개인정보노출등록구분 1:등록 3:미등록
        if (isLogined) {
            YOLRSOTGB = StringUtils.defaultIfEmpty(this.sessionContextManager.getLoginValue("YOLRSOTGB", String.class),
                    StringUtils.defaultString(this.sessionContextManager.getGlobalValue("YOLRSOTGB", String.class)));
        } else {
            YOLRSOTGB = StringUtils.defaultIfEmpty(this.sessionContextManager.getGlobalValue("YOLRSOTGB", String.class),
                    "");
        }

        if ("1".equals(YOLRSOTGB)) {
            List<ICodeItemInfo> custInfoSpTargetMnuList = CodeUtils.getCodes("CSINFOLEAK_BLKMNU");
            if (CollectionUtils.isEmpty(custInfoSpTargetMnuList)) {
                return;
            }

            boolean isTargetMenu = custInfoSpTargetMnuList.stream().filter(Objects::nonNull).map(v -> v.getValue())
                    .anyMatch(v -> v.equals(menuId));
            if (isTargetMenu) {
                throw new PRCServiceException("PRCCMMERRCIL001", "개인정보 노출자는 거래를 진행할 수 없어요.");
            }
        }
        // END 개인정보 노출자 접근금지 (로그인사용자) 2022.12 정기
    }

    /**
     * 핀테크인증서 연계 메뉴인경우를 판단한다.
     * 
     * @param menuId 메뉴ID
     * @return 핀테크인증서 연계메뉴 여부
     */
    private boolean isFintechMenu(String menuId) {
        if (!StringUtils.hasLength(menuId)) {
            return false;
        }

        List<ICodeItemInfo> codeItemList = CodeUtils.getCodes("FINTECH_LINKAGE_MENU");
        if (CollectionUtils.isEmpty(codeItemList)) {
            return false;
        }

        return codeItemList.stream().anyMatch(v -> menuId.equals(v.getKey()));
    }
}
