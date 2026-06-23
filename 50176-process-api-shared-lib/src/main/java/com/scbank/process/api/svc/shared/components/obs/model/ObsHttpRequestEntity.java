package com.scbank.process.api.svc.shared.components.obs.model;

import java.util.Map;

import org.json.JSONObject;

import com.scbank.process.api.svc.shared.components.obs.IObsClientInterceptor;

import lombok.Getter;
import lombok.Setter;

/**
 * 금결원 오픈뱅킹 요청 Entity
 */
@Getter
@Setter
public class ObsHttpRequestEntity {

    public static final int OPT_PARAM_MAP = 1;
    public static final int OPT_PARAM_JSON = 2;

    private String propName; // kftc를 구분할 구분 값.
    private String apiId; // URL등을 가져올 api 구분 값.
    private String url;
    private String accessToken;
    private String method;
    private int option;

    private Map<String, String> headers;
    private JSONObject bodyParameters;
    private IObsClientInterceptor interceptor;

    public void setBodyParameters(JSONObject bodyParameters) {
        this.option = OPT_PARAM_JSON;
        this.bodyParameters = bodyParameters;
    }

    public JSONObject getParameters() {
        return this.bodyParameters;
    }

    public void setParameters(JSONObject bodyParameters) {
        this.option = OPT_PARAM_MAP;
        this.bodyParameters = bodyParameters;
    }
}
