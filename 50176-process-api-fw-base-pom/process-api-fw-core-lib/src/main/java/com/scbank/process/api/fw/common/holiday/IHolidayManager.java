package com.scbank.process.api.fw.common.holiday;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.scbank.process.api.fw.core.lifecycle.IReloadable;

/**
 * 프레임워크 공통 휴일 관리 매니저 인터페이스
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 17.
 */
public interface IHolidayManager extends IReloadable, InitializingBean {

    @Override
    default void afterPropertiesSet() throws Exception {
        init();
    }

    void init();

    /**
     * 현재년을 기준으로 휴일정보 목록을 획득한다.
     * @return
     */
    List<IHolidayInfo> getHolidayList();
    
    /**
     * 휴일정보를 가져온다.
     * 
     * @param date yyyyMMdd 형식의 날짜 문자열
     * @return
     */
    IHolidayInfo getHoliday(String date);

    /**
     * 공휴일 등록 여부를 확인한다.
     *
     * @param date yyyyMMdd 형식읠 날짜 문자열
     * @return 공휴일 등록 여부
     */
    boolean isHoliday(String date);
}