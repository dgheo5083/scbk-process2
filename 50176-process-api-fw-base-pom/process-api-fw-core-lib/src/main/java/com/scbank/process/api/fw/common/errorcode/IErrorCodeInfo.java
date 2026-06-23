package com.scbank.process.api.fw.common.errorcode;

import java.io.Serializable;
import java.util.List;

/**
 * 프레임워크 에러코드 정보 인터페이스
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public interface IErrorCodeInfo extends Serializable {

    /**
     * 에러 코드를 가져온다.
     *
     * @return 에러 코드
     */
    String getCode();

    /**
     * 언어 코드를 가져온다.
     *
     * @return 언어코드
     */
    String getLangCode();

    /**
     * 에러 메시지를 가져온다.
     *
     * @return 에러메시지
     */
    String getMessage();

    /**
     * 에러 노출 타입을 가져온다.
     *
     * @return 에러 노출 타입
     */
    default String getDisplayType() {
        return "";
    }

    /**
     * 에러 가이드 메시지 목록을 가져온다.
     *
     * @return 에러 가이드 메시지 목록
     */
    default List<IErrorGuideMessageInfo> getErrorGuideMessages() {
        return List.of();
    }

    /**
     * 에러페이지 하단 버튼 액션 정보
     *
     * @return 에러페이지 하단 버튼 액션 정보
     */
    default IErrorButtonActionInfo getErrorButtonActionInfo() {
        return null;
    }
}
