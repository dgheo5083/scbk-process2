package com.scbank.process.api.svc.shared.config.nfilter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.scbank.process.api.svc.shared.config.nfilter.storage.IOpenWebNFilterSessionStorage;
import com.scbank.process.api.svc.shared.config.nfilter.storage.impl.DefaultOpenWebNFilterSessionStorage;
import com.scbank.process.api.svc.shared.config.nfilter.storage.impl.RedisOpenWebNFilterSessionStorage;

import net.nshc.nfilter.openweb.service.OpenWebNFilterInitializer;

/**
 * NSHC Open Web NFilter Configuration 클래스
 */
@Configuration
@ConditionalOnProperty(name = "nshc.open-web.enabled", havingValue = "true")
public class OpenWebNFilterServletConfig {

	@Value("${nshc.open-web.config.path}")
	private String nFilterOpenWebConfigPath;
	
	/**
	 * OpenWebNFilterInitializer 서블릿
	 * @return
	 */
	@Bean
    ServletRegistrationBean<OpenWebNFilterInitializer> openWebNFilterInitializer() {
        ServletRegistrationBean<OpenWebNFilterInitializer> registrationBean = new ServletRegistrationBean<>(new OpenWebNFilterInitializer());
        registrationBean.addUrlMappings("/OpenWebFilterInitializer");
        registrationBean.setLoadOnStartup(1);
        Map<String, String> params = new HashMap<String, String>();

        // 프러퍼티 경로 지정
        params.put("nFilterOpenWebConfigPath", nFilterOpenWebConfigPath);

        registrationBean.setInitParameters(params);

        return registrationBean;
    }
	
	/**
	 * OpenWebNFilterKeypadManager 서블릿
	 * @param nFilterSessionStorage
	 * @return
	 */
	@Bean
	public ServletRegistrationBean<ScBankOpenWebNFilterKeypadManager> redisKeypadManager(IOpenWebNFilterSessionStorage nFilterSessionStorage) {
		ServletRegistrationBean<ScBankOpenWebNFilterKeypadManager> keypadManagerBean = new ServletRegistrationBean<>(
				new ScBankOpenWebNFilterKeypadManager(nFilterSessionStorage));
		keypadManagerBean.addUrlMappings("/NFilterKeypadManager");

		return keypadManagerBean;
	}
	
	
	/**
	 * 인메모리 nfilter 세션데이터 저장소
	 * @return
	 */
	@Bean
	@Profile("!redis-session")
	DefaultOpenWebNFilterSessionStorage defaultOpenWebNFilterSessionStorage() {
		return new DefaultOpenWebNFilterSessionStorage();
	}
	
	/**
	 * redis session 사용 nfilter 세션데이터 저장소
	 * @param nfilterRedisTemplate
	 * @return
	 */
	@Bean
	@Profile("redis-session")
	RedisOpenWebNFilterSessionStorage nFilterSessionStorage(@Qualifier("nfilterRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
		return new RedisOpenWebNFilterSessionStorage(redisTemplate);
	}
	
	/**
	 * 
	 * @param connectionFactory
	 * @return
	 */
	@Bean
	@Profile("redis-session")
	RedisTemplate<String, Object> nfilterRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		
		StringRedisSerializer keySerializer = new StringRedisSerializer();
		JdkSerializationRedisSerializer valueSerializer = new JdkSerializationRedisSerializer();
		
		template.setKeySerializer(keySerializer);
		template.setHashKeySerializer(keySerializer);
		
		template.setValueSerializer(valueSerializer);
		template.setHashValueSerializer(valueSerializer);
		
		template.afterPropertiesSet();
		return template;
	}
}
