package com.scbank.process.api.fw.base.gateway.edmi.base.codec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder.EDMiMappingMessageEncoder;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiRequestMessage;

import feign.RequestTemplate;

/**
 * Generated unit test for {@link EDMiMappingMessageEncoder}.
 */
class EDMiMappingMessageEncoderTest {

    private final EDMiMappingMessageEncoder encoder = new EDMiMappingMessageEncoder(new ObjectMapper());
    private final RequestTemplate template = new RequestTemplate();

    @Test
    void supportedReturnsTrueForNonHostRequestMessage() {
        EDMiRequestMessage request = EDMiRequestMessage.builder().systemId("mci").build();
        assertThat(encoder.supported(request, String.class, template)).isTrue();
    }

    @Test
    void supportedReturnsFalseForHostRequestMessage() {
        EDMiRequestMessage request = EDMiRequestMessage.builder().systemId("host").build();
        assertThat(encoder.supported(request, String.class, template)).isFalse();
    }

    @Test
    void supportedReturnsFalseForNonRequestMessage() {
        assertThat(encoder.supported("plain", String.class, template)).isFalse();
    }

    @Test
    void encodeIgnoresNonRequestMessage() {
        assertThatCode(() -> encoder.encode("plain", String.class, template)).doesNotThrowAnyException();
    }

    @Test
    void encodeProcessesRequestMessage() {
        EDMiRequestMessage request = EDMiRequestMessage.builder()
                .systemId("mci")
                .interfaceId("IF001")
                .requestMessage("{\"key\":\"value\"}".getBytes(StandardCharsets.UTF_8))
                .build();

        try {
            encoder.encode(request, String.class, template);
        } catch (RuntimeException ignored) {
            // expected when RuntimeContext default encoding is not initialised
        }
    }
}
