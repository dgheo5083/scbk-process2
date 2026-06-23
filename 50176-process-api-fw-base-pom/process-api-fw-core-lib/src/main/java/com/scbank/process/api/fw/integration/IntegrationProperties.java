package com.scbank.process.api.fw.integration;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.core.enums.CenterMode;
import com.scbank.process.api.fw.message.MessageFormatOptionConfig;
import com.scbank.process.api.fw.message.enums.MessageFormat;

import lombok.Data;

/**
 * <pre>
 * 연동 시스템 구성 정보를 담는 설정 클래스입니다.
 * application.yml의 'csl.integration' 하위 속성과 바인딩됩니다.
 *
 * 시스템별 통신 프로토콜, 포맷, 타임아웃, 재시도 정책 등
 * 다양한 연동 관련 설정을 관리하며, 설정은 서비스 별로 분리하여 등록 가능합니다.
 *
 * 예시 설정 구조:
 * csl:
 *   integration:
 *     enabled: true
 *     system:
 *       MCI:
 *         format: FIXEDLENGTH
 *         charset: UTF-8
 * </pre>
 *
 * @author sungdon.choi
 * @since 2025.04
 */
@Component
@Data
@ConfigurationProperties(prefix = "csl.integration")
public class IntegrationProperties {

    /** 연동 프레임워크 기능 활성화 여부 */
    private boolean enabled;

    /** 연계 대상 시스템별 설정 정보 */
    private Map<String, IntegrationSystemConfig> system;

    /**
     * 연동 대상 시스템 1건에 대한 설정 정보
     *
     * @param format                메시지 포맷 (JSON/XML/FIXEDLENGTH 등)
     * @param charset               문자 인코딩 방식 (예: UTF-8, EUC-KR)
     * @param interceptors          적용할 인터셉터 목록 (Bean 이름 또는 ID)
     * @param properties            시스템별 커스텀 속성
     * @param defaultHeaders        시스템별 기본 송신 헤더
     * @param messageFormatOptions  메시지 포맷별 확장 설정
     * @param reboundStrategyPolicy 리바운드 거래 전략 정책
     * @param simulation
     */
    public record IntegrationSystemConfig(
    		boolean enabled,
            MessageFormat format,
            String charset,
            List<Endpoint> targets,
            List<String> interceptors,
            Map<String, Object> properties,
            Map<String, Object> defaultHeaders,
            MessageFormatOptionConfig messageFormatOptions,
            ReboundStrategyPolicy reboundStrategyPolicy,
            SimulationConfig simulation,
            SocketOption socketOption) {
    }

    /**
     * 
     */
    public record ReboundStrategyPolicy(int maxLoopCnt, String defaultListFieldName) {

    }

    /**
     * 연계시스템별 시뮬레이션 응답 처리
     */
    public record SimulationConfig(boolean enabled, String configLocation) {

        public SimulationConfig {

        }
    }

    /**
     * 연동 타겟 시스템의 접속 정보
     *
     * @param center 센터 구분 (예: MAIN, DR)
     * @param ip     IP 주소
     * @param port   포트 번호
     * @param url    HTTP 요청 URL (HTTP 프로토콜 사용 시)
     */
    public record Endpoint(CenterMode center, String ip, int port, String url) {
    }

    /**
     * TCP 소켓 연결 및 읽기 타임아웃 설정
     *
     * @param connectTimeout 연결 시도 제한 시간 (ms)
     * @param readTimeout    응답 수신 제한 시간 (ms)
     */
    public record SocketOption(long connectTimeout, long readTimeout) {
    }
}
