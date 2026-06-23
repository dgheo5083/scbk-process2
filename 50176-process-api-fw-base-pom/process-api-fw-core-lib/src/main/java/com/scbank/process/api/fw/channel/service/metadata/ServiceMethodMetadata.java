package com.scbank.process.api.fw.channel.service.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <pre>
 * 서비스 메서드의 구조 및 시그니처 정보를 담는 메타데이터 클래스입니다.
 * 이 클래스는 Dispatcher 또는 Executor에서 메서드 실행에 필요한
 * 클래스, 메서드, 파라미터, 리턴타입 정보를 제공합니다.
 * </pre>
 *
 * <p>
 * 예: IServiceComponentExecutor에서 메서드 실행 시 이 정보를 기반으로 reflection 호출
 * </p>
 *
 * @author Min-jun
 * @since 2025.04.25
 */
public class ServiceMethodMetadata {

    /** 해당 메서드가 선언된 클래스 */
    private final Class<?> declaringClass;

    /** 서비스 실행 대상 메서드 객체 (Java Reflection 기반) */
    private final Method method;

    /** 메서드 설명 또는 주석 (선택) */
    private final String description;

    /** 메서드 파라미터 목록에 대한 메타데이터 */
    private final List<ParameterMetadata> parameters;

    /** 리턴 타입 정보 */
    private final Class<?> returnType;

    /**
     * 전체 메타데이터 생성자
     *
     * @param declaringClass 메서드가 선언된 클래스
     * @param method         실행 대상 메서드 객체
     * @param description    메서드 설명
     * @param parameters     파라미터 메타데이터 리스트
     * @param returnType     리턴 타입 클래스
     */
    public ServiceMethodMetadata(
            Class<?> declaringClass,
            Method method,
            String description,
            List<ParameterMetadata> parameters,
            Class<?> returnType) {
        this.declaringClass = declaringClass;
        this.method = method;
        this.description = description;
        this.parameters = parameters;
        this.returnType = returnType;
    }

    /** @return 선언된 클래스 */
    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    /** @return 실행 대상 메서드 */
    public Method getMethod() {
        return method;
    }

    /** @return 메서드 설명 */
    public String getDescription() {
        return description;
    }

    /** @return 파라미터 메타데이터 목록 */
    public List<ParameterMetadata> getParameters() {
        return parameters;
    }

    /** @return 메서드 반환 타입 */
    public Class<?> getReturnType() {
        return returnType;
    }

    /** @return 메서드 이름 */
    public String getMethodName() {
        return method.getName();
    }

    /**
     * @return 컴포넌트 구분용 키 (클래스명@메서드명)
     */
    public String getComponentKey() {
        return declaringClass.getName() + "@" + method.getName();
    }

    /**
     * <pre>
     * 개별 메서드 파라미터의 정보를 담는 내부 클래스입니다.
     * 파라미터의 이름, 타입, 애노테이션, 그리고 메시지 바디 여부를 포함합니다.
     * </pre>
     */
    public static class ParameterMetadata {

        /** 파라미터 이름 */
        private final String name;

        /** 파라미터 타입 */
        private final Class<?> type;

        /** 파라미터에 적용된 애노테이션들 */
        private final Annotation[] annotations;

        /** 메시지 바디(input) 기반 파라미터 여부 */
        private final boolean body;

        /**
         * 파라미터 메타데이터 생성자
         *
         * @param name        파라미터 이름
         * @param type        파라미터 타입
         * @param annotations 파라미터에 선언된 애노테이션
         * @param body        메시지 바디 기반 여부
         */
        public ParameterMetadata(String name, Class<?> type, Annotation[] annotations, boolean body) {
            this.name = name;
            this.type = type;
            this.annotations = annotations;
            this.body = body;
        }

        /** @return 파라미터 이름 */
        public String getName() {
            return name;
        }

        /** @return 파라미터 타입 */
        public Class<?> getType() {
            return type;
        }

        /** @return 애노테이션 배열 */
        public Annotation[] getAnnotations() {
            return annotations;
        }

        /** @return true: 바디 기반 파라미터임 */
        public boolean isBody() {
            return body;
        }

        public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
            if (annotations == null || annotations.length == 0) {
                return false;
            }

            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(annotationType)) {
                    return true;
                }
            }

            return false;
        }

        @SuppressWarnings("unchecked")
        public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
            if (annotations == null || annotations.length == 0) {
                return null;
            }

            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(annotationType)) {
                    return (A) annotation;
                }
            }

            return null;
        }
    }
}
