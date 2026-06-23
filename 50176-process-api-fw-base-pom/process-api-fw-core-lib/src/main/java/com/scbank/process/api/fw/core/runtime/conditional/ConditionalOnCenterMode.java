package com.scbank.process.api.fw.core.runtime.conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Conditional;

/**
 * 센트모드 Conditional
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 17.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Conditional(CenterModeCondition.class)
public @interface ConditionalOnCenterMode {

    /**
     * 
     * @return
     */
    String[] value();
}