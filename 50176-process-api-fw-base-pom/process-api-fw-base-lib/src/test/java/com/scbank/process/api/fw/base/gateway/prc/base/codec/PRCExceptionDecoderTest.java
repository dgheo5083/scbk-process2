package com.scbank.process.api.fw.base.gateway.prc.base.codec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.gateway.prc.base.exception.PRCFeignException;

import feign.Request;
import feign.Request.HttpMethod;
import feign.RequestTemplate;
import feign.Response;

/**
 * Generated unit test for {@link PRCExceptionDecoder}.
 */
class PRCExceptionDecoderTest {

    private final PRCExceptionDecoder decoder = new PRCExceptionDecoder();

    private Response response(int status, String body) {
        Request request = Request.create(HttpMethod.POST, "http://localhost",
                new HashMap<String, Collection<String>>(), (byte[]) null,
                StandardCharsets.UTF_8, new RequestTemplate());
        return Response.builder()
                .status(status)
                .reason("REASON")
                .request(request)
                .headers(new HashMap<String, Collection<String>>())
                .body(body, StandardCharsets.UTF_8)
                .build();
    }

    @Test
    void delegatesToDefaultDecoderForOkStatus() {
        Exception result = decoder.decode("Service#m()", response(200, "{}"));
        assertThat(result).isNotNull();
    }

    @Test
    void returnsDefaultExceptionWhenHeaderMissing() {
        Exception result = decoder.decode("Service#m()", response(500, "{}"));
        assertThat(result).isInstanceOf(PRCServiceException.class);
    }

    @Test
    void returnsDefaultExceptionWhenResCodeIsSuccess() {
        Exception result = decoder.decode("Service#m()", response(500, "{\"header\":{\"resCode\":\"00\"}}"));
        assertThat(result).isInstanceOf(PRCServiceException.class);
    }

    @Test
    void returnsFeignExceptionWhenErrorResponse() {
        String body = "{\"header\":{\"resCode\":\"99\",\"errorCode\":\"E1\","
                + "\"errorLocation\":\"L\",\"errorModule\":\"M\",\"errorMessage\":\"msg\"}}";
        Exception result = decoder.decode("Service#m()", response(500, body));
        assertThat(result).isInstanceOf(PRCFeignException.class);
    }

    @Test
    void wrapsParsingErrorIntoFeignException() {
        assertThatThrownBy(() -> decoder.decode("Service#m()", response(500, "not-json")))
                .isInstanceOf(PRCFeignException.class);
    }
}
