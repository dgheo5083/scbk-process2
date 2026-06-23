package com.scbank.process.api.svc.common.service.sample;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.cert.X509Certificate;
import java.util.List;

import com.initech.asn1.BERDecoder;
// import com.initech.inisafesign.INISAFESign;
import com.initech.pkcs.pkcs7.Data;
import com.initech.pkcs.pkcs7.SignedData;
import com.scbank.process.api.edmi.dto.host.Ti1tbs03H211Req;
import com.scbank.process.api.edmi.dto.host.Ti1tbs03H211Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.exception.OltpSystemException;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.svc.common.dao.SignTransactionDao;
import com.scbank.process.api.svc.common.dao.dto.SignInfoResult;
import com.scbank.process.api.svc.common.service.sample.dto.MultiSignVerifyRequest;
import com.scbank.process.api.svc.common.service.sample.dto.MultiSignVerifyResponse;
import com.scbank.process.api.svc.common.service.sample.dto.SignDecryptSampleRequest;
import com.scbank.process.api.svc.common.service.sample.dto.SignVerifyRequest;
import com.scbank.process.api.svc.common.service.sample.dto.SignVerifyResponse;
import com.scbank.process.api.svc.common.service.sample.dto.TestSignVerifyRequest;
import com.scbank.process.api.svc.common.service.sample.dto.TestTransferInfo;
import com.scbank.process.api.svc.common.utils.Base64Util;
import com.scbank.process.api.svc.shared.components.auth.SecureDataComponent;
import com.scbank.process.api.svc.shared.components.cert.DigitalCertVerifyComponent;
import com.scbank.process.api.svc.shared.components.sign.constants.SignConstants;
import com.scbank.process.api.svc.shared.components.sign.dto.SignVerifyInfo;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignVerifyType;
import com.scbank.process.api.svc.shared.components.sign.utils.SignUtils;
import com.scbank.process.api.svc.shared.constants.PRCSharedEnums.LangType;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.wizvera.WizveraConfig;
import com.wizvera.service.SignVerifier;
import com.wizvera.service.SignVerifierResult;
import com.wizvera.util.Base64;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(url = "/sample/sign", name = "샘플 전자서명 서비스")
public class SampleSignService {

    /**
     * HOST 통합 클라이언트
     */
    private final HostClient hostClient;

    /**
     * 전자서명 기능 Component
     */
    private final SecureDataComponent secureDataComponent;

    /**
     * 전자서명 test dao
     */
    private final SignTransactionDao testSignDao;

    /**
     * 전자서명 dao
     */
    private final com.scbank.process.api.svc.shared.dao.SignTransactionDao signDao;

    /**
     * 디지털인증서 검증 컴포넌트
     */
    private final DigitalCertVerifyComponent digitalCertVerifyComponent;

    @ServiceEndpoint(url = "/testSignVerify", name = "서명 검증 테스트")
    public SignVerifyResponse testSignVerify(IServiceContext serviceContext,
            SignVerifyRequest request) {
        SignVerifyResponse response = new SignVerifyResponse();

        // ... (업무 biz Logic)

        // OLTP 전문 설정
        OltpRequestOptions oltpOpt = hostClient.getOltpRequestOptions("CB_TBS03_D211");

        // OLTP 필수 공통부 설정
        oltpOpt.setImsTranCd("TI1TBS03");
        oltpOpt.setInClassCd("D211");
        oltpOpt.setSvcCd("21B");
        oltpOpt.setCaptureSystem("OLTP");

        // Mci 전문 설정
        // MciRequestOptions mciOpt =
        // this.hostClient.getMciRequestOptions("MCI_IB_LMGE01_01");

        // MCI 전문 설정
        // mciOpt.setTranCd("IB_LMGE01_01"); // BUSINESS_FUNCTION_ID
        // mciOpt.setTxnBrNo("0019"); // HOMEBRANCH
        // mciOpt.setBlngBrNo("0019"); // AGENTBRANCH

        /************************************
         * 전자서명 설정 START
         ************************************/

        /**
         * 1. ASIS 기준 업무프로세스 중 전자서명을 수행하던 업무 대상은 VERIFY_N_SAVE 설정
         * - VERIFY_N_SAVE 으로 설정 하시더라도 ASIS기준 공통처리는 유지됩니다.
         * => 로그인 거래 전체 : VERIFY_N_SAVE 일때 저장 수행 / VERIFY 로 설정하시면 저장 미수행
         * => 비로그인 거래 전체(D47R/D748/D756 제외) : VERIFY_N_SAVE 이더라도 서명 저장 미수행
         */
        oltpOpt.setAttribute(SignConstants.VERIFY_TYPE, SignVerifyType.VERIFY_N_SAVE);

        /************************************
         * 전자서명 설정 END
         ************************************/

        TestTransferInfo data = request.getTransferInfo();

        // 업무 Host 거래 데이터 셋팅
        Ti1tbs03H211Req h211Req = new Ti1tbs03H211Req();
        h211Req.setCMSCode(data.getCMSCode());
        h211Req.setCgAcctNum(data.getCgAcctNum());
        h211Req.setFeeAmt(new BigDecimal(data.getFeeAmt()));
        h211Req.setFeeCode(data.getFeeCode());
        h211Req.setHgRName(data.getHgRName());
        h211Req.setHgSName(data.getHgSName());
        h211Req.setIgBankCode(data.getIgBankCode());
        h211Req.setJYCode(data.getJYCode());
        h211Req.setGrNum(data.getGrNum());
        h211Req.setYyDate(data.getYyDate());
        h211Req.setYyTime(data.getYyTime());
        h211Req.setYyDate(data.getYyDate());
        h211Req.setTotSgAmt(new BigDecimal(data.getTotSgAmt()));
        h211Req.setTotFeeAmt(new BigDecimal(data.getTotFeeAmt()));
        h211Req.setSgAmt(new BigDecimal(data.getSgAmt()));
        h211Req.setJsNum(data.getJsNum());
        h211Req.setJgCode(data.getJgCode());
        h211Req.setJbCode(data.getJbCode());

        // 전자서명 서명 필드(LocalField)
        h211Req.setNoTransField1("거래에");
        h211Req.setNoTransField2("불필요한");
        h211Req.setNoTransField3("필드");

        try {
            OltpResponse<Ti1tbs03H211Res> hostResponse = hostClient.sendOltp(oltpOpt, h211Req,
                    Ti1tbs03H211Res.class);

        } catch (OltpSystemException e) {
            if (log.isErrorEnabled()) {
                log.error("전자서명 샘플 더미데이터 거래오류 SKIP - errorCode : [{}], errorMessage : [{}]", e.getErrorCode(),
                        e.getErrorMessage());
            }
        } catch (PRCServiceException e) {
            if (log.isErrorEnabled()) {
                log.error("전자서명 샘플 검증 실패");
            }
            throw e;
        }

        // ... (업무 biz Logic)

        return response;
    }

    @ServiceEndpoint(url = "/testMultiSignVerify", name = "다건 서명 검증 테스트")
    public MultiSignVerifyResponse testMultiSignVerify(IServiceContext serviceContext,
            MultiSignVerifyRequest request) {
        MultiSignVerifyResponse response = new MultiSignVerifyResponse();

        // ... (업무 biz Logic)

        // Host 전문 설정
        OltpRequestOptions oltpOpt = hostClient.getOltpRequestOptions("CB_TBS03_D211");

        // Host 필수 공통부 설정
        oltpOpt.setImsTranCd("TI1TBS03");
        oltpOpt.setInClassCd("D211");
        oltpOpt.setSvcCd("21B");
        oltpOpt.setCaptureSystem("OLTP");
        oltpOpt.setRealTran(true);

        // 전자서명 설정
        // 전자서명 검증실행 타입 (NONE : 검증&저장 미수행 / VERIFY_N_SAVE : 검증&저장 수행)
        oltpOpt.setAttribute(SignConstants.VERIFY_TYPE, SignVerifyType.VERIFY_N_SAVE);
        // 전자서명 업무 타입 = 생략(default: SIGN)
        // oltpOpt.setAttribute(SignConstants.ACTION_TYPE, SignActionType.SIGN);

        // 공통 보안매체 검증
        // secureDataComponent.verifyVerification();

        List<TestTransferInfo> multiTransferInfoList = request.getMultiTransferInfoList();

        int count = 0;
        for (TestTransferInfo data : multiTransferInfoList) {
            Ti1tbs03H211Req h211Req = new Ti1tbs03H211Req();
            h211Req.setCMSCode(data.getCMSCode());
            h211Req.setCgAcctNum(data.getCgAcctNum());
            h211Req.setFeeAmt(new BigDecimal(data.getFeeAmt()));
            h211Req.setFeeCode(data.getFeeCode());
            h211Req.setHgRName(data.getHgRName());
            h211Req.setHgSName(data.getHgSName());
            h211Req.setIgBankCode(data.getIgBankCode());
            h211Req.setJYCode(data.getJYCode());
            h211Req.setGrNum(data.getGrNum());
            h211Req.setYyDate(data.getYyDate());
            h211Req.setYyTime(data.getYyTime());
            h211Req.setYyDate(data.getYyDate());
            h211Req.setTotSgAmt(new BigDecimal(data.getTotSgAmt()));
            h211Req.setTotFeeAmt(new BigDecimal(data.getTotFeeAmt()));
            h211Req.setSgAmt(new BigDecimal(data.getSgAmt()));
            h211Req.setJsNum(data.getJsNum());
            h211Req.setJgCode(data.getJgCode());
            h211Req.setJbCode(data.getJbCode());

            // 전문DTO 없던 필드(실제 거래 데이터가 아닌 서명검증용 데이터)
            h211Req.setNoTransField("no".repeat(++count));

            log.debug("testMultiSignVerify - count : [{}], NoTransField : {}", count, h211Req.getNoTransField());

            try {
                // 전자서명 검증 및 거래 호출
                OltpResponse<Ti1tbs03H211Res> oltpResponse = this.hostClient.sendOltp(oltpOpt, h211Req,
                        Ti1tbs03H211Res.class);

                // (추가인증)/(보안매체)/전자서명 검증 및 거래 호출
                // OltpResponse<CbTbs03D21100Res> oltpResponse =
                // this.hostClient.sendOltpWithSecure(oltpOpt, d21100Req,
                // CbTbs03D21100Res.class);

            } catch (OltpSystemException e) {
                if (log.isErrorEnabled()) {
                    log.error("전자서명 샘플 더미데이터 거래오류 SKIP - errorCode : [{}], errorMessage : [{}]", e.getErrorCode(),
                            e.getErrorMessage());
                }
                continue;
            } catch (PRCServiceException e) {
                if (log.isErrorEnabled()) {
                    log.error("전자서명 샘플 검증 실패");
                }
                throw e;
            }
        }

        // ... (업무 biz Logic)

        return response;
    }

    @ServiceEndpoint(url = "/testNoTransSignVerify", name = "거래 없이 추가인증/보안매체/전자서명 검증만 하는 케이스 테스트")
    public SignVerifyResponse testNoTransSignVerify(IServiceContext serviceContext,
            SignVerifyRequest request) {
        SignVerifyResponse response = new SignVerifyResponse();

        // ... (업무 biz Logic)

        /**
         * (추가인증)/보안매체 검증 (2번째 인자인 serviceCode가 없었다면 전자서명 검증 X)
         * AS-IS : this.secureTransfer(ctx, "", sendData);
         * AS-IS : this.secureTransfer(ctx, "", sendData, true);
         * 호출하던 업무 에서 전자서명을 진행하지 않던 케이스
         */
        // TO-BE : (추가인증)/보안매체 검증 실행
        // secureDataComponent.verifyVerification();

        /************************************
         * 전자서명 설정 START
         ************************************/
        SignVerifyInfo signVerifyInfo = new SignVerifyInfo();
        /*
         * AS-IS: 공통부 설정 데이터
         * sendData.putMciComm("BUSINESS_FUNCTION_ID", "")
         * sendData.putHostWebComm("IMS_TRAN_CODE", "");
         * sendData.putHostWebComm("IN_CLASS_CODE", "D211");
         * sendData.putHostWebComm("SERVICE_CODE", "");
         * sendData.putHostWebComm("JOB_TYPE", "");
         */

        // TO-BE: 전문 공통부 설정 불가하므로 서명 검증 요청 객체에 설정
        // signVerifyInfo.setTranCd("");
        signVerifyInfo.setImsTranCd("");
        signVerifyInfo.setInClassCd("D211");
        signVerifyInfo.setSvcCd("");
        // signVerifyInfo.setJobTp("");

        // ASIS 기준 업무프로세스 중 전자서명을 수행하던 업무 대상은 VERIFY_N_SAVE 설정
        signVerifyInfo.setSignVerifyType(SignVerifyType.VERIFY_N_SAVE);
        /************************************
         * 전자서명 설정 END
         ************************************/

        Integer multiCount = 1; // 다건일 경우 반복 처리
        for (int i = 0; i < multiCount; i++) {

            // VIEW (프론트엔드)에서 설정한 서명데이터의 키와 동일한 키로 설정
            signVerifyInfo
                    .clearMessage()
                    .putMessage("imsTranCd", "TI1TBS03")
                    .putMessage("inClassCd", "D211")
                    .putMessage("svcCd", "21B")

                    .putMessage("JbCode", "0")
                    .putMessage("JYCode", "1")
                    .putMessage("HgSName", "가나다")
                    .putMessage("GrNum", "0")
                    .putMessage("YyDate", "20251017")
                    .putMessage("TotFeeAmt", "0")
                    .putMessage("YyTime", "0")
                    .putMessage("JgCode", "1")
                    .putMessage("CMSCode", "")
                    .putMessage("FeeAmt", "0")
                    .putMessage("FeeCode", "8")
                    .putMessage("IgBankCode", "007")
                    .putMessage("CgAcctNum", "86020265089")
                    .putMessage("TotSgAmt", "10000")
                    .putMessage("SgAmt", "10000")
                    .putMessage("HgRName", "일고객")
                    .putMessage("JsNum", "8");

            /**
             * 전문거래 없는 전자서명 검증
             * AS-IS : this.transfer(ctx, "", sendData, true);
             * 호출하던 업무 케이스
             */
            // TO-BE : 전자서명 검증 실행
            secureDataComponent.verifySign(signVerifyInfo);

            /**
             * (추가인증)/보안매체/전자서명 검증
             * AS-IS : this.secureTransfer(ctx, "[업무ID]", sendData)
             * AS-IS : this.secureTransfer(ctx, "[업무ID]", sendData, true);
             * 호출하던 업무 케이스
             */
            // TO-BE : (추가인증)/보안매체/전자서명 검증 실행
            // secureDataComponent.verifyAll(signCompareMap, signVerifyInfo);
        }

        // ... (업무 biz Logic)

        return response;
    }

    @ServiceEndpoint(url = "/testVerify", name = "검증 테스트")
    public void testVerify(IServiceContext serviceContext,
            TestSignVerifyRequest request) {

        String connectType = request.getConnectType();
        String signedData = request.getSignedData();

        if (log.isDebugEnabled()) {
            log.debug("SampleSignService > testVerify - connectType : [{}]", connectType);
            log.debug("SampleSignService > testVerify - signedData : [{}]", signedData);
            log.debug("SampleSignService > testVerify - LangType.KOREAN : [{}]", LangType.KOREAN);
            log.debug("SampleSignService > testVerify - getLanguageHeader() : [{}]",
                    PRCSharedUtils.getLanguageHeader());
            log.debug(
                    "SampleSignService > testVerify - currentLanguageCode : [{}]",
                    LangType.fromCode(PRCSharedUtils.getLanguageHeader()));
            log.debug(
                    "SampleSignService > testVerify - LangType.KOREAN == isKorean : [{}]",
                    LangType.KOREAN == LangType.fromCode(PRCSharedUtils.getLanguageHeader()));
        }

        try {
            if ("9".equals(connectType)) {
                // 디지털인증서 검증
                digitalCertVerifyComponent.verify("L", signedData);
            }
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("SampleSignService > testVerify : Fail");
            }
            e.printStackTrace();
        }

    }

    @ServiceEndpoint(url = "/testDecryptSign", name = "서명 복호화 테스트")
    public void testDecryptSign(IServiceContext serviceContext, SignDecryptSampleRequest request) throws Exception {

        byte[] encryptedSignedByte = null;

        if ("1".equals(request.getParamType())) {
            // CASE2 : DB 조회 조건
            List<SignInfoResult> result = testSignDao.selectSignInfo(request.getSelectDBCondition());
            if (result.isEmpty()) {
                throw new PRCServiceException("조건에 맞는 서명데이터 없음");
            }
            log.debug("testDecryptSign result : {}", result.toString());
            encryptedSignedByte = result.get(0).getEncSign();
        } else if ("2".equals(request.getParamType())) {
            // CASE1 : 암호화 전자서명 데이터
            encryptedSignedByte = asis_PMS_SignDataToByteArr(request.getEncryptedSignString());
        } else {
            throw new PRCServiceException("조회조건 없음(1or2)");
        }

        byte[] decryptedSignedByte = SignUtils.decrypt(encryptedSignedByte);

        try {
            asis_PMS_getSignInfo(decryptedSignedByte);
        } catch (Exception e) {
            log.error("Fail to asis getSignInfo");
        }

        try {
            tobe_getSignInfo(decryptedSignedByte);
        } catch (Exception e) {
            log.error("Fail to tobe getSignInfo");
        }

    }

    private static byte[] asis_PMS_SignDataToByteArr(String signDataString) throws Exception {
        byte[] result = null;
        try {
            result = org.apache.commons.codec.binary.Hex.decodeHex(signDataString.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    private static void asis_PMS_getSignInfo(byte[] signedbyte) throws Exception {

        byte[] signByte = Base64Util.encode(signedbyte);
        String signedData = new String(signByte);

        log.debug("asis_PMS_getSignInfo - signedData : {}", signedData);

        try {
            BERDecoder decoder = null;
            InputStream bais = new ByteArrayInputStream(signedbyte);
            decoder = new BERDecoder(bais);
            com.initech.pkcs.pkcs7.ContentInfo ci = new com.initech.pkcs.pkcs7.ContentInfo();
            ci.decode(decoder);
            SignedData signedDataObject = (SignedData) ci.getContent();
            com.initech.pkcs.pkcs7.ContentInfo info = signedDataObject.getContentInfo();

            // String tempclient_sign_data = "";
            // if( "MA30".equals(SYSNAME) || "MA30_B".equals(SYSNAME) ||
            // "MA30_C".equals(SYSNAME) || "MA30_D".equals(SYSNAME) ||
            // "OIBSIT_B".equals(SYSNAME) || "OIB_B".equals(SYSNAME) ){
            // tempclient_sign_data = URLDecoder.decode(getSignData(deCrypedENCSIGN, true),
            // "utf-8");
            String orgData = new String(((Data) info.getContent()).getRawContent(), "utf-8");
            log.debug("asis_PMS_getSignInfo - orgData(Joint/Fin/Toss) : {}", orgData);
            String decodedOrgData = URLDecoder.decode(orgData, "utf-8");
            log.debug("asis_PMS_getSignInfo - decodedOrgData : {}", decodedOrgData);

            // String reuslt2 = ((Data) info.getContent()).toString();
            // log.debug("getSignData - reuslt2(Else) : {}", reuslt2);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        // 로컬 라이센스 불가능(호스트기반)
        // try {
        // final String INISAFESIGN_PROPERTIES_PATH =
        // "C:\\jdk\\MobileBanking\\workspace\\process-api-memo\\ESIGN\\asis_Initech_module\\inisafeSign\\local_INISAFESign.properties";
        // INISAFESign signer = new INISAFESign(Base64Util.encode(signedbyte),
        // INISAFESIGN_PROPERTIES_PATH);
        // boolean isVerified = signer.verifyPKCS7();
        // X509Certificate cert = signer.getCertificate();
        // log.debug("tobe_getX509Data - signTime : {}", "signTime............");
        // log.debug("tobe_getX509Data - getNotBefore : {}", cert.getNotBefore());
        // log.debug("tobe_getX509Data - getNotAfter : {}", cert.getNotAfter());
        // log.debug("tobe_getX509Data - getSerialNumber : {}", cert.getSerialNumber());
        // log.debug("tobe_getX509Data - getIssuerDN : {}", cert.getIssuerDN());
        // log.debug("tobe_getX509Data - getSubjectDN : {}", cert.getSubjectDN());
        // log.debug("tobe_getX509Data - isPKCS7 : {}", isVerified);
        // log.debug("tobe_getX509Data - getOriginSignedRawData : {}",
        // signer.getPKCS7Manager().getPlainTextinSignedData());
        // } catch (Exception e) {
        // e.printStackTrace();
        // throw e;
        // }
    }

    private static void tobe_getSignInfo(byte[] decryptedSignedbyte) throws Exception {

        final String configPath = "C:/jdk/MobileBanking/workspace/50176-bsoi-common/src/main/resources/config/wizvera/delfino.properties";

        X509Certificate cert = null;
        SignVerifierResult signVerifierResult = null;
        try {
            String signedData = Base64.encode(decryptedSignedbyte);

            log.debug("tobe_getX509Data - signedData : {}", signedData);

            WizveraConfig delfinoConfig = new WizveraConfig(configPath);
            SignVerifier signVerifier = new SignVerifier(delfinoConfig);

            // signVerifierResult = signVerifier.verifyPKCS7(signedData,
            // SignVerifier.CERT_STATUS_CHECK_TYPE_NONE);
            signVerifierResult = signVerifier.verifyPKCS7WithoutCertValidation(signedData);
            log.debug("tobe_getX509Data - verify Success");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        try {
            cert = signVerifierResult.getSignerCertificate();

            // TODO : 서명시간 뽑는 기능 확인 필요
            log.debug("tobe_getX509Data - signTime : {}", "ex) 2025/07/24/17:40:17");
            log.debug("tobe_getX509Data - getNotBefore : {}", cert.getNotBefore());
            log.debug("tobe_getX509Data - getNotAfter : {}", cert.getNotAfter());
            log.debug("tobe_getX509Data - getSerialNumber : {}", cert.getSerialNumber());
            log.debug("tobe_getX509Data - getIssuerDN : {}", cert.getIssuerDN());
            log.debug("tobe_getX509Data - getSubjectDN : {}", cert.getSubjectDN());
            log.debug("tobe_getX509Data - isPKCS7 : {}", signVerifierResult != null ? true : false);
            log.debug("tobe_getX509Data - getOriginSignedRawData : {}", signVerifierResult.getOriginSignedRawData());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}