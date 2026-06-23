package com.scbank.process.api.fw.base.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Generated unit test for {@link HttpSessionConfig}.
 */
class HttpSessionConfigTest {

    private final HttpSessionConfig config = new HttpSessionConfig();

    @Test
    void sessionRepositoryBeanIsCreated() {
        MapSessionRepository repository = config.sessionRepository();
        assertThat(repository).isNotNull();
    }

    @Test
    void cookieSerializerBeanIsCreated() {
        CookieSerializer serializer = config.cookieSerializer();
        assertThat(serializer).isInstanceOf(DefaultCookieSerializer.class);
    }
}
