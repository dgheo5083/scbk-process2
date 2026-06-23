package com.scbank.process.api.fw.base.integration.system.mci;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.scbank.process.api.fw.base.integration.system.mci.rebound.MciReboundStrategy;
import com.scbank.process.api.fw.integration.IntegrationProperties;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.connector.initializer.IntegrationConnectorChannelInitializer;

import io.netty.util.AttributeKey;

@Configuration
public class MciManagerConfiguration {

    @Bean
    MciRequestHeaderBuilder mciRequestHeaderBuilder() {
        return new MciRequestHeaderBuilder();
    }

    @Bean
    MciResponseHandler mciResponseHandler() {
        return new MciResponseHandler();
    }

    @Bean
    MciReboundStrategy mciReboundStrategy() {
        return new MciReboundStrategy();
    }
    
    @Bean
    IntegrationConnectorChannelInitializer mciConnectorChannelInitializer() {
        return (channel, context, systemConfig) -> {
            channel.attr(AttributeKey.valueOf("integrationContext")).set(context);
            channel.pipeline().addLast(new MciResponseDecoder(0, 8));
        };
    }

    @Bean("mciManager")
    @ConditionalOnMissingBean(MciManager.class)
    MciManager mciManager(
            IntegrationProperties integrationProperties,
            IntegrationConnectorChannelInitializer mciConnectorChannelInitializer,
            MciRequestHeaderBuilder mciRequestHeaderBuilder,
            MciResponseHandler mciResponseHandler,
            MciReboundStrategy mciReboundStrategy) {
        IntegrationSystemConfig integrationSystemConfig = integrationProperties.getSystem().get("mci");
        return new MciManager(
                integrationSystemConfig,
                mciConnectorChannelInitializer,
                mciRequestHeaderBuilder,
                mciResponseHandler,
                mciReboundStrategy);
    }
}
