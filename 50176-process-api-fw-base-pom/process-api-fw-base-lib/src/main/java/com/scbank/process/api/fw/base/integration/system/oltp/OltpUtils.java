package com.scbank.process.api.fw.base.integration.system.oltp;

import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpError;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 호스트 전문 유틸리티 클래스
 */
@Slf4j
@UtilityClass
public class OltpUtils {
	
	private static final byte[] CAP_ERROR_FLAG = { (byte) 0x82, (byte) 0x80, (byte) 0x80, (byte) 0x80 };

	/**
	 * 호스트 전문 응답이 CAP ABEND 오류인지 확인한다.
	 * @param response 호스트 전문 응답
	 * @return CAP ABEND 오류여부
	 */
	public boolean isCapAbendError(OltpResponse<? extends IMessageObject> response) {
		if (!response.isError()) {
			return false;
		}
		
		OltpResHeader resHeader = response.getHeader();
		if (resHeader == null) {
			return false;
		}
		
		OltpCommon hostCommon = resHeader.getOltpCommon();
		if (hostCommon == null) {
			return false;
		}
		
		return checkCapAbendError(hostCommon);
	}
	
	/**
	 * CapError 상태정보를 가져온다.
	 * 
	 * @param hostCommon 호스트 공통부
	 * @return
	 */
	public String getCapErrorState(OltpCommon hostCommon) {
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtils.defaultIfEmpty(hostCommon.getMsgInfoBlk(), "").trim());
		sb.append(StringUtils.defaultIfEmpty(hostCommon.getModeInfo(), "").trim());
		sb.append(StringUtils.defaultIfEmpty(hostCommon.getDesTp(), "").trim());
		return sb.toString();
	}
	
	/**
	 * CAP ABEND 에러여부를 확인한다.
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

			if (stFlag01 == CAP_ERROR_FLAG[0] &&
					stFlag02 == CAP_ERROR_FLAG[1] &&
					stFlag03 == CAP_ERROR_FLAG[2] &&
					stFlag04 == CAP_ERROR_FLAG[3]) {
				checkFlag = true;
			}
			return checkFlag;	
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return checkFlag;
		}
	}
	
	/**
	 * 오류코드를 가져온다.
	 * @param response 전문 응답 객체
	 * @return 오류코드 문자열
	 */
	public String getErrorCode(OltpResponse<? extends IMessageObject> response) {
		if (!response.isError()) {
			return StringUtils.EMPTY;
		}
		
		OltpCommon hostCommon = response.getHeader().getOltpCommon();
		return hostCommon.getResponseCode();
	}
	
	/**
	 * 오류모듈 문자열을 가져온다.
	 * @param response 호스트 전문 응답
	 * @return
	 */
	public String getErrorModule(OltpResponse<? extends IMessageObject> response) {
		if (!response.isError()) {
			return StringUtils.EMPTY;
		}
		
		OltpError hostError = response.getErrorResponse();
		return hostError != null ? hostError.getSHostErrorModule() : StringUtils.EMPTY;
	}
	
	/**
	 * 에러메시지1 문자열을 획득한다.
	 * @param response 호스트 전문 응답
	 * @return 에러메시지1 문자열
	 */
	public String getErrMsg1(OltpResponse<? extends IMessageObject> response) {
		if (!response.isError()) {
			return StringUtils.EMPTY;
		}
		
		OltpError hostError = response.getErrorResponse();
		return hostError != null ? hostError.getErrMsg1() : StringUtils.EMPTY;
	}
	
	/**
	 * 에러메시지2 문자열을 획득한다.
	 * @param response 호스트 전문 응답
	 * @return
	 */
	public String getErrMsg2(OltpResponse<? extends IMessageObject> response) {
		if (!response.isError()) {
			return StringUtils.EMPTY;
		}
		
		OltpError hostError = response.getErrorResponse();
		return hostError != null ? hostError.getErrMsg2() : StringUtils.EMPTY;
	}
	
	/**
	 * WrnMsg1 문자열을 획득한다.
	 * @param response 호스트 전문 응답
	 * @return WrnMsg1 문자열
	 */
	public String getWrnMsg1(OltpResponse<? extends IMessageObject> response) {
		if (!response.isError()) {
			return StringUtils.EMPTY;
		}
		
		OltpError hostError = response.getErrorResponse();
		return hostError != null ? hostError.getWrnMsg1() : StringUtils.EMPTY;
	}
	
	/**
	 * WrnMsg2 문자열을 획득한다.
	 * @param response 호스트 전문 응답
	 * @return WrnMsg2 문자열
	 */
	public String getWrnMsg2(OltpResponse<? extends IMessageObject> response) {
		if (!response.isError()) {
			return StringUtils.EMPTY;
		}
		
		OltpError hostError = response.getErrorResponse();
		return hostError != null ? hostError.getWrnMsg2() : StringUtils.EMPTY;
	}
	
	/**
	 * WrnMsg3 문자열을 획득한다.
	 * @param response 호스트 전문 응답
	 * @return WrnMsg3 문자열
	 */
	public String getWrnMsg3(OltpResponse<? extends IMessageObject> response) {
		if (!response.isError()) {
			return StringUtils.EMPTY;
		}
		
		OltpError hostError = response.getErrorResponse();
		return hostError != null ? hostError.getWrnMsg3() : StringUtils.EMPTY;
	}
}
