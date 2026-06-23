package com.scbank.process.api.svc.shared.components.obs.utils;

import org.apache.commons.codec.binary.Base64;

import com.initech.inisafenet.KeyFixManager;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;

/*******************************************************************************
 * <pre>
 *  업   무  명 : OBS
 *  서브 업무명 :
 *  설       명 : 
 *  작   성  자 : 김희준
 *  작   성  일 : 2019. 10. 31.
 *  관련 테이블 :
 *  관련 전문   :
 *  Copyright ⓒ SC제일은행. All Right Reserved
 * ******************************************************************************
 *  변경이력 (1.1/2019.12.03/김희준)
 *  최초작성 (1.0/2019.10.31/하정수)
 * </pre>
 ******************************************************************************/
public class ObsCryptoUtils {

	/**
	 * 암호화 모듈
	 * 
	 * @param text
	 * @return
	 */
	public static String encrypt(String text) {
		if (text == null || text.length() == 0) {
			// Error: 전자서명 데이터가 들어오지 않았습니다.
			throw new PRCServiceException("MA3CMM0003", "토큰 데이터가 들어오지 않았습니다.");
		}

		try {
			String IV = PropertiesUtils.getString("OBSCRYPTOUTIL_IV");
			String KEY = PropertiesUtils.getString("OBSCRYPTOUTIL_KEY");
			
			String inisafenetPath = PropertiesUtils.getString("INITECH_INISAFENET_PATH");
			KeyFixManager keyfix = new KeyFixManager(hexToBin(KEY), hexToBin(IV), inisafenetPath);
			// keyfix.setEncryptAlgorithm("SEED/CBC/NoPadding");
			keyfix.setEncryptAlgorithm("SEED/CBC");

			// System.out.println("TTTT: [" + text + "]");

			byte[] b = keyfix.encrypt(Base64.decodeBase64(text.getBytes()));
			String s = new String(Base64.encodeBase64(b));
			String s2 = s.replaceAll("\n", "");
			return s2;
		} catch (Exception e) {
			// 암호화 오류가 발생하면 Exception를 발생시킨다.
			throw new PRCServiceException("MA3CMM0003", "토큰 데이터 처리중 에러가 발생하였습니다.", e);
		}

		// return text;
	}

	/**
	 * 복호화 모듈
	 */
	public static String decrypt(String text) {
		if (text == null || text.length() == 0) {
			// Error: 전자서명 데이터가 들어오지 않았습니다.
			return null;
		}

		try {
			String IV = PropertiesUtils.getString("OBSCRYPTOUTIL_IV");
			String KEY = PropertiesUtils.getString("OBSCRYPTOUTIL_KEY");
			
			String inisafenetPath = PropertiesUtils.getString("INITECH_INISAFENET_PATH");
			KeyFixManager keyfix = new KeyFixManager(hexToBin(KEY), hexToBin(IV), inisafenetPath);
			// keyfix.setEncryptAlgorithm("SEED/CBC/NoPadding");
			keyfix.setEncryptAlgorithm("SEED/CBC");

			byte[] b = keyfix.decrypt(Base64.decodeBase64(text.getBytes()));
			String s = new String(Base64.encodeBase64(b));
			String s2 = s.replaceAll("\n", "");
			return s2;
		} catch (Exception e) {
			// 복호화 오류가 발생하면 Exception를 발생시킨다.
			throw new PRCServiceException("MA3CMM0003", "토큰 데이터 처리중 에러가 발생하였습니다.", e);
		}
	}

	/**
	 * hex를 Bin으로 변환
	 * 
	 * @param str
	 * @return
	 */
	private static byte[] hexToBin(String str) {
		int len = str.length();
		byte[] out = new byte[len / 2];
		int endindx;

		for (int i = 0; i < len; i = i + 2) {
			endindx = i + 2;
			if (endindx > len)
				endindx = len - 1;
			out[i / 2] = (byte) Integer.parseInt(str.substring(i, endindx), 16);
		}
		return out;
	}
}
