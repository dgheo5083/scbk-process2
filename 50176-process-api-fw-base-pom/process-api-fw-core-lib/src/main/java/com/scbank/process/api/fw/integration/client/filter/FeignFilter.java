package com.scbank.process.api.fw.integration.client.filter;

import java.io.IOException;

import feign.RequestTemplate;
import feign.Response;

/**
 * 옌계시스템 통신처리 FeignClient 필터 인터페이스
 * 전문 전송 전/후 처리, 오류 처리 수행
 * 
 * @author sungdon.choi
 */
public interface FeignFilter {

    /**
     * 
     * @param requestTemplate
     * @param ctx
     */
    default void onTemplate(RequestTemplate requestTemplate, FeignFilterContext ctx) {

    }

    /**
     * 
     * @param request
     * @param ctx
     * @return
     */
    default RequestTemplate beforeExecute(RequestTemplate requestTemplate, FeignFilterContext ctx) {
        return requestTemplate;
    }

    /**
     * 
     * @param response
     * @param ctx
     * @return
     */
    default Response afterResponse(Response response, FeignFilterContext ctx) {
        return response;
    }

    /**
     * 
     * @param ex
     * @param ctx
     * @return
     */
    default IOException onError(IOException ex, FeignFilterContext ctx) {
        return ex;
    }

}
