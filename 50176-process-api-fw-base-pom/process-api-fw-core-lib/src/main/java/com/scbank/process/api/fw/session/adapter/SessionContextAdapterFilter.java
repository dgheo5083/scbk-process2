package com.scbank.process.api.fw.session.adapter;

import java.io.IOException;
import java.util.Enumeration;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * SessionContextFilter
 *
 * - 모든 요청에 대해 HttpServletRequest를 래핑하여,
 * getSession() 호출 시 커스텀 HttpSessionWrapper를 반환한다.
 * - HttpSessionWrapper는 세션 속성 set/get 시 ISessionAdapterProvider를 통해
 * 값에 대해 wrap/unwrap(메타 삽입/복원 등)을 수행한다.
 */
@RequiredArgsConstructor
public class SessionContextAdapterFilter extends OncePerRequestFilter {

    /** 세션 값 변환(wrap/unwrap)을 제공하는 어댑터 공급자 */
    private final ISessionAdapterProvider sessionAdapterProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        SessionContextAdapterRequestWrapper requestWrapper = new SessionContextAdapterRequestWrapper(request,
                sessionAdapterProvider);
        try {
            filterChain.doFilter(requestWrapper, response);
        } finally {

        }
    }

    public static class SessionContextAdapterRequestWrapper extends HttpServletRequestWrapper {

        private final ISessionAdapterProvider sessionAdapterProvider;

        private SessionContextAdapterRequestWrapper(HttpServletRequest request,
                ISessionAdapterProvider sessionAdapterProvider) {
            super(request);
            this.sessionAdapterProvider = sessionAdapterProvider;
        }

        @Override
        public HttpSession getSession(boolean create) {
            HttpSession session = super.getSession(create);
            return session != null ? new HttpSessionWrapper(session, sessionAdapterProvider) : null;
        }

		@Override
		public HttpSession getSession() {
			return getSession(true);
		}
    }

    /**
     * HttpSessionWrapper
     *
     * - 실제 HttpSession(delegate)을 보유하고, 속성 접근 시 어댑터를 거쳐 변환한다.
     * - setAttribute: 저장 전에 wrap
     * - getAttribute: 조회 후에 unwrap
     * - 그 외 세션 메타 정보 메서드는 delegate로 위임.
     */
    @RequiredArgsConstructor
    static class HttpSessionWrapper implements jakarta.servlet.http.HttpSession {

        /** 실제 HttpSession 인스턴스 (위임 대상) */
        private final HttpSession delegate;

        /** 세션 어댑터 공급자 (wrap/unwrap 처리 담당) */
        private final ISessionAdapterProvider sessionAdapterProvider;

        
        /**
         * setAttribute
         * - 세션에 값을 저장할 때, 어댑터가 제공되면 wrap(name, value)을 적용해
         * 메타데이터 삽입/타입 호환 포맷 변환 등을 수행한 후 저장한다.
         */
        @Override
        public void setAttribute(String name, Object value) {
            Object wrappedValue = sessionAdapterProvider != null ? sessionAdapterProvider.wrap(name, value) : value;
            if (delegate != null) {
                delegate.setAttribute(name, wrappedValue);
            }
        }

        /**
         * getAttribute
         * - 세션에서 값을 조회할 때, 어댑터가 제공되면 unwrap(name, raw)을 적용해
         * 저장 시 삽입된 메타를 제거하거나 v3/v4 타입 복원 등의 역변환을 수행한 후 반환한다.
         */
        @Override
        public Object getAttribute(String name) {
            if (delegate == null) {
            	return null;
            }
            Object raw = delegate.getAttribute(name);
            return sessionAdapterProvider != null ? sessionAdapterProvider.unwrap(name, raw) : raw;
        }

        // ====== 이하 일반적인 세션 위임 메서드들 ======

        @Override
        public long getCreationTime() {
            return this.delegate.getCreationTime();
        }

        @Override
        public String getId() {
            return delegate != null ? delegate.getId() : null;
        }

        @Override
        public long getLastAccessedTime() {
            return delegate.getLastAccessedTime();
        }

        @Override
        public ServletContext getServletContext() {
            return this.delegate.getServletContext();
        }

        @Override
        public void setMaxInactiveInterval(int interval) {
            this.delegate.setMaxInactiveInterval(interval);
        }

        @Override
        public int getMaxInactiveInterval() {
            return delegate.getMaxInactiveInterval();
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return delegate.getAttributeNames();
        }

        @Override
        public void removeAttribute(String name) {
            delegate.removeAttribute(name);
        }

        @Override
        public void invalidate() {
            delegate.invalidate();
        }

        @Override
        public boolean isNew() {
            return delegate.isNew();
        }
    }
}
