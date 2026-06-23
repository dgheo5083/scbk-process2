package com.scbank.process.api.fw.base.integration.system.mci;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MciResponseDecoder extends ByteToMessageDecoder {

	private enum State {
		READ_LENGTH, READ_BODY
	}

	private final int lengthFieldOffset;
	private final int lengthFieldLength;
	private final int headerSize;

	private State state = State.READ_LENGTH;

	private int bodyLength;

	public MciResponseDecoder(int lengthFieldOffset, int lengthFieldLength) {
		this.lengthFieldOffset = lengthFieldOffset;
		this.lengthFieldLength = lengthFieldLength;
		this.headerSize = lengthFieldOffset + lengthFieldLength;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while (true) {
			switch (state) {
				case READ_LENGTH -> {
					if (in.readableBytes() < headerSize) {
						return;
					}
	
					int readerIdx = in.readerIndex();
					int lenPos = readerIdx + lengthFieldOffset;
	
					String lenStr = in.slice(lenPos, lengthFieldLength).toString(Charset.forName("euc-kr")).trim();
	
					bodyLength = Integer.parseInt(lenStr);
	
					state = State.READ_BODY;
				}
				case READ_BODY -> {
					int frameLength = headerSize + bodyLength;
					if (in.readableBytes() < frameLength) {
						return;
					}
					
					ByteBuf frame = in.readRetainedSlice(frameLength);
					out.add(frame);
					
					bodyLength = 0;
					state = State.READ_LENGTH;
				}
			}
		}
	}
}
