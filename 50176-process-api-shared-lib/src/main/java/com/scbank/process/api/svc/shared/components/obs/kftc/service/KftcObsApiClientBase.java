package com.scbank.process.api.svc.shared.components.obs.kftc.service;

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
import com.scbank.process.api.svc.shared.components.obs.IObsClient;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpResponseEntity;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient.HttpResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 금결원 오픈뱅킹 API 클라이언트 공유 컴포넌트
 * AS-IS를 참고하여 재작성하였음
 */
@Slf4j
@RequiredArgsConstructor
public abstract class KftcObsApiClientBase implements IObsClient {

    /**
     * HttpClient
     */
    private final BaseHttpClient httpClient;

    /**
     * 
     * @param entity
     * @return
     */
    protected ObsHttpResponseEntity executeCall(ObsHttpRequestEntity entity) {
        JSONObject jsonError = new JSONObject();
        ObsHttpResponseEntity responseEntity = null;
        try {
            // 인터셉터 실행
            if (entity.getInterceptor() != null) {
                entity.getInterceptor().before(new Object[] { entity });
            }

            // HTTP 메소드 체크
            String method = entity.getMethod();
            if (!List.of("GET", "POST").contains(method)) {
                throw new PRCServiceException("MA999", "지원하지 않는 METHOD TYPE입니다.");
            }

            // API 요청/응답 처리
            responseEntity = new ObsHttpResponseEntity("GET".equals(method) ? get(entity) : post(entity));

        } catch (PRCServiceException e) {
        	log.error(e.getMessage(), e);
            jsonError.put("rsp_code", "MA999");
            jsonError.put("rsp_message", e.getErrorMessage());

            throw e;
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
            jsonError.put("rsp_code", "MA999");
            jsonError.put("rsp_message", e.getMessage());

            throw new PRCServiceException("MA999", e);
        } finally {
            if (entity.getInterceptor() != null) {
                JSONObject result = responseEntity == null ? jsonError : responseEntity.getAsJsonObject();
                entity.getInterceptor().after(new Object[] { entity, result });
            }
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
    private HttpResponse get(ObsHttpRequestEntity entity) {
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

        log.debug("# KFTC API Call {}, method: {}", entity.getUrl(), entity.getMethod());
        log.debug("# queryParams = {}" + paramList.toString());

        return httpClient.execute(httpGet);
    }

    /**
     * POST 요청
     * 
     * @param entity 요청정보 객체
     * @return 응답정보(status/body)를 담고 있는 응답 객체
     */
    private HttpResponse post(ObsHttpRequestEntity entity) {
    	
    	log.debug("# post entity url={}", entity.getUrl());
    	
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

        // 제3자 정보제공동의만.
        if (entity.getUrl().indexOf("re_agmt") > -1) {
            createHttpHeadersJson(httpPost, entity);
        }

        log.debug("# KFTC API Call {}, method: {}", entity.getUrl(), entity.getMethod());
        log.debug(" - params = {}", httpPost.getEntity());

        return httpClient.execute(httpPost);
    }

    /**
     * 
     * @param params
     * @return
     */
    public List<NameValuePair> convertParameter(JSONObject params) {
        List<NameValuePair> paramList = new ArrayList<>();
        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            paramList.add(new BasicNameValuePair(key, params.optString(key, "")));
        }
        return paramList;
    }

    /**
     * 
     * @param entity
     * @return
     */
    private void createHttpHeaders(HttpUriRequestBase request, ObsHttpRequestEntity entity) {
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        request.addHeader("Accept", "application/json; charset=UTF-8");

        String accessToken = entity.getAccessToken();
        if ((accessToken != null) && (!"".equals(accessToken.trim()))) {
            request.addHeader("Authorization", String.format("Bearer %s", new Object[] { accessToken }));
        }

        if (entity.getHeaders() != null) {
            for (Map.Entry<String, String> entry : entity.getHeaders().entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * JSON 요청 헤더 처리
     * 
     * @param entity
     * @return
     */
    private void createHttpHeadersJson(HttpUriRequestBase request, ObsHttpRequestEntity entity) {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Accept", "application/json; charset=UTF-8");

        String accessToken = entity.getAccessToken();
        if ((accessToken != null) && (!"".equals(accessToken.trim()))) {
            request.addHeader("Authorization", String.format("Bearer %s", new Object[] { accessToken }));
        }

        if (entity.getHeaders() != null) {
            for (Map.Entry<String, String> entry : entity.getHeaders().entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }
}
