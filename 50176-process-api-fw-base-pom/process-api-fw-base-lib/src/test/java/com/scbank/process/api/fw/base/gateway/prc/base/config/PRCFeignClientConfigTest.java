package com.scbank.process.api.fw.base.gateway.prc.base.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.SimulationGatewayResolver;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.SimulationResponseFactory;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.repository.SimulationResponseRepository;

import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;

/**
 * Generated unit test for {@link PRCFeignClientConfig}.
 */
class PRCFeignClientConfigTest {

    private final PRCFeignClientConfig config = new PRCFeignClientConfig();

    @Test
    void feignBuilderIsConfigured() {
        assertThat(config.feignBuilder()).isNotNull();
    }

    @Test
    void localFeignBuilderIsConfigured() {
        assertThat(config.localFeignBuilder(
                mock(SimulationGatewayResolver.class),
                mock(SimulationResponseFactory.class))).isNotNull();
    }

    @Test
    void requestInterceptorBeanIsCreated() {
        RequestInterceptor interceptor = config.requestInterceptor();
        assertThat(interceptor).isNotNull();
    }

    @Test
    void errorDecoderBeanIsCreated() {
        ErrorDecoder errorDecoder = config.errorDecoder();
        assertThat(errorDecoder).isNotNull();
    }

    @Test
    void decoderBeanIsCreated() {
        Decoder decoder = config.decoder();
        assertThat(decoder).isNotNull();
    }

    @Test
    void simulationResponseRepositoryBeanIsCreated() {
        SimulationResponseRepository repository = config.simulationResponseRepository(new ObjectMapper());
        assertThat(repository).isNotNull();
    }

    @Test
    void simulationResponseFactoryBeanIsCreated() {
        SimulationResponseFactory factory = config.simulationResponseFactory(
                mock(SimulationResponseRepository.class),
                config.decoder(),
                config.errorDecoder(),
                new ObjectMapper());
        assertThat(factory).isNotNull();
    }
}
