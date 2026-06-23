package com.scbank.process.api.fw.base.integration.system.edmi;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.base.gateway.edmi.EDMIGatewayRegistry;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.uuid.IntegrationTranNoGenerator;
import com.scbank.process.api.fw.core.uuid.sequence.DailyFileSequenceGenerator;
import com.scbank.process.api.fw.integration.IntegrationProperties;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * 호스트 외 시스템 EDMi 전문통신 spring configuration
 * 
 * @author sungdon.choi
 */
@Slf4j
@Configuration
//@ConditionalOnProperty(prefix = "csl.integration.system.edmi", name = "enabled", havingValue = "true",  matchIfMissing = true)
public class EdmiManagerConfiguration {

	/**
     * 전문 거래번호 생성기 Bean
     * 
     * @param integrationProperties 연계시스템 설정 Properties
     * @return
     */
    @Bean("edmiIntegrationTranNoGenerator")
    IntegrationTranNoGenerator edmiIntegrationTranNoGenerator(IntegrationProperties integrationProperties) {
        IntegrationSystemConfig integrationSystemConfig = integrationProperties.getSystem()
                .get(IntegrationConstant.SYSTEM_ID_EDMI);

        Map<String, Object> properties = integrationSystemConfig.properties();

        String baseDir = "";
        if (!CollectionUtils.isEmpty(properties)) {
            // 연계시스템 프로퍼티에 등록된 전문 거래번호 시퀀스 생성 파일 경로를 가져온다.
            baseDir = (String) integrationSystemConfig.properties().getOrDefault("tranno_sequence_file_path", "");
        }

        // base directory 가 없는경우 user home 으로 설정
        if (baseDir == null || baseDir.isEmpty()) {
            baseDir = System.getProperty("user.home") + File.separator + "sequence";
        }

        if (log.isDebugEnabled()) {
            log.debug("# EDMI 전문 거래번호 시퀀스 파일 경로: {}", baseDir);
        }

        return new IntegrationTranNoGenerator(
                new DailyFileSequenceGenerator(Path.of(baseDir)));
    }
    
	/**
	 * EDMI 요청 공통부 빌더 클래스 Bean
	 * @param integrationProperties
	 * @param edmiIntegrationTranNoGenerator
	 * @return
	 */
    @Bean
    EdmiRequestHeaderBuilder edmiRequestHeaderBuilder(
    		IntegrationTranNoGenerator edmiIntegrationTranNoGenerator) {
        return new EdmiRequestHeaderBuilder(edmiIntegrationTranNoGenerator);
    }

    /**
     * 호스트 시스템 응답 핸들러 클래스 Bean
     * 
     * @return
     */
    @Bean
    EdmiResponseHandler edmiResponseHandler() {
        return new EdmiResponseHandler();
    }

    /**
     * 호스트 시스템 매니저 클래스 Bean
     * 
     * @param integrationProperties    연계시스템 설정 Properties
     * @param hostRequestHeaderBuilder 요청 공통부 빌더
     * @param hostResponseHandler      응답 핸들러
     * @param hostReboundStrategy      연속거래 전략
     * @param gateway                  OpenFeign 게이트웨이
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(EdmiManager.class)
    EdmiManager edmiManager(
            IntegrationProperties integrationProperties,
            EdmiRequestHeaderBuilder edmiRequestHeaderBuilder,
            EdmiResponseHandler edmiResponseHandler,
            EDMIGatewayRegistry edmiGatewayRegistry) {
        IntegrationSystemConfig integrationSystemConfig = integrationProperties.getSystem()
                .get(IntegrationConstant.SYSTEM_ID_EDMI);
        return new EdmiManager(
                integrationSystemConfig,
                edmiRequestHeaderBuilder,
                edmiResponseHandler,
                edmiGatewayRegistry);
    }
}
