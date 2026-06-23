package com.scbank.process.api.fw.base.integration.system.edmi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.gateway.edmi.EDMIGatewayRegistry;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.request.IntegrationRequestHeaderBuilder;
import com.scbank.process.api.fw.integration.response.IntegrationResponseHandler;

/**
 * Generated smoke test for {@link EdmiManager}.
 */
class EdmiManagerTest {

    @SuppressWarnings("rawtypes")
    private EdmiManager newManager() {
        IntegrationSystemConfig systemConfig = mock(IntegrationSystemConfig.class);
        IntegrationRequestHeaderBuilder builder = mock(IntegrationRequestHeaderBuilder.class);
        IntegrationResponseHandler handler = mock(IntegrationResponseHandler.class);
        EDMIGatewayRegistry registry = mock(EDMIGatewayRegistry.class);
        return new EdmiManager(systemConfig, builder, handler, registry);
    }

    @Test
    void initialisesAndExposesSystemId() {
        EdmiManager manager = newManager();
        manager.init();
        assertThat(manager.getSystemId()).isEqualTo("edmi");
    }
}
