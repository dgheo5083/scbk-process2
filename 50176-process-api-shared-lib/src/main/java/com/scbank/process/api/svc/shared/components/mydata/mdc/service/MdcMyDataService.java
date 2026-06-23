package com.scbank.process.api.svc.shared.components.mydata.mdc.service;

import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataAuthVo;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpJsonObject;

/**
 * 마이데이터 연계 서비스 인터페이스
 */
public interface MdcMyDataService {

	/**
	 * 마이데이터 인증키 획득
	 * 
	 * @return
	 */
	MdcMyDataAuthVo getToken();

	/**
	 * 마이데이터 인증키 조회
	 * 
	 * @return
	 */
	MdcMyDataAuthVo getSelectToken();

	/**
	 * 마이데이터 인증키 신규 발급
	 * 
	 * @return
	 */
	MdcMyDataAuthVo changeToken();

	/**
	 * 마이데이터 인증키 활성화
	 * 
	 * @param tokenDate
	 * @param data
	 */
	void enableToken(String tokenDate, MyDataHttpJsonObject data);

	/**
	 * 마이데이터 인증키 폐기
	 */
	void disableToken();
}
