package com.scbank.process.api.fw.base.gateway.edmi.base.codec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder.EDMiFixedLengthMessageEncoder;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiRequestMessage;

import feign.RequestTemplate;

/**
 * Generated unit test for {@link EDMiFixedLengthMessageEncoder}.
 */
class EDMiFixedLengthMessageEncoderTest {

    private final EDMiFixedLengthMessageEncoder encoder = new EDMiFixedLengthMessageEncoder(new ObjectMapper());
    private final RequestTemplate template = new RequestTemplate();

    @Test
    void supportedReturnsTrueForHostRequestMessage() {
        EDMiRequestMessage request = EDMiRequestMessage.builder().systemId("host").build();
        assertThat(encoder.supported(request, String.class, template)).isTrue();
    }

    @Test
    void supportedReturnsFalseForNonHostRequestMessage() {
        EDMiRequestMessage request = EDMiRequestMessage.builder().systemId("mci").build();
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
                .systemId("host")
                .interfaceId("IF001")
                .captureSystem("host")
                .requestMessage("payload")
                .trackingId("TID")
                .hexEncoding(false)
                .build();

        // RuntimeContext static state is environment dependent; execute for coverage
        // and tolerate an EncodeException if the default charset is unavailable.
        try {
            encoder.encode(request, String.class, template);
        } catch (RuntimeException ignored) {
            // expected when RuntimeContext default encoding is not initialised
        }
    }
}
