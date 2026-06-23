package com.scbank.process.api.fw.integration.connector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;

class SimpleTcpConnectorTest {
	
	private static Class<?> findNettyResponseHandlerClass(){
		return Arrays.stream(SimpleTcpConnector.class.getDeclaredClasses()).filter(c -> 
		"NettyResponseHandler".equals(c.getSimpleName())).findFirst().orElseThrow(() -> new IllegalStateException("내부 클래스 못찾음"));
	}

	@Test
	void nettyResponseHandler() throws Exception{
		CompletableFuture<ByteBuf> future = new CompletableFuture<>();
		
		Class<?> handlerClass = findNettyResponseHandlerClass();
		
		Constructor<?> ctor = handlerClass.getDeclaredConstructor(CompletableFuture.class);
		
		ctor.setAccessible(true);
		
		ChannelHandler handler = (ChannelHandler)ctor.newInstance(future);
		
		EmbeddedChannel ch = new EmbeddedChannel((io.netty.channel.ChannelHandler) handler);
		
		ch.writeInbound(Unpooled.wrappedBuffer("OK".getBytes()));
		
		ByteBuf result = future.join();
		byte[] bytes = new byte[result.readableBytes()];
		result.readBytes(bytes);
		
		assertEquals("OK", new String(bytes));
		result.release();
		ch.finishAndReleaseAll();
	}
	
	@Test
	void nettyResponseHandler_channelInactive() throws Exception{
		CompletableFuture<ByteBuf> future = new CompletableFuture<>();
		
		Class<?> handlerClass = findNettyResponseHandlerClass();
		
		Constructor<?> ctor = handlerClass.getDeclaredConstructor(CompletableFuture.class);
		
		ctor.setAccessible(true);
		
		ChannelHandler handler = (ChannelHandler)ctor.newInstance(future);
		
		EmbeddedChannel ch = new EmbeddedChannel(handler);
		
		ch.close();
		
		assertTrue(future.isCompletedExceptionally());
		ch.finishAndReleaseAll();
	}

}
