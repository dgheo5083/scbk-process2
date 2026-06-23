package com.scbank.process.api.fw.common.errorcode;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.scbank.process.api.fw.core.error.IErrorMessage;
import com.scbank.process.api.fw.core.error.IErrorMessageResolver;

import lombok.RequiredArgsConstructor;

/**
 * <pre>
 *     spring 메시지소스 기반 에러메시지 처리 Resolver
 * packageName    : co.kr.framework.common.errorcode
 * fileName       : MessageSourceErrorMessageResolver
 * author         : gasigol
 * date           : 25. 4. 9.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 4. 9.        gasigol       최초 생성
 * </pre>
 */
@RequiredArgsConstructor
public class MessageSourceErrorMessageResolver implements IErrorMessageResolver {

    private final MessageSource messageSource;

    @Override
    public IErrorMessage resolveMessage(String errorCode, Object[] args, Locale locale) {
        return DefaultErrorMessage.builder()
                .errorCode(errorCode)
                .errorMessage(messageSource.getMessage(errorCode, args, locale))
                .build();
    }
}
