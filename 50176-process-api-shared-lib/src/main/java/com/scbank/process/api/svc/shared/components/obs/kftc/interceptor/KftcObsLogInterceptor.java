package com.scbank.process.api.svc.shared.components.obs.kftc.interceptor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.obs.IObsClientInterceptor;
import com.scbank.process.api.svc.shared.components.obs.kftc.model.KftcObsLogVo;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpJsonObject;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpRequestEntity;
import com.scbank.process.api.svc.shared.dao.OpenBankApiLogDao;
import com.scbank.process.api.svc.shared.dao.dto.OpenBankApiLogInsertLogParameter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 금결원 오픈뱅킹 로깅 인터셉터 구현체
 * 
 * @author sungdon.choi
 */
@Slf4j
public class KftcObsLogInterceptor implements IObsClientInterceptor {

	private static final String[] BANK_TRAN_ID_NAMES = {"bank_tran_id", "org_bank_tran_id"};
	private static final String API_TRAN_ID_NAME = "api_tran_id";
	
	public static String MA30_CHANNEL_GUBUN = "MA3";
	public static String MA30_CHANNEL_GUBUN_BATCH = "BAT";
	
	@Getter
	@Setter
	private KftcObsLogVo logVo;

	/**
	 * 
	 * @param userId 사용자ID
	 * @param userCifNo 사용자CI번호
	 * @param tradSeq 거래번호
	 * @return
	 */
	public static IObsClientInterceptor client(String userId, String userCifNo, Integer tradSeq) {
		KftcObsLogInterceptor log = new KftcObsLogInterceptor();
		log.setLogVo(new KftcObsLogVo(userId, userCifNo, tradSeq));
		log.getLogVo().setChnlGb(MA30_CHANNEL_GUBUN);
		
		return log;
	}

	/**
	 * 
	 * @param userId 사용자ID
	 * @param userCifNo 사용자CI번호
	 * @param tradSeq 거래번호
	 * @param chnlGb 채널구분
	 * @return
	 */
	public static IObsClientInterceptor client(String userId, String userCifNo, Integer tradSeq, String chnlGb) {
		KftcObsLogInterceptor log = new KftcObsLogInterceptor();
		log.setLogVo(new KftcObsLogVo(userId, userCifNo, tradSeq));
		log.getLogVo().setChnlGb(chnlGb);
		
		return log;
	}
	
	@Async("logExecutor")
	@Override
	public void before(Object... logValues) {
		
		/** 
		 * 거래로그만 생성시킨다.
		 */
		if (logVo.getUserId() != null && "".equals(logVo.getUserId().trim())) {
			return;
		}
		
		/**
		 * input check : USER_ID, USER_CIF_NO, TRAD_SEQ 
		 */
		String userId = StringUtils.defaultIfBlank(this.logVo.getUserId(), "-");
		String userCifNo = StringUtils.defaultIfBlank(this.logVo.getUserCifNo(), "-");
		
		if ("-".equals(userId) || "-".equals(userCifNo) || this.logVo.getTradSeq() == null) {
			log.error("userId[{}], userCifNo[{}] tradSeq[{}]필수 값입니다. 프로그램 데이타값 확인 부탁드립니다.", userId, userCifNo, this.logVo.getTradSeq());
			return;
		} /* end of if */
		
		ObsHttpRequestEntity entity = (ObsHttpRequestEntity)logValues[0];
		ObsHttpJsonObject jData = ObsHttpJsonObject.fromObject((JSONObject)entity.getBodyParameters());
		
		String bankTranId = StringUtils.defaultIfBlank((String)entity.getParameters().optString(BANK_TRAN_ID_NAMES[0]), "-");
		if ("-".equals(bankTranId)) {
			bankTranId = searchBankTranId(jData.getAsJsonObject());
		} /* end of if */
		
		this.logVo.setTradCls(KftcObsLogVo.TRAD_CLASS_SEND);
		this.logVo.setApiUrl(entity.getUrl());
		this.logVo.setBankTradId(bankTranId);
		this.logVo.setData(jData.getAsJsonString());
		this.logVo.setApiDetailCls(jData.getAsString("scope", "-"));
		this.logVo.setResCode(jData.getAsString("rsp_code", "-"));
		this.logVo.setBankRspCode(jData.getAsString("bank_rsp_code", "-"));
		
		OpenBankApiLogInsertLogParameter parameter = OpenBankApiLogInsertLogParameter.builder()
				.userId(userId)
				.userCifNo(userCifNo)
				.tradCls(this.logVo.getTradCls())
				.bankTradId(this.logVo.getBankTradId())
				.apiTradId("-")
				.tradSeq(this.logVo.getTradSeq())
				.chnlGb(this.logVo.getChnlGb())
				.apiUrl(this.logVo.getApiUrl())
				.data(this.logVo.getData())
				.apiDetailCls(this.logVo.getApiDetailCls())
				.resCode(this.logVo.getResCode())
				.bankRspCode(this.logVo.getBankRspCode())
				.build();
		
		log.debug("# before log parameter: {}", parameter);
		
		this.insertApiLog(parameter);
	}

	@Async("logExecutor")
	@Override
	public void after(Object... logValues) {
		/**
		 * 거래로그만 생성시킨다.
		 */
		if (logVo.getUserId() != null && "".equals(logVo.getUserId().trim())) {
			return;
		}
		/**
		 * input check : USER_ID, USER_CIF_NO, TRAD_SEQ 
		 */
		String userId = StringUtils.defaultIfBlank(this.logVo.getUserId(), "-");
		String userCifNo = StringUtils.defaultIfBlank(this.logVo.getUserCifNo(), "-");
		
		if ("-".equals(userId) || "-".equals(userCifNo) || this.logVo.getTradSeq() == null) {
			log.error("userId[{}], userCifNo[{}] tradSeq[{}]필수 값입니다. ", userId, userCifNo, this.logVo.getTradSeq());
			return;
		} /* end of if */
		
		ObsHttpRequestEntity entity = (ObsHttpRequestEntity)logValues[0];
		ObsHttpJsonObject jData = ObsHttpJsonObject.fromObject((JSONObject)logValues[1]);
		
		//Data OverFlow방지.
		String jData19999 = jData.getAsJsonString();
		if(jData19999.length() > 19999) {
			jData19999 = jData19999.substring(0, 19999);
		}
		
		this.logVo.setTradCls(KftcObsLogVo.TRAD_CLASS_RECV);
		this.logVo.setApiUrl(entity.getUrl());
		this.logVo.setApiTradId(jData.getAsString(API_TRAN_ID_NAME, "-"));
		this.logVo.setData(jData19999);
		this.logVo.setResCode(jData.getAsString("rsp_code", "-"));
		this.logVo.setBankRspCode(jData.getAsString("bank_rsp_code", "-"));
		
		OpenBankApiLogInsertLogParameter parameter = OpenBankApiLogInsertLogParameter.builder()
				.userId(userId)
				.userCifNo(userCifNo)
				.tradCls(this.logVo.getTradCls())
				.bankTradId(this.logVo.getBankTradId())
				.apiTradId(this.logVo.getApiTradId())
				.tradSeq(this.logVo.getTradSeq())
				.chnlGb(this.logVo.getChnlGb())
				.apiUrl(this.logVo.getApiUrl())
				.data(this.logVo.getData())
				.apiDetailCls(this.logVo.getApiDetailCls())
				.resCode(this.logVo.getResCode())
				.bankRspCode(this.logVo.getBankRspCode())
				.build();
		
		log.debug("# after log parameter: {}", parameter);
		
		this.insertApiLog(parameter);
	}
	
	/**
	 * 오픈뱅킹 API 통신 로그를 적재한다.
	 * @param parameter {@link OpenBankApiLogInsertLogParameter}
	 */
	private void insertApiLog(OpenBankApiLogInsertLogParameter parameter) {
		OpenBankApiLogDao openBankApiLogDao = RuntimeContext.getBean(OpenBankApiLogDao.class);
		try {
			openBankApiLogDao.insertLog(parameter);
		} catch (Exception e) {
			log.error("OBS API DB ERROR: " + e.getMessage(), e);
		}
	}

	/**
	 * req_list 배열로 들어왔을 경우 첫번째 데이타를 마스터 데이타로 활요한다.
	 * BANK_TRAN_ID_NAMES = {"bank_tran_id", "org_bank_tran_id"};
	 * @param jObj
	 * @return bank_tran_id:string
	 */
	private String searchBankTranId(JSONObject jObj) {
		if (!jObj.has("req_list")) {
			return "-";
		}
		
		Object list = jObj.opt("req_list");
		if (list instanceof JSONArray) {
			JSONArray jaReqList = (JSONArray)list;
			if (jaReqList.length() > 0) {
				ObsHttpJsonObject oitem = ObsHttpJsonObject.fromObject((JSONObject)jaReqList.opt(0));
				for (String tranId : BANK_TRAN_ID_NAMES) {
					String val = oitem.getAsString(tranId, "-");
					if (!"-".equals(val)) {
						return val;
					} /* end of if */
				} /* end of for */
				
				log.debug("SEND bank_tran_id not found [{}]", oitem.getAsJsonString());
			} /* end of if */
		} /* end of if */
		
		return "-";
	}
}