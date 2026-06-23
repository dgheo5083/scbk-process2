package com.scbank.process.api.svc.shared.components.alchera;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.utils.StringUtils;

import kr.co.useb.AES256;

/**
 * 알체라 AES256 암/복호화 처리 유틸리티 클래스
 */
public class AlcAES256 {

	/**
	 * 평문 AES256 암호화하여 문자열로 반환한다.
	 * @param plaintTex 평문
	 * @return AES256 암호화 처리 결과 문자열
	 * @throws PRCServiceException 암호화 처리 중 발생한 예외
	 */
    public static String encryptBytesToString(String plaintTex) {
        AES256 aes256 = new AES256();
        String encryptData = "";
        try {
            if (StringUtils.isEmpty(plaintTex)) {
                return encryptData;
            }
            encryptData = aes256.encryptBytesToString(plaintTex.getBytes());
        } catch (Exception e) {
            throw new PRCServiceException(AlcClient.ERR_MA008, AlcClient.ERR_MA008_MSG, e);
        }
        return encryptData;
    }

    /**
	 * 평문 AES256 암호화하여 바이트배열로 반환한다.
	 * @param plaintTex 평문
	 * @return AES256 암호화 처리 결과 바이트 배열
	 * @throws PRCServiceException 암호화 처리 중 발생한 예외
	 */
    public static byte[] encryptStringToBytes(String plaintTex) {
        AES256 aes256 = new AES256();
        byte[] encryptData = null;
        try {
            if (StringUtils.isEmpty(plaintTex)) {
                return encryptData;
            }
            encryptData = aes256.encryptStringToBytes(plaintTex);
        } catch (Exception e) {
            throw new PRCServiceException(AlcClient.ERR_MA008, AlcClient.ERR_MA008_MSG, e);
        }
        return encryptData;
    }

    /**
     * AES256 암호화 데이터를 복호화 처리하여 문자열로 반환한다.
     * @param encryptData AES256 암호화된 문자열
     * @return 복호화 처리된 문자열
     * @throws PRCServiceException 복호화 처리 중 발생한 예외
     */
    public static String decrypt(String encryptData) {
        AES256 aes256 = new AES256();
        String decryptData = "";
        try {
            if (StringUtils.isEmpty(encryptData)) {
                return decryptData;
            }
            decryptData = aes256.decrypt(encryptData);
        } catch (Exception e) {
            throw new PRCServiceException(AlcClient.ERR_MA007, AlcClient.ERR_MA007_MSG, e);
        }
        return decryptData;
    }

    /**
     * AES256 암호화 데이터 바이트배열을 복호화 처리하여 바이트배열로 반환한다.
     * @param encryptData AES256 암호화된 바이트배열
     * @return 복호화 처리된 바이트 배열
     * @throws PRCServiceException 복호화 처리 중 발생한 예외
     */
    public static byte[] decryptByteArray(byte[] encryptData) {
        AES256 aes256 = new AES256();
        byte[] decryptData = null;
        try {
            if (StringUtils.isEmpty(new String(encryptData))) {
                return decryptData;
            }
            decryptData = aes256.decryptByteArray(encryptData);
        } catch (Exception e) {
            throw new PRCServiceException(AlcClient.ERR_MA007, AlcClient.ERR_MA007_MSG, e);
        }
        return decryptData;
    }

    /**
     * AES256 암호화 데이터를 복호화 처리하여 바이트배열로 반환한다.
     * @param encryptData AES256 암호화된 문자열
     * @return 복호화 처리된 바이트 배열
     * @throws PRCServiceException 복호화 처리 중 발생한 예외
     */
    public static byte[] decryptStringToBytes(String encryptData) {
        AES256 aes256 = new AES256();
        byte[] decryptData = null;
        try {
            if (StringUtils.isEmpty(encryptData)) {
                return decryptData;
            }
            decryptData = aes256.decryptStringToBytes(encryptData);
        } catch (Exception e) {
            throw new PRCServiceException(AlcClient.ERR_MA007, AlcClient.ERR_MA007_MSG, e);
        }
        return decryptData;
    }

    /**
     * AES256 암호화 데이터를 복호화 처리하여 문자열로 반환한다.
     * @param encryptData AES256 암호화된 문자열
     * @return 복호화 처리된 문자열
     * @throws PRCServiceException 복호화 처리 중 발생한 예외
     */
    public static String decryptStringToBytes(byte[] encryptData) {
        AES256 aes256 = new AES256();
        String decryptData = "";
        try {
            if (StringUtils.isEmpty(new String(encryptData))) {
                return decryptData;
            }
            decryptData = aes256.decryptBytesToString(encryptData);
        } catch (Exception e) {
            throw new PRCServiceException(AlcClient.ERR_MA007, AlcClient.ERR_MA007_MSG, e);
        }
        return decryptData;
    }

}
