package com.scbank.process.api.fw.common.code;

import java.io.Serializable;

/**
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 17.
 */
public interface ICodeItemInfo extends Serializable, Comparable<ICodeItemInfo> {

    /**
     * 공통코드 아이템 키
     */
    String getKey();

    /**
     * 공통코드 아이템 값
     */
    String getValue();

    /**
     * 공통코드 아이템 sort order
     */
    int getOrder();

    @Override
    default int compareTo(ICodeItemInfo o) {
        return Integer.compare(getOrder(), o.getOrder());
    }
}
