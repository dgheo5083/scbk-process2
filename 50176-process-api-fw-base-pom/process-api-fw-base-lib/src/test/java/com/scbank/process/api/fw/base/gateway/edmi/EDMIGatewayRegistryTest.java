package com.scbank.process.api.fw.base.gateway.edmi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.gateway.edmi.base.gateway.EDMIGateway;

/**
 * Generated unit test for {@link EDMIGatewayRegistry}.
 */
class EDMIGatewayRegistryTest {

    @Test
    void resolvesRegisteredGatewayByTypeName() {
        EDMIGateway gateway = mock(EDMIGateway.class);
        when(gateway.getSupportedTypeNames()).thenReturn(List.of("TYPE_A", "TYPE_B"));

        EDMIGatewayRegistry registry = new EDMIGatewayRegistry(List.of(gateway));

        assertThat(registry.resolve("TYPE_A")).isSameAs(gateway);
        assertThat(registry.resolve("TYPE_B")).isSameAs(gateway);
    }

    @Test
    void throwsWhenResolvingUnknownTypeName() {
        EDMIGateway gateway = mock(EDMIGateway.class);
        when(gateway.getSupportedTypeNames()).thenReturn(List.of("TYPE_A"));
        EDMIGatewayRegistry registry = new EDMIGatewayRegistry(List.of(gateway));

        assertThatThrownBy(() -> registry.resolve("UNKNOWN"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwsWhenGatewayHasNoSupportedTypeNames() {
        EDMIGateway gateway = mock(EDMIGateway.class);
        when(gateway.getSupportedTypeNames()).thenReturn(List.of());

        assertThatThrownBy(() -> new EDMIGatewayRegistry(List.of(gateway)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void throwsWhenGatewayHasNullSupportedTypeNames() {
        EDMIGateway gateway = mock(EDMIGateway.class);
        when(gateway.getSupportedTypeNames()).thenReturn(null);

        assertThatThrownBy(() -> new EDMIGatewayRegistry(List.of(gateway)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void throwsWhenTypeNameIsMappedTwice() {
        EDMIGateway first = mock(EDMIGateway.class);
        when(first.getSupportedTypeNames()).thenReturn(List.of("DUP"));
        EDMIGateway second = mock(EDMIGateway.class);
        when(second.getSupportedTypeNames()).thenReturn(List.of("DUP"));

        assertThatThrownBy(() -> new EDMIGatewayRegistry(List.of(first, second)))
                .isInstanceOf(IllegalStateException.class);
    }
}
