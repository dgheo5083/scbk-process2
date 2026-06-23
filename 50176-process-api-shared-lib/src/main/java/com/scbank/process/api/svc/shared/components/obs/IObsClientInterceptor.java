package com.scbank.process.api.svc.shared.components.obs;

/**
 * 금결월 오픈뱅킹 클라이언트 인터셉터
 */
public interface IObsClientInterceptor {
    /**
     * 
     * @param logValues
     */
    void before(Object... logValues);

    /**
     * 
     * @param logValues
     */
    void after(Object... logValues);
}