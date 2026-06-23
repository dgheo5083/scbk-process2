package com.scbank.process.api.fw.session;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * 세션 키 유효성 검증 실패 시 발생하는 런타임 예외
 * <p>
 * {@link ISessionKeyValidator}를 통해 유효하지 않은 세션 키가 사용된 경우,
 * 해당 예외가 발생하며, 공통 오류 코드 {@link FrameworkErrorCode#SESSION_KEY_INVALID}를 사용합니다.
 * </p>
 * 
 * 예: 허용되지 않은 세션 키로 값을 저장하거나 조회할 경우
 * 
 * @see ISessionKeyValidator
 * @see FrameworkErrorCode#SESSION_KEY_INVALID
 * 
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
public class SessionKeyInvalidException extends FrameworkRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 세션 키 유효성 오류 생성자
     *
     * @param errorMessage 상세 오류 메시지
     */
    public SessionKeyInvalidException(String errorMessage) {
        super(FrameworkErrorCode.SESSION_KEY_INVALID.getCode(), errorMessage);
    }
}
