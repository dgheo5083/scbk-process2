package com.scbank.process.api.fw.base.gateway.edmi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.SmartPhoneBankingCommonRoute.SmartPhoneBankingCommonRouteConfig;
import com.scbank.process.api.fw.base.gateway.edmi.base.config.EDMiFeignClientConfig;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiRequestMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiResponseMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiSCBMLErrorResponseFilter;
import com.scbank.process.api.fw.base.gateway.edmi.base.filters.EDMiRequestHeaderFilter;

/**
 * EDMi SmartPhoneBankingCommonRoute Gateway
 */
@FeignClient(name = "smartphonebankingcommonRoute", url = "${edmi.gateway.smartphonebankingcommonRoute.baseUrl:${edmi.gateway.default.baseUrl}}", configuration = {
		SmartPhoneBankingCommonRouteConfig.class })
public interface SmartPhoneBankingCommonRoute {

	/**
	 * EDMi 전문 송/수신
	 * 
	 * @param request EDMiRequest
	 * @return EDMiResponse
	 */
	@PostMapping(path = {
			"${edmi.gateway.smartphonebankingcommonRoute.serviceUrl:${edmi.gateway.default.serviceUrl}}" }, consumes = {
					MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	EDMiResponseMessage send(EDMiRequestMessage request);

	/**
	 * 
	 */
	public class SmartPhoneBankingCommonRouteRequestHeaderFilter extends EDMiRequestHeaderFilter {

		@Value("${edmi.gateway.smartphonebankingcommonRoute.userName}")
	    private String userName;

	    @Value("${edmi.gateway.smartphonebankingcommonRoute.credential}")
	    private String userPassword;

		@Override
		protected String resolveUserName() {
			return this.userName;
		}

		@Override
		protected String resolveUserPassword() {
			return this.userPassword;
		}
	}

	public class SmartPhoneBankingCommonRouteConfig extends EDMiFeignClientConfig {

		// ---------------------------------------
		// Feign Client Filter Bean 정의
		// Order 어노테이션을 이용하여 필터 실행 순서를 정한다.
		// ---------------------------------------

		/**
		 * EDMi 요청 헤더 설정 필터
		 * 
		 * @return
		 */
		@Bean
		@Order(1)
		EDMiRequestHeaderFilter requestTemplateFilter() {
			return new SmartPhoneBankingCommonRouteRequestHeaderFilter();
		}

		@Bean
		@Order(2)
		EDMiSCBMLErrorResponseFilter edMiErrorResponseFilter(ObjectMapper objectMapper) {
			return new EDMiSCBMLErrorResponseFilter(objectMapper);
		}
	}
}
