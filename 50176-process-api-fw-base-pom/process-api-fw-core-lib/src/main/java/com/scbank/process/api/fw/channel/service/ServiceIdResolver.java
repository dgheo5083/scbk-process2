package com.scbank.process.api.fw.channel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties;
import com.scbank.process.api.fw.core.utils.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <pre>
 * HttpServletRequest로부터 서비스 ID를 매핑해주는 전용 Resolver 클래스입니다.
 *
 * 채널 설정에 정의된 서비스 목록(ChannelProperties.ServiceProperties)을 기반으로,
 * 요청 URI가 어느 서비스에 속하는지 판별하여 {@link ServiceId}를 반환합니다.
 * </pre>
 *
 * <p>
 * 기본적으로 basePath + allowedUrlPatterns 경로 기준으로 URI 매칭을 수행합니다.
 * </p>
 *
 * 예:
 * - basePath: {@code /api/common}
 * - allowedUrlPatterns: {@code [/login/**, /auth/**]}
 * - 요청 URI: {@code /api/common/login/userLogin}
 * → 매핑 성공 → ServiceId(serviceId="COMMON", uri="/api/common/login/userLogin")
 *
 * @author gasigol
 * @version 1.0
 * @since 2025.04.16
 */
public class ServiceIdResolver {

    /** 전체 채널 서비스 설정 객체 */
    private final ChannelProperties channelProperties;

    /** URI 패턴 매칭을 위한 유틸 (Spring Ant path) */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * context path
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 생성자
     *
     * @param properties 프레임워크 채널 설정 (yml 기반)
     */
    public ServiceIdResolver(ChannelProperties properties) {
        this.channelProperties = properties;
    }

    /**
     * HttpServletRequest로부터 서비스 ID를 매핑합니다.
     *
     * @param request 현재 요청 객체
     * @return 매핑된 서비스 ID 정보
     * @throws IllegalStateException 매핑 실패 시 (예: 허용된 URI 패턴 없음)
     */
    public ServiceId resolve(HttpServletRequest request) {
        final String resolveUri = this.resolveUri(request.getRequestURI());
        return channelProperties.getService().stream()
                .filter(ServiceProperties::enabled) // 활성화된 서비스만 대상으로
                .filter(s -> s.serviceMapping() != null)
                .filter(service -> {
                    String basePath = StringUtils.defaultIfBlank(service.basePath(), ""); // 예: /api/common
                    var patterns = service.serviceMapping().allowedUrlPatterns(); // 예: [/login/**, /logout/**]
                    return patterns != null && patterns.stream()
                            .map(p -> basePath + p)
                            .anyMatch(p -> antPathMatcher.match(p, resolveUri)); // URI 매칭 시 true
                })
                .findFirst()
                .map(service -> new ServiceId(service.serviceId(), resolveUri))
                .orElseThrow(() -> new IllegalStateException("서비스 ID를 URI로부터 매핑할 수 없습니다: " + resolveUri));
    }

    private String resolveUri(String requestURI) {
        String resolvedUri = StringUtils.defaultIfEmpty(requestURI, "");
        if (StringUtils.hasLength(contextPath)) {
            resolvedUri = StringUtils.replaceOnce(resolvedUri, contextPath, "");
        }
        return resolvedUri;
    }
}
