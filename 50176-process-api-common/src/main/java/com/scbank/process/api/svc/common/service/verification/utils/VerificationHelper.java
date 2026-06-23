package com.scbank.process.api.svc.common.service.verification.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import com.scbank.process.api.edmi.dto.edmi.CbIdentifyDacomReq;
import com.scbank.process.api.edmi.dto.edmi.CbIdentifyDacomRes;
import com.scbank.process.api.fw.base.integration.system.edmi.EdmiRequestOptions;
import com.scbank.process.api.fw.base.integration.system.edmi.EdmiResponse;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplySMSIdentifyRequest;
import com.scbank.process.api.svc.shared.components.verification.dto.AdditionalTranInfo;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.CommonBizUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class VerificationHelper {

    private final HostClient hostClient;

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    @ComponentOperation(name = "인증용 주민번호 조회")
    public String getPerBusNo(String perBusNo1, String perBusNo2) {

        String perBusNo = perBusNo1 + perBusNo2;

        if (sessionManager.isLogin()) {
            perBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
        }

        return perBusNo;
    }

    @ComponentOperation(name = "인증용 고객명 조회")
    public String getCustNm(String custNm, String perBusNo) {

        if (sessionManager.isLogin()) {
            boolean isForeigner = CommonBizUtils.isForeigner(perBusNo);

            // 외국인일경우 성명을 영문명으로 세팅하고 15Byte로 자르기
            if (isForeigner) {
                String foreignerName = sessionManager.getLoginValue("EngCustName", String.class);
                int foreignerNameSize = foreignerName.getBytes().length;

                if (foreignerNameSize > 15) {
                    custNm = new String(foreignerName.getBytes(), 0, 14);
                } else {
                    custNm = foreignerName;
                }
            } else {
                custNm = sessionManager.getLoginValue("CustName", String.class);
            }

        } else {
            sessionManager.setGlobalValue("NOTLOGINSSN", perBusNo);
        }

        return custNm;
    }

    /**
     * 추가인증 가능여부 체크
     * 
     * @param perBusNo
     */
    private HashMap<String, String> checkAdditionalAuth(String perBusNo) {

        HashMap<String, String> checkMap = new HashMap<String, String>();

        boolean isMinor = CommonBizUtils.isMinor(perBusNo);
        boolean isForeigner = CommonBizUtils.isForeigner(perBusNo);

        checkMap.put("minorYn", isMinor ? "Y" : "N");
        checkMap.put("foreignerYn", isForeigner ? "Y" : "N");

        return checkMap;
    }

    /**
     * 로그인 여부 체크 후 필수값 설정
     */
    private HashMap<String, String> checkLoginData(String perBusNo, String custNm) {

        HashMap<String, String> checkMap = new HashMap<String, String>();

        if (sessionManager.isLogin()) {
            perBusNo = sessionManager.getLoginValue("PerBusNo", String.class); // 주민번호
            String foreignerYn = StringUtils.defaultIfBlank(sessionManager.getLoginValue("IsForienType", String.class),
                    "N");

            if ("Y".equals(foreignerYn)) {
                String foreignerName = sessionManager.getLoginValue("EngCustName", String.class);
                int foreignerNameSize = foreignerName.getBytes().length;

                if (foreignerNameSize > 15) {
                    checkMap.put("buyer", new String(foreignerName.getBytes(), 0, 14));
                } else {
                    checkMap.put("buyer", foreignerName);
                }
            } else {
                checkMap.put("buyer", sessionManager.getLoginValue("CustName", String.class));
            }

            checkMap.put("regNum", perBusNo);

        } else {
            checkMap.put("buyer", custNm);
            sessionManager.setGlobalValue("NOTLOGINSSN", perBusNo);
        }

        checkMap.put("NOTLOGINSSN", perBusNo);

        return checkMap;

    }

    /**
     * EDMI : 데이콤 핸드폰 번호 인증
     */
    @ComponentOperation(name = "데이콤 핸드폰 번호 인증")
    public CbIdentifyDacomRes sendMciIdentifyDacom(VerificationAdditionalApplySMSIdentifyRequest request, String buyer,
            String ssn) {
        CbIdentifyDacomReq identifyDacomReq = new CbIdentifyDacomReq();

        identifyDacomReq.setBuyer(buyer);
        identifyDacomReq.setMobileSsn(ssn);

        identifyDacomReq.setDeptCode(request.getDeptCode());
        identifyDacomReq.setMobileStep(request.getPageStep());
        identifyDacomReq.setMobileCom(request.getTelecom());
        identifyDacomReq.setMobileNum(request.getTelNo());
        identifyDacomReq.setCallback(request.getInfoTelNo());
        identifyDacomReq.setMertName(request.getMertName());
        identifyDacomReq.setNameCheckYn(request.getIndentifyCheckYn());

        if ("STEP1".equals(request.getPageStep())) {
            identifyDacomReq.setAuthMode(request.getMode());
        }

        // 인증번호
        if ("STEP2".equals(request.getPageStep())) {
            identifyDacomReq.setAuthNumber(request.getAuthNumber());
            identifyDacomReq.setTId(sessionManager.getGlobalValue("ADD_AUTH_SMS_TID", String.class));
        }

        // PASS 인증 후 SMS인증으로 전환변경시 STEP1에서 전달된 TID 값을 셋팅하여 STEP9로 SMS인증번호 요청
        if ("STEP9".equals(request.getPageStep())) {
            identifyDacomReq.setAuthMode(request.getMode());
            identifyDacomReq.setTId(sessionManager.getGlobalValue("ADD_AUTH_SMS_TID", String.class));
        }

        if (sessionManager.isLogin()) {
            if (StringUtils.isEmpty(request.getCustName())) {
                request.setCustName(identifyDacomReq.getBuyer());
            }
        } else {
            // MCI 로그적재를 위해 임시 세션에 저장 (MCILogUtil에서사용 >> 서비스종료시 remove)
            sessionManager.setGlobalValue("BF_DACOM_PERNO", ssn);
        }

        EdmiRequestOptions cfg = this.hostClient.getEdmiRequestOptions("CB_IDENTIFY_MB");

        cfg.setGsvcd("CB_IDENTIFY_MB");
        cfg.setTrxCd("CB_IDENTIFY_MB");
        cfg.setMacroAi("IDENTIFY_MB");
        cfg.setMacroAo("IDENTIFY_MB");

        EdmiResponse<CbIdentifyDacomRes> edmiResponse = this.hostClient.sendEdmi(cfg, identifyDacomReq,
                CbIdentifyDacomRes.class);

        return edmiResponse.getResponse();
    }

    @ComponentOperation(name = "")
    public void setAdditionalTranInfo(HashMap<String, String> req) {

        List<AdditionalTranInfo> tranList = sessionManager.getGlobalValue("additionalTranInfoList", List.class);

        if (tranList != null && tranList.size() > 0) {
            AdditionalTranInfo firstInfo = tranList.get(0);

            req.put("InBankName", firstInfo.getIgBankName()); // 40Byte
            req.put("InAmount", firstInfo.getRsgAmt()); // 20Byte
            req.put("InClientName", firstInfo.getHgRnam1()); // 40Byte

            // 단건이체
            if (tranList.size() == 1) {
                req.put("TotalCnt", "1"); // 03Byte
                req.put("TotalAmount", firstInfo.getRsgAmt()); // 20Byte
                req.put("ArrayCnt", "0"); // 03Byte
            } else {
                // 정상건수 및 금액 계산
                int totalcount = 0;
                double totalmount = 0;
                for (AdditionalTranInfo tranInfo : tranList) {
                    String rResult = StringUtils.defaultString(tranInfo.getRResult()); // 처리상태
                    String rsgAmt = StringUtils.defaultString(tranInfo.getRsgAmt()); // 보낸(낼) 금액
                    if ("".equals(rsgAmt)) {
                        rsgAmt = "0";
                    }

                    if ("정상".equals(rResult)) {
                        totalcount++;
                        totalmount += Double.parseDouble(rsgAmt);
                    }
                }
            }
        }

    }

    /**
     * EDMI : 데이콤 핸드폰 번호 인증
     */
    @ComponentOperation(name = "ARS 요청 전송코드")
    public String getArsCode(String transType) {

        String arsCode = "";

        if ("PCFIX_0100".equals(transType)) {
            arsCode = "030100"; // 단말기지정 신청
        } else if ("PCFIX_0200".equals(transType)) {
            arsCode = "030200"; // 단말기지정 해지
        } else if ("NONPC_0301".equals(transType)) {
            arsCode = "070301"; // 미지정단말 추가인증 신청.
        } else if ("NONPC_0302".equals(transType)) {
            arsCode = "070302"; // 미지정단말 추가인증 해지.
        } else if ("CERT_0100".equals(transType)) {
            arsCode = "020100"; // 공인인증서 발급
        } else if ("CERT_0200".equals(transType)) {
            arsCode = "020300"; // 공인인증서 타행등록
        } else if ("CERT_0400".equals(transType)) {
            arsCode = "020400"; // 공인인증서 타행해지
        } else if ("TRAN_0100".equals(transType)) {
            arsCode = "050100"; // 즉시이체
        } else if ("IDSE_0100".equals(transType)) {
            arsCode = "070400"; // 가입해지
        } else if ("BLCH_0100".equals(transType)) {
            arsCode = "070600"; // 뱅크사인 보안매체 면제 거래 서비스
        }

        return arsCode;

    }

    @ComponentOperation(name = "ARS 요청 전송코드 - targetService")
    public String getArsTargetServiceCode(String transType) {
        String arsCode = getArsCode(transType);

        if (StringUtils.isBlank(arsCode)) {
            return "";
        }

        String targetServiceCode = arsCode.substring(0, 2);

        Locale locale = LocaleContextHolder.getLocale();
        String langCode = locale.getLanguage();

        if (!"ko".equals(langCode)) {
            targetServiceCode = "01";
        }

        return targetServiceCode;
    }

    @ComponentOperation(name = "ARS 요청 전송코드 - workCode")

    public String getArsWorkCode(String transType) {
        String arsCode = getArsCode(transType);

        if (StringUtils.isBlank(arsCode)) {
            return "";
        }

        return arsCode.substring(2, 4);
    }

    @ComponentOperation(name = "ARS 요청 전송코드 - svcMnChange")
    public String getArsSvcManChangeCode(String transType) {
        String arsCode = getArsCode(transType);

        if (StringUtils.isBlank(arsCode)) {
            return "";
        }

        return arsCode.substring(4, 6);
    }

}
