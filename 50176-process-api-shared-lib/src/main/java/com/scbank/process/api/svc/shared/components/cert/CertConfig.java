/*******************************************************************************
*  업   무  명 : 공통
*  설      명 : 인증센터 환경설정
*  작   성  자 : 이완주
*  작   성  일 : 2025.11.07
*  관련 테이블 :
*  관련 전문   :
* Copyright ⓒ SC제일은행. All Right Reserved
* ******************************************************************************
* 변경이력 (버전/변경일시/작성자)
* <pre>
* 최초작성 (1.0/2025.11.07/이완주)
* </pre>
******************************************************************************/
package com.scbank.process.api.svc.shared.components.cert;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CertConfig {

	// Corp. Info. 관련
	public String RegBank = "KFB";
	public String RegCodeValue = "0023";
	public String ADID = "kfbadmin0309";

	// 공인 인증 RA 등록 정보
	public String OPPRAIP = PropertiesUtils.getString("CRT_RaServer");
	public int OPPRAPORT = PropertiesUtils.getInt("CRT_RaPort", 4000);

	// 이니텍 설정
	public String CertConfigPath = PropertiesUtils.getString("CRT_CertConfigPath");

	// OCSP, OPPRA 설정파일
//	public String INITECH_PROPERTIES_OPPRA_PATH = PropertiesUtils.getString("INITECH_PROPERTIES_OPPRA_PATH");
//	public String INITECH_PROPERTIES_OCSPGD_PATH = PropertiesUtils.getString("INITECH_PROPERTIES_OCSPGD_PATH");

	// 인증서 만료일 계산
	public String TermGijun = PropertiesUtils.getString("CRT_TermGijun");
	public int TermPeriod = PropertiesUtils.getInt("CRT_TermPeriod");
	public int fincertTermPeriod = PropertiesUtils.getInt("CRT_fincertTermPeriod", 3);
	public int kfbTermPeriod = PropertiesUtils.getInt("CRT_kfbTermPeriod");

	// 금결원 정보
	public String YESSIGN_CAIP = PropertiesUtils.getString("CRT_YESSIGN_CAIP");
	public String YESSIGN_CAPORT = PropertiesUtils.getString("CRT_YESSIGN_CAPORT");
	public String RegiBranchNum = PropertiesUtils.getString("CRT_RegiBranchNum");
	public String YESSIGN_KEYBOARD_SECURE_PROVIDER = "ahnlab25_aos";
	public String YESSIGN_CA_NAME = "YESSIGN";

	// 제일은행 DN 정보
	public String kfbDN = "CN=Korea First Bank,OU=INTERNET BANKING,O=KFB,L=SEOUL,C=KR";
	public String kfbDN1 = "CN=Korea First Bank,OU=INTERNET BANKING,O=KFB,L=SEOUL,C=KR";
	public String kfbDN2 = "CN=Korea First Bank, OU=INTERNET BANKING, O=KFB, L=SEOUL, C=KR";

	// 상호연동 관련 공인인증서 유효성 검증 정보
	public String OCSPGD_IP = PropertiesUtils.getString("CRT_RaServer");	// OCSP 게이트웨이 서비스 IP
	public int OCSPGD_PORT = 4100;             								// OCSP 게이트웨이 서비스 PORT
	public int OCSPGD_REGISTER_CODE = 82;									// 인증서 등록 전문

}



