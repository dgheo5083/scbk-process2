package com.scbank.process.api.svc.shared.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.svc.shared.config.HttpClientConfig.HttpClientFactory;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * HTTP API 요청/응답 처리 HTTP 클라이언트 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BaseHttpClient {

    private final HttpClientFactory httpClientFactory;

    /**
     * 대상시스템 url 문자열에 http/https 여부 확인하여 전용
     * 
     * @param url 대상시스템 url 문자열
     * @return
     */
    protected CloseableHttpClient getHttpClient(String url) {
        return this.httpClientFactory.createHttpClient(url);
    }

    /**
     * HTTP 요청/응답 처리
     * 
     * @param request HttpRequest
     * @return HttpResponse
     */
    public HttpResponse execute(ClassicHttpRequest request) {
        return this.execute(request, (status, body) -> {
            return HttpResponse.builder()
                    .code(status)
                    .body(body)
                    .build();
        });
    }

    /**
     * HTTP 요청/응답 처리
     * 
     * @param request HttpRequest
     * @return HttpResponse
     */
    public <T> T execute(ClassicHttpRequest request, HttpResponseMapper<T> responseMapper) {
    	
    	//로그 출력
    	log.debug("# execute scheme={} path={}, uri={}", request.getScheme(), request.getPath(), request.getRequestUri());
    	
        CloseableHttpClient httpClient = this.getHttpClient(request.getScheme());
        try {
            HttpClientResponseHandler<T> responseHandler = (response) -> {
                int status = response.getCode();
                String body = response.getEntity() == null ? null
                        : EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                return responseMapper.map(status, body);
            };
            return httpClient.execute(request, responseHandler);
        } catch (IOException e) {
            throw new PRCServiceException(FrameworkErrorCode.INTERNAL_ERROR.getCode(), e.getMessage(), e);
        }
    }

    @Data
    @Builder
    public static class HttpResponse {
        private int code;
        private String body;

        public boolean isOK() {
            return code == HttpStatus.SC_OK;
        }
    }

    @FunctionalInterface
    public static interface HttpResponseMapper<T> {
        T map(int status, String body);
    }
}
