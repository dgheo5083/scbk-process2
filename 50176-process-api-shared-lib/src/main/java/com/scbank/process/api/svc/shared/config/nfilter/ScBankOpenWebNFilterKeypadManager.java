package com.scbank.process.api.svc.shared.config.nfilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.scbank.process.api.svc.shared.config.nfilter.storage.IOpenWebNFilterSessionStorage;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nshc.nfilter.openweb.OpenWebNFilterConstants;
import net.nshc.nfilter.openweb.service.OpenWebNFilterKeypadManager;

/**
 * 
 */
@Slf4j
@RequiredArgsConstructor
public class ScBankOpenWebNFilterKeypadManager extends OpenWebNFilterKeypadManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private final IOpenWebNFilterSessionStorage nFilterSessionStorage;
	

	
	@Override
	public String doProcess(HttpServletRequest request, HttpServletResponse response, boolean returnMode)
			throws ServletException, IOException {
		try {
			return super.doProcess(request, response, returnMode);
		} finally {
			this.saveSessionData(request);
		}
	}
	
	/**
	 * 세션에 저장된 데이터를 redis에 담는다.
	 * @param request
	 */
	public void saveSessionData(HttpServletRequest request) {
		HttpSession session = request.getSession();
		try {
			Map<String, Object> sessionDataMap = new HashMap<>();
			sessionDataMap.put(OpenWebNFilterConstants.SESSION_NAME_PRIVATE_KEY, session.getAttribute(OpenWebNFilterConstants.SESSION_NAME_PRIVATE_KEY));
			sessionDataMap.put(OpenWebNFilterConstants.SESSION_NAME_KEY_MODULE, session.getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEY_MODULE));
			sessionDataMap.put(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_L, session.getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_L));
			sessionDataMap.put(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_U, session.getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_U));
			sessionDataMap.put(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_S, session.getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_S));
			sessionDataMap.put(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_NUM, session.getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_NUM));
			
			String nfilterType = request.getParameter("nfilterType");
			
			log.debug("### nfilterType : "+ nfilterType);
			
			String storagePrefix = "nfilter:prc:";
			
			if(nfilterType != null && "wiz".equals(nfilterType)) {
				storagePrefix = "nfilter:wiz:";
			}	
			
			this.nFilterSessionStorage.save(storagePrefix + session.getId(), sessionDataMap);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			//스토리지에 담은 후 http session에 저장된 정보 삭제
			this.removeHttpSessionAttr(session);
		}
	}
	
	/**
	 * 가상키패드 세션정보를 삭제한다.
	 * @param session
	 */
	private void removeHttpSessionAttr(HttpSession session) {
		session.removeAttribute(OpenWebNFilterConstants.SESSION_NAME_PRIVATE_KEY);
		session.removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEY_MODULE);
		session.removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_L);
		session.removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_U);
		session.removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_S);
		session.removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_NUM);
	}
}
