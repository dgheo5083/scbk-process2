package com.scbank.process.api.fw.base.gateway.edmi;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.MBOltpCommonRoute.MBOLTPCommonRouteConfig;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiMessageHexDecodeFilter;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiRequestHeaderFilter;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiSCBMLErrorResponseFilter;
import com.scbank.process.api.fw.base.gateway.edmi.host.filter.HostErrorCodeCheckFilter;
import com.scbank.process.api.fw.integration.client.codec.FilteredDecoder;
import com.scbank.process.api.fw.integration.client.codec.FilteredEncoder;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterChain;

import feign.codec.Decoder;
import feign.codec.Encoder;

class MBOltpCommonRouteTest {

	private final MBOLTPCommonRouteConfig config = new MBOLTPCommonRouteConfig();

	@Test
	@DisplayName("edmiEncoder Bean 생성")
	void edmiEncoder() {

		ObjectMapper objectMapper = new ObjectMapper();

		FeignFilterChain filterChain = mock(FeignFilterChain.class);

		Encoder encoder = config.edmiEncoder(objectMapper, filterChain);

		assertNotNull(encoder);
		assertInstanceOf(FilteredEncoder.class, encoder);
	}

	@Test
	@DisplayName("edmiDecoder Bean 생성")
	void edmiDecoder() {

		ObjectMapper objectMapper = new ObjectMapper();

		FeignFilterChain filterChain = mock(FeignFilterChain.class);

		Decoder decoder = config.edmiDecoder(objectMapper, filterChain);

		assertNotNull(decoder);
		assertInstanceOf(FilteredDecoder.class, decoder);
	}

	@Test
	@DisplayName("requestTemplateFilter Bean 생성")
	void requestTemplateFilter() {

		EDMiRequestHeaderFilter filter = config.requestTemplateFilter();

		assertNotNull(filter);
		assertInstanceOf(EDMiRequestHeaderFilter.class, filter);
	}

	@Test
	@DisplayName("hostErrorCodeFilter Bean 생성")
	void hostErrorCodeFilter() {

		HostErrorCodeCheckFilter filter = config.hostErrorCodeFilter();

		assertNotNull(filter);
	}

	@Test
	@DisplayName("edmiMessageHexDecodeFilter Bean 생성")
	void edmiMessageHexDecodeFilter() {

		ObjectMapper objectMapper = new ObjectMapper();

		EDMiMessageHexDecodeFilter filter = config.edmiMessageHexDecodeFilter(objectMapper);

		assertNotNull(filter);
	}

	@Test
	@DisplayName("edMiErrorResponseFilter Bean 생성")
	void edMiErrorResponseFilter() {

		ObjectMapper objectMapper = new ObjectMapper();

		EDMiSCBMLErrorResponseFilter filter = config.edMiErrorResponseFilter(objectMapper);

		assertNotNull(filter);
	}

}
