package com.scbank.process.api.fw.base.integration.system.edmi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.base.gateway.edmi.EDMIGatewayRegistry;
import com.scbank.process.api.fw.base.integration.uuid.IntegrationTranNoGenerator;
import com.scbank.process.api.fw.integration.IntegrationProperties;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;

/**
 * Generated unit test for {@link EdmiManagerConfiguration}.
 */
@ExtendWith(MockitoExtension.class)
class EdmiManagerConfigurationTest {

    private final EdmiManagerConfiguration config = new EdmiManagerConfiguration();

    private IntegrationProperties propertiesWith(Map<String, Object> systemProperties) {
        IntegrationProperties integrationProperties = mock(IntegrationProperties.class);
        IntegrationSystemConfig systemConfig = mock(IntegrationSystemConfig.class);
        Map<String, IntegrationSystemConfig> systemMap = new HashMap<>();
        systemMap.put("edmi", systemConfig);
        when(integrationProperties.getSystem()).thenReturn(systemMap);
        lenient().when(systemConfig.properties()).thenReturn(systemProperties);
        return integrationProperties;
    }

    @Test
    void tranNoGeneratorUsesConfiguredPath(@TempDir Path tempDir) {
        Map<String, Object> props = new HashMap<>();
        props.put("tranno_sequence_file_path", tempDir.toString());

        IntegrationTranNoGenerator generator = config.edmiIntegrationTranNoGenerator(propertiesWith(props));
        assertThat(generator).isNotNull();
    }

    @Test
    void tranNoGeneratorFallsBackToUserHomeWhenNoProperties() {
        IntegrationTranNoGenerator generator = config.edmiIntegrationTranNoGenerator(propertiesWith(new HashMap<>()));
        assertThat(generator).isNotNull();
    }

    @Test
    void requestHeaderBuilderBeanIsCreated() {
        assertThat(config.edmiRequestHeaderBuilder(mock(IntegrationTranNoGenerator.class))).isNotNull();
    }

    @Test
    void responseHandlerBeanIsCreated() {
        assertThat(config.edmiResponseHandler()).isNotNull();
    }

    @Test
    void managerBeanIsCreated() {
        EdmiManager manager = config.edmiManager(
                propertiesWith(new HashMap<>()),
                mock(EdmiRequestHeaderBuilder.class),
                mock(EdmiResponseHandler.class),
                mock(EDMIGatewayRegistry.class));
        assertThat(manager).isNotNull();
    }
}
