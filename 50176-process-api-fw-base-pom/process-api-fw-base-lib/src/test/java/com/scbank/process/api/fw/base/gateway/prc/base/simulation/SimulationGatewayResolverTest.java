package com.scbank.process.api.fw.base.gateway.prc.base.simulation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.gateway.prc.PRCGatewayProperties;
import com.scbank.process.api.fw.base.gateway.prc.PRCGatewayProperties.Gateway;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.SimulationGatewayResolver.ResolvedGateway;

/**
 * Generated unit test for {@link SimulationGatewayResolver}.
 */
class SimulationGatewayResolverTest {

    private final PRCGatewayProperties properties = mock(PRCGatewayProperties.class);
    private final SimulationGatewayResolver resolver = new SimulationGatewayResolver(properties);

    private void registerGateway(String key, String baseUrl) {
        Gateway gateway = mock(Gateway.class);
        when(gateway.getBaseUrl()).thenReturn(baseUrl);
        Map<String, Gateway> map = new HashMap<>();
        map.put(key, gateway);
        when(properties.getGateway()).thenReturn(map);
    }

    @Test
    void resolvesGatewayWhenTargetUrlMatchesBaseUrl() {
        registerGateway("deposit", "http://localhost");

        Optional<ResolvedGateway> resolved = resolver.resolve("http://localhost/api/v1");

        assertThat(resolved).isPresent();
        assertThat(resolved.get().target()).isEqualTo("deposit");
    }

    @Test
    void returnsEmptyWhenNoBaseUrlMatches() {
        registerGateway("deposit", "http://localhost");

        Optional<ResolvedGateway> resolved = resolver.resolve("http://remote-host/api");

        assertThat(resolved).isEmpty();
    }
}
