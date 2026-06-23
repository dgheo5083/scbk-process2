package com.scbank.process.api.svc.shared.integration.interceptor;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.store.ThreadLocalStoreDelegator;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptor;
import com.scbank.process.api.svc.shared.components.accesscontrol.ServiceTimeCheckComponent;
import com.scbank.process.api.svc.shared.components.accesscontrol.dto.ServiceTimeCheckRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 전문 거래코드 별 서비스 이용시간 체크 인터셉터 컴포넌트
 */
@Slf4j
@Component("integrationServiceTimeCheckInterceptor")
@RequiredArgsConstructor
public class IntegrationServiceTimeCheckInterceptor implements IntegrationInterceptor {

	/**
	 * 서비스 이용시간 체크 컴포넌트
	 */
	private final ServiceTimeCheckComponent serviceTimeCheckComponent;
	
	@Override
	public void before(IntegrationContext context, Object request) {
		
		String tranCd = StringUtils.defaultIfEmpty(context.getAttribute("tranCd"), "");
		if (!StringUtils.hasText(tranCd)) {
			return;
		}
		
		String forceCheckCode = ThreadLocalStoreDelegator.getForceCheckCode();
		
		//***********************************
		//전문 거래코드 별 서비스 이용시간 체크
		//***********************************
		serviceTimeCheckComponent.checkServiceTime(ServiceTimeCheckRequest.builder()
				.type("message")
				.code(tranCd)
				.forceCheckCode(forceCheckCode)
				.build());
	}
}
