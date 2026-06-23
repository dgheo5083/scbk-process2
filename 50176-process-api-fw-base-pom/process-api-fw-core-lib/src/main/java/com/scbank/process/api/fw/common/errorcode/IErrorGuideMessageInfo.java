package com.scbank.process.api.fw.common.errorcode;

import java.io.Serializable;

/**
 * 프레임워크 에러 가이드 메시지 정보 인터페이스
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public interface IErrorGuideMessageInfo extends Serializable, Comparable<IErrorGuideMessageInfo> {

    /**
     * 가이드 메시지를 가져온다.
     *
     * @return 가이드 메시지
     */
    String getMessage();

    /**
     * 노출 순서를 가져온다.
     *
     * @return 노출 순서
     */
    default int getOrder() {
        return 0;
    }

    @Override
    default int compareTo(IErrorGuideMessageInfo o) {
        return Integer.compare(getOrder(), o.getOrder());
    }
}
