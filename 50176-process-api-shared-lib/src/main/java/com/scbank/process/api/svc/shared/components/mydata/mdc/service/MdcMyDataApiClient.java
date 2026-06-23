package com.scbank.process.api.svc.shared.components.mydata.mdc.service;

import com.scbank.process.api.svc.shared.components.mydata.IMyDataClient;
import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataAuthVo;

/**
 * 마이데이터 API 클라이언트 인터페이스
 */
public interface MdcMyDataApiClient extends IMyDataClient {

	/**
	 * 초기화를 수행한다.
	 * 
	 * @return
	 */
	MdcMyDataApiClient init();

	/**
	 * 인증정보를 가져온다.
	 * 
	 * @return
	 */
	MdcMyDataAuthVo getAuth();
}
