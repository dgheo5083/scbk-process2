package com.scbank.process.api.svc.shared.components.alchera.service;

import com.scbank.process.api.svc.shared.components.alchera.model.AlcFaceHttpResponseEntity;
import com.scbank.process.api.svc.shared.components.alchera.model.AlcHttpRequestEntity;

/**
 * 
 */
public interface AlcFaceApiClient {

    /**
     * 
     * @param entity
     * @return
     */
    AlcFaceHttpResponseEntity execute(AlcHttpRequestEntity entity);
}
