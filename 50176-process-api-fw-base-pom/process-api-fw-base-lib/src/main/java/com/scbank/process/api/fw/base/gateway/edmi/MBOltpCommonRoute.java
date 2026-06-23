package com.scbank.process.api.fw.base.gateway.edmi;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.MBOltpCommonRoute.MBOLTPCommonRouteConfig;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.decoder.EDMiFixedLengthMessageDecoder;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.decoder.EDMiMappingMessageDecoder;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.decoder.EDMiMessageDecoderComposite;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder.EDMiFixedLengthMessageEncoder;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder.EDMiMappingMessageEncoder;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder.EDMiMessageEncoderCompoiste;
import com.scbank.process.api.fw.base.gateway.edmi.base.config.EDMiFeignClientConfig;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiRequestMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiResponseMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiMessageHexDecodeFilter;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiRequestHeaderFilter;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiSCBMLErrorResponseFilter;
import com.scbank.process.api.fw.base.gateway.edmi.host.filter.HostErrorCodeCheckFilter;
import com.scbank.process.api.fw.integration.client.codec.FilteredDecoder;
import com.scbank.process.api.fw.integration.client.codec.FilteredEncoder;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterChain;

import feign.codec.Decoder;
import feign.codec.Encoder;

/**
 * 연계시스템 전문 송/수신 Host Gateway
 */
@FeignClient(name = "mbOLTPCommonRoute", url = "${edmi.gateway.mbOLTPCommonRoute.baseUrl:${edmi.gateway.default.baseUrl}}", configuration = {
		MBOLTPCommonRouteConfig.class })
public interface MBOltpCommonRoute {

	/**
	 * EDMi 전문 송/수신
	 * 
	 * @param request EDMiRequest
	 * @return EDMiResponse
	 */
	@PostMapping(path = { "${edmi.gateway.mbOLTPCommonRoute.serviceUrl:${edmi.gateway.default.serviceUrl}}" }, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	EDMiResponseMessage send(EDMiRequestMessage request);

	/**
	 * 
	 */
	public class MBOLTPCommonRouteConfig extends EDMiFeignClientConfig {

		// ---------------------------------------
		// Feign Client Filter Bean 정의
		// Order 어노테이션을 이용하여 필터 실행 순서를 정한다.
		// ---------------------------------------
		
		@Bean
		@Override
		protected Encoder edmiEncoder(ObjectMapper objectMapper, FeignFilterChain filterChain) {
			EDMiMessageEncoderCompoiste encoder = new EDMiMessageEncoderCompoiste(
				List.of(
						new EDMiFixedLengthMessageEncoder(objectMapper),
						new EDMiMappingMessageEncoder(objectMapper)
				)
			);
	        return new FilteredEncoder(encoder, filterChain);
	    }
		
		@Bean
		@Override
		protected Decoder edmiDecoder(ObjectMapper objectMapper, FeignFilterChain filterChain) {
			EDMiMessageDecoderComposite decoder = new EDMiMessageDecoderComposite(
				List.of(
					new EDMiFixedLengthMessageDecoder(),
					new EDMiMappingMessageDecoder(objectMapper)
				)
			);
	        return new FilteredDecoder(decoder, filterChain);
	    }

		/**
		 * EDMi 요청 헤더 설정 필터
		 * 
		 * @return
		 */
		@Bean
		@Order(1)
		EDMiRequestHeaderFilter requestTemplateFilter() {
			return new EDMiRequestHeaderFilter();
		}

		@Bean
		@Order(2)
		HostErrorCodeCheckFilter hostErrorCodeFilter() {
			return new HostErrorCodeCheckFilter();
		}

		@Bean
		@Order(3)
		EDMiMessageHexDecodeFilter edmiMessageHexDecodeFilter(ObjectMapper objectMapper) {
			return new EDMiMessageHexDecodeFilter(objectMapper);
		}

		@Bean
		@Order(4)
		EDMiSCBMLErrorResponseFilter edMiErrorResponseFilter(ObjectMapper objectMapper) {
			return new EDMiSCBMLErrorResponseFilter(objectMapper);
		}
	}
}
