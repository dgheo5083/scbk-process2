package com.scbank.process.api.svc.shared.components.toss.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.toss.ITossClient;
import com.scbank.process.api.svc.shared.components.toss.model.TossHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.toss.model.TossHttpResponseEntity;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient.HttpResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "tossClient")
public class TossApiClient implements ITossClient {

    /**
     * 
     */
    private final BaseHttpClient httpClient;

    @ComponentOperation
    public TossApiClient init() {
        return this;
    }

    @ComponentOperation
    @Override
    public TossHttpResponseEntity execute(TossHttpRequestEntity entity) {
        return executeCall(entity);
    }

    /**
     * 
     * @param entity
     * @return
     */
    protected TossHttpResponseEntity executeCall(TossHttpRequestEntity entity) {
        JSONObject jsonError = new JSONObject();
        TossHttpResponseEntity responseEntity = null;
        try {
            // HTTP 메소드 체크
            String method = entity.getMethod();
            if (!List.of("GET", "POST").contains(method)) {
                throw new PRCServiceException(FrameworkErrorCode.INTERNAL_ERROR.getCode(), "지원하지 않는 METHOD TYPE입니다.");
            }

            // API 요청/응답 처리
            responseEntity = new TossHttpResponseEntity("GET".equals(method) ? get(entity) : post(entity));

        } catch (PRCServiceException e) {
            jsonError.put("rsp_code", e.getErrorCode());
            jsonError.put("rsp_message", e.getErrorMessage());

            throw e;
        } catch (Exception e) {
            jsonError.put("rsp_code", "MA999");
            jsonError.put("rsp_message", e.getMessage());

            throw new PRCServiceException("MA999", e);
        }

        if (responseEntity != null) {
            log.debug(" - result = {}", responseEntity.getAsJsonObject().toString());
        }

        return responseEntity;
    }

    /**
     * 오픈뱅킹 API GET 요청
     * 
     * @param entity 요청정보 객체
     * @return 응답정보(status/body)를 담고 있는 응답 객체
     */
    private HttpResponse get(TossHttpRequestEntity entity) {
        List<NameValuePair> paramList = convertParameter(entity.getBodyParameters());

        URI uri = null;
        try {
            uri = new URIBuilder(entity.getUrl())
                    .addParameters(paramList)
                    .build();
        } catch (URISyntaxException e) {
            throw new PRCServiceException(e);
        }

        HttpGet httpGet = new HttpGet(uri);

        createHttpHeaders(httpGet, entity);

        log.debug("# TOSS API Call {}, method: {}", entity.getUrl(), entity.getMethod());
        log.debug("# queryParams = {}" + paramList.toString());

        return httpClient.execute(httpGet);
    }

    /**
     * POST 요청
     * 
     * @param entity 요청정보 객체
     * @return 응답정보(status/body)를 담고 있는 응답 객체
     */
    private HttpResponse post(TossHttpRequestEntity entity) {
        HttpPost httpPost = new HttpPost(entity.getUrl());

        if (entity.getOption() == 1) {
            createHttpHeaders(httpPost, entity);

            List<NameValuePair> paramList = convertParameter(entity.getBodyParameters());
            UrlEncodedFormEntity form = new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8);
            httpPost.setEntity(form);
        } else {
            //JSONObject request = new JSONObject(entity.getBodyParameters());
            StringEntity se = new StringEntity(entity.getBodyParameters().toString(), StandardCharsets.UTF_8);

            httpPost.setEntity(se);

            createHttpHeaders(httpPost, entity);
        }

        log.debug("# TOSS API Call {}, method: {}", entity.getUrl(), entity.getMethod());
        log.debug(" - params = {}", httpPost.getEntity());

        return httpClient.execute(httpPost);
    }

    /**
     * 
     * @param params
     * @return
     */
    @ComponentOperation
    public List<NameValuePair> convertParameter(JSONObject params) {
        List<NameValuePair> paramList = new ArrayList<>();
        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            paramList.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
        return paramList;
    }

    /**
     * JSON 요청 헤더 처리
     * 
     * @param entity
     * @return
     */
    private void createHttpHeaders(HttpUriRequestBase headers, TossHttpRequestEntity entity) {
        headers.setHeader("Content-Type", "application/json");
        headers.setHeader("Accept", "application/json; charset=UTF-8");

        String accessToken = entity.getAccessToken();
        log.debug(" - accessToken = {}", accessToken);

        if (StringUtils.isNotEmpty(accessToken)) {
            headers.setHeader("Authorization", String.format("Bearer %s", new Object[] { accessToken }));
        }

        String host = entity.getHost();
        log.debug(" - host = {}", host);

        if ((host != null) && (!"".equals(host.trim()))) {
            headers.setHeader("Host", host);
        }

        log.debug(" - entity.getHeaders() = {}", entity.getHeaders());

        if (entity.getHeaders() != null)
            for (Map.Entry<String, String> entry : entity.getHeaders().entrySet())
                headers.setHeader(entry.getKey(), entry.getValue());

        log.debug(" - headers.toString() = {}", headers.toString());
    }
}
