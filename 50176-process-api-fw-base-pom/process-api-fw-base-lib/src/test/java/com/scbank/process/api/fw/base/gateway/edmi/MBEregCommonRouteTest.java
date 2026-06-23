package com.scbank.process.api.fw.base.gateway.edmi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiRequestHeaderFilter;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiSCBMLErrorResponseFilter;

class MBEregCommonRouteTest {

	@Test
	@DisplayName("MBEregCommonRouteRequestHeaderFilter username/password")
	void requestHeaderFilter() {

		MBEregCommonRoute.MBEregCommonRouteRequestHeaderFilter filter = new MBEregCommonRoute.MBEregCommonRouteRequestHeaderFilter();

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

		MBEregCommonRoute.MBEregCommonRouteConfig config = new MBEregCommonRoute.MBEregCommonRouteConfig();

		EDMiRequestHeaderFilter filter = config.requestTemplateFilter();

		assertNotNull(filter);

		assertInstanceOf(MBEregCommonRoute.MBEregCommonRouteRequestHeaderFilter.class, filter);
	}

	@Test
	@DisplayName("edMiErrorResponseFilter Bean 생성")
	void edMiErrorResponseFilterBean() {

		MBEregCommonRoute.MBEregCommonRouteConfig config = new MBEregCommonRoute.MBEregCommonRouteConfig();

		ObjectMapper objectMapper = new ObjectMapper();

		EDMiSCBMLErrorResponseFilter filter = config.edMiErrorResponseFilter(objectMapper);

		assertNotNull(filter);
	}

}
