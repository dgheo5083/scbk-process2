package com.scbank.process.api.fw.channel.context.handler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.web.servlet.LocaleResolver;

import com.scbank.process.api.fw.channel.context.DefaultServiceContext;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.channel.service.ServiceIdResolver;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.core.uuid.IIdentifyGenerator;
import com.scbank.process.api.fw.session.ISessionContext;
import com.scbank.process.api.fw.session.ISessionContextResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 기본 서비스 컨텍스트 핸들러 구현체
 *
 * <p>
 * HTTP 요청으로부터 서비스 실행에 필요한 {@link IServiceContext} 객체를 생성합니다.
 * 생성된 컨텍스트에는 요청 UUID, 사용자 디바이스, 로케일, 서비스 메타데이터, 세션 정보 등이 포함됩니다.
 *
 * <p>
 * Spring 기반 환경에서 서비스 호출 시 공통 컨텍스트 구성을 담당합니다.
 *
 * @see AbstractServiceContextHandler
 * @see DefaultServiceContext
 */
public class DefaultServiceContextHandler extends AbstractServiceContextHandler {

    /**
     * 생성자
     *
     * @param identifyGenerator 요청 고유 식별자 생성기
     * @param deviceResolver    디바이스 정보 해석기
     * @param localeResolver    요청 로케일 해석기
     * @param serviceIdResolver 서비스 ID 해석기
     * @param sessionResolver   세션 컨텍스트 해석기
     */
    public DefaultServiceContextHandler(
            IIdentifyGenerator identifyGenerator,
            IDeviceResolver deviceResolver,
            IServiceRegistrar serviceRegistrar,
            LocaleResolver localeResolver,
            ServiceIdResolver serviceIdResolver,
            ISessionContextResolver sessionResolver) {
        super(identifyGenerator, deviceResolver, serviceRegistrar, localeResolver, serviceIdResolver,
                sessionResolver);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * HTTP 요청에서 다음 정보를 추출하여 {@link DefaultServiceContext} 를 생성합니다:
     * <ul>
     * <li>요청 UUID</li>
     * <li>디바이스 정보</li>
     * <li>서비스 정의 메타데이터</li>
     * <li>로케일</li>
     * <li>세션 정보</li>
     * <li>확장 속성 (기본 빈 Map)</li>
     * </ul>
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @return {@link IServiceContext} 구현체
     */
    @Override
    public IServiceContext createServiceContext(HttpServletRequest request, HttpServletResponse response) {
        String channelId = this.getChannelId(request);
        String requestUUID = this.getRequestUUID(request);
        IDevice device = this.getDevice(request);
        ServiceDefinitionMetadata serviceDefinition = this.getServiceDefinition(request);
        Locale locale = this.getLocale(request);
        ISessionContext sessionContext = this.getSessionContext(request);

        // 서비스 파라미터
        Map<String, String> serviceParameter = this.getServiceParameterMap(serviceDefinition);

        return new DefaultServiceContext(
                channelId,
                requestUUID,
                request,
                response,
                device,
                locale,
                serviceDefinition,
                sessionContext,
                new HashMap<>(),
                serviceParameter);
    }
}
