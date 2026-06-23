package com.scbank.process.api.fw.base.gateway.edmi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiRequestMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiResponseMessage;

/**
 * Generated unit test for {@link MBOltpCommonRouteGateway}.
 */
class MBOltpCommonRouteGatewayTest {

    private final MBOltpCommonRoute feignClient = mock(MBOltpCommonRoute.class);
    private final MBOltpCommonRouteGateway gateway = new MBOltpCommonRouteGateway(feignClient);

    @Test
    void exposesSupportedTypeNames() {
        assertThat(gateway.getSupportedTypeNames()).containsExactly("CoreBanking:mbOLTPCommonRoute");
    }

    @Test
    void sendDelegatesToFeignClient() {
        EDMiRequestMessage request = EDMiRequestMessage.builder().build();
        EDMiResponseMessage response = EDMiResponseMessage.builder().build();
        when(feignClient.send(request)).thenReturn(response);

        assertThat(gateway.send(request)).isSameAs(response);
    }
}
