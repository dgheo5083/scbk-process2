package com.scbank.process.api.fw.base.integration.system.oltp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.gateway.edmi.MBOltpCommonRoute;
import com.scbank.process.api.fw.base.integration.system.oltp.rebound.OltpReboundStrategy;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.request.IntegrationRequestHeaderBuilder;
import com.scbank.process.api.fw.integration.response.IntegrationResponseHandler;

/**
 * Generated smoke test for {@link OltpManager}.
 */
class OltpManagerTest {

    @SuppressWarnings("rawtypes")
    private OltpManager newManager() {
        IntegrationSystemConfig systemConfig = mock(IntegrationSystemConfig.class);
        IntegrationRequestHeaderBuilder builder = mock(IntegrationRequestHeaderBuilder.class);
        IntegrationResponseHandler handler = mock(IntegrationResponseHandler.class);
        OltpReboundStrategy rebound = mock(OltpReboundStrategy.class);
        MBOltpCommonRoute gateway = mock(MBOltpCommonRoute.class);
        return new OltpManager(systemConfig, builder, handler, rebound, gateway);
    }

    @Test
    void initialisesAndExposesSystemId() {
        OltpManager manager = newManager();
        manager.init();
        assertThat(manager.getSystemId()).isEqualTo("host");
    }
}
