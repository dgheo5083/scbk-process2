package com.scbank.process.api.fw.channel.interceptor;

import java.util.List;

import org.springframework.util.AntPathMatcher;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 프레임워크 채널 구조에서 요청 필터링을 위한 기본 {@link IRequestInterceptor} 추상 클래스입니다.
 * 
 * URL 패턴 기반으로 인터셉터 적용 여부를 판단하는 기본 로직을 제공합니다.
 * {@code
 * urlPatterns
 * }에 명시된 경로와 {@code
 * HttpServletRequest
 * }의 URI가 매칭될 경우에만 {@code
 * isSupported()
 * }가 true를 반환합니다.
 * </pre>
 *
 * <p>
 * 주요 특징:
 * <ul>
 * <li>{@link IRequestInterceptor} 인터페이스 구현</li>
 * <li>urlPatterns 기반 지원 여부 판단 로직 내장</li>
 * <li>{@link AntPathMatcher} 활용한 URI 매칭</li>
 * <li>인터셉터 실행 순서를 제어할 수 있는 {@code index} 필드 보유</li>
 * </ul>
 *
 * <p>
 * 상속 예시:
 * 
 * <pre>{@code
 * @Component
 * public class AuthCheckInterceptor extends AbstractRequestInterceptor {
 *     public AuthCheckInterceptor() {
 *         super(10, List.of("/api/**"));
 *     }
 * 
 *     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
 *         // 실제 인증 로직
 *     }
 * }
 * }</pre>
 * 
 * @author sungdon.choi
 */
public abstract class AbstractRequestInterceptor implements IRequestInterceptor {

    @Getter
    @Setter
    private int index;

    @Getter
    @Setter
    private List<String> urlPatterns;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean isSupported(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        if (urlPatterns == null || urlPatterns.isEmpty()) {
            return false;
        }

        return urlPatterns.stream()
                .anyMatch(urlPattern -> pathMatcher.match(urlPattern, requestUri));
    }
}
