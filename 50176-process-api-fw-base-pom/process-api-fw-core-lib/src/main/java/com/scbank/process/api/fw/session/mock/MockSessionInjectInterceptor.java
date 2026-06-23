package com.scbank.process.api.fw.session.mock;

import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.scbank.process.api.fw.session.ISessionContextManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 로컬/개발환경에서 Mock 데이터를 이용한 강제 로그인 처리 인터셉터 컴포넌트
 * 활성화 및 로그인 정보는 mock.session.properties 파일에서 관리한다.
 */
@Slf4j
@RequiredArgsConstructor
public class MockSessionInjectInterceptor implements HandlerInterceptor {

    private final MockSessionProperties properties;

    private final ISessionContextManager sessionContextManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 활성화 여부 체크
        if (!this.properties.isEnabled()) {
            return true;
        }

        log.debug("# Mock 세션 처리 시작");

        try {
            // 로그인 세션 처리
            if (this.properties.getLogin().isEnabled()) {
                String userId = this.properties.getLogin().getUserId();

                this.sessionContextManager.login(userId);
                // 로그인 세션 처리
                Map<String, Object> loginSessionMap = this.properties.getLoginSession();
                if (!CollectionUtils.isEmpty(loginSessionMap)) {
                    loginSessionMap.forEach((k, v) -> {
                        this.sessionContextManager.setLoginValue(k, v);
                    });
                }
            }

            // 글로벌 세션 처리
            Map<String, Object> globalSessionMap = this.properties.getGlobalSession();
            if (!CollectionUtils.isEmpty(globalSessionMap)) {
                globalSessionMap.forEach((k, v) -> {
                    this.sessionContextManager.setGlobalValue(k, v);
                });
            }
        } catch (Exception ignored) {
            log.error(ignored.getMessage(), ignored);
        }

        return true;
    }
}
