package com.scbank.process.api.fw.base.gateway.edmi.base.codec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.decoder.EDMiMappingMessageDecoder;

import feign.codec.DecodeException;

/**
 * Generated unit test for {@link EDMiMappingMessageDecoder}.
 */
class EDMiMappingMessageDecoderTest {

    private final EDMiMappingMessageDecoder decoder = new EDMiMappingMessageDecoder(new ObjectMapper());

    @Test
    void decodeReturnsNullWhenBodyIsAbsent() throws Exception {
        assertThat(decoder.decode(FeignTestSupport.responseWithoutBody(), String.class)).isNull();
    }

    @Test
    void decodeWrapsParsingErrorIntoDecodeException() {
        byte[] invalidJson = "not-json".getBytes(StandardCharsets.UTF_8);
        assertThatThrownBy(() -> decoder.decode(FeignTestSupport.response(invalidJson), String.class))
                .isInstanceOf(DecodeException.class);
    }

    @Test
    void supportedReturnsFalseWhenNoFilterContext() {
        assertThat(decoder.supported(FeignTestSupport.responseWithoutBody(), String.class)).isFalse();
    }
}
