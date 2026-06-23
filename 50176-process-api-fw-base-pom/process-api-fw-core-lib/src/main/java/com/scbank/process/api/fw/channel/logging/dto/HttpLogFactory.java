package com.scbank.process.api.fw.channel.logging.dto;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import com.scbank.process.api.fw.channel.context.IServiceContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * HTTP 요청/응답 로그 생성 팩토리 클래스
 */
public class HttpLogFactory {

    /**
     * HTTP 요청 로그 객체 생성
     * 
     * @param ctx  {@link IServiceContext}
     * @param body 응답 body 데이터
     * @return
     */
    public HttpRequestLog buildRequestLog(IServiceContext ctx, Object body) {
        HttpServletRequest request = ctx.request();
        HttpRequestLog log = new HttpRequestLog();
        log.setTrackingId(ctx.requestUId());
        log.setUri(request.getRequestURI());
        log.setMethod(request.getMethod());
        log.setHeaders(this.extractRequestHeader(request));
        log.setBody(body);
        return log;
    }

    /**
     * HTTP 응답 로그 객체 생성
     * 
     * @param ctx  {@link IServiceContext}
     * @param body 응답 body 데이터
     * @return
     */
    public HttpResponseLog buildResponseLog(IServiceContext ctx, Object body) {
        HttpServletRequest request = ctx.request();
        HttpServletResponse response = ctx.response();

        HttpResponseLog log = new HttpResponseLog();
        log.setTrackingId(ctx.requestUId());
        log.setUri(request.getRequestURI());
        log.setMethod(request.getMethod());
        log.setStatus(response.getStatus());
        log.setHeaders(this.extractResponseHeader(response));
        log.setBody(body);

        return log;
    }

    /**
     * 요청헤더를 추출하여 Map으로 변환
     * 
     * @param request {@link HttpServletRequest}
     * @return 요청헤더정보를 담고 있는 Map 객체
     */
    private Map<String, String> extractRequestHeader(HttpServletRequest request) {
        Map<String, String> headers = new LinkedHashMap<>();

        Enumeration<String> heaerNames = request.getHeaderNames();
        while (heaerNames.hasMoreElements()) {
            String headerName = heaerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    /**
     * 응답헤더를 추출하여 Map으로 변환
     * 
     * @param response {@link HttpServletResponse}
     * @return 응답헤더정보를 담고 있는 Map 객체
     */
    private Map<String, String> extractResponseHeader(HttpServletResponse response) {
        Map<String, String> headers = new LinkedHashMap<>();

        response.getHeaderNames().stream().forEach(k -> {
            headers.put(k, response.getHeader(k));
        });
        return headers;
    }
}
