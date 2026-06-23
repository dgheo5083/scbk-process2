package com.scbank.process.api.fw.channel.service.metadata;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * <pre>
 * 서비스 정의 메타데이터 객체.
 * Dispatcher가 서비스 XML을 파싱한 결과를 이 구조로 담아 내부적으로 라우팅, 분기, 시간 제어, 인터셉터 적용 등에 활용합니다.
 * </pre>
 *
 * <p>
 * XML 파일: {@code service-definition.xml}
 * </p>
 *
 * @author sungdon.choi
 * @since 2025.04.16
 */
@Data
@Builder
public class ServiceDefinitionMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 서비스 ID (예: COMMON, AUTH, CMS 등)
     */
    private String serviceId;

    /**
     * 서비스 URL 엔드포인트 (예: /api/common/login)
     */
    private String url;

    /**
     * 서비스 설명
     */
    private String description;

    /**
     * 조건별 실행할 개별 서비스 목록
     */
    private List<ServiceInfo> services;

    /**
     * 전체 서비스에 공통 적용되는 접근제어 정보
     */
    private AccessControlInfo accessControl;

    /**
     * 전체 서비스에 공통 적용되는 서비스 시간 정보
     */
    private ServiceTimeInfo serviceTime;

    /**
     * 전체 서비스에 공통 적용되는 인터셉터 목록
     */
    private List<InterceptorInfo> interceptors;

    /**
     * 전체 서비스에 공통 적용되는 파라미터 목록
     */
    private List<ParameterInfo> parameters;

    /**
     * <pre>
     * 조건에 따라 실행할 단일 서비스의 정보
     * 예: 디바이스 유형, 사용자 구분, fallback, 시간 조건 등으로 분기 처리
     * </pre>
     */
    @Data
    @Builder
    public static class ServiceInfo implements Comparable<ServiceInfo> {

        /**
         * 서비스 설명
         */
        private String description;

        /**
         * 서비스 실행 조건 (예: SpEL 기반 조건식)
         */
        private String condition;

        /**
         * 우선순위 (낮을수록 먼저 평가됨, 기본 100)
         */
        private int priority;

        /**
         * 실제 실행할 서비스 컴포넌트 식별자 (클래스@메서드)
         */
        private String component;

        /**
         * fallback 처리 여부
         * <ul>
         * <li>true인 경우, 다른 조건 실패 시 fallback으로 사용</li>
         * <li>조건이 없고 fallback만 true면 디폴트 서비스로 작동</li>
         * </ul>
         */
        private boolean fallback;

        /**
         * fallback 참조 (다른 컴포넌트@메서드를 fallback으로 호출)
         */
        private String fallbackRef;

        /**
         * 개별 서비스에만 적용되는 접근 제어
         */
        private AccessControlInfo accessControl;

        /**
         * 개별 서비스에만 적용되는 서비스 시간 정보
         */
        private ServiceTimeInfo serviceTime;

        /**
         * 개별 서비스에만 적용되는 인터셉터
         */
        private List<InterceptorInfo> interceptors;

        @Override
        public int compareTo(ServiceInfo o) {
            return Integer.compare(priority, o.priority);
        }
    }

    /**
     * 접근 제어 정보 구조
     */
    public record AccessControlInfo(boolean requiredLogin, String[] allowedChannels) {
    }

    /**
     * 인터셉터 정의 정보
     *
     * @param id Spring Bean ID (또는 컴포넌트 이름)
     */
    public record InterceptorInfo(String id) {
    }

    /**
     * 서비스 시간 조건 정의
     *
     * @param enabled     시간 조건 활성화 여부
     * @param businessDay 영업일 시간 조건 (예: 0900~1800)
     * @param holiday     비영업일 시간 조건 (예: 1000~1600)
     */
    public record ServiceTimeInfo(boolean enabled, TimeRange businessDay, TimeRange holiday) {

        /**
         * 시간 범위 정의
         *
         * @param startTime 시작 시간 (HHmm 형식)
         * @param endTime   종료 시간 (HHmm 형식)
         */
        public record TimeRange(String startTime, String endTime) {
        }
    }

    /**
     * 서비스 파라미터
     */
    public record ParameterInfo(String parameterName, String parameterValue) {

    }
}
