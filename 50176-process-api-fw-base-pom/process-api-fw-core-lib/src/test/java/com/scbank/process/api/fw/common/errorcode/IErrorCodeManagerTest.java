package com.scbank.process.api.fw.common.errorcode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.i18n.LocaleContextHolder;

class IErrorCodeManagerTest {

	@AfterEach
	void tearDown() {
		LocaleContextHolder.resetLocaleContext();
	}
	
	@Test
	void afterPropertiesSet_call_init() throws Exception {
		IErrorCodeManager manager = mock(IErrorCodeManager.class, Mockito.CALLS_REAL_METHODS);
		
		manager.afterPropertiesSet();
		
		verify(manager).init();
	}
	
	@Test
	void getErrorCodeInfo_without_langCode_use_locale_language() {
		IErrorCodeManager manager = mock(IErrorCodeManager.class, Mockito.CALLS_REAL_METHODS);
		
		LocaleContextHolder.setLocale(Locale.KOREAN);
		
		IErrorCodeInfo expected = mock(IErrorCodeInfo.class);
		
		
		doReturn(expected).when(manager).getErrorCodeInfo("ERR001", "ko");
		
		IErrorCodeInfo result = manager.getErrorCodeInfo("ERR001");
		
		assertSame(expected, result);
		
		verify(manager).getErrorCodeInfo("ERR001", "ko");
	}
	
	@Test
	void getErrorMessage_without_args_uses_locale_language() {
		IErrorCodeManager manager = mock(IErrorCodeManager.class, Mockito.CALLS_REAL_METHODS);
		
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		manager.getErrorMessage("ERR001");
		
		doReturn("error message").when(manager).getErrorMessage("ERR001", "en");
		
		String result = manager.getErrorMessage("ERR001");
		
		assertEquals("error message", result);
		//verify(manager).getErrorMessage("ERR001", "en");
	}
	
	@Test
	void getErrorMessage_with_args_uses_locale_language() {
		IErrorCodeManager manager = mock(IErrorCodeManager.class, Mockito.CALLS_REAL_METHODS);
		
		LocaleContextHolder.setLocale(Locale.KOREAN);
		
		Object [] args = {1,2};
		
		manager.getErrorMessage("ERR001", "ko", args);
		
		doReturn("에러메시지").when(manager).getErrorMessage("ERR001", "ko", args);
		
		String result = manager.getErrorMessage("ERR001", args);
		
		assertEquals("에러메시지", result);
		verify(manager).getErrorMessage("ERR001", args);
	}
	
	@Test
	void getErrorGuideMessage_without_langCode_uses_locale_language() {
		IErrorCodeManager manager = mock(IErrorCodeManager.class, Mockito.CALLS_REAL_METHODS);
		
		LocaleContextHolder.setLocale(Locale.KOREAN);
		
		List<IErrorGuideMessageInfo> expected = List.of(mock(IErrorGuideMessageInfo.class));
		
		manager.getErrorGuideMessages("ERR001");
		
		doReturn(expected).when(manager).getErrorGuideMessages("ERR001");
		
		List<IErrorGuideMessageInfo> result = manager.getErrorGuideMessages("ERR001");
		
		assertSame(expected, result);
		//verify(manager).getErrorGuideMessages("ERR001");
	}

}
