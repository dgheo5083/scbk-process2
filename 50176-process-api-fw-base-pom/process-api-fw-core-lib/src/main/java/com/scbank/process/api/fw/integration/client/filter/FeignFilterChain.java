package com.scbank.process.api.fw.integration.client.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

import feign.FeignException.FeignClientException;
import feign.RequestTemplate;
import feign.Response;
import feign.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FeignFilterChain {

    private final List<FeignFilter> filters;

    public void applyOnTemplate(feign.RequestTemplate template, FeignFilterContext ctx) {
        for (FeignFilter f : filters) {
            f.onTemplate(template, ctx);
        }
    }

    public RequestTemplate applyBeforeRequest(feign.RequestTemplate template, FeignFilterContext ctx) {
        RequestTemplate cur = template;
        for (FeignFilter f : filters) {
            cur = f.beforeExecute(cur, ctx);
        }
        return cur;
    }

    public Response applyAfterResponse(Response response, FeignFilterContext ctx) {
        Response cur = response;

        try {
            if (cur.body() != null && ctx.getResponseBytese() == null) {
                ctx.setResponseBytese(Util.toByteArray(cur.body().asInputStream()));
                ctx.setResponseContentType(contentTypeOf(cur));

                cur = rebody(cur, ctx.getResponseBytese(), ctx.getResponseContentType());
            }

            for (int i = filters.size() - 1; i >= 0; i--) {
                FeignFilter f = filters.get(i);
                cur = f.afterResponse(cur, ctx);

                if (cur.body() != null) {
                    byte[] latestBytes = Util.toByteArray(cur.body().asInputStream());
                    cur = rebody(cur, latestBytes, contentTypeOf(cur));
                }
            }
        } catch (FeignClientException e) {
        	throw e;
        } catch (FrameworkRuntimeException e) {
        	throw e;
        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR, e);
        }
        return cur;
    }

    public IOException applyOnError(IOException ex, FeignFilterContext ctx) {
        IOException cur = ex;
        for (FeignFilter f : filters) {
            f.onError(ex, ctx);
        }
        return cur;
    }

    private String contentTypeOf(Response response) {
        Collection<String> v = response.headers().get("Content-Type");
        return (v != null && !v.isEmpty()) ? v.iterator().next() : null;
    }

    public static Response rebody(Response response, byte[] body, String contentType) {
        Map<String, Collection<String>> headers = new LinkedHashMap<>(response.headers());
        headers.put("Content-Length", List.of(String.valueOf(body.length)));

        if (contentType != null) {
            headers.put("Content-Type", List.of(contentType));
        }

        return Response.builder()
                .status(response.status())
                .reason(response.reason())
                .request(response.request())
                .headers(headers)
                .body(body)
                .build();
    }
}
