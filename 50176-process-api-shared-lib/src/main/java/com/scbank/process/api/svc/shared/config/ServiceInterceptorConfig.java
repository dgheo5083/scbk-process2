package com.scbank.process.api.svc.shared.config;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.scbank.process.api.fw.channel.constants.ChannelConstants;
import com.scbank.process.api.fw.channel.service.ServiceEndpointRequests;
import com.scbank.process.api.fw.common.holiday.IHolidayManager;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.fw.session.mock.MockSessionInjectInterceptor;
import com.scbank.process.api.svc.shared.channel.interceptors.LoginDuplicateCheckInterceptor;
import com.scbank.process.api.svc.shared.channel.interceptors.ServiceAuthCheckInterceptor;
import com.scbank.process.api.svc.shared.channel.interceptors.ServiceEndpointTimeCheckInterceptor;
import com.scbank.process.api.svc.shared.components.accesscontrol.MenuAuthorityCheckComponent;
import com.scbank.process.api.svc.shared.dao.LoginSsoHisDao;

import lombok.extern.slf4j.Slf4j;

/**
 * 서비스 인터셉터 설정
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = ChannelConstants.FRAMEWORK_CHANNEL_ENABLED, havingValue = "true")
public class ServiceInterceptorConfig {

	/**
	 * 
	 * @param accessController 메뉴 권한 체크 컴포넌트 {@link MenuAuthorityCheckComponent}
	 * @return
	 */
	@Bean
    @Order(1)
    ServiceAuthCheckInterceptor serviceAuthCheckInterceptor(MenuAuthorityCheckComponent accessController) {
        return new ServiceAuthCheckInterceptor(accessController);
    }

    /**
     * 서비스 Endpoint 시간 체크 인터셉터 빈 등록
     * 
     * @return
     */
    @Bean
    @Order(2)
    ServiceEndpointTimeCheckInterceptor serviceTimeCheckInterceptor(IHolidayManager holidayManager) {
        return new ServiceEndpointTimeCheckInterceptor(holidayManager);
    }

    /**
     * 중복 로그인 체크 인터셉터 빈 등록
     * 
     * @return
     */
    @Bean
    @Order(3)
    LoginDuplicateCheckInterceptor loginDuplicateCheckInterceptor(ISessionContextManager sessionContextManager, LoginSsoHisDao loginSsoHisDao) {
        return new LoginDuplicateCheckInterceptor(sessionContextManager, loginSsoHisDao);
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE + 10)
    @DependsOn({ "serviceRequestInterceptor" })
    WebMvcConfigurer cslWebInterceptorConfigurer(
            ServiceEndpointRequests serviceEndpointRequests,
            Optional<MockSessionInjectInterceptor> mockSessionInjectInterceptor,
            ServiceEndpointTimeCheckInterceptor serviceTimeCheckInterceptor,
            ServiceAuthCheckInterceptor serviceAuthCheckInterceptor,
            LoginDuplicateCheckInterceptor loginDuplicateCheckInterceptor) {

        final List<String> urlPatterns = serviceEndpointRequests.enabledServiceEndpointUrls();
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(@NonNull InterceptorRegistry registry) {
                log.debug("# 공통 서비스 인터셉트 등록 시작");

                // Mock 세션 인잭션 인터셉터
                mockSessionInjectInterceptor
                        .ifPresent(interceptor -> registry.addInterceptor(interceptor).addPathPatterns(urlPatterns));
                // 서비스 권한 체크
                registry.addInterceptor(serviceAuthCheckInterceptor).addPathPatterns(urlPatterns);
                // 서비스 endpoint 시간 체크
                registry.addInterceptor(serviceTimeCheckInterceptor).addPathPatterns(urlPatterns);
                // 중복로그인 체크
                registry.addInterceptor(loginDuplicateCheckInterceptor).addPathPatterns(urlPatterns);
            }
        };
    }
}
