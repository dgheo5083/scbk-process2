package com.scbank.process.api.fw.openapi;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties;

/**
 * {@link ServiceGroupedOpenApiRegistrar} 단위 테스트.
 */
class ServiceGroupedOpenApiRegistrarTest {

    private final ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);
    private final ServiceGroupedOpenApiBeanFactory beanFactory = mock(ServiceGroupedOpenApiBeanFactory.class);

    @Test
    @DisplayName("channelProperties 가 null 이면 아무 작업도 하지 않는다")
    void nullChannelProperties() {
        ServiceGroupedOpenApiRegistrar registrar =
                new ServiceGroupedOpenApiRegistrar(context, null, beanFactory);

        registrar.registerApis();

        verifyNoInteractions(beanFactory);
        verifyNoInteractions(context);
    }

    @Test
    @DisplayName("service 목록이 null 이면 아무 작업도 하지 않는다")
    void nullServiceList() {
        ChannelProperties channelProperties = mock(ChannelProperties.class);
        when(channelProperties.getService()).thenReturn(null);
        ServiceGroupedOpenApiRegistrar registrar =
                new ServiceGroupedOpenApiRegistrar(context, channelProperties, beanFactory);

        registrar.registerApis();

        verify(beanFactory, never()).createGroupedOpenApis(org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    @DisplayName("생성된 GroupedOpenApi 들을 '<group>GroupedOpenApi' 이름으로 싱글톤 등록한다")
    void registersGroupedOpenApis() {
        ChannelProperties channelProperties = mock(ChannelProperties.class);
        List<ServiceProperties> services = List.of();
        when(channelProperties.getService()).thenReturn(services);

        GroupedOpenApi api = GroupedOpenApi.builder()
                .group("loanService")
                .pathsToMatch("/loan/**")
                .build();
        when(beanFactory.createGroupedOpenApis(services)).thenReturn(List.of(api));

        ConfigurableListableBeanFactory listableBeanFactory = mock(ConfigurableListableBeanFactory.class);
        when(context.getBeanFactory()).thenReturn(listableBeanFactory);

        ServiceGroupedOpenApiRegistrar registrar =
                new ServiceGroupedOpenApiRegistrar(context, channelProperties, beanFactory);

        registrar.registerApis();

        verify(listableBeanFactory).registerSingleton(eq("loanServiceGroupedOpenApi"), same(api));
    }
}
