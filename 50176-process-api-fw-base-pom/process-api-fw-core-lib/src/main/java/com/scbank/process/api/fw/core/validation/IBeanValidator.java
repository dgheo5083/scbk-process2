package com.scbank.process.api.fw.core.validation;

/**
 * <pre>
 * packageName    : co.kr.framework.core.validation
 * fileName       : IBeanValidator
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
public interface IBeanValidator<T> {

    void validate(T target);
}
