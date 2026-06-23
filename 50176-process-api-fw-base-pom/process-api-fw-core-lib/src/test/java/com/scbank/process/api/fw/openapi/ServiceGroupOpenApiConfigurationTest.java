package com.scbank.process.api.fw.openapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ConfigurableApplicationContext;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;

/**
 * {@link ServiceGroupOpenApiConfiguration} 의 단순 {@code @Bean} 팩토리 메서드 단위 테스트.
 *
 * <p>{@code multipleOpenApiResource} 는 SpringDoc 내부 컴포넌트를 다수 요구하므로
 * 통합 컨텍스트 테스트 영역으로 남겨둔다.</p>
 */
class ServiceGroupOpenApiConfigurationTest {

    private final ServiceGroupOpenApiConfiguration configuration = new ServiceGroupOpenApiConfiguration();

    @Test
    @DisplayName("serviceGroupedOpenApiBeanFactory 빈 생성")
    @SuppressWarnings("unchecked")
    void serviceGroupedOpenApiBeanFactoryBean() {
        ObjectProvider<IServiceRegistrar> provider = mock(ObjectProvider.class);
        OpenApiHeaderProperties headerProperties = new OpenApiHeaderProperties();

        ServiceGroupedOpenApiBeanFactory factory =
                configuration.serviceGroupedOpenApiBeanFactory(provider, headerProperties);

        assertThat(factory).isNotNull();
    }

    @Test
    @DisplayName("swaggerServiceGroupRegistrar 빈 생성")
    void swaggerServiceGroupRegistrarBean() {
        ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);
        ChannelProperties channelProperties = mock(ChannelProperties.class);
        ServiceGroupedOpenApiBeanFactory factory = mock(ServiceGroupedOpenApiBeanFactory.class);

        ServiceGroupedOpenApiRegistrar registrar =
                configuration.swaggerServiceGroupRegistrar(context, channelProperties, factory);

        assertThat(registrar).isNotNull();
    }
}
