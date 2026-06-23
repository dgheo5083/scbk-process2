package com.scbank.process.api.fw.core.runtime.conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * 운영모드 Conditinal
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 17.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(RunModeCondition.class)
public @interface ConditionalOnRunMode {

    String[] value();
}