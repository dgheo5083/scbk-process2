package com.scbank.process.api.fw.common.code;

import java.io.Serializable;
import java.util.List;

/**
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 17.
 */
public interface ICodeInfo extends Serializable {

    /**
     * 공통코드 키 를 가져온다.
     *
     * @return 공통코드 키
     */
    String getKey();

    /**
     * 공통코드 언어코드를 가져온다.
     *
     * @return 언어코드
     */
    String getLocale();

    /**
     * 공통코드 하위 아이템 목록을 가져온다.
     *
     * @return 공통코드 하위 아이템 목록
     */
    default List<ICodeItemInfo> getCodeItemList() {
        return List.of();
    }
}
