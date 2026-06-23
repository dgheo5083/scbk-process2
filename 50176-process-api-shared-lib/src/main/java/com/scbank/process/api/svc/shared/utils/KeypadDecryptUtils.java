package com.scbank.process.api.svc.shared.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ahnlab.astx2.servlet.ASTX2LibCompact;
import com.ahnlab.astx2.servlet.ASTX2LibLocal;
import com.nshc.NFilter;
import com.nshc.exception.NFilterException;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.config.nfilter.storage.IOpenWebNFilterSessionStorage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import net.nshc.nfilter.openweb.OpenWebNFilterConfig;
import net.nshc.nfilter.openweb.OpenWebNFilterConstants;
import net.nshc.nfilter.openweb.util.crypto.OpenWebNFilterDecryptor;

@Slf4j
public class KeypadDecryptUtils {

	private static final String ENC_TYPE_ASTX2 = "ASTX2";
	private static final String ENC_TYPE_OPEN_WEB_NFILTER = "openWebNFilter";
	private static final String ENC_TYPE_NFILTER = "nFilter";

//	@Value("${astx.config.path}")
//	private String astxConfigPath;

	/**
	 * 복호화 함수
	 * 
	 * @param serviceContext : 복호화에 필요한 업무단에서 넘겨준 serviceContext
	 * @return 평문 Map<Key : front input id, Value : 평문값>
	 */
	public static Map<String, String> decrypt(HttpServletRequest httpRequest) {
		// TODO: 오류 코드 및 오류 메세지 확인 필요[권태민]

		/**
		 * 1. SecureContextStore(ThreadLocal)에서 키패드 정보 추출 (encType : ASTX2, nFilter -
		 * 암호화 타입 / encValue : 암호화 값)
		 */
		Optional<SecureContext> secureContext = SecureContextStore.getContext();

		List<Object> errorList = new ArrayList<>();
		if (secureContext == null || secureContext.isEmpty() || secureContext.get().getKeypad() == null) {
			errorList.add("키패드 정보 없음");
			throw new PRCServiceException("PRCCMM0067", errorList);
		}

		String encryptType = secureContext.get().getKeypad().getEncType();
		String encryptValue = secureContext.get().getKeypad().getEncValue();
		Map<String, String> encryptMap = secureContext.get().getKeypad().getEncMap();
		Map<String, String> extE2EPlainMap = secureContext.get().getKeypad().getExtE2EPlainMap();

		if (StringUtils.isEmpty(encryptType)) {
			errorList.add("암호화 타입 없음");
			throw new PRCServiceException("PRCCMM0067", errorList);
		}

		if (StringUtils.isEmpty(encryptType)) {
			errorList.add("암호화 정보 없음");
			throw new PRCServiceException("PRCCMM0067", errorList);
		}

		log.debug("#### getEncType : [{}]", encryptType);
		log.debug("#### getEncValue : [{}]", encryptValue);
		log.debug("#### encryptMap : [{}]", encryptMap);
		log.debug("#### extE2EPlainMap : [{}]", extE2EPlainMap);

		/**
		 * session id ASTX2 복호화 시 필요한 uniqueId
		 */
		String uniqueId = httpRequest.getSession(false).getId();

		log.debug("#### uniqueId : [{}]", uniqueId);

		Map<String, String> decryptedMap = new HashMap<>();

		if (ENC_TYPE_ASTX2.equals(encryptType)) {
			// ASTX2 (E2E 복호화)
			decryptedMap = decryptASTX2(httpRequest, encryptValue, extE2EPlainMap, uniqueId);
		} else if (ENC_TYPE_OPEN_WEB_NFILTER.equals(encryptType)) {
			// openWeb nfilter 복호화
			decryptedMap = decryptOpenWebNfilter(httpRequest, encryptValue);
		} else if (ENC_TYPE_NFILTER.equals(encryptType)) {
			// nfilter 복호화
			decryptedMap = decryptNFilter(httpRequest, encryptMap);
		} else {
			throw new PRCServiceException("MA4CMM9999", "지원하지 않는 암호화 타입입니다.");
		}

		log.debug("#### decryptedMap : [{}]", decryptedMap);

		return decryptedMap;
	}

	/**
	 * @param httpRequest
	 * @param encryptValue : 암호화값
	 * @param uniqueId     : 복호화에 필요한 uniqueid : session id
	 * @return 평문 Map<Key : front input id, Value : 평문값>
	 * @throws PRCServiceException
	 */
	private static Map<String, String> decryptASTX2(HttpServletRequest httpRequest, String encryptValue,
			Map<String, String> extE2EPlainMap, String uniqueId) throws PRCServiceException {

		String astxConfig = RuntimeContext.getEnv("astx.config.path");

		log.debug("#### astxConfig : [{}]", astxConfig);

		log.debug("#### uniqueId : [{}]", uniqueId);

		Map<String, String> decryptedMap = new HashMap<>();

		ASTX2LibLocal astx2 = new ASTX2LibLocal(astxConfig);
		astx2.setUniqueId(uniqueId);

		List<Object> errorList = new ArrayList<>();

		/**
		 * 암호화값 set
		 */
		boolean success = astx2.setE2EDataValue2(encryptValue);
		if (!success) {
			int errorno = astx2.getLastError();

			errorList.add(errorno);
			// E2E필드 복호화 처리 중 오류 발생 errno={0}
			throw new PRCServiceException("PRCCMM0066", errorList);
		}

		/**
		 * 복호화에 필요한 Front input id
		 */
		String e2eNames = astx2.getE2ENames2();

		log.debug("#### e2eNames 2 : [{}]", e2eNames);

		if (StringUtils.isEmpty(e2eNames)) {
			errorList.add("input 정보 없음");
			throw new PRCServiceException("PRCCMM0067", errorList); // 복호화 실패({0})
		}

		/**
		 * input id 기준으로 평문 map에 저장
		 */
		for (String fieldName : e2eNames.split("\\,")) {
			String decryptValue = astx2.getE2EValue2(fieldName);
			
			// 금액 필드 암호화 값 , 제거
			String replaceValue = decryptValue.replaceAll(",", ""); 
			
			log.info("### 금액 입력 확인 : [{}]", replaceValue);

			// 확장 E2E 확인
			if (extE2EPlainMap != null && extE2EPlainMap.get(fieldName) != null
					&& !replaceValue.equalsIgnoreCase(extE2EPlainMap.get(fieldName))) {
				replaceValue = "";
			}

			decryptedMap.put(fieldName, replaceValue);
		}

		log.debug("#### decryptedMap : [{}]", decryptedMap);

		return decryptedMap;
	}

	/**
	 * @param httpRequest
	 * @param encryptValue 암호화값
	 * @return 평문 Map<Key : front input id, Value : 평문값>
	 * @throws PRCServiceException
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, String> decryptOpenWebNfilter(HttpServletRequest httpRequest, String encryptValue)
			throws PRCServiceException {
		OpenWebNFilterDecryptor process = new OpenWebNFilterDecryptor();

		Map<String, String> decryptedMap = new HashMap<>();
		HttpSession httpSession = httpRequest.getSession();
		try {
			// 세션 사용 여부 관련 로깅
			log.debug("#### OpenWebNFilterConfig.getUnusedSessionUse() : [{}]",
					OpenWebNFilterConfig.getUnusedSessionUse());

			IOpenWebNFilterSessionStorage nFilterSessionStorage = RuntimeContext
					.getBean(IOpenWebNFilterSessionStorage.class);

			HttpSession session = httpRequest.getSession();
			String key = "nfilter:prc:" + session.getId();

			Map<String, Object> sessionDataMap = nFilterSessionStorage.read(key);
			session.setAttribute(OpenWebNFilterConstants.SESSION_NAME_PRIVATE_KEY,
					sessionDataMap.get(OpenWebNFilterConstants.SESSION_NAME_PRIVATE_KEY));
			session.setAttribute(OpenWebNFilterConstants.SESSION_NAME_KEY_MODULE,
					sessionDataMap.get(OpenWebNFilterConstants.SESSION_NAME_KEY_MODULE));
			session.setAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_L,
					sessionDataMap.get(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_L));
			session.setAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_U,
					sessionDataMap.get(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_U));
			session.setAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_S,
					sessionDataMap.get(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_S));
			session.setAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_NUM,
					sessionDataMap.get(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_NUM));

			if (OpenWebNFilterConfig.getUnusedSessionUse().equals("true")) {
				decryptedMap = process.nFilterDecrypt(encryptValue);
			} else {
				decryptedMap = process.nFilterDecrypt(httpRequest, encryptValue, httpSession);
			}
		} catch (Exception e) {
			throw new PRCServiceException("PRCCMM0038", "암호화 세션이 만료되었습니다.", e);
		} finally {
			// 복호화 이후 http session 에 저장된 가상키패드 정보 삭제
			removeOpenwebNFilterHttpSessionAttr(httpSession);
		}

		return decryptedMap;
	}

	private static Map<String, String> decryptNFilter(HttpServletRequest httpRequest, Map<String, String> encryptMap)
			throws PRCServiceException {
		HttpSession session = httpRequest.getSession();

		Map<String, String> nKeyMap = E2EUtil.getNkey(session);

		String publicKey = (String) nKeyMap.get("nFilter_publicKey");
		String privateKey = (String) nKeyMap.get("nFilter_PrivateKey");

		log.debug("publicKey : [{}]", publicKey);
		log.debug("privateKey : [{}]", privateKey);

		Map<String, String> decryptedMap = new HashMap<>();

		try {
			NFilter nfilter = new NFilter(privateKey);

			for (Map.Entry<String, String> entry : encryptMap.entrySet()) {
				String key = entry.getKey();
				String encryptValue = entry.getValue();

				String[] encryptArr = encryptValue.split("\\|");

				if (encryptArr.length > 3) {

					String decValue = "";// 복호화 값
					String encType = encryptArr[0];// 암호화 type(문자, 숫자)
					String encData = encryptArr[1];// nfilter 암호화 원본값
//					String encData2 = encryptArr[2]; // encData2
//					String aesEnc = encryptArr[3];

					try {
						if (nfilter != null && "NUM".equals(encType) && encData != null) {
							decValue = nfilter.decNum(encData);
						} else if (nfilter != null && "STR".equals(encType) && encData != null) {
							decValue = StringUtils.nvl(nfilter.decStr(encData), "").toUpperCase(); // 복호화
						}

						log.debug("#### decValue : [{}]", decValue);
					} catch (NFilterException e) {
						throw new PRCServiceException("PRCCMM0038", "암호화 세션이 만료되었습니다.", e);
					} // 복호화

					if (!"".equalsIgnoreCase(decValue)) {
						decryptedMap.put(key, decValue);
					}

				}
			}

		} catch (NFilterException e) {
			throw new PRCServiceException("PRCCMM0038", "암호화 세션이 만료되었습니다.", e);
		}

		log.debug("#### decryptedMap : [{}]", decryptedMap);

		return decryptedMap;
	}

	/**
	 * 가상키패드 세션정보를 삭제한다.
	 * 
	 * @param session {@link HttpSession}
	 */
	private static void removeOpenwebNFilterHttpSessionAttr(HttpSession session) {
		session.removeAttribute(OpenWebNFilterConstants.SESSION_NAME_PRIVATE_KEY);
		session.removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEY_MODULE);
		session.removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_L);
		session.removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_U);
		session.removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_S);
		session.removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_NUM);
	}
}
