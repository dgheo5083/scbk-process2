package com.scbank.process.api.svc.shared.utils;

import com.dreammirae.kfb.mobile.SecureAnyOtp;
import com.dreammirae.kfb.mobile.SecureAnyOtpException;
import com.scbank.process.api.fw.base.exception.PRCServiceException;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class MotpUtils {

	/**
	 * OTP값 복호화 처리
	 * @param safeCardNum
	 * @param deviceId
	 * @return
	 */
	public String decryptOTP(String safeCardNum, String deviceId) {
		String decryptSafeCardNum = "";
		try {
			decryptSafeCardNum = SecureAnyOtp.decryptOTP(safeCardNum, deviceId);
		} catch (SecureAnyOtpException e) {
			decryptSafeCardNum = "";
			log.error(e.getMessage());
		}
		return decryptSafeCardNum;
	}

	/**
	 * TokenKey 암호화 처리
	 * @param secretKeyB
	 * @param deviceInfo
	 * @param clientRnd
	 * @param pinNumber
	 * @return
	 */
	public String encryptTokenKey(String secretKeyB, String deviceInfo, String clientRnd, String pinNumber) {
		String secretKeyBresult = "";
		try {
			secretKeyBresult = SecureAnyOtp.encryptTokenKey(secretKeyB, deviceInfo, clientRnd, pinNumber);
		} catch (SecureAnyOtpException e) {
			log.error("encryptTokenKey error :: {}", e);
			throw new PRCServiceException(e);
		}
		return secretKeyBresult;
	}

	/**
	 * Challenge 암호화 처리
	 * @param rndChallenge
	 * @param deviceInfo
	 * @param clientRnd
	 * @param pinNumber
	 * @return
	 */
	public String encryptServerChallenge(String rndChallenge, String deviceInfo, String clientRnd, String pinNumber) {
		String rndChallangeBresult = "";
		try {
			rndChallangeBresult = SecureAnyOtp.encryptServerChallenge(rndChallenge, deviceInfo, clientRnd, pinNumber);
		} catch (SecureAnyOtpException e) {
			log.error("encryptServerChallenge error :: {}", e);
			throw new PRCServiceException(e);
		}
		return rndChallangeBresult;
	}

}
