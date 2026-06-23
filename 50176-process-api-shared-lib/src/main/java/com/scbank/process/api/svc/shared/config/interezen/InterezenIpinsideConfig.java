package com.scbank.process.api.svc.shared.config.interezen;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.interezen.sender.SenderInitializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "ipinside.enabled", havingValue = "true")
public class InterezenIpinsideConfig {

	@Value("${ipinside.config.path}")
	private String configPath;
	
	/**
	 * SenderInitializer
	 * @return
	 */
    @Bean
    ServletRegistrationBean<SenderInitializer> senderInitializer() {
    	
    	log.debug("# SenderInitializer Bean");
    	
        ServletRegistrationBean<SenderInitializer> senderInitializerBean = new ServletRegistrationBean<>(new SenderInitializer());
        senderInitializerBean.addUrlMappings("/SenderInitializer");
        senderInitializerBean.setLoadOnStartup(1);
        
        Map<String, String> params = new HashMap<String, String>();
		params.put("SenderPropertiesPath", configPath);
		
		senderInitializerBean.setInitParameters(params);        
        return senderInitializerBean;
    }
}
