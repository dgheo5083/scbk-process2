package com.scbank.process.api.fw.core.validation.attribute;

import jakarta.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * packageName    : co.kr.framework.core.validation.attribute
 * fileName       : DefaultValidationAttributeExtractorRegistry
 * author         : gasigol
 * date           : 25. 4. 11.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 4. 11.        gasigol       최초 생성
 * </pre>
 */
public class DefaultValidationAttributeExtractorRegistry implements IValidationAttributeExtractorRegistry {

    private final Map<String, IValidationAttributeExtractor> registry = new HashMap<>();

    private final IValidationAttributeExtractor Empty = (violation -> new Object[0]);

    public DefaultValidationAttributeExtractorRegistry() {
        // JSR-380 기본 제공 어노테이션 등록 (속성이 있는것만 등록 한다.)
        // size
        registry.put("Size", (violation) -> {
            Map<String, Object> attr = violation.getConstraintDescriptor().getAttributes();
            return new Object[] { attr.get("min"), attr.get("max") };
        });
        // Min
        registry.put("Min", (violation) -> {
            Map<String, Object> attr = violation.getConstraintDescriptor().getAttributes();
            return new Object[] { attr.get("value") };
        });
        // Max
        registry.put("Max", (violation) -> {
            Map<String, Object> attr = violation.getConstraintDescriptor().getAttributes();
            return new Object[] { attr.get("value") };
        });
    }

    public DefaultValidationAttributeExtractorRegistry(Map<String, IValidationAttributeExtractor> extractors) {
        this();
        if (!extractors.isEmpty()) {
            registry.putAll(extractors);
        }
    }

    @Override
    public Object[] getArguments(ConstraintViolation<?> violation) {
        String annotationName = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
        return registry.getOrDefault(annotationName, Empty)
                .extractAttributes(violation);
    }
}
