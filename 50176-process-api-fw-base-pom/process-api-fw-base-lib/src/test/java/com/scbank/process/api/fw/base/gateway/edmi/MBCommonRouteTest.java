package com.scbank.process.api.fw.base.gateway.edmi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiRequestHeaderFilter;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiSCBMLErrorResponseFilter;

class MBCommonRouteTest {

	@Test
	@DisplayName("MBCommonRouteRequestHeaderFilter username/password")
	void requestHeaderFilter() {

		MBCommonRoute.MBCommonRouteRequestHeaderFilter filter = new MBCommonRoute.MBCommonRouteRequestHeaderFilter();

		ReflectionTestUtils.setField(filter, "userName", "testUser");

		ReflectionTestUtils.setField(filter, "userPassword", "testPassword");

		String userName = (String) ReflectionTestUtils.invokeMethod(filter, "resolveUserName");

		String password = (String) ReflectionTestUtils.invokeMethod(filter, "resolveUserPassword");

		assertEquals("testUser", userName);
		assertEquals("testPassword", password);
	}

	@Test
	@DisplayName("requestTemplateFilter Bean 생성")
	void requestTemplateFilterBean() {

		MBCommonRoute.MBCommonRouteConfig config = new MBCommonRoute.MBCommonRouteConfig();

		EDMiRequestHeaderFilter filter = config.requestTemplateFilter();

		assertNotNull(filter);

		assertInstanceOf(MBCommonRoute.MBCommonRouteRequestHeaderFilter.class, filter);
	}

	@Test
	@DisplayName("edMiErrorResponseFilter Bean 생성")
	void edMiErrorResponseFilterBean() {

		MBCommonRoute.MBCommonRouteConfig config = new MBCommonRoute.MBCommonRouteConfig();

		ObjectMapper objectMapper = new ObjectMapper();

		EDMiSCBMLErrorResponseFilter filter = config.edMiErrorResponseFilter(objectMapper);

		assertNotNull(filter);
	}

}
