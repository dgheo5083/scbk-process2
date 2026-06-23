package com.scbank.process.api.fw.base.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.LocaleResolver;

import com.scbank.process.api.fw.channel.context.handler.IServiceContextHandler;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.channel.service.ServiceIdResolver;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.core.uuid.IIdentifyGenerator;
import com.scbank.process.api.fw.session.ISessionContextResolver;

/**
 * Generated unit test for {@link WebConfig}.
 */
class WebConfigTest {

    private final WebConfig config = new WebConfig();

    @Test
    void prcServiceContextHandlerBeanIsCreated() {
        IServiceContextHandler handler = config.prcServiceContextHandler(
                mock(IIdentifyGenerator.class),
                mock(IDeviceResolver.class),
                mock(IServiceRegistrar.class),
                mock(LocaleResolver.class),
                mock(ServiceIdResolver.class),
                mock(ISessionContextResolver.class));

        assertThat(handler).isNotNull();
    }
}
