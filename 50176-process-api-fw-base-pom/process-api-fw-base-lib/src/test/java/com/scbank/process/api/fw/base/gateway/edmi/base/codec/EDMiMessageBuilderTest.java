package com.scbank.process.api.fw.base.gateway.edmi.base.codec;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder.EDMiMessageBuilder;

/**
 * Generated unit test for {@link EDMiMessageBuilder}.
 */
class EDMiMessageBuilderTest {

    @Test
    void fluentSettersConfigureBuilder() {
        EDMiMessageBuilder builder = EDMiMessageBuilder.builder()
                .systemId("edmi")
                .trackingId("TID")
                .payloadFormat("JSON")
                .payloadVersion("1.0")
                .captureSystem("host")
                .conturyCode("KR")
                .messageTypeName("type")
                .messageSenderBody("sender")
                .messageSenderDomainBody("domain")
                .serviceBusId("BUS")
                .requestMessage(Map.of("k", "v"));

        assertThat(builder.getSystemId()).isEqualTo("edmi");
        assertThat(builder.getTrackingId()).isEqualTo("TID");
        assertThat(builder.getServiceBusId()).isEqualTo("BUS");
        assertThat(builder.getRequestMessage()).isNotNull();
    }

    @Test
    void buildProducesEnvelopeWithRequestMessage() {
        Map<String, Object> envelope = EDMiMessageBuilder.builder()
                .captureSystem("host")
                .serviceBusId("BUS")
                .requestMessage(Map.of("k", "v"))
                .build();

        assertThat(envelope).isNotNull().isNotEmpty();
    }

    @Test
    void buildProducesEnvelopeWithoutRequestMessage() {
        Map<String, Object> envelope = EDMiMessageBuilder.builder().build();

        assertThat(envelope).isNotNull().isNotEmpty();
    }
}
