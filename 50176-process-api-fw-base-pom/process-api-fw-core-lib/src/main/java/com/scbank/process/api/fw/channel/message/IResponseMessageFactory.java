package com.scbank.process.api.fw.channel.message;

import jakarta.validation.ConstraintViolationException;

/**
 * <pre>
 * IResponseMessage 생성용 팩토리 인터페이스
 * 응답 메시지 템플릿을 구성할 때, 헤더/바디 조합 또는 에러 메시지 구성 등에 사용
 * </pre>
 *
 * @author sungdon.choi
 */
public interface IResponseMessageFactory<H, T, RM extends IResponseMessage<H, T>> {

    /**
     * 성공 응답메시지 생성
     *
     * @param body 응답 본문
     * @return 생성된 응답 메시지
     */
    RM ok(T body);

    /**
     * 오류 응답 메시지 생성
     *
     * @param cause 예외객체
     * @return 생성된 응답 메시지
     */
    RM fail(Throwable cause);

    /**
     * 유효성 검증 오류 메시지 생성
     *
     * @param cause 유효성 검증 예외 객체
     * @return 생성된 응답 메시지
     */
    RM fail(ConstraintViolationException cause);
}
