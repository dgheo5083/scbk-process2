package com.scbank.process.api.fw.base.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.boot.web.servlet.server.Session.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.adapter.ISessionAdapterProvider;

import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.extern.slf4j.Slf4j;

/**
 * 프로세스API Redis HttpSession Configuration
 * 
 * @author sungdon.choi
 */
@Slf4j
@Profile("redis-session")
@Configuration
@EnableConfigurationProperties({ SessionProperties.class, RedisSessionProperties.class, RedisProperties.class })
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 600)
public class RedisHttpSessionConfig {
	
	private static final String COOKIE_SESSION_ID_NAME = "SESSIONID";

	@Bean
	LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
		return builder -> {
			ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
					.enableAllAdaptiveRefreshTriggers()
					.enablePeriodicRefresh(Duration.ofSeconds(30))
					.build();
			
			ClusterClientOptions clientOptions = ClusterClientOptions.builder()
					.topologyRefreshOptions(topologyRefreshOptions)
					.validateClusterNodeMembership(false)
					.build();
			
			builder
				.useSsl()
				.disablePeerVerification()
				.and()
				.commandTimeout(Duration.ofSeconds(5))
				.clientOptions(clientOptions)
				.build();
		};
	}
//	
//    @Bean
//    LettuceConnectionFactory redisConnectionFactory(RedisStandaloneConfiguration conf, LettuceClientConfiguration clientConfiguration) {
//    	LettuceConnectionFactory factory = new LettuceConnectionFactory(conf, clientConfiguration);
//    	factory.setShareNativeConnection(false);
//    	return factory;
//    }
	
	@Bean
	ClientResources clientResources() {
		return DefaultClientResources.builder().build();
	}

    @Bean
    SessionRepositoryCustomizer<RedisIndexedSessionRepository> sessionRepositoryCustomizer(
            SessionProperties sessionProperties,
            RedisSessionProperties redisSessionProperties) {
        return (repo) -> {
            if (sessionProperties.getTimeout() != null) {
                repo.setDefaultMaxInactiveInterval(sessionProperties.getTimeout());
            }

            if (redisSessionProperties.getFlushMode() != null) {
                repo.setFlushMode(redisSessionProperties.getFlushMode());
            }

            if (redisSessionProperties.getCleanupCron() != null) {
                repo.setCleanupCron(redisSessionProperties.getCleanupCron());
            }
        };
    }

//    @Bean
//    HttpSessionIdResolver httpSessionIdResolver() {
//        return HeaderHttpSessionIdResolver.xAuthToken();
//    }
    
    @Bean
    CookieSerializer cookieSerializer (ServerProperties properties) {
    	Cookie cookie = properties.getServlet().getSession().getCookie();
    	
    	String cookieName = cookie.getName();
        if (StringUtils.isEmpty(cookieName)) {
            cookieName = COOKIE_SESSION_ID_NAME;
        }
        
        String domainName = cookie.getDomain();
        String path = cookie.getPath();
        if (StringUtils.isEmpty(path)) {
        	path = "/";
        }
        
        Boolean httpOnly = cookie.getHttpOnly();
        if (httpOnly == null) {
        	httpOnly = Boolean.FALSE;
        }
        
        Boolean secure = cookie.getSecure();
        if (secure == null) {
        	secure = Boolean.FALSE;
        }
        
        Duration maxAge = cookie.getMaxAge();
        if (maxAge == null) {
        	maxAge = Duration.ofSeconds(3600);
        }
        
        SameSite sameSite = cookie.getSameSite();
        if (sameSite == null) {
        	sameSite = SameSite.LAX;
        }
    	
    	DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
    	cookieSerializer.setCookieName(cookieName);
    	cookieSerializer.setDomainName(domainName);
    	cookieSerializer.setCookiePath(path);
    	cookieSerializer.setUseHttpOnlyCookie(httpOnly);
    	cookieSerializer.setUseSecureCookie(secure);
    	cookieSerializer.setCookieMaxAge((int)maxAge.getSeconds());
    	cookieSerializer.setSameSite(sameSite.attributeValue());
    	cookieSerializer.setUseBase64Encoding(false);
    	return cookieSerializer;
    }

    @Bean
    ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        //TODO 확인필요
		try {
			Class<?> clazz = Class.forName("net.nshc.nfilter.openweb.keypad.keymap.KeyMapSession");
			objectMapper.addMixIn(clazz, KeyMapSessionMixIn.class);
		} catch (ClassNotFoundException e) {
			log.warn("KeyMapSession class not found - MixIn not applied");
		}
        
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY);
        return objectMapper;
    }

    @Bean
    RedisSerializer<Object> springSessionDefaultRedisSerializer(
            ISessionAdapterProvider sessionAdapterProvider,
            ObjectMapper redisObjectMapper) {
        return new GenericJackson2JsonRedisSerializer(redisObjectMapper);
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory,
            ObjectMapper redisObjectMapper) {
        RedisSerializer<Object> redisSerializer = new GenericJackson2JsonRedisSerializer(redisObjectMapper);
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setDefaultSerializer(redisSerializer);
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);

        template.setDefaultSerializer(redisSerializer);
        template.afterPropertiesSet();
        return template;
    }
    
    @JsonIgnoreProperties({"secretKeyCodeSet"})
    abstract class KeyMapSessionMixIn {
    	
    }
}
