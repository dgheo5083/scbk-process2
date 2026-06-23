package com.scbank.process.api.fw.base.gateway.edmi.base.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;
import com.scbank.process.api.fw.integration.exception.IntegrationException;

import feign.Request;
import feign.Request.HttpMethod;
import feign.RequestTemplate;
import feign.Response;

/**
 * Generated unit test for {@link EDMiSCBMLErrorResponseFilter}.
 */
class EDMiSCBMLErrorResponseFilterTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EDMiSCBMLErrorResponseFilter filter = new EDMiSCBMLErrorResponseFilter(objectMapper);

    private FeignFilterContext ctx() {
        FeignFilterContext ctx = mock(FeignFilterContext.class);
        lenient().when(ctx.getSystemId()).thenReturn("host");
        return ctx;
    }

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

    private String json(Map<String, Object> map) throws Exception {
        return objectMapper.writeValueAsString(map);
    }

    private Map<String, Object> scbml(Map<String, Object> header) {
        Map<String, Object> root = new HashMap<>();
        root.put("ns:header", header);
        Map<String, Object> scbml = new HashMap<>();
        scbml.put("SCBML", root);
        return scbml;
    }

    @Test
    void returnsResponseWhenNoExceptions() throws Exception {
        Map<String, Object> header = new HashMap<>();
        header.put("ns:captureSystem", "OLTP");

        Response result = filter.afterResponse(response(json(scbml(header))), ctx());

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(200);
    }

    @Test
    void returnsResponseWhenExceptionListEmpty() throws Exception {
        Map<String, Object> exceptions = new HashMap<>();
        exceptions.put("ns:exception", List.of());
        Map<String, Object> header = new HashMap<>();
        header.put("ns:exceptions", exceptions);

        Response result = filter.afterResponse(response(json(scbml(header))), ctx());

        assertThat(result).isNotNull();
    }

    @Test
    void throwsIntegrationExceptionForImsException() throws Exception {
        Response response = response(json(scbml(headerWithException("ERR01", "prefix IMSException: boom\nrest"))));

        assertThatThrownBy(() -> filter.afterResponse(response, ctx()))
                .isInstanceOf(IntegrationException.class);
    }

    @Test
    void throwsIntegrationExceptionForGenericError() throws Exception {
        Response response = response(json(scbml(headerWithException("ERR99", "generic failure\nsecond line"))));

        assertThatThrownBy(() -> filter.afterResponse(response, ctx()))
                .isInstanceOf(IntegrationException.class);
    }

    @Test
    void wrapsUnexpectedErrorIntoFrameworkRuntimeException() {
        assertThatThrownBy(() -> filter.afterResponse(response("{}"), ctx()))
                .isInstanceOf(FrameworkRuntimeException.class);
    }

    private Map<String, Object> headerWithException(String code, String description) {
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("*body", code);
        Map<String, Object> exceptionEntry = new HashMap<>();
        exceptionEntry.put("ns:code", codeMap);
        exceptionEntry.put("ns:description", description);
        Map<String, Object> exceptions = new HashMap<>();
        exceptions.put("ns:exception", List.of(exceptionEntry));
        Map<String, Object> header = new HashMap<>();
        header.put("ns:captureSystem", "OLTP");
        header.put("ns:exceptions", exceptions);
        return header;
    }
}
