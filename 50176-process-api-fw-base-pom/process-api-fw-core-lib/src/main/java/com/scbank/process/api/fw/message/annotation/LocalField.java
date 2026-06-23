package com.scbank.process.api.fw.message.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * DTO에 필드를 정의하지만 전문필드 대상이 아닌필드에 대한 어노테이션 정의가 필요할때 사용
 * 
 * @author sungdon.choi
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LocalField {

	String value() default "";
}
