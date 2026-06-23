package com.scbank.process.api.fw.message.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 연동 메시지 클래스에 적용되는 마커 어노테이션입니다.
 * 
 * <p>
 * 이 어노테이션은 통합 메시지 구조(예: 전문)의 식별자와 타입 정보를 부여하며,
 * 메시지 변환기, 메시지 직렬화/역직렬화, 문서 생성 등의 처리 흐름에서
 * 메타데이터로 사용됩니다.
 * </p>
 * 
 * <p>
 * {@code @IntegrationMessage}는 클래스 수준에만 적용되며, 런타임에 반영됩니다.
 * </p>
 * 
 * 예시:
 * 
 * <pre>
 * {@code
 * &#64;IntegrationMessage(id = "IF001", type = IntegrationMessage.Type.REQUEST)
 * public class SomeRequestDto implements IMessageObject {
 *     ...
 * }
 * }
 * </pre>
 * 
 * @see co.kr.scbank.framework.message.IMessageObject
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegrationMessage {

    /**
     * 메시지의 인터페이스 ID를 지정합니다.
     * 
     * <p>
     * 이 ID는 시스템 간 연동 시 식별자로 사용됩니다.
     * </p>
     *
     * @return 인터페이스 ID
     */
    String id();

    /**
     * 메시지 타입을 지정합니다. (요청, 응답, 헤더 등)
     *
     * @return 메시지 타입
     */
    Type type() default Type.NONE;

    /**
     * 메시지 설명입니다. 주석이나 문서화 목적으로 사용됩니다.
     *
     * @return 설명 텍스트
     */
    String description() default "";

    /**
     * 
     * @return
     */
    boolean xmlRootNameWrap() default true;

    /**
     * XML 기반 메시지의 루트 엘리먼트 이름을 지정합니다.
     * 
     * <p>
     * XML 메시지 변환 시 루트 태그 이름으로 사용됩니다.
     * </p>
     *
     * @return XML 루트 이름
     */
    String xmlRootName() default "";

    /**
     * EDMI 캡쳐시스템
     * 
     * @return
     */
    String captureSystem() default "";
    
    /**
     * EDMI TypeName
     * 
     * @return
     */
    String typeName() default "";
    
    /**
     * EDMI senderBody
     * 
     * @return
     */
    String messageSenderBody() default "";
    
    /**
     * 
     * @return
     */
    String senderDomainBody() default "";
    

    /**
     * 연동 메시지 타입을 정의하는 열거형입니다.
     */
    enum Type {
        /** 명시되지 않음 */
        NONE,

        /** 전문 헤더 메시지 */
        HEADER,

        /** 요청 메시지 */
        REQUEST,

        /** 응답 메시지 */
        RESPONSE
    }
}
