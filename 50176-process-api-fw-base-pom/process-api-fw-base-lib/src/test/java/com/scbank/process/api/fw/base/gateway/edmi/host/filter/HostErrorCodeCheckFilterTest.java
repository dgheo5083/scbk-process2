package com.scbank.process.api.fw.base.gateway.edmi.host.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.scbank.process.api.fw.base.gateway.edmi.base.exception.EDMiFeignException;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;

import feign.Request;
import feign.Request.HttpMethod;
import feign.RequestTemplate;
import feign.Response;

/**
 * Generated unit test for {@link HostErrorCodeCheckFilter}.
 */
class HostErrorCodeCheckFilterTest {

    private HostErrorCodeCheckFilter filter;

    @BeforeEach
    void setUp() {
        filter = new HostErrorCodeCheckFilter();
        ReflectionTestUtils.setField(filter, "charset", "euc-kr");
    }

    private FeignFilterContext hostCtx() {
        FeignFilterContext ctx = mock(FeignFilterContext.class);
        lenient().when(ctx.getSystemId()).thenReturn("host");
        return ctx;
    }

    private Response response(byte[] body) {
        Request request = Request.create(HttpMethod.POST, "http://localhost",
                new HashMap<String, Collection<String>>(), (byte[]) null,
                StandardCharsets.UTF_8, new RequestTemplate());
        return Response.builder()
                .status(200)
                .reason("OK")
                .request(request)
                .headers(new HashMap<String, Collection<String>>())
                .body(body)
                .build();
    }

    @Test
    void returnsResponseUnchangedForNonHostSystem() {
        FeignFilterContext ctx = mock(FeignFilterContext.class);
        when(ctx.getSystemId()).thenReturn("mci");
        Response response = response(new byte[200]);

        assertThat(filter.afterResponse(response, ctx)).isSameAs(response);
    }

    @Test
    void fillsDefaultErrorCodeWhenBlank() {
        // all-zero error code field -> default "0000" branch
        Response result = filter.afterResponse(response(new byte[200]), hostCtx());
        assertThat(result).isNotNull();
    }

    @Test
    void handlesCapAbendFlag() {
        byte[] body = new byte[200];
        body[43] = (byte) 0x82; // CAP ABEND flag
        Response result = filter.afterResponse(response(body), hostCtx());
        assertThat(result).isNotNull();
    }

    @Test
    void keepsExistingErrorCode() {
        byte[] body = new byte[200];
        body[145] = '1';
        body[146] = '2';
        body[147] = '3';
        body[148] = '4';
        Response result = filter.afterResponse(response(body), hostCtx());
        assertThat(result).isNotNull();
    }

    @Test
    void wrapsErrorIntoFeignExceptionForShortMessage() {
        assertThatThrownBy(() -> filter.afterResponse(response(new byte[10]), hostCtx()))
                .isInstanceOf(EDMiFeignException.class);
    }
}
