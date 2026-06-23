package com.scbank.process.api.fw.base.channel.context;

import org.springframework.web.servlet.LocaleResolver;

import com.scbank.process.api.fw.base.store.ThreadLocalStore;
import com.scbank.process.api.fw.base.store.ThreadLocalStoreDelegator;
import com.scbank.process.api.fw.channel.context.handler.DefaultServiceContextHandler;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.channel.service.ServiceIdResolver;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.core.uuid.IIdentifyGenerator;
import com.scbank.process.api.fw.session.ISessionContextResolver;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 프로세스 API 커스텀용 서비스 컨텍스트 핸들러 구현 클래스
 */
public class PRCServiceContextHandler extends DefaultServiceContextHandler {

	/**
	 * 
	 * @param identifyGenerator
	 * @param deviceResolver
	 * @param serviceRegistrar
	 * @param localeResolver
	 * @param serviceIdResolver
	 * @param sessionResolver
	 */
	public PRCServiceContextHandler(
			IIdentifyGenerator identifyGenerator, 
			IDeviceResolver deviceResolver,
			IServiceRegistrar serviceRegistrar, 
			LocaleResolver localeResolver, 
			ServiceIdResolver serviceIdResolver,
			ISessionContextResolver sessionResolver) {
		super(identifyGenerator, deviceResolver, serviceRegistrar, localeResolver, serviceIdResolver, sessionResolver);
	}

	@Override
	protected String getRequestUUID() {
		String trackingId = ThreadLocalStoreDelegator.getTrackingId();
		if (StringUtils.hasLength(trackingId)) {
			return trackingId;
		}
		
		String requestUUID = super.getRequestUUID();
		
		ThreadLocalStore threadLocalStore = ThreadLocalStore.getInstance();
		threadLocalStore.setTrackingId(requestUUID);
		return requestUUID;
	}

	@Override
	protected String getRequestUUID(HttpServletRequest request) {
		return this.getRequestUUID();
	}
}
