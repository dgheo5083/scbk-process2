package com.scbank.process.api.fw.base.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Profile("session")
@Configuration
@EnableSpringHttpSession
public class HttpSessionConfig {

	private static final String COOKIE_SESSION_ID_NAME = "SESSIONID";
	
    @Bean
    public MapSessionRepository sessionRepository() {
        return new MapSessionRepository(new ConcurrentHashMap<>());
    }

//    @Bean
//    HttpSessionIdResolver httpSessionIdResolver() {
//        return HeaderHttpSessionIdResolver.xAuthToken();
//    }
    
    @Bean
    CookieSerializer cookieSerializer () {
    	DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
    	cookieSerializer.setCookieName(COOKIE_SESSION_ID_NAME);
    	cookieSerializer.setCookiePath("/");
    	return cookieSerializer;
    }
}
