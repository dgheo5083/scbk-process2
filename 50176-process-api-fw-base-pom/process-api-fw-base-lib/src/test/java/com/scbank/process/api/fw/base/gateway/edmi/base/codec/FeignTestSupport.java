package com.scbank.process.api.fw.base.gateway.edmi.base.codec;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import feign.Request;
import feign.Request.HttpMethod;
import feign.RequestTemplate;
import feign.Response;

/**
 * Small helper for building real {@link feign.Response} instances in tests.
 *
 * <p>{@code feign.Response} and {@code feign.Request} are final and therefore
 * cannot be mocked, so concrete instances must be assembled.</p>
 */
final class FeignTestSupport {

    private FeignTestSupport() {
    }

    static Request request() {
        return Request.create(HttpMethod.POST, "http://localhost",
                new HashMap<String, Collection<String>>(), (byte[]) null,
                StandardCharsets.UTF_8, new RequestTemplate());
    }

    static Response response(byte[] body) {
        Map<String, Collection<String>> headers = new HashMap<>();
        Response.Builder builder = Response.builder()
                .status(200)
                .reason("OK")
                .request(request())
                .headers(headers);
        if (body != null) {
            builder.body(body);
        }
        return builder.build();
    }

    static Response responseWithoutBody() {
        return Response.builder()
                .status(200)
                .reason("OK")
                .request(request())
                .headers(new HashMap<>())
                .build();
    }
}
