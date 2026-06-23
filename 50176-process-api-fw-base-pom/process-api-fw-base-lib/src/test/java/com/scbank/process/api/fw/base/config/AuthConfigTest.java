package com.scbank.process.api.fw.base.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import com.scbank.process.api.fw.base.channel.filters.AuthFilter;
import com.scbank.process.api.fw.base.channel.filters.ContextFilter;
import com.scbank.process.api.fw.base.properties.AuthFilterProperty;

/**
 * Generated unit test for {@link AuthConfig}.
 */
class AuthConfigTest {

    private final AuthConfig config = new AuthConfig();

    @Test
    void authFilterIsRegistered() {
        FilterRegistrationBean<AuthFilter> bean = config.authFilter(new AuthFilterProperty());

        assertThat(bean).isNotNull();
        assertThat(bean.getFilter()).isInstanceOf(AuthFilter.class);
        assertThat(bean.getUrlPatterns()).contains("/*");
    }

    @Test
    void contextFilterIsRegistered() {
        FilterRegistrationBean<ContextFilter> bean = config.contextFilter();

        assertThat(bean).isNotNull();
        assertThat(bean.getFilter()).isInstanceOf(ContextFilter.class);
        assertThat(bean.getUrlPatterns()).contains("/*");
    }
}
