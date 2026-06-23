package com.scbank.process.api.svc.common.components;

import java.util.List;
import java.util.Map;

import com.interezen.loader.QLoader;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "인증 - 보안매체 컴포넌트")
public class VerificationTokensComponent {

    private final double ADDITIONAL_LIMIT_AMOUNT = 1000000; // 금융사기방지 :: 추가인증 Limit 금액.
    private final double ADDITIONAL_LIMIT_AMOUNT_ABROAD = 3000000; // 금융사기방지 :: 추가인증 해외 Limit 금액.

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    private final IpinsideComponent ipinsideService;

    @ComponentOperation(name = "내계좌송금 여부 체크")
    public boolean isMyAcctTran(String tranType) {

        boolean isMyAcct = false;

        log.debug("isMyAcctTran tranType > {}", tranType);

        if (tranType.indexOf("TRAN_") > -1) {

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> tranInfoList = sessionManager.getGlobalValue("addCertTranInfoList",
                    List.class);

            log.debug("isMyAcctTran tranInfoList > {}", tranInfoList);

            if (tranInfoList != null && tranInfoList.size() == 1) {
                Map<String, Object> firstInfo = tranInfoList.get(0);
                String jgCode = StringUtils.defaultString((String) firstInfo.get("JgCode")); // 내계좌여부 ( M : 내계좌 )

                if ("M".equals(jgCode)) {
                    isMyAcct = true;
                }
            }

        }

        return isMyAcct;
    }

    @ComponentOperation(name = "추가인증 사용여부")
    public boolean isAdditional(String additionalYn, String tranType, double todayTranAmt) {

        String connectType = SessionUtils.getSessionValue("ConnectType");
        String pcFixValue = SessionUtils.getSessionValue("PcFixValue");
        String safeCardKind = SessionUtils.getSessionValue("SafeCardKind");

        String resultYn = ""; // 추가인증 사용여부 (Y/N)

        // 추가인증여부를 지정하지 않을 경우
        if (StringUtils.isEmpty(additionalYn)) {

            // 거래타입을 지정한 경우 (거래코드가 없으면 추가인증 PASS)
            if (StringUtils.isNotEmpty(tranType)) {

                // 인증센터는 추가인증 필수
                if (tranType.indexOf("CERT_") > -1) {
                    log.info("##### 인증센터 업무는 추가인증 필수 ####");
                    resultYn = "Y";
                }

                // PC지정서비스 체크
                if (!this.isPcFixService()) {

                    boolean isPreCheck = false;

                    // 이체거래일 경우
                    if (tranType.indexOf("TRAN_") > -1) {

                        // 해외IP일 경우
                        if ("Y".equals(this.getAbroadYn())) { // 해외출금
                            if (todayTranAmt >= ADDITIONAL_LIMIT_AMOUNT_ABROAD) {
                                isPreCheck = true;
                            }
                        } else { // 국내출금
                            if (todayTranAmt >= ADDITIONAL_LIMIT_AMOUNT) {
                                isPreCheck = true;
                            }
                        }

                        // 디지털인증서(9), 금융인증서(생체인증)(C) 추가인증 Skip
                        if (isPreCheck && ("9".equals(connectType) || "C".equals(connectType))) {
                            isPreCheck = false;
                            resultYn = "N";
                            log.info("로그인 수단에 의한 추가인증 SKIP > " + ("9".equals(connectType) ? "디지털인증" : "금융인증서(생체인증)"));
                        } else if (isPreCheck) {
                            resultYn = "Y";
                        }

                    } else {
                        // 이체성 거래가 아닌 경우, 기본체크 실행
                        isPreCheck = true;
                    }

                    if (isPreCheck && ("1".equals(pcFixValue) || "1".equals(safeCardKind))) {
                        resultYn = "Y";
                    }

                } else {
                    resultYn = "N";
                }

            }
        } else {
            // 추가인증이 노출여부에 따름
            // additionalYn (Y: 항상 노출, N: 항상 미노출)
            resultYn = additionalYn;
        }

        return "Y".equals(resultYn) ? true : false;

    }

    @ComponentOperation(name = "PC지정서비스 가입여부")
    public boolean isPcFixService() {
        String pcFixValue = SessionUtils.getSessionValue("PcFixValue", String.class);
        String otherPCYes = SessionUtils.getSessionValue("OtherPCYes", String.class);

        boolean isPcFix = false;

        log.debug("HDG Debug isPcFixService pcFixValue [{}], otherPCYes [{}]", pcFixValue, otherPCYes);

        // 미지정PC거래 금지 처리
        if ("1".equals(pcFixValue) && !"Y".equals(otherPCYes)) {
            log.debug("HDG Debug isPcFixService 미지정 PC거래 금지처리!!!!");
            throw new PRCServiceException("PRCCMM0034", "단말기 지정 서비스 등록 후 사용 가능합니다.<br />단말기 지정 서비스 메뉴로 이동합니다.");
        }

        // 지정PC인경우 추가인증 pass
        if ("2".equals(pcFixValue)) {
            log.debug("HDG Debug isPcFixService 지정PC인경우 추가인증 pass!!!!");
            isPcFix = true;
        }

        return isPcFix;
    }

    @ComponentOperation(name = "해외접속 여부조회")
    public String getAbroadYn() {
        String flagIsAbroadYN = SessionUtils.getSessionValue("FlagIsAbroadYN", String.class);

        if (!sessionManager.isLogin()) {
            // // 비로그인인 경우 해외여부 체크
            flagIsAbroadYN = "N"; // default 는 국내접속
            QLoader loader = ipinsideService.sendNPInsideInfo("CheckCountMA", true, false);

            try {
                com.interezen.flag.FlagBean flag = loader.getFlag();

                if (flag.getFlagValue() < 0) { // 인터리젠 서버값 비정상일 때 IB는 정상일 수 있도록 처리
                    flagIsAbroadYN = "N";
                } else {
                    flagIsAbroadYN = flag.isAbroadIP() ? "Y" : "N";
                }
            } catch (Exception e) {
                log.error("getAbroadYn - interezen FlagBean", e);
                throw new PRCServiceException(e);
            }
        }

        return flagIsAbroadYN;
    }
}
