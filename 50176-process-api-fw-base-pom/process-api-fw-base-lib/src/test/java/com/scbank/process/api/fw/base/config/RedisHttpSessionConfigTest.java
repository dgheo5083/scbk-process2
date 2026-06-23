package com.scbank.process.api.fw.base.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.session.adapter.ISessionAdapterProvider;

import io.lettuce.core.resource.ClientResources;

/**
 * Generated unit test for {@link RedisHttpSessionConfig}.
 */
class RedisHttpSessionConfigTest {

    private final RedisHttpSessionConfig config = new RedisHttpSessionConfig();

    @Test
    void clientResourcesBeanIsCreated() {
        ClientResources resources = config.clientResources();
        assertThat(resources).isNotNull();
        resources.shutdown();
    }

    @Test
    void lettuceCustomizerBeanIsCreated() {
        assertThat(config.lettuceClientConfigurationBuilderCustomizer()).isNotNull();
    }

    @Test
    void cookieSerializerUsesDefaultsWhenCookieIsEmpty() {
        CookieSerializer serializer = config.cookieSerializer(new ServerProperties());
        assertThat(serializer).isNotNull();
    }

    @Test
    void cookieSerializerUsesConfiguredCookieValues() {
        ServerProperties properties = new ServerProperties();
        var cookie = properties.getServlet().getSession().getCookie();
        cookie.setName("MYSESSION");
        cookie.setDomain("example.com");
        cookie.setPath("/app");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(Duration.ofSeconds(120));
        cookie.setSameSite(SameSite.STRICT);

        CookieSerializer serializer = config.cookieSerializer(properties);
        assertThat(serializer).isNotNull();
    }

    @Test
    void sessionRepositoryCustomizerSkipsNullValues() {
        SessionProperties sessionProperties = mock(SessionProperties.class);
        RedisSessionProperties redisSessionProperties = mock(RedisSessionProperties.class);
        RedisIndexedSessionRepository repository = mock(RedisIndexedSessionRepository.class);

        SessionRepositoryCustomizer<RedisIndexedSessionRepository> customizer =
                config.sessionRepositoryCustomizer(sessionProperties, redisSessionProperties);

        assertThatCode(() -> customizer.customize(repository)).doesNotThrowAnyException();
    }

    @Test
    void sessionRepositoryCustomizerAppliesTimeout() {
        SessionProperties sessionProperties = mock(SessionProperties.class);
        RedisSessionProperties redisSessionProperties = mock(RedisSessionProperties.class);
        RedisIndexedSessionRepository repository = mock(RedisIndexedSessionRepository.class);
        when(sessionProperties.getTimeout()).thenReturn(Duration.ofMinutes(30));

        SessionRepositoryCustomizer<RedisIndexedSessionRepository> customizer =
                config.sessionRepositoryCustomizer(sessionProperties, redisSessionProperties);

        assertThatCode(() -> customizer.customize(repository)).doesNotThrowAnyException();
    }

    @Test
    void redisObjectMapperBeanIsCreated() {
        ObjectMapper objectMapper = config.redisObjectMapper();
        assertThat(objectMapper).isNotNull();
    }

    @Test
    void springSessionDefaultRedisSerializerBeanIsCreated() {
        RedisSerializer<Object> serializer = config.springSessionDefaultRedisSerializer(
                mock(ISessionAdapterProvider.class), config.redisObjectMapper());
        assertThat(serializer).isNotNull();
    }

    @Test
    void redisTemplateBeanIsCreated() {
        RedisTemplate<String, Object> template = config.redisTemplate(
                mock(RedisConnectionFactory.class), config.redisObjectMapper());
        assertThat(template).isNotNull();
    }
}
