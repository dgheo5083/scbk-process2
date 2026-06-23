package com.scbank.process.api.fw.core.component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * 배치 컴포넌트
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface BatchComponent {

    /**
     * 배치 컴포넌트 ID
     * 
     * @return
     */
    String id();

    /**
     * 배치 컴포넌트 설명
     * 
     * @return
     */
    String description() default "";

    /**
     * 배치 컴포넌트 작성자
     * 
     * @return
     */
    String author() default "";
}
