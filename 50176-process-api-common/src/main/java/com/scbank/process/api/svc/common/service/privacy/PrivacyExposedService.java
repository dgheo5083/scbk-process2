package com.scbank.process.api.svc.common.service.privacy;

import java.util.List;
import java.util.stream.Collectors;

import com.scbank.process.api.edmi.dto.oltp.CBIbk01D75600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D75600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00601Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00601Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H50400Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H50400Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H87300Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H87300Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.service.privacy.dto.InquiryExposedPersonRequest;
import com.scbank.process.api.svc.common.service.privacy.dto.InquiryExposedPersonResponse;
import com.scbank.process.api.svc.common.service.privacy.dto.PrimaryAccountRequest;
import com.scbank.process.api.svc.common.service.privacy.dto.PrimaryAccountResponse;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.FormatUtils;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(url = "/privacy/exposed", name = "개인정보노출")
public class PrivacyExposedService {

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    /**
     * HOST 통합 클라이언트
     */
    private final HostClient hostClient;

    @ServiceEndpoint(url = "/inquiryExposedPerson", name = "개인정보노출자 조회")
    public InquiryExposedPersonResponse inquiryExposedPerson(IServiceContext serviceContext,
            InquiryExposedPersonRequest request) {
        InquiryExposedPersonResponse response = new InquiryExposedPersonResponse();

        String sessionPerBusNo = StringUtils.defaultString(SessionUtils.getSessionValue("PerBusNo"));
        String perBusNo = "";

        if (StringUtils.isEmpty(sessionPerBusNo)) {
            perBusNo = request.getPerBusNo();

            if (StringUtils.isEmpty(perBusNo)) {
                response.setResCode("01");
                return response;
            }

            sessionManager.setGlobalValue("PerBusNo", perBusNo);
        }

        String sessionYoLrsotGb = SessionUtils.getSessionValue("YOLRSOTGB");

        if (StringUtils.isNotEmpty(sessionYoLrsotGb)) {
            // 이미 세션에 값이 있으면 다시 조회할 필요가 없다
            response.setResCode("00");
            response.setYoLrsotGb(sessionYoLrsotGb);
            return response;
        }

        OltpRequestOptions oltpOpt = hostClient.getOltpRequestOptions("CB_IBK01_H504");

        // 필수 공통부 설정
        oltpOpt.setImsTranCd("TI1IBK01");
        oltpOpt.setInClassCd("H504");
        oltpOpt.setSvcCd("504");

        // 업무 Host 거래 데이터 셋팅
        CbIbk01H50400Req h50400Req = new CbIbk01H50400Req();
        if (sessionManager.isLogin()) {
            String ssrBrNo = sessionManager.getLoginValue("SSRBrNo", String.class);
            String ssrKmCd = sessionManager.getLoginValue("SSRKmCD", String.class);
            String ssrGeNo = sessionManager.getLoginValue("SSRGeNo", String.class);
            String userAcctNum = ssrBrNo + ssrKmCd + ssrGeNo;

            h50400Req.setYILGGB("Y");
            h50400Req.setYIUSID(sessionManager.getLoginValue("UserID", String.class));
            h50400Req.setYIPASS(sessionManager.getLoginValue("TSPassword", String.class));
            h50400Req.setYIGJNO(userAcctNum);
        } else {
            h50400Req.setYILGGB("N");
            h50400Req.setYIUSID("FIRST999");
            h50400Req.setYIPASS("111111");
            h50400Req.setYIGJNO("");
        }
        h50400Req.setE2ERegNum(perBusNo);
        
        if(log.isDebugEnabled()) {
        	log.debug("PrivacyExposedService > inquiryExposedPerson - h50400Req : {} ", h50400Req.toString());
        }

        OltpResponse<CbIbk01H50400Res> oltpResponse = hostClient.sendOltp(oltpOpt, h50400Req,
                CbIbk01H50400Res.class);
        CbIbk01H50400Res h50400Res = oltpResponse.getResponse();
        
        if(log.isDebugEnabled()) {
        	log.debug("PrivacyExposedService > inquiryExposedPerson - h50400Res : {} ", h50400Res.toString());
        }
        
        response.setResCode("00");
        response.setYoLrsotGb(h50400Res.getYOLRSOTGB());
        
        return response;
    }
    
    
    @ServiceEndpoint(url = "/primaryAccount", name = "기본계좌변경")
    public PrimaryAccountResponse primaryAccount(PrimaryAccountRequest request) {
    	if("H866".equals(request.getServiceId())) { // 계좌목록 조회
    		return h866Service(request);
    		
    	} else if("H873".equals(request.getServiceId())) { // 계좌번호비밀번호 검증
    		return h873Service(request);
    		
    	} else if("D756".equals(request.getServiceId())) { // 기본계좌변경
    		return d756Service(request);
    		
    	} else {
    		log.error("serviceId is null");
    		throw new PRCServiceException("");
    	}
    }
    
    private PrimaryAccountResponse h866Service(PrimaryAccountRequest request) {
    	// MA3LGNPST001_101S
    	String e_connectType = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("e_ConnectType", String.class), "");
		String e_userID = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("e_UserID", String.class), "");
		String e_perNO = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("session_cid", String.class), "");

		String noLogin = StringUtils.defaultIfEmpty(request.getNoLogin(), "");
		String custNo = StringUtils.defaultIfEmpty(request.getCustNo(), "");
		String yocdcok = StringUtils.defaultIfEmpty(request.getYocdcok(), "");
		
		// 보유계좌조회 시작
        OltpRequestOptions h866Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H866");
        h866Options.setImsTranCd("TI1IBK01");
        h866Options.setInClassCd("H866");
        h866Options.setSvcCd("866");
        h866Options.setVanTp(IntegrationConstant.SELF_VAN_TYPE);
        
		// 개별부 세팅
        CbIbk01H86600Req h866Req = new CbIbk01H86600Req();
        
		if("Y".equals(noLogin)) {
			String perBusNo = sessionManager.getGlobalValue("PerBusNo", String.class);
			e_userID = StringUtils.defaultIfEmpty(request.getUserId(), "");
			
			// 본인확인 전문 처리
			OltpRequestOptions h006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");
			// 공통부 세팅
			h006Options.setImsTranCd("TI1IBK01");
			h006Options.setInClassCd("H006");
			h006Options.setSvcCd("006");
			
			//개별부 세팅
			CbIbk01H00601Req h006Req = new CbIbk01H00601Req();
			h006Req.setUserID(e_userID);
			h006Req.setCustJumin1(perBusNo.substring(0, 6)); //실명번호1
			h006Req.setCustJumin2(perBusNo.substring(6, 13)); //실명번호2
			h006Req.setSimplifyGB("2"); //인증서간소화 체크
			h006Req.setCertTranCode("6"); //거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
			h006Req.setChuryGubun("N"); //처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
			h006Req.setCAGubun("3"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증, 9:금융인증서)
			h006Req.setCustGubun("1"); //발급자구분(1:개인,2:기업)
			h006Req.setYIGSGB("1"); // 인증서간소발급자
			this.hostClient.sendOltp(h006Options, h006Req, CbIbk01H00601Res.class); // 7923 오류코드 UI로 던져질수있음
			
			h866Req.setUserID(e_userID);
			h866Req.setYIGIBONCH("Y"); // 이용자번호 기반인 경우 플래그값 (YIGIBONCH) 세팅
			
		} else {
			h866Req.setUserID( StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("UserID"), sessionManager.getGlobalValue("uid", String.class)));
			h866Req.setYIJMNO(StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), sessionManager.getGlobalValue("cid", String.class)));
			
			if("".equals(e_connectType)) {
				throw new PRCServiceException("PST0001", "조회 중 오류가 발생했습니다. 다시 시도해 주세요.");
			}
			
			if(e_connectType.matches("1|B|C")) {
				if("".equals(e_perNO)) {
					throw new PRCServiceException("PST0001", "조회 중 오류가 발생했습니다. 다시 시도해 주세요.");
				}
				h866Req.setYIJMNO(e_perNO);
				
			} else {
				if("".equals(e_userID)) {
					throw new PRCServiceException("PST0001", "조회 중 오류가 발생했습니다. 다시 시도해 주세요.");
				}
				h866Req.setUserID(e_userID);
				h866Req.setYIGIBONCH("Y");
				h866Req.setYIJMNO("");
			}
		}
		
		h866Req.setTSPassword(SessionUtils.getSessionValue("TSPassword", String.class));
		
        // 전문발송
        CbIbk01H86600Res h866Res = this.hostClient.sendOltp(h866Options, h866Req, CbIbk01H86600Res.class).getResponse();
    	
        PrimaryAccountResponse response = new PrimaryAccountResponse();
        
        response.setYoGUNSU(h866Res.getYOGUNSU());
        response.setYoIBID(h866Res.getYOIBID());
        response.setYoNAME(h866Res.getYONAME());
        response.setConnectType(e_connectType);
        response.setNoLogin(noLogin);
        response.setYocdcok(yocdcok);
        response.setCustNo(custNo);
        List<CbIbk01H86600Res.YOMYINF_REC> yomyinfRec = h866Res.getYOMYINF_REC();
        if (yomyinfRec != null) {
            response.setYoMYINF_REC(yomyinfRec.stream().map(o -> {
            	PrimaryAccountResponse.myinfRec record = new PrimaryAccountResponse.myinfRec();
                record.setMygj(o.getYOMYGJ());
                record.setMygjNm(PRCSharedUtils.getAccountName(o.getYOMYGJ().substring(3, 5), o.getYOZONG()));
                record.setMygjFmt(FormatUtils.getFrmAcct(o.getYOMYGJ()));
                return record;
            }).collect(Collectors.toList()));
        }
        
        //이용자번호로 조회 시 주민번호를 알 수 없어 주민번호를 세션에 저장한다.
        sessionManager.setGlobalValue("PerBusNo", StringUtils.defaultIfEmpty(e_perNO, h866Res.getYOJMNO()));
        sessionManager.setGlobalValue("UserID", StringUtils.defaultIfEmpty(e_userID, h866Res.getYOIBID()));
        sessionManager.setGlobalValue("D756_UserID", StringUtils.defaultIfEmpty(e_userID, h866Res.getYOIBID()));
		
    	return response;
    }
    
    private PrimaryAccountResponse h873Service(PrimaryAccountRequest request) {
    	// 본인확인 전문 처리
		OltpRequestOptions h873Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H873");
		// 공통부 세팅
		h873Options.setImsTranCd("TI1IBK01");
		h873Options.setInClassCd("H873");
		h873Options.setSvcCd("873");
		
		//개별부 세팅
		CbIbk01H87300Req h873Req = new CbIbk01H87300Req();
		h873Req.setCGAcctNum(request.getSelAcctNum());
		h873Req.setAcctPassword(request.getAcctBb());
		h873Req.setPasswordVerifiYorN("Y");
		h873Req.setPerNo(SessionUtils.getSessionValue("PerBusNo"));
		h873Req.setUserID(StringUtils.defaultIfEmpty(request.getUserId() , CommonBizConstants.DEFAULT_USER_ID));
		h873Req.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);
		this.hostClient.sendOltp(h873Options, h873Req, CbIbk01H87300Res.class);

		return new PrimaryAccountResponse();
    }
    
    private PrimaryAccountResponse d756Service(PrimaryAccountRequest request) {
    	String userID = StringUtils.nvl(request.getUserId(), "");
		String yigjwjn = StringUtils.nvl(request.getSubFAcctNum(), "");		//계좌점번호(3)
		String yigjwkm = StringUtils.nvl(request.getSubSAcctNum(), "");		//계좌과목(2)
		String yigjwno = StringUtils.nvl(request.getSubTAcctNum(), "");		//계좌번호(6)
		
		// 기본계좌변경 전문 처리
		OltpRequestOptions d756Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D756");
		// 공통부 세팅
		d756Options.setImsTranCd("TI1IBK01");
		d756Options.setInClassCd("D756");
		d756Options.setSvcCd("756");
		
		//개별부 세팅
		CbIbk01D75600Req d756Req = new CbIbk01D75600Req();
		d756Req.setYIUSID1(userID);
		d756Req.setYIPASS(CommonBizConstants.DEFAULT_TS_PASS_WORD);
		d756Req.setYIGJWJN(yigjwjn);
		d756Req.setYIGJWKM(yigjwkm);
		d756Req.setYIGJWNO(yigjwno);
		this.hostClient.sendOltp(d756Options, d756Req, CBIbk01D75600Res.class);
		
		//본거래 에러발생하지 않고 정상 응답 시 D756 거래를위해 임시로 세팅한 세션값을 초기화한다. 
		sessionManager.removeGlobalValue("FINCERT_VERIFIER_CINO");
		sessionManager.removeGlobalValue("FINCERT_VERIFIER_SERIAL");
		sessionManager.removeGlobalValue("FINCERT_VERIFIER_TYPE");
		sessionManager.removeGlobalValue("FINCERT_VERIFIER_TYPE");
		sessionManager.removeGlobalValue("FINCERT_VERIFIER_TYPE");
		sessionManager.removeGlobalValue("e_ConnectType");
		sessionManager.removeGlobalValue("e_UserID");
		sessionManager.removeGlobalValue("PerBusNo");
		sessionManager.removeGlobalValue("UserID");
		sessionManager.removeGlobalValue("D756_UserID");
		if(!sessionManager.isLogin()) sessionManager.removeGlobalValue("BIZ_AUTH_FLAG");
		
    	return new PrimaryAccountResponse();
    }
    
}