package com.scbank.process.api.svc.common.service.verification;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.interezen.loader.QLoader;
import com.scbank.process.api.edmi.dto.edmi.CbIdentifyDacomRes;
import com.scbank.process.api.edmi.dto.mci.MciIbFep01001Req;
import com.scbank.process.api.edmi.dto.mci.MciIbFep01001Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H76500Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H76500Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequestOptions;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.base.integration.system.mci.exception.MciSystemException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.CertificationSharedComponent;
import com.scbank.process.api.svc.common.components.VerificationAdditionalComponent;
import com.scbank.process.api.svc.common.components.dto.VerificationGetTelNoDto;
import com.scbank.process.api.svc.common.dao.UsimAuthMgtDao;
import com.scbank.process.api.svc.common.dao.dto.TokenAuthParameter;
import com.scbank.process.api.svc.common.dao.dto.TokenAuthResult;
import com.scbank.process.api.svc.common.mapper.VerificationAdditionalMapper;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalActivateIdVerificationResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalActivateRequest;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalActivateResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplyARSRequest;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplyARSResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplySMSIdentifyRequest;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplySMSIdentifyResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplySimpleRequest;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplySimpleResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalConfirmOverseasDepartureResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalCreateARSAuthKeyResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalGetDeviceAuthResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalRegistDeviceAuthRequest;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalRegistDeviceAuthResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalValidateSimpleRequest;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalValidateSimpleResponse;
import com.scbank.process.api.svc.common.service.verification.utils.VerificationHelper;
import com.scbank.process.api.svc.shared.components.account.AccountListComponent;
import com.scbank.process.api.svc.shared.components.ars.SyncAtComponent;
import com.scbank.process.api.svc.shared.components.ars.dto.SyncAtClientCycleRequest;
import com.scbank.process.api.svc.shared.components.ars.dto.SyncAtClientCycleResponse;
import com.scbank.process.api.svc.shared.components.auth.SecureDataComponent;
import com.scbank.process.api.svc.shared.components.customer.dao.NfCustomerMgtDao;
import com.scbank.process.api.svc.shared.components.customer.dao.dto.NonFaceCustomerInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.customer.dao.dto.NonFaceCustomerInfoParameter;
import com.scbank.process.api.svc.shared.components.device.DeviceAuthComponent;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.components.ipinside.dto.PcFixInfo;
import com.scbank.process.api.svc.shared.components.simpleAuth.TCertComponent;
import com.scbank.process.api.svc.shared.components.simpleAuth.dto.SimpleAuthRequest;
import com.scbank.process.api.svc.shared.components.simpleAuth.dto.SimpleAuthResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfTradeInfoMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.RegisterOngoingTradeInfoParameter;
import com.scbank.process.api.svc.shared.components.verification.VerificationComponent;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationGetCustomerCiRequest;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationGetCustomerCiResponse;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationInsertCustomerCiRequest;
import com.scbank.process.api.svc.shared.dao.DeviceAuthUserDao;
import com.scbank.process.api.svc.shared.dao.dto.DeviceAuthUserParameter;
import com.scbank.process.api.svc.shared.dao.dto.DeviceAuthUserResult;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.CommonBizUtils;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.RandomKeyUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "인증 - 추가인증", url = "/verification/additional")
public class VerificationAdditionalService {

    private final HostClient hostClient;

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    private final UsimAuthMgtDao usimAuthMgtDao;
    private final DeviceAuthUserDao deviceAuthUserDao;
    private final NfCustomerMgtDao nfCustomerMgtDao;
    private final NfTradeInfoMgtDao nfTradeInfoMgtDao;

    private final TCertComponent tcert;

    private final IpinsideComponent ipinside;

    private final VerificationAdditionalComponent additional;

    private final VerificationHelper helper;

    private final VerificationComponent verificationComponent;

    private final CertificationSharedComponent certificationSharedComponent;

    private final AccountListComponent account;

    private final DeviceAuthComponent deviceAuth;

    private final SecureDataComponent secureData;

    private final VerificationAdditionalMapper mapper;

    /**
     * 전문 거래번호 생성기
     */
    // private final IntegrationTranNoGenerator integrationTranNoGenerator;

    /**
     * ASIS : SCBKAddAuthMdm, SCBKAddAuthUtils
     * 
     * @param serviceContext
     * @param request
     * @return
     */
    @ServiceEndpoint(url = "activate", name = "추가인증 초기화")
    public VerificationAdditionalActivateResponse activate(IServiceContext serviceContext,
            VerificationAdditionalActivateRequest request) {

        VerificationAdditionalActivateResponse response = new VerificationAdditionalActivateResponse();

        sessionManager.removeGlobalValue("IDENTIFY_SSN"); // 비로그인시 사용

        String additionalType = additional.getAdditionalType(request.getAdditionalType(), request.getTransType());

        response.setAdditionalType(additionalType);

        VerificationGetTelNoDto telNoInfo = additional.getTelNo();

        response.setWasTranNo(telNoInfo.getWasTranNo());

        response.setPhoneNo(StringUtils.defaultString(telNoInfo.getPhoneNo()));
        response.setMaskPhoneNo(StringUtils.defaultString(telNoInfo.getMaskPhoneNo()));
        response.setPhoneNo1(StringUtils.defaultString(telNoInfo.getPhoneNo1()));
        response.setPhoneNo2(StringUtils.defaultString(telNoInfo.getPhoneNo2()));
        response.setPhoneNo3(StringUtils.defaultString(telNoInfo.getPhoneNo3()));

        response.setHomeTelNo(StringUtils.defaultString(telNoInfo.getHomeTelNo()));
        response.setMaskHomeTelNo(StringUtils.defaultString(telNoInfo.getMaskHomeTelNo()));
        response.setHomeTelNo1(StringUtils.defaultString(telNoInfo.getHomeTelNo1()));
        response.setHomeTelNo2(StringUtils.defaultString(telNoInfo.getHomeTelNo2()));
        response.setHomeTelNo3(StringUtils.defaultString(telNoInfo.getHomeTelNo3()));

        response.setJobTelNo(StringUtils.defaultString(telNoInfo.getJobTelNo()));
        response.setMaskJobTelNo(StringUtils.defaultString(telNoInfo.getMaskJobTelNo()));
        response.setJobTelNo1(StringUtils.defaultString(telNoInfo.getJobTelNo1()));
        response.setJobTelNo2(StringUtils.defaultString(telNoInfo.getJobTelNo2()));
        response.setJobTelNo3(StringUtils.defaultString(telNoInfo.getJobTelNo3()));

        response.setTransType(request.getTransType());

        if (StringUtils.isNotEmpty(additionalType)) {
            // 추가인증 대상여부 Session값 초기화
            sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "N");
        } else {
            // 추가인증 PASS
            sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
        }

        log.debug("ADD_AUTH_SUCCESS_FLAG > " + sessionManager.getGlobalValue("ADD_AUTH_SUCCESS_FLAG", String.class));

        return response;

    }

    /**
     * ASIS : MA3CMMAAT001_101S
     * 
     * @param serviceContext
     * @return
     */
    @ServiceEndpoint(url = "createARSAuthNumber", name = "ARS 인증번호 생성")
    public VerificationAdditionalCreateARSAuthKeyResponse createARSAuthNumber(IServiceContext serviceContext) {

        VerificationAdditionalCreateARSAuthKeyResponse response = new VerificationAdditionalCreateARSAuthKeyResponse();

        String randomKey = RandomKeyUtils.getKey().substring(1, 3);

        sessionManager.setGlobalValue("SEND_ARS_AUTH_NUMBER", randomKey);

        response.setAuthNumber(randomKey);

        return response;

    }

    /**
     * ASIS : addCert.js > _addAuthARSBtn_2, MA3CMMAAT001_201S
     * - MA3CMMAAT000_101S(로컬 임시서비스 적용여부 확인필요)
     * 
     * @param serviceContext
     * @param request
     * @return
     */
    @ServiceEndpoint(url = "applyARS", name = "ARS 인증요청")
    public VerificationAdditionalApplyARSResponse applyARS(IServiceContext serviceContext,
            VerificationAdditionalApplyARSRequest request) {

        VerificationAdditionalApplyARSResponse response = new VerificationAdditionalApplyARSResponse();

        String sendArsAuthNum = sessionManager.getGlobalValue("SEND_ARS_AUTH_NUMBER", String.class);

        String custName = SessionUtils.getSessionValue("CustName");

        /* TODO: 이체프로세스를 확인 후 이체정보를 설정하여야함 */

        /* TODO: 임시로 특정번호만 호출될 수 있도록 수정 */
        if ("01029705083".equals(request.getAuthTelNo())) {
            SyncAtComponent client = new SyncAtComponent();

            SyncAtClientCycleRequest syncAtRequest = SyncAtClientCycleRequest.builder()
                    .authTelNo(request.getAuthTelNo())
                    .account(request.getAccount())
                    .targetService(helper.getArsTargetServiceCode(request.getTransType()))
                    .tranId(request.getWasTranNo())
                    .workCode(helper.getArsWorkCode(request.getTransType()))
                    .svcManChange(helper.getArsSvcManChangeCode(request.getTransType())).clientName(custName)
                    .ssn(sendArsAuthNum)
                    .build();

            SyncAtClientCycleResponse syncAtResponse = client.syncAtClientCycle(syncAtRequest);

            response.setResultCode(syncAtResponse.getResultCode());
            response.setTranId(syncAtResponse.getTranId());

            if ("0000".equals(response.getResultCode())) {

                sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
                sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_TYPE", "D");
            }
        } else {

            response.setResultCode("0000");
            response.setTranId(request.getWasTranNo());

            sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
            sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_TYPE", "D");

        }

        return response;

    }

    /*
     * ASIS - MA3CMMAAT003_101S
     */
    @ServiceEndpoint(url = "confirmOverseasDeparture", name = "해외출국사실확인")
    public VerificationAdditionalConfirmOverseasDepartureResponse confirmOverseasDeparture(
            IServiceContext serviceContext) {

        VerificationAdditionalConfirmOverseasDepartureResponse response = new VerificationAdditionalConfirmOverseasDepartureResponse();

        String custName = SessionUtils.getSessionValue("CustName");
        String perBusNo = SessionUtils.getSessionValue("PerBusNo");

        MciRequestOptions mciCfg = this.hostClient.getMciRequestOptions("MCI_IB_FEP01_001");

        mciCfg.setTranCd("IB_FEP01_001");

        MciIbFep01001Req inputDto = new MciIbFep01001Req();

        inputDto.setCLASS_CD("10");
        inputDto.setBIZ_KIND_CD("01");
        inputDto.setREC_CNT(1);
        inputDto.setREC_LEN(90);
        inputDto.setORG_CD("023");
        inputDto.setRES_CD("");
        inputDto.setABROAD_YN("");
        inputDto.setINQ_CUS_NAME(custName);
        inputDto.setINQ_CUS_SSN(perBusNo);
        inputDto.setRESERVE_FLD("");

        log.debug("host inputDto dto : {}", inputDto.toString());

        /* TODO: MCI에 해외체류 사실 조회 전문 요청시 필수값 조립 */
        /*
         * MA3CMMAAT003_101S line 69~87 조회일련번호(INQ_SERIAL_NO) 설정하는 값을 instance ID로 하는데
         * 알수 없어 우선 시분초로만 설정
         */
        inputDto.setINQ_SERIAL_NO(DateUtils.getCurrentDate(DateUtils.HHMMSS));

        inputDto.setREQ_DT(DateUtils.getCurrentDate(DateUtils.YYYYMMDDHHMMSS));

        /* TODO: 전문 통신이 안되어서 주석처리 후 응답값 세팅 */
        MciResponse<MciIbFep01001Res> mciResponse = this.hostClient.sendMci(mciCfg,
                inputDto, MciIbFep01001Res.class);
        MciIbFep01001Res outputDto = mciResponse.getResponse();
        // MciIbFep01001Res outputDto = new MciIbFep01001Res();
        // outputDto.setRES_CD("000");
        // outputDto.setABROAD_YN("0");

        response.setResultCode(outputDto.getRES_CD());
        response.setAbroadYn(outputDto.getABROAD_YN());

        /*
         * 출국정보조회 응답코드(RES_CD)
         * - 000 정상 처리되었습니다.
         * - 110 전문 종별코드가 잘못되었습니다.
         * - 120 레코드 수가 잘못 되었습니다.
         * - 130 데이터 포맷 오류입니다.(세부내용)
         * - 140 전문 일련번호가 잘못되었습니다.
         * - 150 필수항목의 데이터 내용이 누락되어 있습니다.
         * - 160 등록되지 않은 참가기관 코드입니다.
         * - 170 권한이 없는 참가기관 코드입니다.
         * - 180 전문 전송일시가 잘못되었습니다.
         * - 190 출입국관리사무소 시스템의 응답이 없습니다.
         * - 200 주민등록번호가 잘못되었습니다.
         * - 210 한국국적이 아닙니다. (주민등록번호 7번째 자리로 식별)
         * - 220 출입국관리사무소 시스템 점검 중입니다.
         */

        if ("000".equals(outputDto.getRES_CD()) || "0".equals(outputDto.getRES_CD())) {
            if ("00".equals(outputDto.getABROAD_YN()) || "0".equals(outputDto.getABROAD_YN())) {

                sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
                sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_TYPE", "F");

            }
        }

        log.debug("host output dto : {}", outputDto);

        return response;
    }

    /**
     * ASIS - MA3CMMAAT004_101S 참조
     */
    @ServiceEndpoint(url = "applySimple", name = "간편인증 요청")
    public VerificationAdditionalApplySimpleResponse applySimple(IServiceContext serviceContext,
            VerificationAdditionalApplySimpleRequest request) {

        VerificationAdditionalApplySimpleResponse response = new VerificationAdditionalApplySimpleResponse();

        // 디지털인증서 발급/재발급 화며아이디 CRTDCT001
        boolean isDigitalCertificateMenu = "CRTDCT001".equals(PRCSharedUtils.getMenuId());

        String perBusNo = request.getPerBusNo1() + request.getPerBusNo2();

        if (sessionManager.isLogin()) {
            request.setCustName(sessionManager.getLoginValue("CustName", String.class));
            perBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
        }

        if (StringUtils.isEmpty(request.getCustName()) || StringUtils.isEmpty(perBusNo)
                || StringUtils.isEmpty(request.getTelecom()) || StringUtils.isEmpty(request.getTelNo())) {
            throw new PRCServiceException("PRCCMM0031",
                    "접속이 끊겼습니다. 서비스를 다시 이용해 주세요. 일정시간 거래가 없었거나 통신상태가 불안정한 경우 또는 다른 기기로 동시 접속 시 종료됩니다.");
        }

        String imei = "";
        String uiccid = "";
        String[] arrHddInfo = ipinside.simpleDataDecode2();
        String deviceToken = PRCSharedUtils.getSimSerial();

        log.debug("hdg applySimple arrHddInfo.length {}", arrHddInfo.length);
        log.debug("hdg applySimple PRCSharedUtils.isAndroid {}", PRCSharedUtils.isAndroid());

        if (!PRCSharedUtils.isAndroid()) {

            TokenAuthParameter tokenAuthParams = new TokenAuthParameter();
            tokenAuthParams.setCtn(request.getTelNo());

            tokenAuthParams.setDeviceToken(deviceToken);

            List<TokenAuthResult> tokenAuthResult = this.usimAuthMgtDao.selectTokenAuth(tokenAuthParams);

            if (!(tokenAuthResult != null && tokenAuthResult.size() > 0)) {
                response.setAuthYn("N");
                response.setResultCode("9999");
                response.setResultMsg("MO 미등록 단말입니다.");

                return response;
            }

        } else if (PRCSharedUtils.isAndroid() && arrHddInfo != null && arrHddInfo.length >= 2) {

            int osVersion = Integer.parseInt(PRCSharedUtils.getOsVersion().split("\\.")[0]);

            if (osVersion > 9) {
                // 라온시큐어 요청 안드로이드 10 이상부터는 IMEI&Usim 시리얼 빈값으로 세팅 요청

                String uuid = PRCSharedUtils.getDeviceUUID();

                TokenAuthParameter tokenAuthParams = new TokenAuthParameter();
                tokenAuthParams.setCtn(request.getTelNo());
                tokenAuthParams.setDeviceToken(uuid);

                List<TokenAuthResult> tokenAuthResult = this.usimAuthMgtDao.selectTokenAuth(tokenAuthParams);

                if (!(tokenAuthResult != null && tokenAuthResult.size() > 0)) {
                    response.setAuthYn("N");
                    response.setResultCode("9999");
                    response.setResultMsg("MO 미등록 단말입니다.");

                    return response;
                }

            } else {

                imei = arrHddInfo[1];

                if (StringUtils.isNotEmpty(deviceToken) && deviceToken.length() >= 19) {
                    uiccid = deviceToken.substring(5, deviceToken.length() - 5);

                    if (uiccid.length() > 19) {
                        uiccid = uiccid.substring(0, 19);
                    }
                }

            }

        }

        String birthDay = CommonBizUtils.getBirthday(perBusNo);
        String gender = CommonBizUtils.getGender(perBusNo);

        // 간편인증 확인
        SimpleAuthResponse simpleAuthResponse = new SimpleAuthResponse();
        try {
            SimpleAuthRequest simpleAuthRequest = new SimpleAuthRequest();

            simpleAuthRequest.setName(request.getCustName());
            simpleAuthRequest.setTelecomType(request.getTelecom());
            simpleAuthRequest.setCtn(request.getTelNo());
            simpleAuthRequest.setUiccid(uiccid);
            simpleAuthRequest.setImei(imei);
            simpleAuthRequest.setGender(gender);
            simpleAuthRequest.setBirthday(birthDay);
            simpleAuthRequest.setMos(PRCSharedUtils.isAndroid() ? "A" : "I");
            simpleAuthRequest.setPrivacySharingAgreeYn(request.getPrivacySharingAgreeYn());
            simpleAuthRequest.setThirdPartyProvisionAgreeYn(request.getThirdPartyProvisionAgreeYn());

            simpleAuthResponse = tcert.sendTauth(simpleAuthRequest);

            log.debug("simpleAuthResponse {}", simpleAuthResponse);
        } catch (Exception e) {
            if (RunMode.PRD.equals(RuntimeContext.getRunMode())) {
                throw e;
            } else {
                log.error("applySimple tcert.sendTauth error = " + e);
            }
        }

        response.setAuthYn(simpleAuthResponse.getAuthYn());
        response.setResultCode(simpleAuthResponse.getResultCode());
        response.setResultMsg(simpleAuthResponse.getResultMsg());

        /*
         * 디지털인증서 발급 메뉴인경우, 법인폰 사용자여부 체크 => SMS명의인증으로 전환해야됨
         * - 라온 응답코드 0013 && SKT/LGU+/KT 인 경우(알뜰통신사도 동일) - (LGU+:1, SKT:2, KT:3)
         * - MA3CMMAAT004_101S > line.228
         */
        if (isDigitalCertificateMenu
                && ("0013".equals(simpleAuthResponse.getResultCode()) && request.getTelecom().matches("1|2|3"))) {
            response.setAuthYn("N");
            response.setChangeSmsAuthYn("Y");
        } else if (!RunMode.PRD.equals(RuntimeContext.getRunMode())) {// 운영이 아니라면 강제 통과
            response.setAuthYn("Y");
            response.setResultCode("0000");
        }

        // 인증 성공인 경우 추가인증 세션값 처리
        if ("0000".equals(response.getResultCode()) && "Y".equals(response.getAuthYn())) {
            // S - 휴면계좌보유고객 조회
            if (StringUtils.isNotBlank(perBusNo)) {

                try {
                    OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK01_H866");

                    hostCfg.setRealTran(true);

                    hostCfg.setImsTranCd("TI1IBK01");
                    hostCfg.setInClassCd("H866");
                    hostCfg.setSvcCd("866");

                    CbIbk01H86600Req inputDto = new CbIbk01H86600Req();

                    inputDto.setUserID("FIRST999");
                    inputDto.setTSPassword("111111");
                    inputDto.setYIJMNO(perBusNo);

                    OltpResponse<CbIbk01H86600Res> hostResponse = this.hostClient.sendOltp(hostCfg, inputDto,
                            CbIbk01H86600Res.class);

                    CbIbk01H86600Res outputDto = hostResponse.getResponse();

                    response.setYohumgb(outputDto.getYOHUMGB());
                } catch (Exception e) {
                    response.setYohumgb("N");
                }

            } else {
                response.setYohumgb("N");
            }
            // E - 휴면계좌보유고객 조회

            // 휴면계좌보유고객이 아니면 인증성공 처리
            if (!"Y".equals(response.getYohumgb())) {

                // 고객정보 세션에 저장
                sessionManager.setGlobalValue("CustName", request.getCustName());
                sessionManager.setGlobalValue("PerBusNo", perBusNo);
                sessionManager.setGlobalValue("Telecom", request.getTelecom());
                sessionManager.setGlobalValue("HpNum", request.getTelNo());

                // 세션값 갱신
                sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
                sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_TYPE", "A");

                // 개인정보노출자 판단
                certificationSharedComponent.getYoLRSOTGB(SessionUtils.getSessionValue("PerBusNo"));
            }

        }

        return response;

    }

    /*
     * ASIS - MA3CMMAAT004_201S
     */
    @ServiceEndpoint(url = "validateSimple", name = "간편인증 등록검증")
    public VerificationAdditionalValidateSimpleResponse validateSimple(IServiceContext serviceContext,
            VerificationAdditionalValidateSimpleRequest request) {

        VerificationAdditionalValidateSimpleResponse response = new VerificationAdditionalValidateSimpleResponse();

        String simSerial = PRCSharedUtils.getSimSerial();

        TokenAuthParameter tokenAuthParams = new TokenAuthParameter();
        tokenAuthParams.setCtn(request.getTelNo());
        tokenAuthParams.setDeviceToken(simSerial);

        List<TokenAuthResult> tokenAuthResult = this.usimAuthMgtDao.selectTokenAuth(tokenAuthParams);

        if (tokenAuthResult != null && tokenAuthResult.size() > 0) {
            response.setResultYn("Y");
        } else {

            // 운영이 아닐때만 체크해서 등록처리 해준다. ( 개발기 테스트용 )
            if (!RunMode.PRD.equals(RuntimeContext.getRunMode())) {
                tokenAuthParams.setMoMsg("[0810]" + simSerial);
                tokenAuthParams.setMoNumber(request.getMoNumber());
                tokenAuthParams.setMoRcvDate(DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
                this.usimAuthMgtDao.insertTokenAuth(tokenAuthParams);
                response.setResultYn("Y");
            }

            // 최대 5번 ( 매 1초) 까지 재조회를 해보고, 결과에 따른다.
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }

                tokenAuthResult = this.usimAuthMgtDao.selectTokenAuth(tokenAuthParams);

                if (tokenAuthResult != null && tokenAuthResult.size() > 0) {
                    response.setResultYn("Y");
                    break;
                }
            }
        }

        return response;
    }

    /*
     * ASIS - MA3CMMAAT005_101S
     */
    @ServiceEndpoint(url = "applySMSIdentify", name = "SMS명의인증 실행")
    public VerificationAdditionalApplySMSIdentifyResponse applySMSIdentify(IServiceContext serviceContext,
            VerificationAdditionalApplySMSIdentifyRequest request) {

        sessionManager.removeGlobalValue("USER_CI_INFO");

        VerificationAdditionalApplySMSIdentifyResponse response = new VerificationAdditionalApplySMSIdentifyResponse();

        String perBusNo = "";

        if (sessionManager.isLogin()) {
            perBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
        } else if ("STEP1".equals(request.getPageStep())) { // 비로그인
            perBusNo = request.getPerBusNo1() + request.getPerBusNo2();
            sessionManager.setGlobalValue("IDENTIFY_SSN", request.getPerBusNo1() + request.getPerBusNo2());
        } else { // 비로그인
            perBusNo = sessionManager.getGlobalValue("IDENTIFY_SSN", String.class);
        }

        String custNm = helper.getCustNm(request.getCustName(), perBusNo);

        boolean isMinor = CommonBizUtils.isMinor(perBusNo);
        boolean isForeigner = CommonBizUtils.isForeigner(perBusNo);

        log.debug("HDG Debug isMinor [{}], isForeigner [{}]", isMinor, isForeigner);

        // 미성년자 또는 외국인은 제외
        if (isMinor || isForeigner) {
            response.setMinorYn(isMinor ? "Y" : "N");
            response.setForeignerYn(isForeigner ? "Y" : "N");
        } else {
            CbIdentifyDacomRes identifyDacomRes = helper.sendMciIdentifyDacom(request, custNm, perBusNo);

            log.debug("HDG Debug identifyDacomRes.getMobileSsn() [{}]", identifyDacomRes.getMobileSsn());

            response.setMaskPerBusNo(identifyDacomRes.getMobileSsn().substring(0, 7) + "******");

            if (!"0000".equals(identifyDacomRes.getRespCode())) {
                throw new PRCServiceException("PRCCMM0055",
                        List.of(identifyDacomRes.getRespMsg() + " (" + identifyDacomRes.getRespCode() + ")"));
            }

            if ("STEP1".equals(request.getPageStep())) {
                sessionManager.setGlobalValue("ADD_AUTH_SMS_TID", identifyDacomRes.getTId());
            }

            if ("STEP2".equals(request.getPageStep())) {

                if (RunMode.PRD.equals(RuntimeContext.getRunMode())) {

                    CbTbs03H76500Req cbTbs03H76500Req = new CbTbs03H76500Req();

                    /* TODO: 이게뭐자?? */
                    // kcbInqParam.putHostWebComm("VAN_TYPE", "56");

                    cbTbs03H76500Req.setUserID(SessionUtils.getSessionValue("UserID", String.class));
                    cbTbs03H76500Req.setTSPassword(SessionUtils.getSessionValue("TSPassword", String.class));
                    cbTbs03H76500Req.setYIJUMIN(perBusNo);

                    cbTbs03H76500Req.setYITCOM(request.getTelecom());

                    String[] phoneNumberArr = CommonBizUtils.getPhoneNumberArr(request.getTelNo());
                    cbTbs03H76500Req.setYITEL1(phoneNumberArr[0]);
                    cbTbs03H76500Req.setYITEL2(phoneNumberArr[1]);
                    cbTbs03H76500Req.setYITEL3(phoneNumberArr[2]);

                    if (StringUtils.isNotEmpty(identifyDacomRes.getCi())
                            && identifyDacomRes.getCi().length() > 21) {
                        cbTbs03H76500Req.setYIMINO(identifyDacomRes.getCi().substring(0, 22));
                    }

                    if (StringUtils.isNotEmpty(request.getCustName()) && request.getCustName().length() > 2) {
                        cbTbs03H76500Req.setYINAME(request.getCustName().substring(0, 3));
                    } else {
                        cbTbs03H76500Req.setYINAME(request.getCustName());
                    }

                    OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("TI1TBS03_H765");

                    OltpResponse<CbTbs03H76500Res> hostResponse = this.hostClient.sendOltp(hostCfg, cbTbs03H76500Req,
                            CbTbs03H76500Res.class);

                    CbTbs03H76500Res cbTbs03H76500Res = hostResponse.getResponse();

                    if (!cbTbs03H76500Res.getYOCINO().equals(identifyDacomRes.getCi())) {

                        log.error("##### MCI_IDENTIFU_DACOM kcbCI    ::" + cbTbs03H76500Res.getYOCINO() + "::");
                        log.error("##### TI1TBS03_H765 dacomCI  ::" + identifyDacomRes.getCi() + "::");

                        response.setKcbCiErrYn("Y"); // CI 불일치

                        sessionManager.removeGlobalValue("BF_DACOM_PERNO");

                        return response;
                    }

                }

                sessionManager.setGlobalValue("CustName", request.getCustName());
                sessionManager.setGlobalValue("PerBusNo", perBusNo);
                sessionManager.setGlobalValue("Telecom", request.getTelecom());
                sessionManager.setGlobalValue("HpNum", request.getTelNo());

                sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
                sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_TYPE", "B");
                // sessionManager.setGlobalValue("BIZ_AUTH_FLAG", "Y");

                certificationSharedComponent.getYoLRSOTGB(perBusNo);

                /* TODO: 토스 CI비교로직 처리 */
                if (RunMode.PRD.equals(RuntimeContext.getRunMode())) {

                    String custCi = sessionManager.getGlobalValue("CUST_CI", String.class);

                    if (StringUtils.isNotBlank(custCi)) {

                        if (!custCi.equals(identifyDacomRes.getCi())) {
                            sessionManager.removeGlobalValue("CUST_CI");
                            sessionManager.removeGlobalValue("CNNCTN_TRAD_NO");
                            throw new PRCServiceException("CIINFO_ERR", "고객정보가 일치 하지 않습니다.(CI불일치)");

                        }
                    }

                    // 2019.11.13-SK2-금결원 오픈뱅킹 서비스 때문에 추가.
                    sessionManager.setGlobalValue("USER_CI_INFO", identifyDacomRes.getCi());
                    // 2019.11.13-SK2-금결원 오픈뱅킹 서비스 때문에 추가.

                } else {
                    StringBuilder sb = new StringBuilder("CI" + perBusNo);
                    // String ci = "CI" + perBusNo;

                    do {
                        // ci += request.getTelNo();
                        sb.append(request.getTelNo());

                    } while (sb.toString().length() < 88);

                    String ci = sb.toString().substring(0, 88);
                    // ci = ci.substring(0, 88);

                    sessionManager.setGlobalValue("USER_CI_INFO", ci);
                }

                VerificationGetCustomerCiResponse customerCiResponse = checkUserCiInfo(custNm);

                response.setCiErrYn(StringUtils.defaultString(customerCiResponse.getIsCIERR()));
                response.setNameErrYn(StringUtils.defaultString(customerCiResponse.getIsNameChkErr()));

            }
        }

        // MCI 로그적재를위한 임시 세션 삭제
        sessionManager.removeGlobalValue("BF_DACOM_PERNO");

        return response;

    }

    /**
     * ASIS: MA3CMMAAT005_101S
     * 
     * @param custName
     * @return
     */
    private VerificationGetCustomerCiResponse checkUserCiInfo(String custName) {

        VerificationGetCustomerCiResponse result = new VerificationGetCustomerCiResponse();
        try {
            String ciInfo = StringUtils.defaultString(sessionManager.getGlobalValue("USER_CI_INFO", String.class));

            VerificationGetCustomerCiRequest request = new VerificationGetCustomerCiRequest();

            if (RunMode.PRD.equals(RuntimeContext.getRunMode())) {
                request.setCi(ciInfo);
            } else {
                String perBusNum = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("PerBusNo", String.class),
                        "");
                request.setCi(ciInfo);// CI정보
                request.setAismno(perBusNum);// 실명번호
                request.setAicrgb("3");// 처리구분(1:CI번호, 2:CIF번호, 3:주민번호, 4:계좌번호)
            }

            request.setDacomName(custName);
            request.setDacomTranYN("Y");

            VerificationGetCustomerCiResponse response = verificationComponent.getCustomerCi(request);

            String isCiYn = StringUtils.defaultIfBlank(response.getResult(), "N");
            String isCiErr = StringUtils.defaultIfBlank(response.getIsCIERR(), "N");
            String statusYn = StringUtils.defaultIfBlank(response.getStatusYN(), "N");
            String isNameChkErr = StringUtils.defaultIfBlank(response.getIsNameChkErr(), "N");

            log.debug("HDG Debug VerificationGetCustomerCiResponse, [{}]", response.toString());

            if ("Y".equals(isNameChkErr)) {
                result.setIsNameChkErr(isNameChkErr);
            } else {
                if ("Y".equals(isCiErr)) {
                    result.setIsCIERR(isCiErr);
                } else if (!("Y".equals(isCiYn) && "Y".equals(statusYn))) {

                    VerificationInsertCustomerCiRequest req = new VerificationInsertCustomerCiRequest();

                    if (RunMode.PRD.equals(RuntimeContext.getRunMode())) {
                        req.setUserCiInfo(ciInfo);
                    } else {
                        req.setUserCiInfo(StringUtils
                                .defaultIfBlank(sessionManager.getGlobalValue("TEST_CI_INFO", String.class), ciInfo));
                    }

                    verificationComponent.insertCustomerCi(req);
                }
            }
        } catch (MciSystemException e) {
            log.info("checkUserCiInfo Exception -> {}", e.getMessage());
            e.printStackTrace();
        }

        return result;

    }

    /**
     * ASIS: MA3CMMAAT012_101S, MA3CMMCMM002_101S
     * 추가인증 FDS 출금정지 비대면실명확인 초기화
     * 
     * @param serviceContext
     * @param request
     * @return
     */
    @ServiceEndpoint(url = "activateIdVerification", name = "추가인증 FDS 출금정지 비대면실명확인 초기화")
    public VerificationAdditionalActivateIdVerificationResponse activateIdVerification(IServiceContext serviceContext) {

        VerificationAdditionalActivateIdVerificationResponse response = new VerificationAdditionalActivateIdVerificationResponse();

        String branchNum = "";
        String tradNo = "";
        String custNo = "";
        String mobileNum = StringUtils.defaultIfBlank(sessionManager.getGlobalValue("MOBILENUM", String.class),
                "00000000000");
        String custNm = StringUtils.defaultIfBlank(sessionManager.getGlobalValue("CustName", String.class), "신분증공통");
        String perBusNo = SessionUtils.getSessionValue("PerBusNo");
        String prdctId = "2100"; // 상품ID
        String prdctCd = "2100"; // 상품코드
        String prdctNm = "신분증촬영공통"; // 상품명
        String bizType = "IDCM"; // 업무구분

        // TODO: 임시 주민번호 설정ㅇ으로 테스트후 삭제해야됨,
        // perBusNo = "8007071999994";

        // ASIS : MA3CMMAAT012_101S (S)
        if (sessionManager.isLogin()) {
            branchNum = StringUtils.defaultIfBlank(sessionManager.getLoginValue("RegiBranchNum", String.class), "100");
        } else {
            branchNum = StringUtils.defaultIfBlank(sessionManager.getGlobalValue("BranchNum", String.class), "100");
        }
        // ASIS : MA3CMMAAT012_101S (E)

        if (StringUtils.isEmpty(perBusNo)) {
            throw new PRCServiceException("INI399", "필수 항목(주민등록번호)가 존재 하지 않습니다.");
        }

        // ASIS : MA3CMMCMM002_101S (S)
        NonFaceCustomerInfoParameter nonFaceCustomerInfoParameter = new NonFaceCustomerInfoParameter();
        nonFaceCustomerInfoParameter.setSsn(perBusNo);

        NonFaceCustomerInfoInquiryResult nonFaceCustomerInfoInquiryResult = nfCustomerMgtDao
                .selectNonFaceCustomerInfo(nonFaceCustomerInfoParameter);

        if (nonFaceCustomerInfoInquiryResult == null) {

            String cmpndCheckKey = perBusNo + "_" + DateUtils.getCurrentDate("yyyyMMddHHmmss");
            nonFaceCustomerInfoParameter.setCmpndCheckKey(cmpndCheckKey);
            nonFaceCustomerInfoParameter.setMblphnNo(mobileNum);
            nonFaceCustomerInfoParameter.setUserNm(custNm);
            nonFaceCustomerInfoParameter.setSrcNewUserFlg("N");
            nonFaceCustomerInfoParameter.setNewUserFlg("N");
            nonFaceCustomerInfoParameter.setSrcDelObjFlg("");
            nonFaceCustomerInfoParameter.setDelObjFlg("");

            nfCustomerMgtDao.insertNonFaceCustomerInfo(nonFaceCustomerInfoParameter);

            nonFaceCustomerInfoInquiryResult = nfCustomerMgtDao
                    .selectNonFaceCustomerInfo(nonFaceCustomerInfoParameter);
        }

        custNo = nonFaceCustomerInfoInquiryResult.getCustNo();

        RegisterOngoingTradeInfoParameter registerOngoingTradeInfoParameter = new RegisterOngoingTradeInfoParameter();
        registerOngoingTradeInfoParameter.setCustNo(custNo);
        registerOngoingTradeInfoParameter.setBizType(bizType);
        registerOngoingTradeInfoParameter.setPrdctId(prdctId);
        registerOngoingTradeInfoParameter.setPrdctCd(prdctCd);
        registerOngoingTradeInfoParameter.setPrdctNm(prdctNm);
        registerOngoingTradeInfoParameter.setBrnchNo(branchNum);
        registerOngoingTradeInfoParameter.setCddReqCd("N");

        synchronized (this) {
            nfTradeInfoMgtDao.insertNonfaceTradeInfo(registerOngoingTradeInfoParameter);
            tradNo = registerOngoingTradeInfoParameter.getTradNo();
        }

        sessionManager.removeGlobalValue("faceYn");
        sessionManager.removeGlobalValue("videoCallYn");

        sessionManager.removeGlobalValue("CUST_NO");
        sessionManager.removeGlobalValue("TRAD_NO");
        sessionManager.removeGlobalValue("BIZ_TYPE");

        sessionManager.setGlobalValue("CUST_NO", custNo);
        sessionManager.setGlobalValue("TRAD_NO", tradNo);
        sessionManager.setGlobalValue("BIZ_TYPE", bizType);

        response.setCustNo(custNo);
        response.setTradNo(tradNo);
        response.setBizType(bizType);
        response.setPrdctCd(prdctCd);
        response.setPrdctNm(prdctNm);
        response.setBrnchNo(branchNum);

        return response;

        // ASIS : MA3CMMCMM002_101S (E)
    }

    @ServiceEndpoint(url = "getDeviceAuth", name = "단말기지정서비스 조회")
    public VerificationAdditionalGetDeviceAuthResponse getDeviceAuth(IServiceContext serviceContext) {

        String userId = sessionManager.getGlobalValue("UserID", String.class);
        if (sessionManager.isLogin()) {
            userId = sessionManager.getLoginValue("UserID", String.class);
        }

        VerificationAdditionalGetDeviceAuthResponse response = new VerificationAdditionalGetDeviceAuthResponse();

        int deviceCount = 0;
        List<DeviceAuthUserResult> deviceAuthList = null;

        if (StringUtils.isNotBlank(userId)) {
            DeviceAuthUserParameter params = DeviceAuthUserParameter.builder().userId(userId).build();

            // 단말기지정서비스 등록 건수
            deviceCount = deviceAuthUserDao.selectDeviceAuthCount(params);

            // 단말기지정서비스 등록정보 목록
            deviceAuthList = deviceAuthUserDao.selectDeviceAuthInfo(params);
        }

        String[] arrHddInfo = ipinside.simpleDataDecode2();

        String macAddress = arrHddInfo[0];

        response.setMacAddress(macAddress);
        response.setMaskMacAddress(macAddress.substring(0, 12) + "**-**");

        response.setDeviceCount(deviceCount);
        response.setDeviceAuthList(mapper.toDeviceAuthInfo(deviceAuthList));

        return response;

    }

    @ServiceEndpoint(url = "registDeviceAuth", name = "단말기지정서비스 등록")
    @Transactional(value = "ipinsideTransactionManager", rollbackFor = { Exception.class })
    public VerificationAdditionalRegistDeviceAuthResponse registDeviceAuth(IServiceContext serviceContext,
            VerificationAdditionalRegistDeviceAuthRequest request) {

        VerificationAdditionalRegistDeviceAuthResponse response = new VerificationAdditionalRegistDeviceAuthResponse();

        String[] arrHddInfo = ipinside.simpleDataDecode2();

        String hddSrlNo = arrHddInfo[1];
        String macAddress = arrHddInfo[0];

        String userId = sessionManager.getGlobalValue("UserID", String.class);
        if (sessionManager.isLogin()) {
            userId = sessionManager.getLoginValue("UserID", String.class);
        }
        String userNm = SessionUtils.getSessionValue("CustName");

        int deviceCount = deviceAuth.getDeviceAuthCount(userId);

        // 추가인증 + 보안매체 검증
        secureData.verifyVerification();

        DeviceAuthUserParameter params = DeviceAuthUserParameter.builder().macAddress(macAddress)
                .deviceAlias(request.getDeviceAlias()).phoneNo(request.getAuthTelNo()).regGubun("1").deviceClass("01")
                .hddSrlNo(hddSrlNo)
                .userId(userId).userNm(userNm).build();

        // 단말기 지정 서비스 등록
        deviceAuthUserDao.insertDeviceAuthSvc(params);

        if (deviceCount == 0) {
            if ("Y".equals(request.getAllowOtherDeviceYn())) {
                // 미지정 단말 허용
                deviceAuthUserDao.updateAllowOtherDeviceAuthUser(params);
                deviceAuthUserDao.insertAllowOtherDeviceAuthUser(params);
            } else {
                // 미지정 단말 미허용
                deviceAuthUserDao.updateBlockOtherDeviceAuthUser(params);
                deviceAuthUserDao.insertBlockOtherDeviceAuthUser(params);
            }
        }

        /* 기기정보 세션 재설정 (S) */
        QLoader qLoader = ipinside.sendNPInsideInfo(userId, true, false);

        // 기기 지정 등록 코드
        // (-1 접속오류, 0:미등록PC, 1:공인인증서 사용만 가능한 PC, 2:공인인증서 발급도 가능한PC)
        int nResult = 0;

        nResult = qLoader.getPCDeginateInfo(null, "1016020500", userId, PRCSharedUtils.getIpinsideHdd());

        PcFixInfo fixInfo = ipinside.getPcFixInfoNOtherPCYes(String.valueOf(nResult), userId);

        sessionManager.setLoginValue("OtherPCYes", fixInfo.getOtherPcYes());
        sessionManager.setLoginValue("PcFixValue", fixInfo.getPcFixValue());
        /* 기기정보 세션 재설정 (E) */

        return response;

    }
}
