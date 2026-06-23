package com.scbank.process.api.fw.base.gateway.edmi.base.config;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.base.client.EDMIFeignClient;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.decoder.EDMiMappingMessageDecoder;
import com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder.EDMiMappingMessageEncoder;
import com.scbank.process.api.fw.base.gateway.edmi.base.ssl.SSLContextFactory;
import com.scbank.process.api.fw.base.gateway.edmi.base.ssl.SSLProperties;
import com.scbank.process.api.fw.integration.client.codec.FilteredDecoder;
import com.scbank.process.api.fw.integration.client.codec.FilteredEncoder;
import com.scbank.process.api.fw.integration.client.config.BaseFeignClientConfig;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterChain;

import feign.Client;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.slf4j.Slf4jLogger;
import okhttp3.OkHttpClient;

/**
 * EDMi Gateway Base OpenFeign Config
 * 
 * @see BaseFeignClientConfig
 * @author sungdon.choi
 */
public abstract class EDMiFeignClientConfig extends BaseFeignClientConfig {

    // ---------------------------------------
	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

    @Bean
    @ConditionalOnMissingBean(Encoder.class)
	protected Encoder edmiEncoder(ObjectMapper objectMapper, FeignFilterChain filterChain) {
        return new FilteredEncoder(new EDMiMappingMessageEncoder(objectMapper), filterChain);
    }

    @Bean
    @ConditionalOnMissingBean(Decoder.class)
    protected Decoder edmiDecoder(ObjectMapper objectMapper, FeignFilterChain filterChain) {
        return new FilteredDecoder(new EDMiMappingMessageDecoder(objectMapper), filterChain);
    }
    
    /**
     * HTTP 인경우 설정
     * 
     * @param properties
     * @return
     * @throws Exception
     */
    @Bean("okHttpClient")
    @ConditionalOnProperty(prefix = "edmi.gateway.default.ssl", name = "enabled", havingValue = "false", matchIfMissing = true)
    okhttp3.OkHttpClient okHttpClient() throws Exception {
        return new okhttp3.OkHttpClient.Builder()
                .build();
    }
    
    @Bean("delegateFeignClient")
    @ConditionalOnProperty(prefix = "edmi.gateway.default.ssl", name = "enabled", havingValue = "false", matchIfMissing = true)
    Client delegateFeignClient(@Qualifier("okHttpClient") OkHttpClient okHttpClient) {
        return new feign.okhttp.OkHttpClient(okHttpClient);
    }

    /**
     * HTTPS 인경우 설정
     * 
     * @param properties
     * @return
     * @throws Exception
     */
    @Bean("sslOkHttpClient")
    @ConditionalOnProperty(prefix = "edmi.gateway.default.ssl", name = "enabled", havingValue = "true", matchIfMissing = true)
    okhttp3.OkHttpClient sslOkHttpClient(SSLProperties properties) throws Exception {
    	KeyStore keyStore = SSLContextFactory.load(properties.getTrustStore());
    	SSLContext sslContext = SSLContextFactory.create(properties);
        X509TrustManager trustManager = SSLContextFactory.createTrustManager(keyStore);
        return new okhttp3.OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                .hostnameVerifier((h,s) -> true)
                .build();
    }

    @Bean("delegateFeignClient")
    @ConditionalOnProperty(prefix = "edmi.gateway.default.ssl", name = "enabled", havingValue = "true", matchIfMissing = true)
    Client delegateFeignClientWithSsl(@Qualifier("sslOkHttpClient") OkHttpClient okHttpClient) {
        return new feign.okhttp.OkHttpClient(okHttpClient);
    }
    
    @Bean
    @DependsOn({"delegateFeignClient"})
    Client edmiFeignClient(Client delegateFeignClient) {
    	return new EDMIFeignClient(delegateFeignClient);
    }
    
    @Bean
    Feign.Builder feignBuilder(
    		InvocationHandlerFactory invocationHandlerFactory, 
    		@Qualifier("delegateFeignClient") Client feignClient) {
        return Feign.builder()
        		.client(feignClient)
        		.logger(new Slf4jLogger(feignClient.getClass()))
        		.logLevel(Logger.Level.FULL)
                .invocationHandlerFactory(invocationHandlerFactory);
    }
}
