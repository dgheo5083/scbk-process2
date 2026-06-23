package com.scbank.process.api.fw.channel.context.handler;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.LocaleResolver;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.channel.service.ServiceIdResolver;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ParameterInfo;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.core.uuid.IIdentifyGenerator;
import com.scbank.process.api.fw.session.ISessionContext;
import com.scbank.process.api.fw.session.ISessionContextResolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 서비스 요청 컨텍스트 생성 추상 클래스
 *
 * <p>
 * 공통적으로 사용되는 요청 처리 요소들(ID 생성, 디바이스 확인, 서비스 매핑, 세션, 로케일 등)을 기반으로
 * {@link IServiceContext}를 구성하기 위한 유틸리티 메서드를 제공합니다.
 * 실제 컨텍스트 생성은 {@link DefaultServiceContextHandler}에서 구현됩니다.
 *
 * <p>
 * Spring MVC의 HTTP 요청 기반으로 동작합니다.
 *
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractServiceContextHandler implements IServiceContextHandler {

    /** 요청 UUID 생성기 */
    private final IIdentifyGenerator identifyGenerator;

    /** 디바이스 판별기 (User-Agent, Header 등으로 추론) */
    private final IDeviceResolver deviceResolver;

    /** 서비스 매핑 정의 등록자 */
    private final IServiceRegistrar serviceRegistrar;

    /** 로케일 해석기 */
    private final LocaleResolver localeResolver;

    /** 요청으로부터 서비스 ID를 추출하는 Resolver */
    private final ServiceIdResolver serviceIdResolver;

    /** 세션 컨텍스트 해석기 */
    private final ISessionContextResolver sessionResolver;

    /**
     * 요청 채널ID를 가져온다.
     * 
     * @param request {@link HttpServletRequest}
     * @return 요청 채널ID
     */
    protected String getChannelId(HttpServletRequest request) {
        String channelId = StringUtils.defaultString(request.getHeader("channel")).trim();
        return channelId;
    }

    /**
     * 요청 UUID를 생성합니다.
     * <p>
     * 요청의 트래킹이나 로그 추적 등에 사용됩니다.
     *
     * @return 생성된 UUID 문자열
     */
    protected String getRequestUUID() {
        return this.identifyGenerator.generateId();
    }

    protected String getRequestUUID(HttpServletRequest request) {
        String requestId = request.getHeader("x-tracking-id");
        if (StringUtils.isBlank(requestId)) {
            log.debug("RequestId not found in header, generating new UUID");
            requestId = this.getRequestUUID();
        }
        return requestId;
    }

    /**
     * 요청으로부터 디바이스 정보를 추출합니다.
     * <p>
     * 주로 헤더나 User-Agent 기반으로 분석됩니다.
     *
     * @param request HTTP 요청
     * @return 디바이스 식별 객체
     */
    protected IDevice getDevice(HttpServletRequest request) {
        return this.deviceResolver.resolve(request);
    }

    /**
     * 요청에 따른 로케일 정보를 해석합니다.
     *
     * @param request HTTP 요청
     * @return 로케일
     */
    protected Locale getLocale(HttpServletRequest request) {
        return this.localeResolver.resolveLocale(request);
    }

    /**
     * 요청으로부터 서비스 ID를 추출하고, 해당 서비스의 메타데이터를 조회합니다.
     *
     * @param request HTTP 요청
     * @return 서비스 정의 메타데이터
     */
    protected ServiceDefinitionMetadata getServiceDefinition(HttpServletRequest request) {
        return this.serviceRegistrar.getServiceDefinition(serviceIdResolver.resolve(request));
    }

    /**
     * 서비스에 정의된 파라미터 목록을 Map 형태로 변환하여 획득
     * 
     * @param serviceDefinition 서비스 정의 메타데이터
     * @return 파라미터 목록을 Map 으로 변환환 결과
     */
    protected Map<String, String> getServiceParameterMap(ServiceDefinitionMetadata serviceDefinition) {
        if (serviceDefinition == null) {
            return Map.of();
        }
        List<ParameterInfo> serviceParameters = serviceDefinition.getParameters();
        if (CollectionUtils.isEmpty(serviceParameters)) {
            return Map.of();
        }

        // 서비스 파라미터 Map 변환
        Map<String, String> serviceParameter = serviceParameters.stream()
                .collect(Collectors.toMap(
                        p -> p.parameterName(),
                        p -> p.parameterValue()));
        return serviceParameter;
    }

    /**
     * 요청에 해당하는 사용자 세션 컨텍스트를 반환합니다.
     *
     * @param request HTTP 요청
     * @return 세션 컨텍스트
     */
    protected ISessionContext getSessionContext(HttpServletRequest request) {
        return this.sessionResolver.resolve(request);
    }
}
