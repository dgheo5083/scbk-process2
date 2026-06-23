package com.scbank.process.api.fw.base.gateway.edmi.base.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.scbank.process.api.fw.integration.client.options.FeignRequestOptionsContext;

import feign.Client;
import feign.Request;
import feign.Request.HttpMethod;
import feign.Request.Options;
import feign.RequestTemplate;
import feign.Response;

/**
 * Generated unit test for {@link EDMIFeignClient}.
 */
class EDMIFeignClientTest {

    private final Client delegate = mock(Client.class);
    private final EDMIFeignClient client = new EDMIFeignClient(delegate);

    private Request request() {
        return Request.create(HttpMethod.POST, "http://localhost",
                new HashMap<String, Collection<String>>(), (byte[]) null,
                StandardCharsets.UTF_8, new RequestTemplate());
    }

    private Response response(Request request) {
        return Response.builder()
                .status(200)
                .reason("OK")
                .request(request)
                .headers(new HashMap<String, Collection<String>>())
                .build();
    }

    @Test
    void executeUsesOriginalOptionsWhenNoOverride() throws Exception {
        Request request = request();
        Options options = new Options(1000, TimeUnit.MILLISECONDS, 2000, TimeUnit.MILLISECONDS, true);
        Response expected = response(request);
        when(delegate.execute(eq(request), org.mockito.ArgumentMatchers.any(Options.class))).thenReturn(expected);

        Response actual = client.execute(request, options);

        assertThat(actual).isSameAs(expected);
    }
    
    @Test
    void merge_should_use_overridden_timeout() throws Exception {
    	
    	Request request = mock(Request.class);
    	
    	Request.Options original = new Request.Options(
    			1000, TimeUnit.MILLISECONDS,
    			2000, TimeUnit.MILLISECONDS, true);
    	
    	Request.Options override = new Request.Options(
    			3000, TimeUnit.MILLISECONDS,
				4000, TimeUnit.MILLISECONDS, true);
    	
    	FeignRequestOptionsContext.set(override);
    	
    	Response response = mock(Response.class);
    	
    	when(delegate.execute(any(), any())).thenReturn(response);
    	
    	client.execute(request, original);
    	
    	ArgumentCaptor<Request.Options> captor = ArgumentCaptor.forClass(Request.Options.class);
    	
    	verify(delegate).execute(eq(request), captor.capture());
    	
    	Request.Options actual = captor.getValue();
    	
    	assertEquals(3000, actual.connectTimeoutMillis());
    	assertEquals(4000, actual.readTimeoutMillis());
    }
}
