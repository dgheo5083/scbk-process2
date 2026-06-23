package com.scbank.process.api.svc.shared.components.frs.kftc.service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.Timeout;
import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.components.frs.IFrsClient;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpResponseEntity;
import com.scbank.process.api.svc.shared.components.frs.util.KftcFrsHelper;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient.HttpResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class KftcFrsApiClientBase implements IFrsClient {
	
	/**
     * HttpClient
     */
    private final BaseHttpClient httpClient;
	
	protected FrsHttpResponseEntity executeCall(FrsHttpRequestEntity entity) {
		JSONObject jsonError = new JSONObject();

		FrsHttpResponseEntity responseEntity = null;
		
		try {

			if (entity.getInterceptor() != null) {
				entity.getInterceptor().before(new Object[] { entity });
			}
			
			String method = entity.getMethod();
			
			if ("POST".equalsIgnoreCase(method)) {
				log.debug("FrsHttpResponseEntity executeCall entity => {}", entity);
				
				responseEntity = new FrsHttpResponseEntity(post(entity.getUrl(), entity));
			} else {
				throw new PRCServiceException("MA999", "지원하지 않는 METHOD TYPE입니다.");
			}
				
		} catch (PRCServiceException e) {
			log.debug("executeCall PRCServiceException => {}", e.getMessage());
			log.debug("executeCall PRCServiceException => {}", e);
			jsonError.put("rsp_code", e.getErrorCode());
			jsonError.put("rsp_message", e.getErrorMessage());

			throw e;
		} catch (Exception e) {
			log.debug("executeCall Exception => {}", e.getMessage());
			log.debug("executeCall Exception => {}", e);
			jsonError.put("rsp_code", "MA999");
			jsonError.put("rsp_message", e.getMessage());

			throw new PRCServiceException("MA999", e.getMessage(), e);
		} finally {
			
			if (entity.getInterceptor() != null) {
				JSONObject result = responseEntity == null ? jsonError : responseEntity.getAsJsonObject();
				entity.getInterceptor().after(new Object[] { entity, result });
			}
		}

		log.debug(" - result = {}", responseEntity != null ? responseEntity.getAsJsonObject(): "NULL");

		return responseEntity;
	}

	protected HttpResponse post(String url, FrsHttpRequestEntity entity) {
		HttpPost httpPost = new HttpPost(url);
		
		RequestConfig requestConfig = RequestConfig.custom()
                // .setConnectTimeout(Timeout.of(Duration.ofSeconds(60)))
                .setConnectionRequestTimeout(Timeout.of(Duration.ofSeconds(60))) //커넥션을 맺는데 걸리는 timeout 시간 설정 60초
                .setResponseTimeout(Timeout.of(Duration.ofSeconds(60))) //커넥션을 맺은후 응답대기 timeout 시간 설정 60초
                .build();
        httpPost.setConfig(requestConfig);
		
		log.debug(" - getOption = {}", entity.getOption());
		
		if (entity.getOption() == 1) {
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setHeader("Accept", "application/json; charset=UTF-8");
			
			List<NameValuePair> paramList = convertParameter(entity.getBodyParameters());
			UrlEncodedFormEntity form = new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8);
			httpPost.setEntity(form);
			
			log.debug("HttpResponse form => {} ", form);

		} else {
			JSONObject request = entity.getBodyParameters();

			StringEntity se = new StringEntity(request.toString(), ContentType.APPLICATION_JSON.withCharset("UTF-8"));
 
			httpPost.setEntity(se);
			
			String accessToken = entity.getAccessToken();
			if ((accessToken != null) && (!"".equals(accessToken.trim()))) {
				httpPost.setHeader("Authorization", String.format("Bearer %s", new Object[] { accessToken }));
			}
			
			httpPost.setHeader("Client-Id", KftcFrsHelper.getClientId().trim());
		}

		log.debug("KFTC FACE API Call [{}] method:[{}]", entity.getUrl(), entity.getMethod());
		log.debug(" - params = {}", httpPost.getEntity());
		log.debug(" - getAllHeaders = {}", Arrays.toString(httpPost.getHeaders()));
		log.debug(" - httpPost = {}", httpPost);
		
		HttpResponse result = httpClient.execute(httpPost);
		
		log.debug(" - result = {}", result);

		return result;
	}


	public List<NameValuePair> convertParameter(JSONObject params) {
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		Iterator<String> keys = params.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			paramList.add(new BasicNameValuePair(key, params.get(key).toString()));
		}

		return paramList;
	}
}
