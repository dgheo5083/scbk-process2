package com.scbank.process.api.fw.base.gateway.prc.base.interceptors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.session.ISessionContext;

import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Generated unit test for {@link PRCRequestInterceptor}.
 */
class PRCRequestInterceptorTest {

    private final PRCRequestInterceptor interceptor = new PRCRequestInterceptor();

    @Test
    void appliesHeadersFromPopulatedContext() {
        IServiceContext serviceContext = mock(IServiceContext.class);
        ISessionContext sessionContext = mock(ISessionContext.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(serviceContext.request()).thenReturn(request);
        when(serviceContext.session()).thenReturn(sessionContext);
        when(sessionContext.getSessionId()).thenReturn("SID");
        when(serviceContext.channelId()).thenReturn("MB");
        when(serviceContext.locale()).thenReturn(Locale.KOREAN);

        RequestTemplate template = new RequestTemplate();

        try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class)) {
            holder.when(ServiceContextHolder::getContext).thenReturn(serviceContext);
            interceptor.apply(template);
        }

        assertThat(template.headers()).containsKey("channel");
        assertThat(template.headers().get("channel")).contains("MB");
        assertThat(template.headers().get("Accept-Language")).contains("ko");
    }

    @Test
    void resolvesChannelFromRequestHeaderWhenChannelIdEmpty() {
        IServiceContext serviceContext = mock(IServiceContext.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(serviceContext.request()).thenReturn(request);
        when(serviceContext.session()).thenReturn(null);
        when(serviceContext.channelId()).thenReturn("");
        when(serviceContext.locale()).thenReturn(Locale.ENGLISH);
        lenient().when(request.getHeader("channel")).thenReturn("WEB");

        RequestTemplate template = new RequestTemplate();

        try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class)) {
            holder.when(ServiceContextHolder::getContext).thenReturn(serviceContext);
            interceptor.apply(template);
        }

        assertThat(template.headers().get("channel")).contains("WEB");
    }
}
