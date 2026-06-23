package com.scbank.process.api.fw.base.gateway.edmi.base.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;

import feign.RequestTemplate;

/**
 * Generated unit test for {@link EDMiRequestHeaderFilter}.
 */
class EDMiRequestHeaderFilterTest {

    @Test
    void onTemplateSetsHeadersAndAuthorization() {
        EDMiRequestHeaderFilter filter = new EDMiRequestHeaderFilter();
        filter.setUserName("user");
        filter.setUserPassword("pass");
        filter.setCharset("UTF-8");

        RequestTemplate template = new RequestTemplate();
        filter.onTemplate(template, mock(FeignFilterContext.class));

        String expectedAuth = "Basic " + Base64.getEncoder().encodeToString("user:pass".getBytes());

        assertThat(template.headers().get("Content-Type")).contains("application/json");
        assertThat(template.headers().get("Charset")).contains("UTF-8");
        assertThat(template.headers().get("Accept")).contains("application/json");
        assertThat(template.headers().get("Accept-Charset")).contains("UTF-8");
        assertThat(template.headers().get("Authorization")).contains(expectedAuth);
    }
}
