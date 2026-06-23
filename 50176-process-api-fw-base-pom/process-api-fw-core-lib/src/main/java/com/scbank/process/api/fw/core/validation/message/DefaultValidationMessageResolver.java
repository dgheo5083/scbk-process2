package com.scbank.process.api.fw.core.validation.message;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.scbank.process.api.fw.core.validation.attribute.IValidationAttributeExtractorRegistry;

import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * packageName    : co.kr.framework.core.validation
 * fileName       : DefaultValidationMessageResolver
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
public class DefaultValidationMessageResolver implements IValidationMessageResolver {

    private final MessageSource messageSource;

    private final IValidationAttributeExtractorRegistry validationAttributeExtractorRegistry;

    @Override
    public String resolveMessage(String code, ConstraintViolation<?> violation, Locale locale) {
        try {
            Object[] args = validationAttributeExtractorRegistry.getArguments(violation);
            return messageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            return code; // fallback
        }
    }
}
