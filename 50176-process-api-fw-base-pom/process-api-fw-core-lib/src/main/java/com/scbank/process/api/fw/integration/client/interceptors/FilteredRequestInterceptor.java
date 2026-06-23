package com.scbank.process.api.fw.integration.client.interceptors;

import com.scbank.process.api.fw.integration.client.filter.FeignFilterChain;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext.FeignFilterContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

/**
 * Filter 기반 Feign RequestInterceptor
 * 
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public class FilteredRequestInterceptor implements RequestInterceptor {

    private final FeignFilterChain filterChain;

    @Override
    public void apply(RequestTemplate template) {

        FeignFilterContext ctx = FeignFilterContextHolder.get();

        filterChain.applyOnTemplate(template, ctx);
    }
}
