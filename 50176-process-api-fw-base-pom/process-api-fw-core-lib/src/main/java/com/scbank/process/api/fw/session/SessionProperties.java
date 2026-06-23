package com.scbank.process.api.fw.session;

import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 프레임워크 공통 세션 키 관리용 프로퍼티 클래스
 * <p>
 * application.yml 또는 application.properties에 정의된
 * {@code framework.session} 관련 설정 값을 자동 바인딩하여 관리합니다.
 * </p>
 *
 * <pre>
 * 예시:
 * framework:
 *   session:
 *     enabled: true
 *     allowed-keys:
 *       GLOBAL:
 *         - traceId
 *         - channelId
 *       LOGIN:
 *         - userId
 *         - userRole
 * </pre>
 *
 * @see SessionScope
 * @see ISessionKeyValidator
 * 
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
@Data
@Component
@ConfigurationProperties(prefix = "csl.session")
public class SessionProperties {

    /**
     * 세션 기능 사용 여부 설정
     */
    private boolean enabled;

    /**
     * 허용된 세션 키 목록
     * <p>
     * 세션 범위별 허용된 세션 키들을 정의함 (예: LOGIN, GLOBAL).
     * 키는 {@link SessionScope} enum 기준으로 분류됩니다.
     * </p>
     */
    private Map<SessionScope, List<String>> allowedKeys = Map.of();
}
