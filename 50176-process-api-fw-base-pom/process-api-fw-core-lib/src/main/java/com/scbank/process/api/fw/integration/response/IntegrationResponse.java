package com.scbank.process.api.fw.integration.response;

import com.scbank.process.api.fw.message.IMessageObject;

/**
 * 프레임워크 외부 시스템 연계 응답 데이터 인터페이스
 *
 * <p>
 * 연계 응답 데이터를 표준 구조로 추상화합니다.
 * <ul>
 * <li>헤더, 정상 응답, 오류 응답을 각각 분리하여 제공</li>
 * <li>오류 여부에 따라 후속 처리를 분기할 수 있도록 isError() 포함</li>
 * </ul>
 *
 * @param <H> 헤더 객체 타입
 * @param <B> 정상 응답 바디 타입
 * @param <E> 오류 응답 바디 타입
 * @author sungdon.choi
 */
public interface IntegrationResponse<H extends IMessageObject, B extends IMessageObject, E extends IMessageObject> {

    /**
     * 연계 응답의 헤더 객체를 반환합니다.
     * <p>
     * 예: 응답 코드, 거래일시, 처리 결과 등의 메타데이터
     * </p>
     *
     * @return H 타입의 헤더 객체
     */
    H getHeader();

    /**
     * 정상 응답 데이터를 반환합니다.
     * <p>
     * 오류가 발생하지 않은 경우에만 유효한 데이터
     * </p>
     *
     * @return B 타입의 응답 바디 객체
     */
    B getResponse();

    /**
     * 오류 응답 데이터를 반환합니다.
     * <p>
     * 오류 발생 시, 상세 오류 정보를 포함한 객체
     * </p>
     *
     * @return E 타입의 오류 응답 객체
     */
    E getErrorResponse();

    /**
     * 현재 응답이 오류 응답인지 여부를 반환합니다.
     * <p>
     * isError()가 true이면 getResponse()는 null이며, getErrorResponse()에 값이 존재합니다.
     * </p>
     *
     * @return true: 오류 응답, false: 정상 응답
     */
    boolean isError();
}
