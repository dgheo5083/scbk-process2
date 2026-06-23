package com.scbank.process.api.fw.base.integration.system.oltp;

import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.integration.cfg.AbstractIntegrationRequestOptions;

import lombok.Getter;
import lombok.Setter;

/**
 * 호스트 공통부 설정 클래스
 * 
 * @author sungdon.choi
 */
public class OltpRequestOptions extends AbstractIntegrationRequestOptions {

	/**
	 * IMS 거래코드
	 */
	@Getter
	@Setter
	private String imsTranCd;

	/**
	 * 대상점 번호
	 */
	@Getter
	@Setter
	private String branchNo;

	/**
	 * 서비스 코드
	 */
	@Getter
	@Setter
	private String svcCd;

	/**
	 * 업무 식별 코드
	 */
	@Getter
	@Setter
	private String jobTp;

	/**
	 * 입력 식별 코드
	 */
	@Getter
	@Setter
	private String inClassCd;

	/**
	 * 처리구분 F:최초거래,N:연속,L:마지막
	 */
	@Getter
	@Setter
	private String processTp;

	/**
	 * sheft no/van구분
	 */
	@Getter
	@Setter
	private String vanTp;

	/**
	 * 입출력매체구분
	 */
	@Getter
	@Setter
	private String inOutCnlTp;

	@Getter
	@Setter
	private OltpCommon oltpCommon;
	
	/**
	 * 예비거래 여부를 설정한다.
	 */
	@Getter
	@Setter
	private boolean isPreTran;
	
	/**
	 * 본거래여부를 설정한다.
	 */
	@Getter
	@Setter
	private boolean isRealTran;
}
