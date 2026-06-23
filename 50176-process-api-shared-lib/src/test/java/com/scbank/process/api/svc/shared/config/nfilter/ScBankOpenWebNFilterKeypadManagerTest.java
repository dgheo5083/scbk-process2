package com.scbank.process.api.svc.shared.config.nfilter;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.scbank.process.api.svc.shared.config.nfilter.storage.IOpenWebNFilterSessionStorage;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.nshc.nfilter.openweb.OpenWebNFilterConstants;
import net.nshc.nfilter.openweb.service.OpenWebNFilterLicenseManager;

@ExtendWith(MockitoExtension.class)
class ScBankOpenWebNFilterKeypadManagerTest {

	@Mock
	private IOpenWebNFilterSessionStorage storage;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private HttpSession session;

	private ScBankOpenWebNFilterKeypadManager manager;
	
	@Mock
	private OpenWebNFilterLicenseManager licenseManager;
	
	@Mock
	private ServletConfig servletConfig;
	
	@Mock
	private ServletContext servletContext;
	

	@BeforeEach
	void setUp() throws Exception {
		manager = new ScBankOpenWebNFilterKeypadManager(storage);
		manager.init(servletConfig);
	}

//	@Test
//	void doProcess_호출시_sessionData저장한다() throws Exception {
//		
//		Enumeration<String> params = Collections.enumeration(List.of("nfilter_is_init", "screen_reader", "clipub_for_uusession"));
// 		try (MockedStatic<OpenWebNFilterLicenseManager> mocked = Mockito.mockStatic(OpenWebNFilterLicenseManager.class);
// 				MockedStatic<OpenWebNFilterConfig> configMocked = Mockito.mockStatic(OpenWebNFilterConfig.class)) {
//			
//			mocked.when(OpenWebNFilterLicenseManager::getInstance).thenReturn(licenseManager);
//			doNothing().when(licenseManager).check();
//			
//			configMocked.when(OpenWebNFilterConfig::isConfigInit).thenReturn(true);
//			configMocked.when(OpenWebNFilterConfig::getUnusedSessionUse).thenReturn("false");
//			configMocked.when(OpenWebNFilterConfig::getAlgName).thenReturn("NONE");
//			configMocked.when(OpenWebNFilterConfig::getSecurityProvider).thenReturn("NONE");
//			configMocked.when(OpenWebNFilterConfig::getNscryptoUse).thenReturn("false");
//			
//			when(request.getSession()).thenReturn(session);
//			when(request.getParameterNames()).thenReturn(params);
//			
//			when(request.getParameter("nfilter_is_init")).thenReturn("false");
//			when(request.getParameter("screen_reader")).thenReturn("false");
//			when(request.getParameter("clipub_for_uusession")).thenReturn("false");
//			
//			when(session.getAttribute(OpenWebNFilterConstants.SESSION_NAME_PRIVATE_KEY)).thenReturn("privateKey");
//			when(session.getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEY_MODULE)).thenReturn("keyModule");
//			when(session.getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_L)).thenReturn("charL");
//			when(session.getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_U)).thenReturn("charU");
//			when(session.getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_S)).thenReturn("charS");
//			when(session.getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_NUM)).thenReturn("num");
//			
//			when(request.getParameter("nfilterType")).thenReturn("wiz");
//			
//			try {
//				manager.doProcess(request, response, false);
//			} catch (Exception e) {
//				
//			}
//			
//			//verify(request).getSession();
//			
//			verify(session).getAttribute(OpenWebNFilterConstants.SESSION_NAME_PRIVATE_KEY);
//			verify(session).getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEY_MODULE);
//			verify(session).getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_L);
//			verify(session).getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_U);
//			verify(session).getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_S);
//			verify(session).getAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_NUM);
//			
//			verify(storage).save(eq("wiz"), anyMap());
//		}
//	}

	@Test
	void saveSessionData_wizType() {
		// given
		when(request.getSession()).thenReturn(session);
		when(session.getId()).thenReturn("SESSION123");
		when(request.getParameter("nfilterType")).thenReturn("wiz");

		// when
		manager.saveSessionData(request);
		
		verify(storage).save(eq("nfilter:wiz:SESSION123"), anyMap());
		verifyRemoveSessionAttr();
	}

	@Test
	void saveSessionData_defaultType() {
		// given
		when(request.getSession()).thenReturn(session);
		when(session.getId()).thenReturn("SESSION123");
		when(request.getParameter("nfilterType")).thenReturn("other");

		// when
		manager.saveSessionData(request);
		// then
		verify(storage).save(eq("nfilter:prc:SESSION123"), anyMap());
		verifyRemoveSessionAttr();
	}
	
	@Test
	void saveSessionData_nullType() {
		// given
		when(request.getSession()).thenReturn(session);
		when(session.getId()).thenReturn("SESSION123");
		when(request.getParameter("nfilterType")).thenReturn(null);

		// when
		manager.saveSessionData(request);
		// then
		verify(storage).save(eq("nfilter:prc:SESSION123"), anyMap());
		verifyRemoveSessionAttr();
	}
	
	@Test
	void saveSessionData_storageException_removeSessionAttrAnyway() {
		// given
		when(request.getSession()).thenReturn(session);
		when(session.getId()).thenReturn("SESSION123");
		when(request.getParameter("nfilterType")).thenReturn("wiz");
		
		doThrow(new RuntimeException("redis error")).when(storage).save(anyString(), anyMap());
		
		manager.saveSessionData(request);
		
		verifyRemoveSessionAttr();
	}

	@Test
	void saveSessionData_storageException_removeSessionAttributesAnyway() {

		// given
		when(request.getSession()).thenReturn(session);
		when(session.getId()).thenReturn("SESSION123");

		doThrow(new RuntimeException("redis error")).when(storage).save(anyString(), anyMap());

		// when
		ReflectionTestUtils.invokeMethod(manager, "saveSessionData", request);
	}
	
	private void verifyRemoveSessionAttr() {
		// then
		verify(session).removeAttribute(OpenWebNFilterConstants.SESSION_NAME_PRIVATE_KEY);
		verify(session).removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEY_MODULE);
		verify(session).removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_L);
		verify(session).removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_U);
		verify(session).removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_CHAR_S);
		verify(session).removeAttribute(OpenWebNFilterConstants.SESSION_NAME_KEYMAP_NUM);
	}
}
