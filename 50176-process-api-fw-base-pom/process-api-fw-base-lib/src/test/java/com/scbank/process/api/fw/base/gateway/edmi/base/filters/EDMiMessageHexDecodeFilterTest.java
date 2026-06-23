package com.scbank.process.api.fw.base.gateway.edmi.base.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.base.exception.EDMiFeignException;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;

import feign.Request;
import feign.Request.HttpMethod;
import feign.RequestTemplate;
import feign.Response;

/**
 * Generated unit test for {@link EDMiMessageHexDecodeFilter}.
 */
class EDMiMessageHexDecodeFilterTest {

    private final EDMiMessageHexDecodeFilter filter = new EDMiMessageHexDecodeFilter(new ObjectMapper());

    private Response response(String body) {
        Request request = Request.create(HttpMethod.POST, "http://localhost",
                new HashMap<String, Collection<String>>(), (byte[]) null,
                StandardCharsets.UTF_8, new RequestTemplate());
        return Response.builder()
                .status(200)
                .reason("OK")
                .request(request)
                .headers(new HashMap<String, Collection<String>>())
                .body(body, StandardCharsets.UTF_8)
                .build();
    }

    @Test
    void returnsResponseUnchangedForNonHostSystem() {
        FeignFilterContext ctx = mock(FeignFilterContext.class);
        when(ctx.getSystemId()).thenReturn("mci");
        Response response = response("{}");

        assertThat(filter.afterResponse(response, ctx)).isSameAs(response);
    }

    @Test
    void wrapsDecodingErrorIntoFeignExceptionForHostSystem() {
        FeignFilterContext ctx = mock(FeignFilterContext.class);
        lenient().when(ctx.getSystemId()).thenReturn("host");
        Response response = response("{}");

        assertThatThrownBy(() -> filter.afterResponse(response, ctx))
                .isInstanceOf(EDMiFeignException.class);
    }
}
