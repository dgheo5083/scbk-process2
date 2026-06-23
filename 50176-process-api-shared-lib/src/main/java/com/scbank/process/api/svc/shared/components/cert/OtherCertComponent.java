/*******************************************************************************
*  업   무  명 : 공통
*  설      명  : 타기관(금결원 외) 인증서 공통 컴포넌트
*  작   성  자 : 오은진
*  작   성  일 : 2026.01.14
*  관련 테이블 :
*  관련 전문   :
* Copyright ⓒ SC제일은행. All Right Reserved
* ******************************************************************************
* 변경이력 (버전/변경일시/작성자)
* <pre>
* 최초작성 (1.0/2026.01.14/오은진)
* </pre>
******************************************************************************/
package com.scbank.process.api.svc.shared.components.cert;

import java.security.cert.X509Certificate;

import com.initech.ocspgd.OCSPGD;
import com.initech.ocspgd.OCSPGDConnectException;
import com.initech.ocspgd.OCSPGDIllegalFormatException;
import com.initech.ocspgd.OCSPGDReadException;
import com.initech.ocspgd.OCSPGDSystemException;
import com.initech.ocspgd.util.DefaultCodeType;
import com.initech.ocspgd.util.DefaultDataParser;
import com.initech.ocspgd.util.InquiryMsg;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent(name = "타기관(금결원 외) 인증서 공통 컴포넌트", description = "타기관(금결원 외) 인증서 공통 컴포넌트", author = "오은진")
@RequiredArgsConstructor
public class OtherCertComponent {

	/**
	 * 타기관(금결원 외) 인증서 등록
	 * @param userCert
	 * @param userId
	 * @param idn
	 */
	@ComponentOperation(name = "issueCert", description = "타기관(금결원 외) 인증서 등록", author = "오은진")
	public void issueCert(X509Certificate userCert, String userId, String idn) {
		log.debug("======OtherCert==  타기관 금융결제원이 아닌 인증서 제출");

		// 타기관 공인인증서 사용 등록
		OCSPGD ocspgd = null;
		String common = null;
		String data = null;
		String resCode;

		try {
			log.debug("idn :: {}", idn);
			String inmsg = InquiryMsg.createRegMsg(idn, userId, userCert);

			ocspgd = new OCSPGD(CertConfig.OCSPGD_IP, CertConfig.OCSPGD_PORT);
			ocspgd.initialize();
			ocspgd.requestRAW(CertConfig.OCSPGD_REGISTER_CODE, inmsg);

			common = ocspgd.getResponseCommonPart();
			data = ocspgd.getResponseDataPart();

			resCode = DefaultDataParser.getMsg(data, DefaultCodeType.ResCode).trim();

		} catch (OCSPGDReadException e) {
			throw new PRCServiceException("PRCCRT20554", "OCSPGD의 소켓으로부터 읽기 쓰기 실패입니다. 자세한 내용은 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} catch (OCSPGDConnectException e) {
			throw new PRCServiceException("PRCCRT20555", "OCSPGD와 연결할 수 없습니다.[OCSPGD Connection Fail] 현재 금융결제원 공인인증센터 장애로 인하여 해당거래가 불가하오니 잠시후에 거래하여 주시기 바랍니다.");
		} catch (OCSPGDSystemException e) {
			throw new PRCServiceException("PRCCRT20556", "OCSPGD 서버 점검중입니다. 자세한 내용은 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} catch (OCSPGDIllegalFormatException e) {
			throw new PRCServiceException("PRCCRT20557", "OCSPGD로 보내는 전문 메시지가 올바르지 않습니다. 자세한 내용은 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} catch (Exception e) {
			log.error("CertTask exception :: {}", e.toString());
			throw new PRCServiceException("PRCCRT20558", "OCSPGD관련 알수 없는 오류가 발생하였습니다. 자세한 내용은 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} finally {
			try {
				if (ocspgd != null) {
					ocspgd.close();
				}
			} catch (Exception e) {
			}
		}

		if (!(resCode.equals("000"))) {
			throw new PRCServiceException(resCode, "인증서 효력정지 상태");
		}
	}

}
