package com.scbank.process.api.fw.core.exception;

import java.util.List;
import java.util.Map;

/**
 * 공통 예외 정보 인터페이스입니다.
 * <p>
 * 이 인터페이스를 구현한 예외 클래스는 전역 예외 처리기나 메시지 리졸버에서
 * 일관된 방식으로 오류코드, 발생 위치, 메시지 치환 파라미터를 추출할 수 있습니다.
 * </p>
 * 
 * @author sungdon.choi
 */
public interface IExceptionInfo {

    /**
     * 오류 코드 반환 (예: BFE0401)
     * 
     * @return 오류코드 문자열
     */
    String getErrorCode();

    /**
     * 오류 발생 위치 (선택값, 예: HOST, MCI, CORE 등)
     * 
     * @return 오류 발생 위치 식별 문자열
     */
    String getErrorLocation();

    /**
     * 
     * @return
     */
    String getErrorMessage();

    /**
     * 고객 가이드 메시지
     * 
     * @return
     */
    String getErrorGuideMessage();

    /**
     * OLPT 호스트 에러모듈 문자열
     * @return
     */
    String getErrorModule();

    /**
     * 메시지 템플릿에 바인딩될 파라미터 목록
     * 
     * @return 메시지 치환 인자 리스트
     */
    List<Object> getMessageArgs();
    
    /**
     * 오류 출력 이후 이동할 다음 페이지를 획득한다.
     * @return 오류 출력 이후 이동할 다음 페이지
     */
    String getNextPage();
    
    /**
     * 오류 출력 이후 이동할 다음 페이지 파라미터를 획득한다.
     * @return 오류 출력 이후 이동할 다음 페이지 파라미터
     */
    Map<String, Object> getNextPageParameters();
    
    /**
     * 오류 페이지 이동여부에 대한 파라미터를 획득한다.
     * @return 오류 이동여부에 대한 파라미터
     */
    Map<String, Object> getErrorPageParameters();
}
