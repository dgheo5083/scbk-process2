package com.scbank.process.api.svc.shared.config.nfilter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nshc.service.NFilterInitializer;

/**
 * NSHC 네이티브 보안키패드 대응 NFilter Configuration 클래스
 */
 @Configuration
 @ConditionalOnProperty(name = "nshc.nfilter.enabled", havingValue = "true")
public class NFilterServletConfig {
	
	@Value("${nshc.nfilter.config.path}")
	private String nFilterConfigPath;
    
	/**
	 * NFilterIntializer
	 * @return
	 */
    @Bean
    ServletRegistrationBean<NFilterInitializer> nativeKeypadManager() {
        ServletRegistrationBean<NFilterInitializer> nativeKeypadBean = new ServletRegistrationBean<>(new NFilterInitializer());
        nativeKeypadBean.addUrlMappings("/NativeNFilterInitializer");
        nativeKeypadBean.setLoadOnStartup(1);
        
        Map<String, String> params = new HashMap<String, String>();
		params.put("nFilterConfigPath", nFilterConfigPath);
		
		nativeKeypadBean.setInitParameters(params);        
        return nativeKeypadBean;
    }
}
