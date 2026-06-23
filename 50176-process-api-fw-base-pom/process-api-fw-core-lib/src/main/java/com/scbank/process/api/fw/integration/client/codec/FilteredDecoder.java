package com.scbank.process.api.fw.integration.client.codec;

import java.io.IOException;
import java.lang.reflect.Type;

import com.scbank.process.api.fw.integration.client.filter.FeignFilterChain;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext.FeignFilterContextHolder;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public class FilteredDecoder implements Decoder {

    private final Decoder decoder;

    private final FeignFilterChain filterChain;

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {

        FeignFilterContext ctx = FeignFilterContextHolder.get();

        Response filteredResponse = filterChain.applyAfterResponse(response, ctx);

        return decoder.decode(filteredResponse, type);
    }
}
