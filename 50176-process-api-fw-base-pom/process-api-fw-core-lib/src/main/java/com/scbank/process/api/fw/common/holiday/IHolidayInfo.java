package com.scbank.process.api.fw.common.holiday;

import java.io.Serializable;

/**
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 17.
 */
public interface IHolidayInfo extends Serializable {

    /**
     * 공휴일 yyyyMMdd 형식의 날짜를 가져온다.
     *
     * @return yyyyMMdd 형식의 날짜 문자열
     */
    String getDate();

    /**
     * 공휴일 설명 ex) 현충일
     *
     * @return 공휴일 설명
     */
    String getDescription();
}
