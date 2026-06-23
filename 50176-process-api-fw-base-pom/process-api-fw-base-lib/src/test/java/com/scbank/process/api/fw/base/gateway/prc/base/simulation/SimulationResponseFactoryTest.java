package com.scbank.process.api.fw.base.gateway.prc.base.simulation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.annotation.SimulationMode;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.repository.SimulationResponseRepository;

import feign.Response;
import feign.Target;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;

/**
 * Generated unit test for {@link SimulationResponseFactory}.
 */
class SimulationResponseFactoryTest {

    interface Api {
        @GetMapping("/g")
        String get();
    }

    private final SimulationResponseRepository repository = mock(SimulationResponseRepository.class);
    private final Decoder decoder = mock(Decoder.class);
    private final ErrorDecoder errorDecoder = mock(ErrorDecoder.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimulationResponseFactory factory =
            new SimulationResponseFactory(repository, decoder, errorDecoder, objectMapper);

    private final SimulationMode simulation = mock(SimulationMode.class);
    @SuppressWarnings("rawtypes")
    private final Target target = mock(Target.class);
    private Method method;

    private void prepare() throws Exception {
        method = Api.class.getMethod("get");
        lenient().when(simulation.senario()).thenReturn("default");
        lenient().when(target.url()).thenReturn("http://localhost");
        doReturn(Api.class).when(target).type();
    }

    @Test
    @SuppressWarnings("unchecked")
    void returnsNullWhenNoSimulationResponse() throws Exception {
        prepare();
        when(repository.getResponse(any(), anyString(), anyString())).thenReturn(null);

        assertThat(factory.createOrThrow(target, method, simulation, "config")).isNull();
    }

    @Test
    @SuppressWarnings("unchecked")
    void decodesNormalResponse() throws Exception {
        prepare();
        JsonNode node = objectMapper.readTree(
                "{\"headers\":{\"X-A\":[\"1\",\"2\"],\"X-B\":\"single\"},\"payload\":{\"k\":\"v\"}}");
        when(repository.getResponse(any(), anyString(), anyString())).thenReturn(node);
        when(decoder.decode(any(Response.class), any())).thenReturn("decoded");

        assertThat(factory.createOrThrow(target, method, simulation, "config")).isEqualTo("decoded");
    }

    @Test
    @SuppressWarnings("unchecked")
    void throwsDecodedHttpError() throws Exception {
        prepare();
        JsonNode node = objectMapper.readTree(
                "{\"type\":\"HTTP_ERROR\",\"status\":404,\"reason\":\"NOT_FOUND\",\"payload\":\"err\"}");
        when(repository.getResponse(any(), anyString(), anyString())).thenReturn(node);
        when(errorDecoder.decode(anyString(), any(Response.class)))
                .thenReturn(new IllegalStateException("simulated"));

        assertThatThrownBy(() -> factory.createOrThrow(target, method, simulation, "config"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("simulated");
    }
}
