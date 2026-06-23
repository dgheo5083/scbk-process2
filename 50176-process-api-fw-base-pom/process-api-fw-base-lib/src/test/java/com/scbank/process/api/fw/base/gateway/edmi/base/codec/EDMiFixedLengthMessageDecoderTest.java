package com.scbank.process.api.fw.base.gateway.edmi.base.codec;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.gateway.edmi.base.codec.decoder.EDMiFixedLengthMessageDecoder;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiResponseMessage;

/**
 * Generated unit test for {@link EDMiFixedLengthMessageDecoder}.
 */
class EDMiFixedLengthMessageDecoderTest {

    private final EDMiFixedLengthMessageDecoder decoder = new EDMiFixedLengthMessageDecoder();

    @Test
    void decodeReturnsNullWhenBodyIsAbsent() throws Exception {
        assertThat(decoder.decode(FeignTestSupport.responseWithoutBody(), String.class)).isNull();
    }

    @Test
    void decodeWrapsBytesIntoResponseMessage() throws Exception {
        byte[] payload = "RAWMESSAGE".getBytes(StandardCharsets.UTF_8);

        Object decoded = decoder.decode(FeignTestSupport.response(payload), String.class);

        assertThat(decoded).isInstanceOf(EDMiResponseMessage.class);
        assertThat(((EDMiResponseMessage) decoded).getResponseMessage()).isEqualTo(payload);
    }

    @Test
    void supportedReturnsFalseWhenNoFilterContext() {
        assertThat(decoder.supported(FeignTestSupport.responseWithoutBody(), String.class)).isFalse();
    }
}
