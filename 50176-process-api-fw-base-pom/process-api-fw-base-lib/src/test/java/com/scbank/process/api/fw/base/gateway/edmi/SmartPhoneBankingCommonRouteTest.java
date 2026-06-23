package com.scbank.process.api.fw.base.gateway.edmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiSCBMLErrorResponseFilter;

class SmartPhoneBankingCommonRouteTest {
	
	private static void setField(Object target, String fieldName, Object value) {
		Class<?> clazz = target.getClass();
		while(clazz != null) {
			try {
				Field f = clazz.getDeclaredField(fieldName);
				f.setAccessible(true);
				f.set(target, value);
				return;
			}catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}catch (Exception e) {
				throw new RuntimeException("필드 주입 실패 : "+ fieldName, e);
			}
		}
	}
	
	static class TestableHeaderFilter extends SmartPhoneBankingCommonRoute.SmartPhoneBankingCommonRouteRequestHeaderFilter {
		public String getUserName() {
			return super.resolveUserName();
		}
		
		public String getUserPassword() {
			return super.resolveUserPassword();
		}
	}

	@Test
	@DisplayName("RequestHeader filter")
	void requestHeaderFilter_resolveUseNameAndPassword() {
		TestableHeaderFilter filter = new TestableHeaderFilter();
		
		setField(filter, "userName", "testUser");
		setField(filter, "userPassword", "testPw");
		
		assertEquals("testUser", filter.getUserName());
		assertEquals("testPw", filter.getUserPassword());
		
	}
	
	@Test
	@DisplayName("config 생성 확인")
	void config_requestTemplateFilterBean() {
		SmartPhoneBankingCommonRoute.SmartPhoneBankingCommonRouteConfig config = new SmartPhoneBankingCommonRoute.SmartPhoneBankingCommonRouteConfig();
		
		Object bean = config.requestTemplateFilter();
		
		assertNotNull(bean);
		assertTrue(bean instanceof SmartPhoneBankingCommonRoute.SmartPhoneBankingCommonRouteRequestHeaderFilter);
	}
	
	@Test
	@DisplayName("edmErrorResponseFilter bean 생성 확인")
	void config_edmErrorResponseFilterBean() {
		SmartPhoneBankingCommonRoute.SmartPhoneBankingCommonRouteConfig config = new SmartPhoneBankingCommonRoute.SmartPhoneBankingCommonRouteConfig();
		
		ObjectMapper om = new ObjectMapper();
		Object bean = config.edMiErrorResponseFilter(om);
		
		assertNotNull(bean);
		assertTrue(bean instanceof EDMiSCBMLErrorResponseFilter);
//		assertEquals("EDMiSCMBLErrorResponseFilter", bean.getClass().getSimpleName());
	}

}

