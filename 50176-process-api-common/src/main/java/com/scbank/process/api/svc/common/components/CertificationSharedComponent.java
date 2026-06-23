package com.scbank.process.api.svc.common.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.scbank.process.api.edmi.dto.host.CbIbk01H504Req;
import com.scbank.process.api.edmi.dto.host.CbIbk01H504Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "인증센터 공용기능")
public class CertificationSharedComponent {
	
	private final HostClient hostClient;
	private final ISessionContextManager sessionManager;
	
	@ComponentOperation(name = "개인정보 노출자 여부 판단", description = "개인정보 노출자 여부 판단", author = "오은진")
	public Map<String, String> getYoLRSOTGB(String ssn) {
		log.debug("getYoLRSOTGB 시작");

		String perBusNo = SessionUtils.getSessionValue("PerBusNo", String.class);
		String sessionYOLRSOTGB = SessionUtils.getSessionValue("YOLRSOTGB", String.class);
		String yoLRSOTGB = "";
		boolean isLoginFlg = sessionManager.isLogin();

		// 응답값
		Map<String, String> resMap = new HashMap<String, String>();

		if(StringUtils.isEmpty(perBusNo)) {
			perBusNo = ssn.isEmpty()? "" : ssn;

			if("".equalsIgnoreCase(perBusNo)) {
				resMap.put("ERRORKEY", "Y");
				return resMap;
			} else {
				sessionManager.setGlobalValue(perBusNo, perBusNo);
			}
		}

		if(StringUtils.isNotEmpty(sessionYOLRSOTGB)) {
			// 이미 세션에 존재하는 경우 세션값 사용
			yoLRSOTGB = sessionYOLRSOTGB;
			resMap.put("YOLRSOTGB", yoLRSOTGB);
		} else {
			log.debug("조회 시작");
			try {
				OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK01_H504");
				CbIbk01H504Req h504Req = new CbIbk01H504Req();

				hostCfg.setImsTranCd("TI1IBK01");
				hostCfg.setInClassCd("H504");
                hostCfg.setSvcCd("504");
                h504Req.setE2ERegNum(perBusNo);	//주민번호

                if(isLoginFlg) {
                	String ssrBrNo = sessionManager.getLoginValue("SSRBrNo", String.class);
					String ssrKmCd = sessionManager.getLoginValue("SSRKmCD", String.class);
					String ssrGeNo = sessionManager.getLoginValue("SSRGeNo", String.class);
					String userAcctNum = ssrBrNo + ssrKmCd + ssrGeNo;

					h504Req.setYILGGB("Y");															//로그인여부
					h504Req.setYIUSID(sessionManager.getLoginValue("UserID", String.class));			//이용자번호
					h504Req.setYIPASS(sessionManager.getLoginValue("TSPassword", String.class));		//이용자비밀번호
					h504Req.setYIGJNO(userAcctNum);													//계좌번호
                } else {
                	h504Req.setYILGGB("N");	//로그인여부
					h504Req.setYIUSID("N");	//이용자번호
					h504Req.setYIPASS("N");	//이용자비밀번호
					h504Req.setYIGJNO("");		//계좌번호
                }

                OltpResponse<CbIbk01H504Res> hostResponse = this.hostClient.sendOltp(hostCfg, h504Req, CbIbk01H504Res.class);
                CbIbk01H504Res h504Res = hostResponse.getResponse();

                yoLRSOTGB = com.scbank.process.api.fw.core.utils.StringUtils.nvl(h504Res.getYOLRSOTGB(), "") ;
                log.debug("getYoLRSOTGB YOLRSOTGB :: {}", yoLRSOTGB);
                log.debug("getYoLRSOTGB isLogined :: {}", isLoginFlg);
			} catch (PRCServiceException e) {
				log.debug("getYoLRSOTGB EXCEPTION START");
				e.printStackTrace();
				log.debug("getYoLRSOTGB EXCEPTION END");
			}

			resMap.put("YOLRSOTGB", yoLRSOTGB);

			sessionManager.setGlobalValue("YOLRSOTGB", yoLRSOTGB);
			if(isLoginFlg) {
				sessionManager.setLoginValue("YOLRSOTGB", yoLRSOTGB);
			}
		}

		log.debug("getYoLRSOTGB 종료");
		return resMap;
	}

}