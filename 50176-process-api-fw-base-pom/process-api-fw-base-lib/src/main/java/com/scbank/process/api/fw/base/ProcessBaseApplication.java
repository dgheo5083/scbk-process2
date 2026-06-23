package com.scbank.process.api.fw.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.scbank.process.api.fw.base.integration.system.edmi.EdmiManagerConfiguration;
import com.scbank.process.api.fw.base.integration.system.mci.MciManagerConfiguration;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpManagerConfiguration;
import com.scbank.process.api.fw.batch.BatchConfiguration;
import com.scbank.process.api.fw.channel.ChannelConfiguration;
import com.scbank.process.api.fw.common.CommonConfiguration;
import com.scbank.process.api.fw.core.cache.CacheConfiguration;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.dao.DaoConfiguration;
import com.scbank.process.api.fw.integration.IntegrationConfiguration;
import com.scbank.process.api.fw.message.MessageConfiguration;
import com.scbank.process.api.fw.security.SecurityConfiguration;
import com.scbank.process.api.fw.session.SessionConfiguration;
import com.scbank.process.api.fw.session.adapter.SessionAdapterConfiguration;

@EnableFeignClients(basePackages = { "com.scbank" })
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "com.scbank")
// @EnableWebMvc
@EnableCaching
@Import({
        CacheConfiguration.class,
        DaoConfiguration.class,
        MessageConfiguration.class,
        IntegrationConfiguration.class,
        ChannelConfiguration.class,
        CommonConfiguration.class,
        BatchConfiguration.class,
        SecurityConfiguration.class,
        SessionConfiguration.class,
        SessionAdapterConfiguration.class,

        // 연계시스템 매니저 설정
        OltpManagerConfiguration.class,
        MciManagerConfiguration.class,
        EdmiManagerConfiguration.class,
})
@SpringBootApplication(scanBasePackages = { "com.scbank" }, exclude = {
        DataSourceAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        QuartzAutoConfiguration.class,
        SessionAutoConfiguration.class
// ManagementWebSecurityAutoConfiguration.class
})
public abstract class ProcessBaseApplication extends SpringBootServletInitializer {

    private static final Map<String, Object> defaultProperties = new HashMap<>();

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(new Class[] { getClass() });
    }

    protected static void initBase(SpringApplicationBuilder builder, String applicationName) {
        if (StringUtils.isBlank(applicationName)) {
            throw new RuntimeException("Please specify a proper application name like experience-api-xxx.");
        }

        Map<String, Object> baseProperties = loadBaseProperties();

        defaultProperties.putAll(baseProperties);
        defaultProperties.put("spring.application.name", applicationName);

        setServiceNameInMDC(applicationName);
        builder.properties(defaultProperties);
    }

    /**
     * 
     * @param applicationName
     */
    private static void setServiceNameInMDC(String applicationName) {
        MDC.put("serviceName", applicationName);
    }

    /**
     * 
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    static Map<String, Object> loadBaseProperties() {
        try {
            YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
            List<PropertySource<?>> propertySource = loader.load("baseProperties",
                    (Resource) new ClassPathResource("/config/base-application.yml"));
            return (Map<String, Object>) ((PropertySource) propertySource.get(0)).getSource();
        } catch (IOException e) {
            throw new RuntimeException("Error loading base-application.yaml", e);
        }
    }
}
