/*******************************************************************************
*  업   무  명 : 공통
*  설      명  : 한국정보인증 공통 컴포넌트
*  작   성  자 : 오은진
*  작   성  일 : 2026.01.15
*  관련 테이블 :
*  관련 전문   :
* Copyright ⓒ SC제일은행. All Right Reserved
* ******************************************************************************
* 변경이력 (버전/변경일시/작성자)
* <pre>
* 최초작성 (1.0/2026.01.15/오은진)
* </pre>
******************************************************************************/
package com.scbank.process.api.svc.shared.components.cert;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import secu.bra.BRAProc;

@Slf4j
@SharedComponent(name = "한국정보인증 공통 컴포넌트", description = "한국정보인증 공통 컴포넌트", author = "오은진")
@RequiredArgsConstructor
public class KicaCertComponent {

	/**
	 * 한국정보인증 인증서 상태 검증
	 * @param marketCode
	 * @param userId
	 * @param regNo
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "getBraCertStatus", description = "한국정보인증 인증서 상태 검증", author = "오은진")
	public Map<String, String> getBraCertStatus(String marketCode, String userId, String regNo) throws Exception {
		Map<String, String> rtn = new HashMap<>();

		log.debug("getBraCertStatus START");

		String isRequestAccountAuth = "N"; // 계좌 인증을 해야 하는경우(재발급 대상)
		String BRAID = (InetAddress.getLocalHost()).getHostAddress();	//BRAID(요청IP)
		int serverCode = 3;												//서버 코드:3 or 4사용
		int registerCase = 0;
		String oppStatusCode = "";
		String sgSSRGubun = "1";

		String	currentChkRefNo		=	"";
		String	currentChkAuthCode	=	"";

		StringBuffer sendPackBuffer = new StringBuffer();
		sendPackBuffer.append("2"); 					// IniCertConstants.SG_CAID : "2"
		sendPackBuffer.append("$").append(marketCode); 	// IniCertConstants.SG_MARKET_CODE : "1"
		sendPackBuffer.append("$").append(userId);
		sendPackBuffer.append("$").append(regNo);

		Vector vSendData = new Vector();
		Vector vRsltData = new Vector();

		vSendData.addElement(sendPackBuffer.toString());
		log.debug("getBraCertStatus vSendData :: {}", vSendData);

		BRAProc braProc = new BRAProc(serverCode);
		braProc.setLog(true, true);
		log.debug("BRAProc serverCode :: {}", serverCode);

		// IniCertConstants.SG_BRA_VIEW_CERTSTATUS : 사용자 인증서 상태조회 (20)
		vRsltData = braProc.sendData(20, vSendData);
		log.debug("getBraCertStatus : SG_BRA_VIEW_CERTSTATUS=20");
		log.debug("getBraCertStatus : vRsltData {}", vRsltData);
		braProc.disconnect();
		braProc = null;

		if(vRsltData == null || vRsltData.size() == 0){
			throw new PRCServiceException("PRCCRT70001","RA(CA)서버로 데이터 전송에 실패했습니다(SGE001).다시 시도하십시요");
		}

		if(((String)vRsltData.elementAt(0)).equals("nok")){
			String	errorMsg	=	(String)vRsltData.elementAt(1);

			// 에러메시지중 NODATA라는 말이 없으면 에러로 빠진다
			if(errorMsg == null || errorMsg.indexOf("NODATA") == -1){
				System.out.println("@#@#@@@@@@@@@@@@@@@#@#@#@#@#@#@#@#");
				throw new PRCServiceException("PRCCRT70001","RA(CA)서버로 데이터 전송에 실패했습니다(SGE001).다시 시도하십시요");
			}
			// 신규상태인경우다
			registerCase = 2; // IniCertConstants.SG_BRA_USER_NEW : 2 (인증서 상태 신규)
			oppStatusCode =	"2";
		} else {
			oppStatusCode	=	(String)vRsltData.elementAt(1);
			if(oppStatusCode == null){	oppStatusCode	=	"";}

			// 재등록인 경우 SG_UPDATE_CASE		=	".20.21.23.24.22.15.10.12.16.13.14.11. [무료]
			if (".20.21.23.24.22.15.10.12.16.13.14.11.".indexOf(oppStatusCode) != -1) {
				registerCase = 8; // IniCertConstants.SG_BRA_USER_KEYREC : 8 (사용자 재등록)
			} else if (".25.30.".indexOf(oppStatusCode) != -1) {
				// 인가 상태인경우 SG_INGA_CASE = ".25.30." [유료]
				registerCase = 19; // IniCertConstants.SG_BRA_USER_ISSUE : 19 (사용자 재인가)
			} else {
				// 사용자재등록도 재인가도 아닌코드가 넘어오면 에러다
				// 이렇게 될 가능성은 거의 없다
				throw new PRCServiceException("PRCCRT70003","RA(CA)서버로 부터 전송받은 데이터 검증에 실패했습니다(SGE003).문의바랍니다.");
			}

			// 여기서 만일 법인인 경우에 대해서
			// 또는 개인이라도 결제가 필요한 경우에 한해서
			// 결제처리 화면으로 이동한다
			// 이때 결제를 하게 되는 경우는 상태가
			// [신규:2 ,재인가:30 갱신:21] => 갱신은 사용자가 바로 CA에 붙어서 작업한다
			if (oppStatusCode.trim().equals("2") || oppStatusCode.trim().equals("25") || oppStatusCode.trim().equals("30")) {
				// 수수료 납부
				sgSSRGubun = "1";
			} else {
				sgSSRGubun = "2";

				if (vRsltData.size() >= 5) {
					if (((String) vRsltData.elementAt(5)).equals("Y") && !oppStatusCode.trim().equals("30")) {
						isRequestAccountAuth = "Y";
					}
				}
				if (vRsltData.size() >= 4) {
					currentChkRefNo = (String) vRsltData.elementAt(2);
					currentChkAuthCode = (String) vRsltData.elementAt(3);
				}
			}
		}

		rtn.put("IS_REQUEST_ACCOUNT_AUTH", isRequestAccountAuth);	//계좌인증여부값
		rtn.put("RA_REG_CASE", String.valueOf(registerCase));	//인증서 상태 [신규:2 ,재발급:8, 사용자 재인가:19]
		rtn.put("SGSSRGubun", sgSSRGubun);
		rtn.put("SGAuthCode", currentChkAuthCode.trim());
		rtn.put("SGRefNo", currentChkRefNo.trim());
		rtn.put("SGStatusCode", oppStatusCode);

		log.debug("getBraCertStatusResult IS_REQUEST_ACCOUNT_AUTH:: {}", isRequestAccountAuth);
		log.debug("getBraCertStatusResult RA_REG_CASE:: {}", String.valueOf(registerCase));
		log.debug("getBraCertStatusResult SGStatusCode:: {}", oppStatusCode);
		log.debug("getBraCertStatusResult SGRefNo:: {}", currentChkRefNo.trim());
		log.debug("getBraCertStatusResult SGAuthCode:: {}", currentChkAuthCode.trim());
		log.debug("getBraCertStatusResult SGSSRGubun:: {}", sgSSRGubun);

		log.debug("getBraCertStatusResult END");

		return rtn;
	}

	/**
	 * 한국정보인증 인증서 참조번호, 인가코드 발급
	 * @param userId
	 * @param regNo
	 * @param oppStatusCode
	 * @param marketCode
	 * @param registerCase
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "reIssueCert", description = "한국정보인증 인증서 참조번호, 인가코드 발급", author = "오은진")
	public Map<String, String> reIssueCert(String userId, String regNo, String oppStatusCode, String marketCode, int registerCase) throws Exception {
		Map<String, String> rtn = new HashMap<>();

		String BRAID = (InetAddress.getLocalHost()).getHostAddress();	//BRAID(요청IP)
		int serverCode = 3;												//서버 코드:3 or 4사용

		StringBuffer sendPackBuffer = new StringBuffer();
		sendPackBuffer.append("2"); 						// IniCertConstants.SG_CAID : "2"
		sendPackBuffer.append("$").append(marketCode); 	// IniCertConstants.SG_MARKET_CODE : "1"
		sendPackBuffer.append("$").append(userId);
		sendPackBuffer.append("$").append(regNo);
		sendPackBuffer.append("$").append(BRAID);

		Vector vSendData = new Vector();
		Vector vRsltData = new Vector();

		vSendData.addElement(sendPackBuffer.toString());
		log.debug("---------getBraCertIssue vSendData :: {}", vSendData);

		BRAProc braProc = new BRAProc(serverCode);

		vRsltData = braProc.sendData (registerCase, vSendData);
		log.debug("---------getBraCertIssue vRsltData2 :: {}", vRsltData);
		braProc.disconnect();

		//만일 결과값을 못받아 왔을 경우 에러페이지로 진행한다
		if(vRsltData == null || vRsltData.size() == 0){
			throw new PRCServiceException("PRCCRT70011", "인증서 (재)발급에 대한 응답전문을 수신하지 못하였습니다.");
		}

		//결과를 받아왔는데 에러는 그냥 보통에러페이지로 진행한다
		if(((String)vRsltData.elementAt(0)).equals("nok")){
			throw new PRCServiceException("PRCCRT70010", "다음과 같은 오류가 발생하였습니다 : " + (String)vRsltData.elementAt(1));
		}

		// 결과를 받아왔는데 결과가 참조번호 인가코드를 포함하고 있지않다면 에러로 처리한다
		if(vRsltData.size() < 3){
			throw new PRCServiceException("PRCCRT70012", "인증서 (재)발급에서 참조번호,인가코드를 받아오는데 실패하였습니다.");
		}

		// 성공하면 성공페이지로 이동한다
		// 세션에
		// (1) 참조번호
		// (2) 인가코드
		// (3) 이름
		// (4) 메일주소
		// 를 입력해준다
		// 상태가 발행 or 재발행 이면 참조번호 인가코드 Parsing 위치 조정
		if(oppStatusCode.trim().equals("20") || oppStatusCode.trim().equals("22")) {
			rtn.put("SGRefNo", ((String)vRsltData.elementAt(1)).trim());
			rtn.put("SGAuthCode", ((String)vRsltData.elementAt(2)).trim());
		} else {
			rtn.put("SGRefNo", ((String)vRsltData.elementAt(2)).trim());
			rtn.put("SGAuthCode", ((String)vRsltData.elementAt(3)).trim());
		}

		return rtn;
	}

	/**
	 * 한국정보인증 인증서 발급
	 * @param isPayment		세금계산서 발급 여부(1:발급, 2:미발급)
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "issueCert", description = "한국정보인증 인증서 발급", author = "오은진")
	public Map<String, String> issueCert(int isPayment, Map<String, Object> uniCertSession, String raRegCase, String personOrCompanyCode) throws Exception {
		Map<String, String> rtn = new HashMap<>();

		String deptPersonName = (String) uniCertSession.get("DeptPersonName");
		String regNo = (String) uniCertSession.get("RegNo");
		String saupjaNo = (String) uniCertSession.get("SaupjaNo");
		String companyName = (String) uniCertSession.get("CompanyName");

		String BRAID = (InetAddress.getLocalHost()).getHostAddress();	//BRAID(요청IP)
		int serverCode = 3;												//서버 코드:3 or 4사용
		int registerCase = 0;

		StringBuffer sendPackBuffer = new StringBuffer();
		Vector vSendData = new Vector();
		Vector vRsltData = new Vector();

		String policyId = "";
		String marketCode = "1";	//SC제일은행 마케팅아이디

		if(personOrCompanyCode.equals("1")) {
			personOrCompanyCode = "P";
			policyId = "1 등급 인증서(개인)";
		} else if(personOrCompanyCode.equals("2")) {
			personOrCompanyCode	= "C";
			policyId = "1 등급 인증서(법인)";
			regNo						=	saupjaNo;
			deptPersonName = companyName;

		} else {
			personOrCompanyCode = "C";
			policyId = "1 등급 인증서(법인)";
		}

		registerCase = Integer.parseInt(raRegCase);

		sendPackBuffer.append("2"); 					// IniCertConstants.SG_CAID : "2"
		if(registerCase == 2)	// IniCertConstants.SG_BRA_USER_NEW : 2 (사용자 신규 등록)
		{
			sendPackBuffer.append("$").append(deptPersonName);
			sendPackBuffer.append("$").append(regNo);
			sendPackBuffer.append("$").append((String) uniCertSession.get("EmailAddr"));//EmailAddr
			sendPackBuffer.append("$").append((String) uniCertSession.get("TelCode"));	//TelCode
			sendPackBuffer.append("$").append("");	//FaxCode
			sendPackBuffer.append("$").append((String) uniCertSession.get("TelCode"));	//TelCode
			sendPackBuffer.append("$").append((String) uniCertSession.get("ZipCode"));	//ZipCode
			sendPackBuffer.append("$").append((String) uniCertSession.get("Address"));	//Address
			sendPackBuffer.append("$").append(companyName);	//CompanyName
			sendPackBuffer.append("$").append("");	//DepartName
			sendPackBuffer.append("$").append("");	//CompanyAddress
			sendPackBuffer.append("$").append(marketCode);		// IniCertConstants.SG_MARKET_CODE : "1"
			sendPackBuffer.append("$").append((String) uniCertSession.get("EngName"));	//EngName
			sendPackBuffer.append("$").append(personOrCompanyCode);
			sendPackBuffer.append("$").append(policyId);
			sendPackBuffer.append("$").append(BRAID);
			sendPackBuffer.append("$").append((String) uniCertSession.get("UserID"));	//UserID

			// 세금계산서 관련 정보를 넘겨준다
			if(isPayment == 1) {
				sendPackBuffer.append("$").append("Y");
				sendPackBuffer.append("$").append((String) uniCertSession.get("TypesOfIndustry"));	// 업종
				sendPackBuffer.append("$").append((String) uniCertSession.get("Item"));	//종목
				sendPackBuffer.append("$").append((String) uniCertSession.get("ChairManName"));	// 대표자명
			} else {
				sendPackBuffer.append("$").append("N");
				sendPackBuffer.append("$").append("");
				sendPackBuffer.append("$").append("");
				sendPackBuffer.append("$").append("");
			}
		}
		else if (registerCase == 19)	// IniCertConstants.SG_BRA_USER_ISSUE : 19 (사용자 재인가)
		{
			sendPackBuffer.append("$").append(marketCode);
			sendPackBuffer.append("$").append((String) uniCertSession.get("UserID"));	//UserID
			sendPackBuffer.append("$").append(regNo);
			sendPackBuffer.append("$").append(BRAID);

			// 세금계산서 관련 정보를 넘겨준다
			if(isPayment == 1) {
				sendPackBuffer.append("$").append("Y");
				sendPackBuffer.append("$").append((String) uniCertSession.get("TypesOfIndustry"));	// 업종
				sendPackBuffer.append("$").append((String) uniCertSession.get("Item"));	//종목
				sendPackBuffer.append("$").append((String) uniCertSession.get("ChairManName"));	// 대표자명
			} else {
				sendPackBuffer.append("$").append("N");
				sendPackBuffer.append("$").append("");
				sendPackBuffer.append("$").append("");
				sendPackBuffer.append("$").append("");
			}
		}
		else
		{
			throw new PRCServiceException("PRCCRT70000","인증시스템에서 오류가 발생하였습니다.");
		}
		log.debug("---------KICA_CertIssue : 전문 :: {}", sendPackBuffer.toString());

		vSendData.addElement( sendPackBuffer.toString() );
		BRAProc	braProc = new BRAProc(serverCode);
		vRsltData = braProc.sendData (registerCase, vSendData);
		log.debug("---------KICA_CertIssue :vRsltData=[" + vRsltData + "]");
		braProc.disconnect();

		if(vRsltData == null || vRsltData.size() == 0){
			throw new PRCServiceException("PRCCRT70011",  "인증서 (재)발급에 대한 응답전문을 수신하지 못하였습니다.");
		}

		//결과를 받아왔는데 에러는 그냥 보통에러페이지로 진행한다
		if(((String)vRsltData.elementAt(0)).equals("nok")){
			throw new PRCServiceException("PRCCRT70010", "다음과 같은 오류가 발생하였습니다 : " + (String)vRsltData.elementAt(1));
		}

		// 결과를 받아왔는데 결과가 참조번호 인가코드를 포함하고 있지않다면 에러로 처리한다
		if(vRsltData.size() < 3){
			throw new PRCServiceException("PRCCRT70012", "인증서 (재)발급에서 참조번호,인가코드를 받아오는데 실패하였습니다.");
		}

		rtn.put("SGRefNo", ((String)vRsltData.elementAt(1)).trim());
		rtn.put("SGAuthCode", ((String)vRsltData.elementAt(2)).trim());
		rtn.put("SGDeptPersonName", deptPersonName);

		return rtn;
	}

}
