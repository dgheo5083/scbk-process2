package com.scbank.process.api.fw.base.integration.system.oltp;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.base.gateway.edmi.MBOltpCommonRoute;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.oltp.rebound.OltpReboundStrategy;
import com.scbank.process.api.fw.base.integration.uuid.IntegrationTranNoGenerator;
import com.scbank.process.api.fw.core.uuid.sequence.DailyFileSequenceGenerator;
import com.scbank.process.api.fw.integration.IntegrationProperties;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * OLTP 시스템 spring configuration
 * 
 * @author sungdon.choi
 */
@Slf4j
@Configuration
public class OltpManagerConfiguration {

    /**
     * 전문 거래번호 생성기 Bean
     * 
     * @param integrationProperties 연계시스템 설정 Properties
     * @return
     */
    @Bean("oltpIntegrationTranNoGenerator")
    IntegrationTranNoGenerator oltpIntegrationTranNoGenerator(IntegrationProperties integrationProperties) {
        IntegrationSystemConfig integrationSystemConfig = integrationProperties.getSystem()
                .get(IntegrationConstant.SYSTEM_ID_HOST);

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
            log.debug("# 호스트 전문 거래번호 시퀀스 파일 경로: {}", baseDir);
        }

        return new IntegrationTranNoGenerator(
                new DailyFileSequenceGenerator(Path.of(baseDir)));
    }

    /**
     * 호스트 시스템 요청 공통부 빌더 클래스 Bean
     * @param oltpIntegrationTranNoGenerator
     * @param sessionContextManager
     * @return
     */
    @Bean
    @DependsOn({ "oltpIntegrationTranNoGenerator"})
    OltpRequestHeaderBuilder hostRequestHeaderBuilder(IntegrationTranNoGenerator oltpIntegrationTranNoGenerator) {
        return new OltpRequestHeaderBuilder(oltpIntegrationTranNoGenerator);
    }

    /**
     * 호스트 시스템 응답 핸들러 클래스 Bean
     * 
     * @return
     */
    @Bean
    OltpResponseHandler hostResponseHandler() {
        return new OltpResponseHandler();
    }

    /**
     * 호스트 시스템 연속거래 전략 클래스 Bean
     * 
     * @return
     */
    @Bean
    @DependsOn({ "runtimeContextInitializer" })
    OltpReboundStrategy hostReboundStrategy() {
        return new OltpReboundStrategy();
    }

    /**
     * 호스트 시스템 매니저 클래스 Bean
     * 
     * @param integrationProperties    연계시스템 설정 Properties
     * @param hostRequestHeaderBuilder 요청 공통부 빌더
     * @param hostResponseHandler      응답 핸들러
     * @param hostReboundStrategy      연속거래 전략
     * @param gateway                  호스트 게이트웨이
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OltpManager.class)
    OltpManager oltpManager(
            IntegrationProperties integrationProperties,
            OltpRequestHeaderBuilder hostRequestHeaderBuilder,
            OltpResponseHandler hostResponseHandler,
            OltpReboundStrategy hostReboundStrategy,
            MBOltpCommonRoute gateway) {
        IntegrationSystemConfig integrationSystemConfig = integrationProperties.getSystem()
                .get(IntegrationConstant.SYSTEM_ID_HOST);
        return new OltpManager(
                integrationSystemConfig,
                hostRequestHeaderBuilder,
                hostResponseHandler,
                hostReboundStrategy,
                gateway);
    }
}
