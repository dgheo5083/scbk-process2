package com.scbank.process.api.fw.base.integration.system.edmi;

import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiSystemHeader;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiTranCommonHeader;
import com.scbank.process.api.fw.integration.cfg.AbstractIntegrationRequestOptions;

import lombok.Getter;
import lombok.Setter;

/**
 * EDMi 거래 설정 클래스
 */
public class EdmiRequestOptions extends AbstractIntegrationRequestOptions  {

	/**
	 * 점번기번
	 */
	@Getter
	@Setter
	private String tmsgCreSysNm;
	
	/**
	 * 채널유형코드
	 */
	@Getter
	@Setter
	private String chnlTypCd;
	
	/**
	 * 입력식별코드
	 */
	@Getter
	@Setter
	private String inputDistCd;
	
	/**
	 * 입력식별취소코드
	 */
	@Getter
	@Setter
	private String inputDistCdCancel;
	
	/**
	 * 업무식별코드
	 */
	@Getter
	@Setter
	private String bizDistCd;
	
	/**
	 * 채널 ID
	 */
	@Getter
	@Setter
	private String channelId;
	
	/**
	 * Macro AI
	 */
	@Getter
	@Setter
	private String macroAi;
	
	/**
	 * Macro AO
	 */
	@Getter
	@Setter
	private String macroAo;
	
	/**
	 * 구과목코드
	 */
	@Getter
	@Setter
	private String oldAcctCd;
	
	/**
	 * OLTP Transaction Code;
	 */
	@Getter
	@Setter
	private String trxCd;
	
	/**
	 * 서비스코드
	 */
	@Getter
	@Setter
	private String gsvcd;
	
	/**
	 * VAN TYPE
	 */
	@Getter
	@Setter
	private String gshelf;
	
	/**
	 * 직원번호
	 */
	@Getter
	@Setter
	private String empNo;
	
	/**
	 * 화면번호
	 */
	@Getter
	@Setter
	private String scrnId;
	
	/**
	 * 소속점번호
	 */
	@Getter
	@Setter
	private String blngBrNo;
	
	/**
	 * 계좌점번호
	 */
	@Getter
	@Setter
	private String txnBrNo;
	
	/**
	 * 채널 입/출력 전문 시스템 헤더부
	 */
	@Getter
	@Setter
	private EdmiSystemHeader systemHeader;
	
	/**
	 * 채널 입/출력 전문 거래공통부
	 */
	@Getter
	@Setter
	private EdmiTranCommonHeader tranCommonHeader;
}
