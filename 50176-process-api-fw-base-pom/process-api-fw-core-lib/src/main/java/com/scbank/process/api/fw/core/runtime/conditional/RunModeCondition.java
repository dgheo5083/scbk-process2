package com.scbank.process.api.fw.core.runtime.conditional;

import java.util.Arrays;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

import lombok.extern.slf4j.Slf4j;

/**
 * 운영모드에 따른 빈 등록 Condition 클래스
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 17.
 */
@Slf4j
public class RunModeCondition implements Condition {

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        String[] expectedValues = (String[]) metadata.getAnnotationAttributes(ConditionalOnRunMode.class.getName())
                .get("value");

        if (log.isDebugEnabled()) {
            log.debug("# RunModeCondition expectedValues: {}", Arrays.toString(expectedValues));
        }

        String currentMode = context.getEnvironment().getProperty("csl.runtime.run-mode");
        return Arrays.stream(expectedValues).anyMatch(expected -> {
            if (expected.startsWith("!")) {
                String notValue = expected.substring(1);
                return !notValue.equalsIgnoreCase(currentMode);
            } else {
                return expected.equalsIgnoreCase(currentMode);
            }
        });
    }
}
