package com.scbank.process.api.fw.base.gateway.edmi.base.codec;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder.EDMiMessageEncoderCompoiste;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder.IEDMiMessageEncoder;

import feign.RequestTemplate;

/**
 * Generated unit test for {@link EDMiMessageEncoderCompoiste}.
 */
class EDMiMessageEncoderCompositeTest {

    private final RequestTemplate template = new RequestTemplate();
    private final Object payload = new Object();

    @Test
    void delegatesToFirstSupportedEncoder() {
        IEDMiMessageEncoder encoder = mock(IEDMiMessageEncoder.class);
        when(encoder.supported(any(), any(), any())).thenReturn(true);

        EDMiMessageEncoderCompoiste composite = new EDMiMessageEncoderCompoiste(List.of(encoder));
        composite.encode(payload, String.class, template);

        verify(encoder, times(1)).encode(payload, String.class, template);
    }

    @Test
    void doesNothingWhenNoEncoderSupports() {
        IEDMiMessageEncoder encoder = mock(IEDMiMessageEncoder.class);
        when(encoder.supported(any(), any(), any())).thenReturn(false);

        EDMiMessageEncoderCompoiste composite = new EDMiMessageEncoderCompoiste(List.of(encoder));
        composite.encode(payload, String.class, template);

        verify(encoder, never()).encode(any(), any(), any());
    }
}
