package com.scbank.process.api.fw.core.error;

import java.util.Locale;

/**
 * 에러 메시지 국제화(다국어) 메시지 해석기 인터페이스
 * <p>
 * 에러 코드, 메시지 파라미터, 로케일을 기준으로 {@link IErrorMessage} 객체를 생성해주는 기능을 정의합니다.
 * 주로 {@code GlobalExceptionHandler}, {@code ResponseMessageFactory} 등에서 사용됩니다.
 * </p>
 *
 * <p>
 * 예: 메시지 키 "E40001" → "세션 키가 유효하지 않습니다."로 매핑
 * </p>
 *
 * <pre>
 * [application.yml]
 * messages:
 *   basename: i18n/messages
 *
 * [messages_ko.properties]
 * E40001=세션 키가 유효하지 않습니다.
 * </pre>
 *
 * @see IErrorMessage
 * @see IErrorCode
 * @author
 */
@FunctionalInterface
public interface IErrorMessageResolver {

    /**
     * 에러코드와 로케일을 기준으로 에러 메시지를 생성
     *
     * @param errorCode 메시지 키 (예: "E40001")
     * @param args      메시지 포맷 인자 (예: 사용자 ID 등)
     * @param locale    사용자 로케일
     * @return 해석된 에러 메시지 객체
     */
    IErrorMessage resolveMessage(String errorCode, Object[] args, Locale locale);
}
