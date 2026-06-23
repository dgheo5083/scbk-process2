package com.scbank.process.api.fw.integration.connector;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.scbank.process.api.fw.core.utils.ByteBuffWrap;
import com.scbank.process.api.fw.integration.IntegrationProperties.Endpoint;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.IntegrationProperties.SocketOption;
import com.scbank.process.api.fw.integration.connector.initializer.IntegrationConnectorChannelInitializer;
import com.scbank.process.api.fw.integration.connector.loadbalance.LoadBalanceStrategy;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.exception.IntegrationException;
import com.scbank.process.api.fw.integration.exception.IntegrationSystemException;
import com.scbank.process.api.fw.integration.exception.IntegrationTimeoutException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.RequiredArgsConstructor;

/**
 * DefaultTcpConnector
 *
 * <p>Netty 기반 비동기 TCP 클라이언트 구현체입니다. 
 * IntegrationContext 및 설정 정보(IntegrationSystemConfig)를 기반으로 TCP 연결을 생성하고,
 * 요청(ByteBuffWrap)을 서버로 송신한 후, 응답(ByteBuffWrap)을 수신합니다.
 *
 * <p>주요 기능:
 * - Load Balancer를 통한 엔드포인트 선택
 * - Netty Bootstrap 설정 및 채널 초기화
 * - TCP 요청/응답 처리 및 타임아웃 관리
 * - 응답을 CountDownLatch를 통해 동기화 처리
 *
 * <p>TCP 통신 중 발생하는 오류는 IntegrationException 계열로 변환되어 던져집니다.
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
@RequiredArgsConstructor
public class SimpleTcpConnector implements IntegrationConnector<ByteBuffWrap, ByteBuffWrap> {

	/** 시스템 구성 정보 (엔드포인트, 소켓 옵션 등) */
    private final IntegrationSystemConfig systemConfig;

    /** 채널 초기화 핸들러 */
    private final IntegrationConnectorChannelInitializer channelInitializer;

    /** 로드 밸런서 전략 (기본 사용) */
    private final LoadBalanceStrategy loadBalanceStrategy = LoadBalanceStrategy.Default;
    
	@Override
	public ByteBuffWrap send(IntegrationContext context, ByteBuffWrap request) throws IntegrationException {
		CompletableFuture<ByteBuf> responseFuture = new CompletableFuture<>();
		EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        Endpoint endpoint = loadBalanceStrategy.select(systemConfig.targets());
        SocketOption option = systemConfig.socketOption();
        long connectTimeout = option.connectTimeout();
        long readTimeout = option.readTimeout();

        try {
            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) connectTimeout)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        channelInitializer.initialize(ch, context, systemConfig);
                        ch.pipeline().addLast(new NettyResponseHandler(responseFuture));
                    }
                });

            ChannelFuture cf = bootstrap.connect(endpoint.ip(), endpoint.port()).sync();
            cf.addListener((ChannelFutureListener)f -> {
            	if (!f.isSuccess()) {
            		responseFuture.completeExceptionally(f.cause());
            		return;
            	}
            	
            	ByteBuf buf = Unpooled.wrappedBuffer(request.getByteArray());
            	f.channel().writeAndFlush(buf).addListener((ChannelFutureListener) wf -> {
            		if (!wf.isSuccess() && !responseFuture.isDone()) {
            			responseFuture.completeExceptionally(wf.cause());
            			f.channel().close();
            		}
            	});
            	
            	f.channel().eventLoop().schedule(() -> {
            		if (!responseFuture.isDone()) {
            			responseFuture.completeExceptionally(
            				new TimeoutException("Read Timeout: "+ readTimeout)
            			);
            			f.channel().close();
            		}
            	}, readTimeout, TimeUnit.MILLISECONDS);
            });
            
            CompletableFuture<ByteBuffWrap> response = responseFuture.handle((buf, ex) -> {
            	if (ex != null) {
            		throw new CompletionException(ex);
            	}
            	
            	try {
            		byte[] bytes = new byte[buf.readableBytes()];
            		buf.readBytes(bytes);
            		return ByteBuffWrap.wrap(bytes);
            	} finally {
            		buf.release();
            	}
            });
            return response.get(readTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new IntegrationTimeoutException(context.getSystemId(), e);
        } catch (IntegrationTimeoutException e) {
            throw e;
        } catch (Exception e) {
            throw new IntegrationSystemException(context.getSystemId(), e);
        } finally {
            //group.shutdownGracefully();
        }
	}
	
	/**
     * Netty 채널로부터 수신된 응답을 처리하는 핸들러입니다.
     */
	@RequiredArgsConstructor
    private static class NettyResponseHandler extends SimpleChannelInboundHandler<ByteBuf> {

        private final CompletableFuture<ByteBuf> responseFuture;

        /**
         * 채널로부터 수신된 ByteBuf 데이터를 읽어 응답에 저장하고, 수신 완료 신호를 보냅니다.
         *
         * @param ctx ChannelHandlerContext
         * @param msg 수신된 ByteBuf 메시지
         */
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        	ByteBuf retained = msg.retainedDuplicate();
        	responseFuture.complete(retained);
        	
        	ctx.close();
        }
        
        @Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			if (!responseFuture.isDone()) {
				responseFuture.completeExceptionally(new IllegalStateException("Channel close before response"));
			}
		}
        
        @Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}
    }
}
