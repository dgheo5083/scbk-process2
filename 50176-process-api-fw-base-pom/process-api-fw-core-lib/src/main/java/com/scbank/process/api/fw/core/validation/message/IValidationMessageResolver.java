package com.scbank.process.api.fw.core.validation.message;

import jakarta.validation.ConstraintViolation;
import java.util.Locale;

/**
 * <pre>
 * packageName    : co.kr.framework.core.validation
 * fileName       : IValidationMessageResolver
 * author         : gasigol
 * date           : 25. 4. 11.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 4. 11.        gasigol       최초 생성
 * </pre>
 */
@FunctionalInterface
public interface IValidationMessageResolver {

    String resolveMessage(String code, ConstraintViolation<?> violation, Locale locale);
}
