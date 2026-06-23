package com.scbank.process.api.fw.base.integration.system.mci;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * Generated unit test for {@link MciResponseDecoder}.
 */
class MciResponseDecoderTest {

    private static final Charset EUC_KR = Charset.forName("euc-kr");

    private ByteBuf frame(int bodyLength, String body) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(String.format("%8s", bodyLength).getBytes(EUC_KR));
        buf.writeBytes(body.getBytes(EUC_KR));
        return buf;
    }

    @Test
    void decodesCompleteFrame() {
        EmbeddedChannel channel = new EmbeddedChannel(new MciResponseDecoder(0, 8));

        assertThat(channel.writeInbound(frame(5, "HELLO"))).isTrue();

        ByteBuf out = channel.readInbound();
        assertThat(out).isNotNull();
        assertThat(out.readableBytes()).isEqualTo(13);
        out.release();
        channel.finishAndReleaseAll();
    }

    @Test
    void waitsForCompleteLengthField() {
        EmbeddedChannel channel = new EmbeddedChannel(new MciResponseDecoder(0, 8));

        channel.writeInbound(Unpooled.wrappedBuffer("123".getBytes(EUC_KR)));

        assertThat((Object) channel.readInbound()).isNull();
        channel.finishAndReleaseAll();
    }

    @Test
    void waitsForCompleteBody() {
        EmbeddedChannel channel = new EmbeddedChannel(new MciResponseDecoder(0, 8));

        // length says 5 but only 2 body bytes available
        channel.writeInbound(frame(5, "AB"));

        assertThat((Object) channel.readInbound()).isNull();
        channel.finishAndReleaseAll();
    }
}
