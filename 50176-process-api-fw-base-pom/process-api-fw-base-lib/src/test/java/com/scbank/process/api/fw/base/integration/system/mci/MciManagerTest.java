package com.scbank.process.api.fw.base.integration.system.mci;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.integration.system.mci.rebound.MciReboundStrategy;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.connector.initializer.IntegrationConnectorChannelInitializer;
import com.scbank.process.api.fw.integration.request.IntegrationRequestHeaderBuilder;
import com.scbank.process.api.fw.integration.response.IntegrationResponseHandler;

/**
 * Generated smoke test for {@link MciManager}.
 */
class MciManagerTest {

    @SuppressWarnings("rawtypes")
    private MciManager newManager() {
        IntegrationSystemConfig systemConfig = mock(IntegrationSystemConfig.class);
        IntegrationConnectorChannelInitializer initializer = mock(IntegrationConnectorChannelInitializer.class);
        IntegrationRequestHeaderBuilder builder = mock(IntegrationRequestHeaderBuilder.class);
        IntegrationResponseHandler handler = mock(IntegrationResponseHandler.class);
        MciReboundStrategy rebound = mock(MciReboundStrategy.class);
        return new MciManager(systemConfig, initializer, builder, handler, rebound);
    }

    @Test
    void initialisesAndExposesSystemId() {
        MciManager manager = newManager();
        manager.init();
        assertThat(manager.getSystemId()).isEqualTo("mci");
    }
}
