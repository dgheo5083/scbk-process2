package com.scbank.process.api.fw.common.errorcode;

import java.io.Serializable;

/**
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public interface IErrorButtonActionInfo extends Serializable {

    /**
     * 버튼 처리 타입
     *
     * @return 버튼 처리 타입
     */
    String getType();

    /**
     * 버튼 라벨 문자열
     *
     * @return 버튼 라벨 문자열
     */
    String getLabel();

    /**
     * 버튼 클릭 시 이동 할 타켓
     *
     * @return 버튼 클릭 시 이동 할 타켓
     */
    default String getTarget() {
        return "";
    }
}
