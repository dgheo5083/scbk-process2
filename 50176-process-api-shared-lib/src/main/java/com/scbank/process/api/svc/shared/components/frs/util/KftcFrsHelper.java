package com.scbank.process.api.svc.shared.components.frs.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author 
 *
 */
@Slf4j
public class KftcFrsHelper {

	private static final String PROP_BASEURL		= "kftc.face-id.api.base.url";
	private static final String PROP_CLIENT_ID		= "kftc.face-id.api.client_id";
	private static final String PROP_CLIENT_SECRET	= "kftc.face-id.api.client_secret";

	public final static String FACE_ID		= "FACE_ID";
	public final static String ORG_CODE		= "023";
	
	public final static String TOKEN		= "TOKEN";
	public final static String GRANT_TYPE	= "client_credentials";
	public final static String SCOPE		= "face_id";
	
	/** 금결원 정상 코드 */
	public final static String SUCCESS_RSP_CODE = "000";

	public static HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return sra.getRequest();
	}

	public static String getClientId() {
		String clientId = StringUtils.defaultIfBlank(PropertiesUtils.getString(PROP_CLIENT_ID), "-");
		
		// TODO : FRS 복호화: properties값을 암호화 하여 저장한다. 로컬 구동 불가능으로 인한 암호화 방법 ?
		
//		if ("-".equals(clientId)) {
//			throw new PRCServiceException(String.format("KftcFaceRecognition.xx.properties::%s 파일 또는 속성값이 있는지 확인해 주세요.", PROP_CLIENT_ID));
//		} /* end of if */
//
//		try {
//
//			return FrsCryptoUtils.decrypt(clientId);
//
//		} catch (Exception e) {
//
//			String enClientId = "암호화 오류!";
//
//			try {
//				enClientId = FrsCryptoUtils.encrypt(clientId);
//			} catch (Exception e1) {
//				new PRCServiceException("암호화 오류!", e1);
//			}
//
//			log.error(String.format("암호화되지 않았습니다. 설정된 값을 [%s]변경하세요", enClientId));
//		}

		return clientId;
	}

	public static String getClientSecret() {

		String clientSecret = StringUtils.defaultIfBlank(PropertiesUtils.getString(PROP_CLIENT_SECRET), "-");

		// TODO : FRS 복호화: properties값을 암호화 하여 저장한다.
//		if ("-".equals(clientSecret)) {
//			throw new PRCServiceException(String.format("KftcFaceRecognition.xx.properties::%s 파일 또는 속성값이 있는지 확인해 주세요.", PROP_CLIENT_SECRET));
//		} /* end of if */
//
//		try {
//			return FrsCryptoUtils.decrypt(clientSecret);
//		} catch (Exception e) {
//			String enClientSecret = null;
//
//			try {
//				enClientSecret = FrsCryptoUtils.encrypt(clientSecret);
//			} catch (Exception e1) {
//				new PRCServiceException("clientSecret 암호화 오류!", e1);
//			}
//
//			log.error(String.format("clientSecret 암호화되지 않았습니다. 설정된 값을 [%s]변경하세요", enClientSecret));
//		} /* end of if */

		return clientSecret;
	}

	public static String getBaseUrl() {
		return PropertiesUtils.getString(PROP_BASEURL);
	}
	
	public static boolean isHttps() {
		return (getBaseUrl().indexOf("https:") == 0);
	}

}