package com.scbank.process.api.svc.shared.components.frs.kftc.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.svc.shared.components.frs.IFrsClientInterceptor;
import com.scbank.process.api.svc.shared.components.frs.kftc.model.KftcFrsLogVo;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpJsonObject;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpRequestEntity;
import com.scbank.process.api.svc.shared.dao.FaceRecApiLogDao;
import com.scbank.process.api.svc.shared.dao.dto.FaceRecApiLogInsertParameter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 금결원 안면인식 로깅 인터셉터 구현체
 * 
 * @author sungdon.choi
 */
@Slf4j
public class KftcFrsLogInterceptor implements IFrsClientInterceptor {

	public static String MA30_CHANNEL_GUBUN = "MA3";
	public static String MA30_CHANNEL_GUBUN_BATCH = "BAT";
	
	@Getter
	@Setter
	private KftcFrsLogVo logVo;
	
	public static IFrsClientInterceptor client(String userCifNo, String faceTransactionId, String custNo, String tradNo) {
		
		KftcFrsLogInterceptor log = new KftcFrsLogInterceptor();
		log.setFrsLogVo(new KftcFrsLogVo(userCifNo, faceTransactionId, custNo, tradNo));
		log.getFrsLogVo().setChnlGb(MA30_CHANNEL_GUBUN);
		
		return log;
	}
	
	public KftcFrsLogVo getFrsLogVo() {
		return logVo;
	}

	public void setFrsLogVo(KftcFrsLogVo logVo) {
		this.logVo = logVo;
	}
	
	@Async("logExecutor")
	@Override
	public void before (Object... logValues) {
		
		/**
		 * 거래로그만 생성시킨다.
		 */
		if(!inputValidation()) return;
		
		FrsHttpRequestEntity entity = (FrsHttpRequestEntity)logValues[0];
		
		FrsHttpJsonObject jData = FrsHttpJsonObject.fromObject((JSONObject)entity.getBodyParameters());
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("transaction_id", jData.getAsString("transaction_id", ""));
		jsonObj.put("customer_channel", jData.getAsString("customer_channel", ""));
		jsonObj.put("id_card_code", jData.getAsString("id_card_code", ""));
		jsonObj.put("request_datetime", jData.getAsString("request_datetime", ""));
		jsonObj.put("org_code", jData.getAsString("org_code", ""));
		jsonObj.put("selfie_data", "");
		jsonObj.put("id_card_data", "");
		
		String jData19999 = preventDataOverFlow(jsonObj.toString());
		
		this.logVo.setTradCls(KftcFrsLogVo.TRAD_CLASS_SEND);
		this.logVo.setApiUrl(entity.getUrl());
		this.logVo.setData(jData19999);
		this.logVo.setResCode(jData.getAsString("response_code", "-"));
		
		FaceRecApiLogInsertParameter params = FaceRecApiLogInsertParameter.builder()
				.userCifNo(logVo.getUserCifNo())
				.faceTransactionId(logVo.getFaceTransactionId())
				.tradNo(logVo.getTradNo())
				.tradCls(logVo.getTradCls())
				.chnlGb(logVo.getChnlGb())
				.apiUrl(logVo.getApiUrl())
				.data(logVo.getData())
				.resCode(logVo.getResCode())
				.custNo(logVo.getCustNo())
				.build();
		
		log.debug("안면인식 거래 전 로그 parameter => {}", params);
		
		logInsertFrsApi(params);
	}

	/**
	 * 전문호출 후에 호출된다.
	 */
	@Async("logExecutor")
	@Override
	public void after (Object... logValues) {
		
		/**
		 * 거래로그만 생성시킨다.
		 */
		if(!inputValidation()) return;

		FrsHttpRequestEntity entity = (FrsHttpRequestEntity)logValues[0];
		FrsHttpJsonObject jData = FrsHttpJsonObject.fromObject((JSONObject)logValues[1]);
		
		String jData19999 = preventDataOverFlow(jData.getAsJsonString());
		
		this.logVo.setTradCls(KftcFrsLogVo.TRAD_CLASS_RECV);
		this.logVo.setApiUrl(entity.getUrl());
		this.logVo.setData(jData19999);
		this.logVo.setResCode(jData.getAsString("response_code", "-"));
		
		FaceRecApiLogInsertParameter params = FaceRecApiLogInsertParameter.builder()
				.userCifNo(logVo.getUserCifNo())
				.faceTransactionId(logVo.getFaceTransactionId())
				.tradNo(logVo.getTradNo())
				.tradCls(logVo.getTradCls())
				.chnlGb(logVo.getChnlGb())
				.apiUrl(logVo.getApiUrl())
				.data(logVo.getData())
				.resCode(logVo.getResCode())
				.custNo(logVo.getCustNo())
				.build();
		
		log.debug("안면인식 거래 후 로그 parameter => {}", params);
		
		logInsertFrsApi(params);
	}
	
	/**
	 * 안면인식 API 로그 적재
	 * @param params
	 */
	private void logInsertFrsApi(FaceRecApiLogInsertParameter params) {	
		try {
			FaceRecApiLogDao faceRecApiLogDao = RuntimeContext.getBean(FaceRecApiLogDao.class);
			if(faceRecApiLogDao != null) {
				faceRecApiLogDao.insertFaceRecApiLog(params);
			}
		} catch (Exception e) {
			log.error("FRS API DB LOG ERROR :: ", e.getMessage(), e);
		}
	}
	
	private boolean inputValidation() {
		if (logVo.getUserCifNo() != null && "".equals(logVo.getUserCifNo().trim())) {
			return false;
		}
		
		String userCifNo = StringUtils.defaultIfBlank(this.logVo.getUserCifNo(), "-");
		String faceTransactionId = StringUtils.defaultIfBlank(this.logVo.getFaceTransactionId(), "-");
		String custNo = StringUtils.defaultIfBlank(this.logVo.getCustNo(), "-");
		String tradNo = StringUtils.defaultIfBlank(this.logVo.getTradNo(), "-");
		
		if ("-".equals(userCifNo) || "-".equals(faceTransactionId) || "-".equals(custNo) || "-".equals(tradNo)) {
			log.error("userCifNo[{}], faceTransactionId[{}], custNo [{}], tradNo [{}] 필수 값입니다. 프로그램 데이타값 확인 부탁드립니다."
					,userCifNo
					,faceTransactionId
					,custNo
					,tradNo);
			return false;
		}
		
		return true;
	}
	
	private String preventDataOverFlow(String jData) {
		//Data OverFlow방지.
		if(jData.length() > 19999) {
			jData = jData.substring(0, 19999);
		}
		return jData;
	}

}
