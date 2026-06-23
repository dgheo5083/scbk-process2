package com.scbank.process.api.svc.shared.utils;

import java.util.HashMap;
import java.util.Map;

import com.nshc.NFilter;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/*******************************************************************************
 * <pre>
 * 업   무  명 : 공통
 * 서브 업무명 : E2EUtil
 * 설       명 :
 * 작   성  자 : 
 * 작   성  일 : 2018. 12. 14
 * 관련 테이블 :
 * 관련 전문   :
 * Copyright ⓒ SC제일은행. All Right Reserved
 *******************************************************************************
 * 변경이력 (버전/변경일시/작성자)
 * 최초작성 (1.0/2018. 12. 14/)
 *           1.1/2025. 01. 17/김이교
 * </pre>
 ******************************************************************************/
@Slf4j
public class E2EUtil {
	public static final String NFILTER_PUBLICKEY = "nFilter_publicKey";
	public static final String NFILTER_PRIVATEKEY = "nFilter_PrivateKey";

	public static Map<String, String> getNkey(HttpSession session) {
		Map<String, String> resultMap = new HashMap<>();

		String nFilter_publicKey = (String) session.getAttribute(NFILTER_PUBLICKEY);
		String nFilter_PrivateKey = (String) session.getAttribute(NFILTER_PRIVATEKEY);

		try {
			if (nFilter_publicKey == null && nFilter_PrivateKey == null) {

				NFilter nfilter = new NFilter();
				
				byte[][] ecckeypair = nfilter.GenerateKeypair();

				nFilter_publicKey = nfilter.Base64Encode(ecckeypair[0]);
				nFilter_PrivateKey = nfilter.Base64Encode(ecckeypair[1]);
				session.setAttribute(NFILTER_PUBLICKEY, nFilter_publicKey);
				session.setAttribute(NFILTER_PRIVATEKEY, nFilter_PrivateKey);
			}
		} catch (Exception e) {
			log.error("error : [{}] ", e);
		}

		resultMap.put(NFILTER_PUBLICKEY, nFilter_publicKey);
		resultMap.put(NFILTER_PRIVATEKEY, nFilter_PrivateKey);
		return resultMap;
	}

	public static void setNkey(HttpSession session, Map<String, String> map) {
		session.setAttribute(NFILTER_PUBLICKEY, map.get(NFILTER_PUBLICKEY));
		session.setAttribute(NFILTER_PRIVATEKEY, map.get(NFILTER_PRIVATEKEY));
	}

}