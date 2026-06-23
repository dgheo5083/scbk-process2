package com.scbank.process.api.fw.integration.client.codec;

import java.lang.reflect.Type;

import com.scbank.process.api.fw.integration.client.filter.FeignFilterChain;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext.FeignFilterContextHolder;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import lombok.RequiredArgsConstructor;

/**
 * 필터 기반 메시지 인코더 구현 클래스
 * 
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public class FilteredEncoder implements Encoder {

    /**
     * 메시지 인코더
     */
    private final Encoder encoder;

    /**
     * 필터 체인
     */
    private final FeignFilterChain filterChain;

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {

        encoder.encode(object, bodyType, template);

        filterChain.applyBeforeRequest(template, FeignFilterContextHolder.get());
    }
}
