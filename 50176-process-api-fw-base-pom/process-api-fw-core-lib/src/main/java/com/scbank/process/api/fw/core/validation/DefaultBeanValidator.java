package com.scbank.process.api.fw.core.validation;

import java.io.Serializable;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * packageName    : co.kr.framework.core.validation
 * fileName       : DefaultBeanValidator
 * author         : gasigol
 * date           : 25. 4. 11.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 4. 11.        gasigol       최초 생성
 * </pre>
 */
@RequiredArgsConstructor
public class DefaultBeanValidator<T extends Serializable> implements IBeanValidator<T> {

    private final Validator validator;

    @Override
    public void validate(T target) {
        Set<ConstraintViolation<T>> violations = validator.validate(target);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
