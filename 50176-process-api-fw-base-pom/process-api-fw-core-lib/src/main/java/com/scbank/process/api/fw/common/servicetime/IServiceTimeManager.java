package com.scbank.process.api.fw.common.servicetime;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.scbank.process.api.fw.core.lifecycle.IReloadable;

/**
 * 서비스 이용시간 관리 매니저 인터페이스
 */
public interface IServiceTimeManager extends IReloadable, InitializingBean {

    @Override
    default void afterPropertiesSet() throws Exception {
        init();
    }

    void init();

    public List<IServiceTimeInfo> getServiceTimeList(ServiceTimeGroup group);

    public IServiceTimeInfo getServiceTime(ServiceTimeGroup group, String code);
}
