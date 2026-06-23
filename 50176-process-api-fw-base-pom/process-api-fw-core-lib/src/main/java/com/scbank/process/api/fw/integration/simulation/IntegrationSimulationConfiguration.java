package com.scbank.process.api.fw.integration.simulation;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.core.runtime.conditional.ConditionalOnRunMode;
import com.scbank.process.api.fw.integration.IntegrationProperties;
import com.scbank.process.api.fw.integration.codec.XmlIntegrationClientCodec;
import com.scbank.process.api.fw.integration.simulation.impl.DefaultIntegrationSimulationRepository;
import com.scbank.process.api.fw.integration.support.IntegrationMessageContextCreator;

/**
 * 시스템 연계 대응답 메시지 Spring Configuration 클래스 (운영환경이 아닌경우 활성화)
 * 
 * @author sungdon.choi
 */
@Configuration
@ConditionalOnProperty(prefix = "csl.integration", name = "enabled", havingValue = "true")
@ConditionalOnRunMode(value = {"!prd"})
public class IntegrationSimulationConfiguration {

    /**
     * 
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(IntegrationSimulationRepository.class)
    IntegrationSimulationRepository integrationSimulationRepository(IntegrationProperties properties,
            List<IntegrationSimulationHeaderStrategy<?, ?>> headerStrategies,
            XmlMapper xmlMapper, XmlIntegrationClientCodec xmlCodec,
            IntegrationMessageContextCreator integrationMessageContextCreator) {
        return new DefaultIntegrationSimulationRepository(properties, headerStrategies, xmlMapper, xmlCodec,
                integrationMessageContextCreator);
    }
}
