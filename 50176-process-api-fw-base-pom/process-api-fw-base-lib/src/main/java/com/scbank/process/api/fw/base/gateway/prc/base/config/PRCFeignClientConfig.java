package com.scbank.process.api.fw.base.gateway.prc.base.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.prc.base.codec.PRCExceptionDecoder;
import com.scbank.process.api.fw.base.gateway.prc.base.interceptors.PRCRequestInterceptor;
import com.scbank.process.api.fw.base.gateway.prc.base.invocation.DefaultInvocationHandlerFactory;
import com.scbank.process.api.fw.base.gateway.prc.base.invocation.SimluationInvocationHandlerFactory;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.SimulationGatewayResolver;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.SimulationResponseFactory;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.repository.SimulationResponseRepository;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.repository.impl.PRCSimulationResponseRepository;
import com.scbank.process.api.fw.core.runtime.conditional.ConditionalOnRunMode;

import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;

/**
 * 프로세스 API Feign Client Configuration
 * 
 * @author sungdon.choi
 */
public class PRCFeignClientConfig {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    @ConditionalOnRunMode(value = "!local")
    feign.Feign.Builder feignBuilder() {
        return feign.Feign.builder()
        		.invocationHandlerFactory(new DefaultInvocationHandlerFactory());
    }
    
    /**
     * 
     * @param gatewayResolver
     * @param responseFactory
     * @return
     */
    @Bean
    @ConditionalOnRunMode(value = "local")
    feign.Feign.Builder localFeignBuilder(SimulationGatewayResolver gatewayResolver, SimulationResponseFactory responseFactory) {
        return feign.Feign.builder()
        		.invocationHandlerFactory(new SimluationInvocationHandlerFactory(gatewayResolver, responseFactory));
    }

    /**
     * 프로세스 API 엔드포인트 요청 인터셉터 빈 등록
     * 
     * @return
     */
    @Bean
    RequestInterceptor requestInterceptor() {
        return new PRCRequestInterceptor();
    }

    /**
     * 프로세스 API 엔드포인트 에러응답 메시지 디코더 빈 등록
     * 
     * @return
     */
    @Bean
    feign.codec.ErrorDecoder errorDecoder() {
        return new PRCExceptionDecoder();
    }

    /**
     * 프로세스 API 엔드포인트 응답 메시지 디코더 빈 등록
     * 
     * @return
     */
    @Bean
    feign.codec.Decoder decoder() {
        return (response, type) -> {
            String bodyStr = response.body() == null ? null
                    : feign.Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            if (bodyStr == null || bodyStr.isEmpty()) {
                return null;
            }

            JsonNode root = objectMapper.readTree(bodyStr);
            JsonNode bodyNode = root.path("body");
            return objectMapper.treeToValue(bodyNode, objectMapper.constructType(type));
        };
    }
    
    /**
     * 시뮬레이션 응답 레파지토리 빈 등록
     * @param resourceLoader {@link ResourceLoader}
     * @param objectMapper {@link ObjectMapper}
     * @return
     */
    @Bean
    @ConditionalOnRunMode(value = "local")
	SimulationResponseRepository simulationResponseRepository(ObjectMapper objectMapper) {
		return new PRCSimulationResponseRepository(objectMapper);
	}
	
    /**
     * 시뮬레이션 응답 생성 팩토리 빈 등록
     * @param repository 시뮬레이션 응답 레파지토리
     * @param errorDecoder 에러 디코더
     * @param objectMapper {@link ObjectMapper}
     * @return
     */
	@Bean
	@DependsOn({"simulationResponseRepository"})
	@ConditionalOnRunMode(value = "local")	
	SimulationResponseFactory simulationResponseFactory(
			SimulationResponseRepository repository, 
			Decoder decoder,
			ErrorDecoder errorDecoder,
			ObjectMapper objectMapper) {
		return new SimulationResponseFactory(repository, decoder, errorDecoder, objectMapper);
	}
}
