package com.scbank.process.api.fw.base.gateway.edmi.base.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.base.ssl.SSLProperties;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterChain;

import feign.Client;
import feign.InvocationHandlerFactory;
import feign.Logger;

/**
 * Generated unit test for {@link EDMiFeignClientConfig}.
 *
 * <p>The class is abstract and extends an external base configuration, so a
 * Mockito partial mock ({@code CALLS_REAL_METHODS}) is used to exercise the
 * concrete {@code @Bean} factory methods without invoking the unknown super
 * constructor.</p>
 */
class EDMiFeignClientConfigTest {

    private EDMiFeignClientConfig config;

    @BeforeEach
    void setUp() {
        config = mock(EDMiFeignClientConfig.class, CALLS_REAL_METHODS);
    }

    @Test
    void feignLoggerLevelIsFull() {
        assertThat(config.feignLoggerLevel()).isEqualTo(Logger.Level.FULL);
    }

    @Test
    void encoderAndDecoderBeansAreCreated() {
        ObjectMapper objectMapper = new ObjectMapper();
        FeignFilterChain filterChain = mock(FeignFilterChain.class);

        assertThat(config.edmiEncoder(objectMapper, filterChain)).isNotNull();
        assertThat(config.edmiDecoder(objectMapper, filterChain)).isNotNull();
    }

    @Test
    void plainHttpClientsAreCreated() throws Exception {
        okhttp3.OkHttpClient okHttpClient = config.okHttpClient();
        assertThat(okHttpClient).isNotNull();

        Client client = config.delegateFeignClient(okHttpClient);
        assertThat(client).isNotNull();

        Client sslDelegate = config.delegateFeignClientWithSsl(okHttpClient);
        assertThat(sslDelegate).isNotNull();
    }

    @Test
    void edmiFeignClientWrapsDelegate() {
        Client delegate = mock(Client.class);
        assertThat(config.edmiFeignClient(delegate)).isNotNull();
    }

    @Test
    void feignBuilderIsConfigured() {
        Client delegate = mock(Client.class);
        InvocationHandlerFactory factory = mock(InvocationHandlerFactory.class);
        assertThat(config.feignBuilder(factory, delegate)).isNotNull();
    }

    @Test
    void sslOkHttpClientFailsWithoutValidKeystore() {
        SSLProperties properties = new SSLProperties();
        assertThatThrownBy(() -> config.sslOkHttpClient(properties)).isInstanceOf(Exception.class);
    }
}
