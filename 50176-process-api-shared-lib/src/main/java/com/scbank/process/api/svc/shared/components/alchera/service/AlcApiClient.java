package com.scbank.process.api.svc.shared.components.alchera.service;

import com.scbank.process.api.svc.shared.components.alchera.model.AlcHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.alchera.model.AlcHttpResponseEntity;

/**
 * 알체라 API 요청/응답 처리 클라이언트 인터페이스
 */
public interface AlcApiClient {

    /**
     * 알체라 API 요청/응답 처리
     * 
     * @param entity 요청 객체 AlcHttpRequestEntity
     * @return 응답 객체 AlcHttpResponseEntity
     */
    AlcHttpResponseEntity execute(AlcHttpRequestEntity entity);
}
