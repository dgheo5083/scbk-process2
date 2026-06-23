package com.scbank.process.api.fw.base.integration.system.oltp;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.oltp.exception.OltpSystemException;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpError;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpReqHeader;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;
import com.scbank.process.api.fw.base.shared.ScbkChnnlErrMgtComponent;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.response.IntegrationResponseHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 호스트 전문 응답 처리 핸들러 구현 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
public class OltpResponseHandler implements IntegrationResponseHandler<OltpReqHeader, OltpResHeader, OltpError> {

	private static final byte[] bCapErrorCode = { (byte) 0x82, (byte) 0x80, (byte) 0x80, (byte) 0x80 };

	private static final String VAL_CAPABEND = "CAPABEND";
	private static final String VAL_CAP_ERR = "CAP_ERR";

	private static final String ERROR_TYPE_CAP = "CAP";
	private static final String ERROR_TYPE_HOST = "HOST";

	private static final String DEFAULT_CHANNEL_TYPE = "SM";

	private ScbkChnnlErrMgtComponent scbkChnnlErrMgt_COM;

	/**
	 * CAP 에러 체크
	 * @param context {@link IntegrationContext}
	 * @param requestHeader {@link OltpReqHeader}
	 * @param header {@link OltpResHeader}
	 * @param error {@link OltpError}
	 */
	public void checkCapError(IntegrationContext context, OltpReqHeader requestHeader, OltpResHeader header,
			OltpError error) {
		if (!this.isError(header)) {
			return;
		}
		
		OltpCommon hostCommon = header.getOltpCommon();
		
		String errMsg1 = StringUtils.defaultIfEmpty(error.getErrMsg1(), StringUtils.EMPTY).trim();
		String errMsg2 = StringUtils.defaultIfEmpty(error.getErrMsg2(), StringUtils.EMPTY).trim();
		
		String sHostErrorModule = StringUtils.defaultIfEmpty(error.getSHostErrorModule(), StringUtils.EMPTY).trim();
		String capErrorState = this.getCapErrorState(hostCommon);
		String errorCode = this.getResponseCode(header);
		String errorLocation = "";
		
		String errMsg = StringUtils.EMPTY;
		String wrnMsg = StringUtils.EMPTY;
		String countryCode = this.getCountryCode(context);
		String chnlType = DEFAULT_CHANNEL_TYPE;
		
		byte[] checkFlagBytes = this.getCheckFlagBytes(hostCommon);
		if (checkFlagBytes[0] != (byte)0x80) {
			/* CAP ABEND ==> Region Down   */
			if (checkFlagBytes[0] == (byte)0x00)
			{
				errorCode = IntegrationConstant.ERRCODE_HOST_CAPABEND_RD;
				sHostErrorModule = IntegrationConstant.ERRMESG_HOST_CAPABEND_RD;
				errMsg1 = IntegrationConstant.ERRMESG_HOST_CAPABEND_RD1;
				errMsg2 = IntegrationConstant.ERRMESG_HOST_CAPABEND_RD2;
				wrnMsg = IntegrationConstant.ERRMESG_HOST_CAPABEND_RD3;
				
				String errorCustomerMessage = errMsg1;
				if (StringUtils.isNotEmpty(errMsg2)) {
	    			errorCustomerMessage += "^" + errMsg2;
	    		}

				OltpSystemException ex = new OltpSystemException(header, errorCode, errorCustomerMessage);
				ex.setErrorCode(errorCode);
				ex.setErrorModule(sHostErrorModule);
				ex.setErrorGuideMessage(wrnMsg);
				throw ex;
			} else {
				
				OltpCommon reqCommon = requestHeader.getOltpCommon();
				String reqInCd = reqCommon.getInClassCd(); //요청 입력식별코드
				String resInCd = hostCommon.getInClassCd(); //응답 입력식별코드
				String resIniInCla = hostCommon.getIniInCla(); //응답 초기 입력식별코드
				
				if (reqInCd.equals(resInCd)) {
					errorCode = IntegrationConstant.ERRCODE_HOST_CAPABEND;
					sHostErrorModule = IntegrationConstant.ERRMESG_HOST_CAPABEND;
					errMsg1	= IntegrationConstant.ERRMESG_HOST_CAPABEND1;
					errMsg2 = IntegrationConstant.ERRMESG_HOST_CAPABEND2;
					wrnMsg = IntegrationConstant.ERRMESG_HOST_CAPABEND3;
				} 
				//ABEND 오류가 발생하면서 요청 입력식별코드와 응답 입력식별코드가 다른경우
				//응답의 초기 입력식별코드가 요청 입력식별코드와 같은지 판단 후 수신  CAP ABEND로 판단
				else
				{
					//수신 CAP ABEND 오류 처리
					if (reqInCd.equals(resIniInCla))
					{
						errorCode = IntegrationConstant.ERRCODE_HOST_CAPABEND_A;
						sHostErrorModule = IntegrationConstant.ERRMESG_HOST_CAPABEND_A;
						errMsg1	= IntegrationConstant.ERRMESG_HOST_CAPABEND_A1;
						errMsg2 = IntegrationConstant.ERRMESG_HOST_CAPABEND_A2;
					}
					else
					{
						errorCode = IntegrationConstant.ERRCODE_HOST_CAPABEND;
						sHostErrorModule = IntegrationConstant.ERRMESG_HOST_CAPABEND;
						errMsg1	= IntegrationConstant.ERRMESG_HOST_CAPABEND1;
						errMsg2 = IntegrationConstant.ERRMESG_HOST_CAPABEND2;
						wrnMsg = IntegrationConstant.ERRMESG_HOST_CAPABEND3;
					}
				}
				
				String errorCustomerMessage = errMsg1;
				if (StringUtils.isNotEmpty(errMsg2)) {
	    			errorCustomerMessage += "^" + errMsg2;
	    		}
				
				OltpSystemException ex = new OltpSystemException(header, errorCode, errorCustomerMessage);
				ex.setErrorCode(errorCode);
				ex.setErrorModule(sHostErrorModule);
				ex.setErrorGuideMessage(wrnMsg);
				throw ex;
			}
		} else if (checkFlagBytes[3] != (byte)0x80) {
			log.debug("HOST DUMMY MESSAGE RECEIVE");
			return;
		}
		
		// CAPABEND 에러여부 확인
		if (this.checkCapAbendError(hostCommon)) {
			errorCode = capErrorState;
			errorLocation = ERROR_TYPE_CAP;
		}

		if (VAL_CAPABEND.equals(sHostErrorModule) ||
				VAL_CAP_ERR.equals(sHostErrorModule)) {
			errorLocation = ERROR_TYPE_CAP;
		}
		
		if (ERROR_TYPE_CAP.equals(errorLocation)) {
			String dbErrMsg = this.getDBErrorMessage(chnlType, ERROR_TYPE_CAP, countryCode, sHostErrorModule);
			if (StringUtils.isNotEmpty(dbErrMsg)) {
				errMsg = dbErrMsg;
			} else {
				errMsg = errMsg1 + errMsg2;
			}
			wrnMsg = "";
			
			OltpSystemException ex = new OltpSystemException(header, errorCode, errMsg);
			//호스트 에러부 추가
			ex.setError(error);
			ex.setErrorLocation(errorLocation);
			ex.setErrorModule(sHostErrorModule);
			ex.setErrorGuideMessage(wrnMsg);
			throw ex;
		}
	}
	
	@Override
	public void checkErrorAndThrowable(IntegrationContext context, OltpReqHeader requestHeader, OltpResHeader header,
			OltpError error) {
		if (!this.isError(header)) {
			return;
		}
		
		this.checkCapError(context, requestHeader, header, error);

		OltpCommon hostCommon = header.getOltpCommon();

		String errMsg1 = StringUtils.defaultIfEmpty(error.getErrMsg1(), StringUtils.EMPTY).trim();
		String errMsg2 = StringUtils.defaultIfEmpty(error.getErrMsg2(), StringUtils.EMPTY).trim();
		String wrnMsg1 = StringUtils.defaultIfEmpty(error.getWrnMsg1(), StringUtils.EMPTY).trim();
		String wrnMsg2 = StringUtils.defaultIfEmpty(error.getWrnMsg2(), StringUtils.EMPTY).trim();
		String wrnMsg3 = StringUtils.defaultIfEmpty(error.getWrnMsg3(), StringUtils.EMPTY).trim();

		String sHostErrorModule = StringUtils.defaultIfEmpty(error.getSHostErrorModule(), StringUtils.EMPTY).trim();
		String capErrorState = this.getCapErrorState(hostCommon);
		String errorCode = this.getResponseCode(header);
		String errorLocation = ERROR_TYPE_HOST;

		String errMsg = StringUtils.EMPTY;
		String wrnMsg = StringUtils.EMPTY;
		String countryCode = this.getCountryCode(context);
		String chnlType = DEFAULT_CHANNEL_TYPE;

		// CAPABEND 에러여부 확인
		if (this.checkCapAbendError(hostCommon)) {
			errorCode = capErrorState;
			errorLocation = ERROR_TYPE_CAP;
		}

		if (VAL_CAPABEND.equals(sHostErrorModule) ||
				VAL_CAP_ERR.equals(sHostErrorModule)) {
			errorLocation = ERROR_TYPE_CAP;
		}

		if (ERROR_TYPE_CAP.equals(errorLocation)) {
			errMsg1 = "요청하신 거래는 처리중이오니 송금[이체]거래인 경우, [거래명세조회]에서 처리여부를 반드시 확인하시기 바랍니다.";
		}

		if ("KR".equals(countryCode)) {
			if (this.isMsgChangeErrorCode(context, errorCode)
					&& this.isMsgChangeErrorModule(context, sHostErrorModule)) {
				errMsg = this.getDBErrorMessage(chnlType, errorCode, countryCode, sHostErrorModule);
				if (StringUtils.isEmpty(errMsg)) {
					errMsg = errMsg1.trim() + "^" + errMsg2.trim() + "^";
				} // end if

				errMsg = errMsg.replace(".", ".^");
				wrnMsg = wrnMsg1.trim() + " " + wrnMsg2.trim() + " " + wrnMsg3.trim(); // Dynamic 메시지
			} else {
				// 에러처리 스킵 오류코드 목록을 가져온다.
				List<String> specialErrorList = Arrays.asList(
						StringUtils.defaultIfEmpty(context.getAttribute("special_error_list"), "").trim()
								.split("\\,"));

				if (specialErrorList.contains(errorCode) ||
						!"".equals(wrnMsg1) ||
						!"".equals(wrnMsg2)) {
					errMsg = errMsg1.trim() + "^" + errMsg2.trim();
					wrnMsg = wrnMsg1.trim() + "^" + wrnMsg2.trim() + "^" + wrnMsg3.trim();
				} else {
					errMsg = this.getDBErrorMessage(chnlType, errorCode, countryCode, sHostErrorModule);
					if (StringUtils.isEmpty(errMsg)) {
						errMsg = errMsg1.trim() + "^" + errMsg2.trim() + "^";
					} // end if

					errMsg = errMsg.replace(".", ".^");
					wrnMsg = wrnMsg1.trim() + " " + wrnMsg2.trim() + " " + wrnMsg3.trim(); // Dynamic 메시지
				}
			}
		} else {
			errMsg = this.getDBErrorMessage(chnlType, errorCode, countryCode, sHostErrorModule);
			if (StringUtils.isEmpty(errMsg)) {
				errMsg = errMsg1.trim() + "^" + errMsg2.trim() + "^";
			} // end if

			errMsg = errMsg.replace(".", ".^");
			wrnMsg = wrnMsg1.trim() + " " + wrnMsg2.trim() + " " + wrnMsg3.trim(); // Dynamic 메시지
		}

		OltpSystemException ex = new OltpSystemException(header, errorCode, errMsg);
		//호스트 에러부 추가
		ex.setError(error);
		ex.setErrorLocation(errorLocation);
		ex.setErrorModule(sHostErrorModule);
		ex.setErrorGuideMessage(wrnMsg);
		throw ex;
	}

	@Override
	public String getResponseCode(OltpResHeader header) {
		OltpCommon hostCommon = header.getOltpCommon();
		return hostCommon.getErrorCode();
	}

	@Override
	public boolean isError(OltpResHeader header) {
		String errorCode = this.getResponseCode(header);
		return !"0000".equals(errorCode);
	}

	/**
	 * CapError 상태정보를 가져온다.
	 * 
	 * @param hostCommon 호스트 공통부
	 * @return
	 */
	private String getCapErrorState(OltpCommon hostCommon) {
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtils.defaultIfEmpty(hostCommon.getMsgInfoBlk(), "").trim());
		sb.append(StringUtils.defaultIfEmpty(hostCommon.getModeInfo(), "").trim());
		sb.append(StringUtils.defaultIfEmpty(hostCommon.getDesTp(), "").trim());
		return sb.toString();
	}
	
	/**
	 * 
	 * @param hostCommon
	 * @return
	 */
	private byte[] getCheckFlagBytes(OltpCommon hostCommon)
	{
		byte[] checkFlags = new byte[4];
		try {
			String stFlag1 = StringUtils.defaultIfEmpty(hostCommon.getStFlag1(), StringUtils.EMPTY).trim();
			String stFlag2 = StringUtils.defaultIfEmpty(hostCommon.getStFlag2(), StringUtils.EMPTY).trim();
			String stFlag3 = StringUtils.defaultIfEmpty(hostCommon.getStFlag3(), StringUtils.EMPTY).trim();
			String stFlag4 = StringUtils.defaultIfEmpty(hostCommon.getStFlag4(), StringUtils.EMPTY).trim();

			byte stFlag01 = stFlag1.getBytes(IntegrationConstant.HOST_ENCODING)[0];
			byte stFlag02 = stFlag2.getBytes(IntegrationConstant.HOST_ENCODING)[0];
			byte stFlag03 = stFlag3.getBytes(IntegrationConstant.HOST_ENCODING)[0];
			byte stFlag04 = stFlag4.getBytes(IntegrationConstant.HOST_ENCODING)[0];

			return new byte[] {stFlag01, stFlag02, stFlag03, stFlag04};
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return checkFlags;
		}
	}

	/**
	 * CAPABEND 에러여부를 확인한다.
	 * 
	 * @param hostCommon 응답전문 호스트 공통부
	 * @return CAPABEND 에러여부 (true: CAPABEND 에러, false: CAPABEND 에러아님)
	 */
	private boolean checkCapAbendError(OltpCommon hostCommon) {
		boolean checkFlag = false;
		try {
			String stFlag1 = StringUtils.defaultIfEmpty(hostCommon.getStFlag1(), StringUtils.EMPTY).trim();
			String stFlag2 = StringUtils.defaultIfEmpty(hostCommon.getStFlag2(), StringUtils.EMPTY).trim();
			String stFlag3 = StringUtils.defaultIfEmpty(hostCommon.getStFlag3(), StringUtils.EMPTY).trim();
			String stFlag4 = StringUtils.defaultIfEmpty(hostCommon.getStFlag4(), StringUtils.EMPTY).trim();

			byte stFlag01 = stFlag1.getBytes(IntegrationConstant.HOST_ENCODING)[0];
			byte stFlag02 = stFlag2.getBytes(IntegrationConstant.HOST_ENCODING)[0];
			byte stFlag03 = stFlag3.getBytes(IntegrationConstant.HOST_ENCODING)[0];
			byte stFlag04 = stFlag4.getBytes(IntegrationConstant.HOST_ENCODING)[0];

			if (stFlag01 == bCapErrorCode[0] &&
					stFlag02 == bCapErrorCode[1] &&
					stFlag03 == bCapErrorCode[2] &&
					stFlag04 == bCapErrorCode[3]) {
				checkFlag = true;
			}
			return checkFlag;	
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return checkFlag;
		}
	}

	/**
	 * 현재 요청 Locale을 기준으로 국가코드를 가져온다.
	 * 
	 * @param ctx 연계시스템 컨텍스트 객체
	 * @return 획득한 국가코드
	 */
	private String getCountryCode(IntegrationContext ctx) {
		Locale locale = ctx.getLocale();
		String countryCode = StringUtils.defaultIfEmpty(locale.getCountry(), StringUtils.EMPTY).toUpperCase();
		return countryCode;
	}

	/**
	 * 
	 * @param channelType
	 * @param errorCode
	 * @param countryCode
	 * @param errorMode
	 * @return
	 */
	private String getDBErrorMessage(String channelType, String errorCode, String countryCode, String errorMode) {
		scbkChnnlErrMgt_COM = RuntimeContext.getBean(ScbkChnnlErrMgtComponent.class);
		String errMsg = scbkChnnlErrMgt_COM.getErrorMessage(channelType, errorCode, countryCode,
				errorMode);
		return errMsg;
	}

	/**
	 * 
	 * @param context
	 * @param errorCode
	 * @return
	 */
	private boolean isMsgChangeErrorCode(IntegrationContext context, String errorCode) {
		List<String> msgChangeErrorCodeList = Arrays.asList(
				StringUtils.defaultIfEmpty(context.getAttribute("msg_change_errorcode_list"), StringUtils.EMPTY).trim()
						.split("\\,"));
		return msgChangeErrorCodeList.contains(errorCode);
	}

	/**
	 * 
	 * @param context
	 * @param errorModule
	 * @return
	 */
	private boolean isMsgChangeErrorModule(IntegrationContext context, String errorModule) {
		List<String> msgChangeErrorModuleList = Arrays.asList(
				StringUtils.defaultIfEmpty(context.getAttribute("msg_change_errormodule_list"), StringUtils.EMPTY)
						.trim()
						.split("\\,"));
		return msgChangeErrorModuleList.contains(errorModule);
	}
}
