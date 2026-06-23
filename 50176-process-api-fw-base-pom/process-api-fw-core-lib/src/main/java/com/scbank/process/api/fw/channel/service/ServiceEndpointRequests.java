package com.scbank.process.api.fw.channel.service;

import java.util.List;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.RequiredArgsConstructor;

/**
 * 서비스 엔드포인트 요청 경로 유틸리티 클래스
 *
 * <p>
 * application.yml에 정의된 service 설정 중 URL 매핑 정보를 조합하여,
 * 실제 서비스별 endpoint URL 목록을 반환합니다.
 * basePath + allowedUrlPatterns 조합으로 구성됩니다.
 *
 * <p>
 * 예: basePath: "/api/common", allowedUrlPatterns: ["/join", "/info"]
 * → "/api/common/join", "/api/common/info"
 *
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public class ServiceEndpointRequests {

    /**
     * 채널 서비스 설정 정보
     */
    private final ChannelProperties properties;

    /**
     * 활성화된 서비스에 대한 endpoint URL 목록을 반환합니다.
     *
     * <p>
     * ServiceProperties의 enabled 값이 true인 경우만 포함됩니다.
     *
     * @return 활성화된 서비스들의 전체 엔드포인트 URL 리스트
     */
    public List<String> enabledServiceEndpointUrls() {
        if (properties == null || properties.getService() == null) {
            return List.of();
        }

        return properties.getService().stream()
                .filter(ChannelProperties.ServiceProperties::enabled)
                .flatMap(s -> {
                    String basePath = StringUtils.defaultIfEmpty(s.basePath(), "");
                    return s.serviceMapping().allowedUrlPatterns()
                            .stream()
                            .map(url -> basePath + url);
                })
                .toList();
    }

    /**
     * 설정된 모든 서비스에 대한 endpoint URL 목록을 반환합니다.
     *
     * <p>
     * enabled 여부와 상관없이 모든 서비스 매핑 정보를 포함합니다.
     *
     * @return 전체 서비스 엔드포인트 URL 리스트
     */
    public List<String> serviceEndpointUrls() {
        if (properties == null || properties.getService() == null) {
            return List.of();
        }

        return properties.getService().stream()
                .flatMap(s -> {
                    String basePath = StringUtils.defaultIfBlank(s.basePath(), "");
                    return s.serviceMapping().allowedUrlPatterns()
                            .stream()
                            .map(url -> basePath + url);
                })
                .toList();
    }
}
