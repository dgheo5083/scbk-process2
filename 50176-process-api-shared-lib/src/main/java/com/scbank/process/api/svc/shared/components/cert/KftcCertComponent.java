/*******************************************************************************
*  업   무  명 : 공통
*  설      명  : 금융결제원 CA 공통 컴포넌트
*  작   성  자 : 이완주
*  작   성  일 : 2026.03.13
*  관련 테이블 :
*  관련 전문   :
* Copyright ⓒ SC제일은행. All Right Reserved
* ******************************************************************************
* 변경이력 (버전/변경일시/작성자)
* <pre>
* 최초작성 (1.0/2026.03.13/이완주)
* </pre>
******************************************************************************/
package com.scbank.process.api.svc.shared.components.cert;

import java.util.HashMap;
import java.util.Map;

import com.initech.oppra.IniOPPRA;
import com.initech.oppra.IniOPPRAIllegalFormatException;
import com.initech.oppra.IniOPPRAReadException;
import com.initech.oppra.IniOPPRASystemException;
import com.initech.oppra.util.IssueDataParser;
import com.initech.oppra.util.OppraMessageDataParser;
import com.initech.oppra.util.OppraSendDataParser;
import com.initech.oppra.util.StatusChangeDataParser;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent(name = "금결원 CA 공통 컴포넌트", description = "금결원 CA 공통 컴포넌트", author = "이완주")
@RequiredArgsConstructor
public class KftcCertComponent {

	private final CertUtils certUtils;

	/**
	 * 금결원 인증서 상태조회 OPPRA-50
	 * @param certPolicy
	 * @param cid 주민등록번호 or 사업자등록번호
	 * @return
	 */
	@ComponentOperation(name = "searchCert", description = "금결원 인증서 상태조회", author = "이완주")
	public Map<String, String> searchCert(String certPolicy, String cid) {

		Map<String, String> rtn = new HashMap<String, String>();

		IniOPPRA oppra = null;

		try {
			oppra = certUtils.getOppra();

			StringBuilder sendMsg = new StringBuilder();
			sendMsg.append(CertConfig.RegCodeValue).append("$").append(CertConfig.ADID);
			sendMsg.append("$").append(certPolicy).append("$3$").append(cid).append("$000$001$");
			log.debug(sendMsg.toString());

			String res = oppra.requestRAW(50, sendMsg.toString());

			OppraMessageDataParser odp = new OppraMessageDataParser(50);
			odp.setLoopData(oppra.getResCommonPart(), oppra.getResDataPart());
			odp.setRecord(0);

			log.debug(odp.getResCode());

			// 응답값
			rtn.put("opprRes", res);

			// 응답코드
			rtn.put("ResCode", odp.getResCode());

			// 응답데이터부
			rtn.put("CERTSERIAL", odp.getCodeData("CERTSERIAL"));
			rtn.put("EVDATE", odp.getCodeData("EVDATE"));
			rtn.put("CERTSTATUS", odp.getCodeData("CERTSTATUS"));
			rtn.put("CERTPOLICY", odp.getCodeData("CERTPOLICY"));

			log.debug(rtn.toString());

		} catch (IniOPPRAReadException e) {
			throw new PRCServiceException("PRCFCA01018", "잠시 후 다시 시도해 주세요[1].");
		} catch (IniOPPRAIllegalFormatException e) {
			throw new PRCServiceException("PRCFCA01020", "잠시 후 다시 시도해 주세요[3].");
		} catch (Exception e) {
			throw new PRCServiceException("PRCFCA01021", "잠시 후 다시 시도해 주세요[5].");
		} finally {
			certUtils.closeOppra(oppra);
		}

		return rtn;
	}

	/**
	 * 금결원 인증서 상태변경 OPPRA-40
	 * @param userSerial
	 * @param certPolicy
	 * @param status 30:폐기, 40:효력정지, 41:효력회복
	 * @return
	 */
	@ComponentOperation(name = "changeStatusCert", description = "금결원 인증서 상태변경", author = "이완주")
	public Map<String, String> changeStatusCert(String userSerial, String certPolicy, String status) {
		Map<String, String> rtn = new HashMap<String, String>();

		IniOPPRA oppra = null;
		try {
			StringBuilder sendMsg = new StringBuilder();
			sendMsg.append(CertConfig.RegCodeValue).append("$").append(CertConfig.ADID);
			sendMsg.append("$").append(certPolicy).append("$");
			sendMsg.append(certUtils.formatString(userSerial)).append("$").append(status).append("$");
			log.debug("changeStatusCert MSG {}", sendMsg.toString());

			oppra = certUtils.getOppra();

			oppra.requestRAW(40, sendMsg.toString());

			StatusChangeDataParser sdp = new StatusChangeDataParser(oppra.getResDataPart());

			rtn.put("ResCode", sdp.getResCode());
			rtn.put("ResMsg", sdp.getResMsg());
			// 0109412$인증서가 이미 폐지 또는 정지되었습니다.$0023$kfbadmin0309$16$000005086099$30$000$정상처리되었습니다.$ $ $
			log.debug("changeStatusCert Result {} {}", sdp.getResCode(), sdp.getResMsg());

		} catch (IniOPPRAReadException e) {
			throw new PRCServiceException("PRCFCA01008", "인증 등록기관의 시스템 점검중입니다. 인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} catch (IniOPPRAIllegalFormatException e) {
			throw new PRCServiceException("PRCFCA01010", "인증 등록기관의 시스템 점검중입니다. 인증 등록기관 관리자에게 문의하시기 바랍니다.");
		}  catch (Exception e) {
			throw new PRCServiceException("PRCFCA01012", "인증 등록기관의 시스템 점검중입니다. 인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} finally {
			certUtils.closeOppra(oppra);
		}

		return rtn;
	}

	/**
	 * 금결원 인증서 발급 OPPRA-20, OPPRA-25
	 * @param issueGubun 1:신규, 2:재발급
	 * @param raMap
	 * @return
	 */
	@ComponentOperation(name = "issueCert", description = "금결원 인증서 발급", author = "이완주")
	public Map<String, String> issueCert(String issueGubun, Map<String, Object> raMap) {
		Map<String, String> rtn = new HashMap<String, String>();

		// 01:개인범용 02:기업범용 04:개인(은행/보험) 05:기업범용 84:조합번호용
		String certPolicyCode = (String)raMap.getOrDefault("CERTCODE", "");

		IniOPPRA oppra = null;			//최초 신규시
		IniOPPRA oppraRevoke = null;	//922 926 927 에러시 폐기처리
		IniOPPRA oppraRetry = null;	//922 926 927 에러시 신규/재발급 재시도
		String sendMsg = "";
		OppraMessageDataParser odp = null;
		OppraMessageDataParser odp_retry = null;
		String refNum = "";
		String appCode = "";
		String msgId = "";
		try {
			oppra = certUtils.getRegOppra();

			if("1".equals(issueGubun)) { // 신규
				msgId = "20";
			}else if("2".equals(issueGubun)) { // 재발급
				msgId = "25";
			}

			OppraSendDataParser oppraSendDataParser = new OppraSendDataParser(msgId, raMap);
			sendMsg = oppraSendDataParser.getSendLastData();
			log.debug("##authcenter [전문ID][{}]", msgId);
			log.debug("##authcenter [요청][{}]", sendMsg);

			/** 공동인증서 요청값
			 *  [신규]
			 *  202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$
			 *  [재발급]
			 *  252026011610382500010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$
			 */

			/** 금융인증서 요청값
			 *  [신규]
			 *  202026011611252800010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$16$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$
			 *  [재발급]
			 *  252026011610515800010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$16$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$
			 */

			String oppraResponse = oppra.requestRAW(Integer.parseInt(msgId), sendMsg);
			log.debug("##authcenter [응답][{}]", oppraResponse);
			odp = new OppraMessageDataParser(msgId, oppra.getResDataPart());

			/** 공동인증서 응답값
			 *  [신규]
			 *	999$Error occurred during processing.$410$이미 유효한 인증서가 존재합니다.$LOANTE80$000005086112$ $ $
			 *  [재발급]
			 *  000$정상처리되었습니다.$1952608$77465613442455031832$20260116103825$000001$
			 */

			/** 금융인증서 응답값
			 *  [신규]
			 *  927$가입자 재등록 후, 미발급 사용자 입니다.(재등록 요청을 하십시요)$000$정상처리되었습니다.$ $ $ $ $
			 *  [재발급]
			 *  000$정상처리되었습니다.$1952626$78751296902471917351$20260116105158$000003$
			 */

			if(odp != null) {
				odp.setLoopData(oppra.getResCommonPart(), oppra.getResDataPart());
			}

		} catch (IniOPPRAReadException e) {
			throw new PRCServiceException("PRCFCA01008", "인증 등록기관의 시스템 점검중입니다. 인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} catch (IniOPPRAIllegalFormatException e) {
			throw new PRCServiceException("PRCFCA01010", "인증 등록기관의 시스템 점검중입니다. 인증 등록기관 관리자에게 문의하시기 바랍니다.");
		}  catch (Exception e) {
			throw new PRCServiceException("PRCFCA01012", "인증 등록기관의 시스템 점검중입니다. 인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} finally {
			certUtils.closeOppra(oppra);
		}

		String resCode = odp.getResCode();
		String errMsg = odp.getCodeData("RESMSG");
		log.debug("##authcenter [errMsg][{}]", errMsg);
		log.debug("##authcenter [getResCode][{}]", resCode);

		if("000".equals(resCode)) {
			// 정상
			refNum = odp.getCodeData("REFNUM");
			appCode = odp.getCodeData("APPCODE");

		} else if("820".equals(resCode)) {
			// 정보통신부의 정책에 따라 금융결제원 범용 인증서 발급이 허용되지 않습니다
			throw new PRCServiceException("PRCCRT20142", "정보통신부의 정책에 따라 금융결제원 범용 인증서 발급이 허용되지 않습니다.");

		} else if("216".equals(resCode)) {
			// [00은행]에서 등록 및 미발급 상태 : ID변경으로 인한 ID상이
			throw new PRCServiceException("PRCCRT20118", "고객의 인증서는 " + errMsg + "로 남아 있습니다. 해당 은행의 인터넷뱅킹 웹사이트에서 공인인증서를 발급받으신 후 사용하시기 바랍니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");

		} else if("922".equals(resCode) || "926".equals(resCode) || "927".equals(resCode)) {
			// 갱신 등록 후 미발급 상태
			try {
				oppraRevoke = certUtils.getOppra();
				// 참조번호강제폐기 전문 발송
				StringBuilder infoDeleteMsg = new StringBuilder();
	            infoDeleteMsg.append(CertConfig.RegCodeValue).append("$");
	            infoDeleteMsg.append(raMap.get("CERTCODE")).append("$").append(raMap.get("IDNO")).append("$");
	            infoDeleteMsg.append(CertConfig.RegCodeValue).append(raMap.get("USERID")).append("$");

	            oppraRevoke.requestRAW(82, infoDeleteMsg.toString());

	            // 재요청
	            oppraRetry = certUtils.getRegOppra();
	            oppraRetry.requestRAW(Integer.parseInt(msgId), sendMsg);

	            odp_retry = new OppraMessageDataParser(msgId, oppraRetry.getResDataPart());

            	odp_retry.setLoopData(oppraRetry.getResCommonPart(), oppraRetry.getResDataPart());

            	if (odp_retry.getResCode().equals("000")) {
					refNum = odp_retry.getCodeData("REFNUM");
					appCode = odp_retry.getCodeData("APPCODE");
				} else {
					String errMsg2 = odp_retry.getCodeData("RESMSG");
					throw new PRCServiceException(odp_retry.getResCode(), errMsg2);
		    	}

			} catch (IniOPPRAReadException e) {
				throw new PRCServiceException("PRCFCA01008", "인증 등록기관의 시스템 점검중입니다. 인증 등록기관 관리자에게 문의하시기 바랍니다.");
			} catch (IniOPPRAIllegalFormatException e) {
				throw new PRCServiceException("PRCFCA01010", "인증 등록기관의 시스템 점검중입니다. 인증 등록기관 관리자에게 문의하시기 바랍니다.");
			} catch (PRCServiceException e) {
				throw e;
			} catch (Exception e) {
				throw new PRCServiceException("PRCFCA01012", "인증 등록기관의 시스템 점검중입니다. 인증 등록기관 관리자에게 문의하시기 바랍니다.");
			} finally {
				certUtils.closeOppra(oppraRevoke);
				certUtils.closeOppra(oppraRetry);
			}

		} else if("217".equals(resCode)) {
			// 금융인증서랑 공동인증서랑 오류코드 분기
			if("16".equals(certPolicyCode)) {
				throw new PRCServiceException("PRCFCA01014", "고객께서는 이미 인증서를 발급받으셨으며 현재 효력정지 상태입니다. 효력회복을 하셔서 사용하시기 바랍니다.");
			}else {
				throw new PRCServiceException("PRCCRT20116", "고객께서는 이미 인증서를 발급받으셨으며 현재 효력정지 상태입니다. 효력회복을 하셔서 사용하시기 바랍니다.");
			}

		} else if("214".equals(resCode)) {
			if("16".equals(certPolicyCode)) {
				// 화면에서 이 오류코드를 식별하여 타행금융인증서강제폐기 API 호출한다
				throw new PRCServiceException("PRCFCA01015", "이미 발급하신 금융인증서가 존재합니다.타기관인증서 등록 후 이용하시기 바랍니다. <br/>기존 금융인증서를 폐기 후 신규 발급을 진행하시겠습니까?");
			}else {
				throw new PRCServiceException("PRCCRT20114", errMsg + "금융결제원에서 발행하는 공인인증서는 금융기관중 한곳에서만 발급받으실 수 있습니다. 1. 당행에서 발급 받으셨다면 인증서 재발급 메뉴를, 2. 타행에서 발급 받으셨다면 타기관 인증서 등록 메뉴를, 3. 지점에서 이용자번호를 새로 신규하셨으면 기존 인증서 폐기를 먼저하시기 바랍니다.");
			}

		} else if("210".equals(resCode)) {
			throw new PRCServiceException("PRCCRT20114", errMsg + "금융결제원에서 발행하는 공인인증서는 금융기관중 한곳에서만 발급받으실 수 있습니다. 1. 당행에서 발급 받으셨다면 인증서 재발급 메뉴를, 2. 타행에서 발급 받으셨다면 타기관 인증서 등록 메뉴를, 3. 지점에서 이용자번호를 새로 신규하셨으면 기존 인증서 폐기를 먼저하시기 바랍니다.");

		} else if("211".equals(resCode)) {
			throw new PRCServiceException("PRCFCA01016", "가입자 등록에 실패하였습니다. 인증 등록기관 관리자에게 문의하시기 바랍니다.");

		} else if("999".equals(resCode)) {
			String advanceErrorCode = odp.getCodeData("ADDRESCODE");
			String advanceErrorMsg = odp.getCodeData("ADDRESMSG") + "\n serial:["+odp.getCodeData("RESERVE2")+"]";
			log.error("[{}] {}", advanceErrorCode, advanceErrorMsg);
			throw new PRCServiceException(advanceErrorCode, advanceErrorMsg);
		} else {
			throw new PRCServiceException(resCode, errMsg);
		}

		rtn.put("refNum", refNum);
		rtn.put("appCode", appCode);

		return rtn;
	}


	/**
	 * 타기관 인증서 등록 OPPRA-90
	 *
	 * @param serial
	 * @param certPolicy
	 * @param userId
	 * @return
	 */
	@ComponentOperation(name = "regOtherCert", description = "타기관 인증서 등록", author = "이완주")
	public Map<String, String> regOtherCert(String serial, String certPolicy, String userId) {
		Map<String, String> rtn = new HashMap<String, String>();

		IniOPPRA oppra = null;
		try {
			String msg = CertConfig.RegCodeValue + "$" + CertConfig.ADID + "$" + certPolicy + "$1$" + serial + "$000$001$"+ userId +"$";

			oppra = certUtils.getOppra();
			oppra.requestRAW(90, msg);

			OppraMessageDataParser odp = new OppraMessageDataParser(90, oppra.getResDataPart());

			String resCode = odp.getResCode();
			String certStatus = odp.getCodeData("CERTSTATUS");

			// 응답
			rtn.put("ResCode", resCode);
			rtn.put("CodeData", certStatus);

			if("999".equals(resCode)) {
				String iniCode = odp.getCodeData("INICODE");
				String iniMsg = odp.getCodeData("INIMSG");
				throw new PRCServiceException(iniCode, iniMsg);
			}

		} catch (PRCServiceException e) {
			throw e;

		} catch (IniOPPRASystemException e) {
			if("16".equals(certPolicy)) {
				throw new PRCServiceException("PRCFCA02015", "인증기관 시스템 점검중으로 타기관 금융인증서 등록 요청서비스를 제공하여 드리지 못하여 죄송합니다. 인증기관 관리자에게 문의하시기 바랍니다.");
			}else {
				throw new PRCServiceException("PRCCRT20513", "공인인증 등록기관 시스템 점검중으로 타기관 공인인증서 등록 요청서비스를 제공하여 드리지 못하여 죄송합니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
			}

		} catch (IniOPPRAIllegalFormatException e) {
			if("16".equals(certPolicy)) {
				throw new PRCServiceException("PRCFCA02016", "인증기관 시스템 점검중으로 타기관 공동인증서 등록 요청서비스를 제공하여 드리지 못하여 죄송합니다. 인증기관 관리자에게 문의하시기 바랍니다.");
			}else {
				throw new PRCServiceException("PRCCRT20513", "공인인증 등록기관 시스템 점검중으로 타기관 공인인증서 등록 요청서비스를 제공하여 드리지 못하여 죄송합니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
			}

		} finally {
			certUtils.closeOppra(oppra);
		}
		return rtn;
	}

	/**
	 * 타행 금결원 인증서 등록 해제 OPPRA-98
	 * @param userSerial
	 * @param certPolicy
	 * @return
	 */
	@ComponentOperation(name = "revokeOtherCert", description = "타행 금결원 인증서 등록 해제", author = "이완주")
	public Map<String, String> revokeOtherCert(String userSerial, String certPolicy) {
		Map<String, String> rtn = new HashMap<String, String>();
		IniOPPRA oppra = null;

		try {
			StringBuilder sendMsg = new StringBuilder();
			sendMsg.append(CertConfig.RegCodeValue).append("$").append(CertConfig.ADID);
			sendMsg.append("$").append("1").append("$").append(certPolicy).append("$");
			sendMsg.append(certUtils.formatString(userSerial)).append("$");

			oppra = certUtils.getOppra();

			oppra.requestRAW(98, sendMsg.toString());

			StatusChangeDataParser sdp = new StatusChangeDataParser(oppra.getResDataPart());

			rtn.put("ResCode", sdp.getResCode());
			rtn.put("ResMsg", sdp.getResMsg());

			log.debug("revokeOtherCert Result[{}][{}]", sdp.getResCode(), sdp.getResMsg());

		} catch (PRCServiceException e) {
			throw e;

		} catch (IniOPPRASystemException e) {
			if("16".equals(certPolicy)) {
				throw new PRCServiceException("PRCFCA02015", "인증기관 시스템 점검중으로 타기관 금융인증서 등록 요청서비스를 제공하여 드리지 못하여 죄송합니다. 인증기관 관리자에게 문의하시기 바랍니다.");
			}else {
				throw new PRCServiceException("PRCCRT20513", "공인인증 등록기관 시스템 점검중으로 타기관 공인인증서 등록 요청서비스를 제공하여 드리지 못하여 죄송합니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
			}

		} catch (IniOPPRAIllegalFormatException e) {
			if("16".equals(certPolicy)) {
				throw new PRCServiceException("PRCFCA02016", "인증기관 시스템 점검중으로 타기관 공동인증서 등록 요청서비스를 제공하여 드리지 못하여 죄송합니다. 인증기관 관리자에게 문의하시기 바랍니다.");
			}else {
				throw new PRCServiceException("PRCCRT20513", "공인인증 등록기관 시스템 점검중으로 타기관 공인인증서 등록 요청서비스를 제공하여 드리지 못하여 죄송합니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
			}

		} finally {
			certUtils.closeOppra(oppra);
		}
		return rtn;
	}

	/**
	 * 금결원 인증서 갱신 OPPRA-28
	 *
	 * @param custGubun
	 * @param oppUserId
	 * @param juminNo
	 * @param certPolicy
	 * @param emailAddr
	 * @param telenumCell
	 * @param telenumHome
	 * @param telenumOffice
	 * @return
	 */
	@ComponentOperation(name = "renewCert", description = "금결원 인증서 갱신", author = "이완주")
	public Map<String, String> renewCert(String custGubun, String oppUserId, String juminNo, String certPolicy, String emailAddr, String telenumCell, String telenumHome, String telenumOffice) {
		Map<String, String> rtn = new HashMap<String, String>();

		IniOPPRA oppra = null;

		try {
			StringBuilder sendMsg = new StringBuilder();
			sendMsg.append(CertConfig.RegCodeValue).append("$").append(CertConfig.ADID);
			sendMsg.append("$20000823204004$123456$").append(custGubun).append("$$$");
			sendMsg.append(oppUserId).append("$").append(juminNo).append("$");
			sendMsg.append(certPolicy).append("$").append(emailAddr).append("$");
			sendMsg.append(telenumCell).append("$$$$");
			sendMsg.append(telenumHome).append("$$$");
			sendMsg.append(telenumOffice).append("$");

			String msg = sendMsg.toString();

			log.debug("renewCert msg={}", msg);

			oppra = certUtils.getOppra();
			oppra.requestRAW(28, msg);

			IssueDataParser idp = new IssueDataParser(oppra.getResDataPart());

			rtn.put("ResCode", idp.getResCode());
			rtn.put("ResMsg", idp.getResMsg());
			rtn.put("InitechResCode", idp.getInitechResCode());
			rtn.put("InitechResMsg", idp.getInitechResMsg());

		} catch (IniOPPRAReadException | IniOPPRAIllegalFormatException e) {
			throw new PRCServiceException("PRCCRT20833", "갱신 중 오류가 발생하였습니다. 자세한 사항은 고객서비스센터(1588-1599)로 문의 하시기 바랍니다.");

		} finally {
			certUtils.closeOppra(oppra);
		}

		return rtn;
	}
}
