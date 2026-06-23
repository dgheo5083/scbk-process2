package com.scbank.process.api.fw.base.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.LocaleResolver;

import com.scbank.process.api.fw.base.channel.context.PRCServiceContextHandler;
import com.scbank.process.api.fw.channel.context.handler.IServiceContextHandler;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.channel.service.ServiceIdResolver;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.core.uuid.IIdentifyGenerator;
import com.scbank.process.api.fw.session.ISessionContextResolver;

import lombok.extern.slf4j.Slf4j;

/**
 * 프로세스 API Application Web Configuration
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "csl.channel", name = "enabled", havingValue = "true")
public class WebConfig {

	/**
     * Dispatcher 흐름용 서비스 컨텍스트 핸들러 등록
     */
    @Bean
    @Primary
    IServiceContextHandler prcServiceContextHandler(
            @Qualifier("requestUUIdGenerator") IIdentifyGenerator identifyGenerator,
            IDeviceResolver deviceResolver,
            IServiceRegistrar serviceRegistrar,
            LocaleResolver localeResolver,
            ServiceIdResolver serviceIdResolver,
            ISessionContextResolver sessionContextResolver) {
        return new PRCServiceContextHandler(identifyGenerator, deviceResolver, serviceRegistrar,
                localeResolver, serviceIdResolver, sessionContextResolver);
    }
}
