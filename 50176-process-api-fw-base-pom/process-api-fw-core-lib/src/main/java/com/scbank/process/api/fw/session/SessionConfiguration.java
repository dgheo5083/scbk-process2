package com.scbank.process.api.fw.session;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.scbank.process.api.fw.core.runtime.conditional.ConditionalOnRunMode;
import com.scbank.process.api.fw.session.impl.DefaultSessionContextManager;
import com.scbank.process.api.fw.session.impl.DefaultSessionKeyValidator;

import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 세션 관련 Spring Bean 구성 클래스
 * <p>
 * 세션 키 검증기, 세션 컨텍스트 매니저, 개발용 세션 이벤트 리스너를 등록합니다.
 * </p>
 *
 * <ul>
 * <li>{@link ISessionKeyValidator} – 세션 키 화이트리스트 유효성 검사</li>
 * <li>{@link ISessionContextManager} – 세션 컨텍스트 CRUD 처리기</li>
 * <li>{@link HttpSessionListener}, {@link HttpSessionAttributeListener} – 세션
 * 로깅용 (local 모드 전용)</li>
 * </ul>
 *
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
@Slf4j
@Configuration
public class SessionConfiguration {

    /**
     * 세션 키 유효성 검사기 등록
     * <p>
     * 설정 파일 기반 허용 세션 키 리스트를 검증하는 기본 구현체를 등록합니다.
     * </p>
     *
     * @param properties 세션 설정 정보
     * @return {@link ISessionKeyValidator} 구현체
     */
    @Bean
    @ConditionalOnMissingBean(ISessionKeyValidator.class)
    ISessionKeyValidator sessionKeyValidator(SessionProperties properties) {
        return new DefaultSessionKeyValidator(properties);
    }

    /**
     * 세션 매니저 등록 (요청 범위)
     * <p>
     * HTTP 요청 단위로 별도 세션 매니저 인스턴스를 생성합니다.
     * Spring의 Request Scope를 사용하며, 프록시 모드를 통해 의존성 주입됩니다.
     * </p>
     *
     * @param session 현재 요청의 HttpSession
     * @return {@link ISessionContextManager} 구현체
     */
    @Bean(ISessionContextManager.BEAN_ID)
    @ConditionalOnMissingBean(ISessionContextManager.class)
    ISessionContextManager sessionManager() {
        return new DefaultSessionContextManager();
    }

    /**
     * HttpSession 생성/소멸 로그 출력용 리스너 (로컬 모드 전용)
     *
     * @return {@link HttpSessionListener} 구현체
     */
    @Bean
    @ConditionalOnRunMode({ "local" })
    HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                log.debug("# sessionCreated id: {}", se.getSession().getId());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                log.debug("# sessionDestroyed id: {}", se.getSession().getId());
            }
        };
    }

    /**
     * HttpSession 속성 변경 로그 출력용 리스너 (로컬 모드 전용)
     *
     * @return {@link HttpSessionAttributeListener} 구현체
     */
    @Bean
    @ConditionalOnRunMode({ "local" })
    HttpSessionAttributeListener httpSessionAttributeListener() {
        return new HttpSessionAttributeListener() {
            @Override
            public void attributeAdded(HttpSessionBindingEvent event) {
                log.trace("# attributeAdded id: {}, name: {}, value: {}",
                        event.getSession().getId(), event.getName(), event.getValue());
            }

            @Override
            public void attributeRemoved(HttpSessionBindingEvent event) {
                log.trace("# attributeRemoved id: {}, name: {}, value: {}",
                        event.getSession().getId(), event.getName(), event.getValue());
            }

            @Override
            public void attributeReplaced(HttpSessionBindingEvent event) {
                log.trace("# attributeReplaced id: {}, name: {}, value: {}",
                        event.getSession().getId(), event.getName(), event.getValue());
            }
        };
    }
}
