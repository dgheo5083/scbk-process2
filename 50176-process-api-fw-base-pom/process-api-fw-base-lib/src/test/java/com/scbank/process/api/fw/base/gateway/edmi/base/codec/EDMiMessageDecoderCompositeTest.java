package com.scbank.process.api.fw.base.gateway.edmi.base.codec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.gateway.edmi.base.codec.decoder.EDMiMessageDecoderComposite;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.decoder.IEDMiMessageDecoder;

import feign.Response;
import feign.codec.DecodeException;

/**
 * Generated unit test for {@link EDMiMessageDecoderComposite}.
 */
class EDMiMessageDecoderCompositeTest {

    private final Response response = FeignTestSupport.responseWithoutBody();

    @Test
    void delegatesToFirstSupportedDecoder() throws Exception {
        IEDMiMessageDecoder decoder = mock(IEDMiMessageDecoder.class);
        when(decoder.supported(any(), any())).thenReturn(true);
        when(decoder.decode(any(), any())).thenReturn("decoded");

        EDMiMessageDecoderComposite composite = new EDMiMessageDecoderComposite(List.of(decoder));

        assertThat(composite.decode(response, String.class)).isEqualTo("decoded");
    }

    @Test
    void skipsUnsupportedDecoderAndThrowsWhenNoneMatch() {
        IEDMiMessageDecoder decoder = mock(IEDMiMessageDecoder.class);
        when(decoder.supported(any(), any())).thenReturn(false);

        EDMiMessageDecoderComposite composite = new EDMiMessageDecoderComposite(List.of(decoder));

        assertThatThrownBy(() -> composite.decode(response, String.class))
                .isInstanceOf(DecodeException.class)
                .hasMessageContaining("Not Found");
    }

    @Test
    void wrapsDecoderExceptionIntoDecodeException() throws Exception {
        IEDMiMessageDecoder decoder = mock(IEDMiMessageDecoder.class);
        when(decoder.supported(any(), any())).thenReturn(true);
        when(decoder.decode(any(), any())).thenThrow(new RuntimeException("boom"));

        EDMiMessageDecoderComposite composite = new EDMiMessageDecoderComposite(List.of(decoder));

        assertThatThrownBy(() -> composite.decode(response, String.class))
                .isInstanceOf(DecodeException.class)
                .hasMessageContaining("Decode failed");
    }
}
