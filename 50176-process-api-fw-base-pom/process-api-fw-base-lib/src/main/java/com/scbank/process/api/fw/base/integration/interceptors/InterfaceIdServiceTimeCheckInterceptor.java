package com.scbank.process.api.fw.base.integration.interceptors;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.common.servicetime.IServiceTimeInfo;
import com.scbank.process.api.fw.common.servicetime.IServiceTimeManager;
import com.scbank.process.api.fw.common.servicetime.ServiceTimeGroup;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 전문 인터페이스ID 서비스 영업시간 체크 인터셉터
 */
@Deprecated
@Slf4j
@Component("interfaceIdServiceTimeCheckInterceptor")
@RequiredArgsConstructor
public class InterfaceIdServiceTimeCheckInterceptor implements IntegrationInterceptor {

	/**
	 * 서비스 영업시간 관리 매니저
	 */
	private final IServiceTimeManager serviceTimeManager;
	
	@Override
	@ComponentOperation(name = "전문 인터페이스 ID 서비스 영업시간 체크 전처리")
	public void before(IntegrationContext context, Object request) {

		String interfaceId = context.getInterfaceId();

		IServiceTimeInfo serviceTime = this.serviceTimeManager.getServiceTime(ServiceTimeGroup.MESSAGE, interfaceId);
		if (serviceTime == null || !"Y".equals(serviceTime.getChkYn())) {
			if (log.isDebugEnabled()) {
				log.debug("# 전문 인터페이스 서비스영업시간 체크하지 않음, 인터페이스ID: {}", interfaceId);
			}
			return;
		}

		// TOOD 현재 시간과 서비스이용시간 비교하여 예외 처리 수행
		String startTime = serviceTime.getStartTime();
		String endTime = serviceTime.getEndTime();

		if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
			if (log.isDebugEnabled()) {
				log.debug("# 전문 인터페이스 서비스이용시간 설정오류(영업시간 값이 비어있음): 인터페이스ID: {}, 영업시간: {}~{}", interfaceId, startTime,
						endTime);
			}
			return;
		}
	}
}
