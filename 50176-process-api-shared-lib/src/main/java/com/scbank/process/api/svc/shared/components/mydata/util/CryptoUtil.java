package com.scbank.process.api.svc.shared.components.mydata.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

//import org.apache.commons.codec.binary.Base64;

//import sun.misc.BASE64Decoder;	//공식 사용 해야함 -> java버전 상승으로 사용 불가 java.util.Base64 로 해결함.
//import sun.misc.BASE64Encoder;	//공식 사용 해야함 -> java버전 상승으로 사용 불가 java.util.Base64 로 해결함.

/**
 * @Package Name : mdds.cmm.util
 * @Class Name : CryptoUtil.java
 * @Description : 암복호화 유틸
 * @Author : EKW
 * @Since : 2020. 10. 14. 오후 12:26:01
 */
public class CryptoUtil {
	private static final String CHARSET = "UTF-8";

	/**
	 * @Method Name : generateSHA256
	 * @Class Name : CryptoUtil
	 * @Description : SHA256 해쉬
	 * @Author : EKW
	 * @Since : 2020. 10. 15. 오후 3:35:37
	 * @param message
	 * @return
	 */
	public static String generateSHA256(String key, String message) {
		return getEncrypt(message, key.getBytes(), "SHA-256");
	}

	/**
	 * @Method Name : generateSHA384
	 * @Class Name : CryptoUtil
	 * @Description : SHA384 해쉬
	 * @Author : EKW
	 * @Since : 2020. 10. 15. 오후 3:35:54
	 * @param message
	 * @return
	 */
	public static String generateSHA384(String key, String message) {
		return getEncrypt(message, key.getBytes(), "SHA-384");
	}

	/**
	 * @Method Name : generateSHA512
	 * @Class Name : CryptoUtil
	 * @Description : SHA512 해쉬
	 * @Author : EKW
	 * @Since : 2020. 10. 15. 오후 3:36:17
	 * @param message
	 * @return
	 */
	public static String generateSHA512(String key, String message) {
		return getEncrypt(message, key.getBytes(), "SHA-512");
	}

	/**
	 * @Method Name : encrypt
	 * @Class Name : CryptoUtil
	 * @Description : ARIA 암호화
	 * @Author : EKW
	 * @Since : 2020. 10. 15. 오후 3:36:38
	 * @param key
	 * @param message
	 * @return
	 */
	public static String encrypt(String key, String message) {
		try {
			byte[] keyByte = key.getBytes();
			byte[] messageByte = message.getBytes(CHARSET);

			ARIAUtil ariaUtil = new ARIAUtil(keyByte);
			byte[] t1 = ariaUtil.encrypt(messageByte);
			String encResult = new String(Base64.getEncoder().encode(t1), "UTF-8");

			encResult = encResult.replaceAll("\r\n", "");
			encResult = encResult.replaceAll("\n", "");
			return encResult;
		} catch (InvalidKeyException e) {
			return e.getMessage();
		} catch (UnsupportedEncodingException e) {
			return e.getMessage();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * @Method Name : decrypt
	 * @Class Name : CryptoUtil
	 * @Description : ARIA 복호화
	 * @Author : EKW
	 * @Since : 2020. 10. 15. 오후 3:36:55
	 * @param key
	 * @param message
	 * @return
	 */
	public static String decrypt(String key, String message) {
		try {
			byte[] keyByte = key.getBytes();
			ARIAUtil ariaUtil = new ARIAUtil(keyByte);

			String msg = new String(message.getBytes(CHARSET));
			byte[] t1 = ariaUtil.decrypt(Base64.getDecoder().decode(msg));
			return new String(t1, CHARSET);
		} catch (InvalidKeyException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * @Method Name : innerTransEncrypt
	 * @Class Name : CryptoUtil
	 * @Description : 내부통신용 암호화
	 * @Author : EKW
	 * @Since : 2020. 11. 13. 오후 4:42:00
	 * @param key
	 * @param str
	 * @return
	 */
	public static String innerTransEncrypt(String key, String str) {
		return encrypt(generateSHA256(key, key), str);
	}

	/**
	 * @Method Name : innerTransDecrypt
	 * @Class Name : CryptoUtil
	 * @Description : 내부통신용 복호화
	 * @Author : EKW
	 * @Since : 2020. 11. 13. 오후 4:42:13
	 * @param key
	 * @param str
	 * @return
	 */
	public static String innerTransDecrypt(String key, String str) {
		return decrypt(generateSHA256(key, key), str);
	}

	/**
	 * @Method Name : getEncrypt
	 * @Class Name : CryptoUtil
	 * @Description : SHA 암호화
	 * @Author : EKW
	 * @Since : 2020. 12. 11. 오전 8:50:20
	 * @param source 원본
	 * @param type   SHA
	 * @return
	 */
	public static String getEncrypt(String source, byte[] key, String type) {
		String result = "";

		byte[] a = source.getBytes();
		byte[] bytes = new byte[a.length + key.length];

		System.arraycopy(a, 0, bytes, 0, a.length);
		System.arraycopy(key, 0, bytes, a.length, key.length);

		try {
			// 암호화 방식 지정 메소드
			MessageDigest md = MessageDigest.getInstance(type);
			md.update(bytes);

			byte[] byteData = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toString((byteData[i] & 0xFF) + 0 * 100, 16);

				if (hex.length() == 1) {
					sb.append(0);
				}

				sb.append(hex.substring(1));
			}

			result = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			result = source;
		}

		return result;
	}
}