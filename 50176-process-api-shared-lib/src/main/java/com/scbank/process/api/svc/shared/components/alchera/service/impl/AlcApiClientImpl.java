package com.scbank.process.api.svc.shared.components.alchera.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.util.Timeout;
import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.alchera.AlcAES256;
import com.scbank.process.api.svc.shared.components.alchera.AlcClient;
import com.scbank.process.api.svc.shared.components.alchera.model.AlcHttpJsonObject;
import com.scbank.process.api.svc.shared.components.alchera.model.AlcHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.alchera.model.AlcHttpResponseEntity;
import com.scbank.process.api.svc.shared.components.alchera.service.AlcApiClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient.HttpResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 알체라 API 요청/응답 처리 구현 클래스
 */
@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "알체라 API 요청/응답 공유 컴포넌트")
public class AlcApiClientImpl implements AlcApiClient {

    /**
     * 알체라 옵션
     * notsave : 서버 이미지 저장 유무
     * org_image : 신분증 원본 이미지 base64 리턴
     * portrait : 신분증 얼굴 사진 base64로 리턴
     * marked_image : 신분증 크롭 마스킹 이미지 base64로 리턴
     */
    private String options[] = { "notsave", "org_image", "portrait", "marked_image", "unmarked_image" };

    private final BaseHttpClient httpClient;

    @Override
    @ComponentOperation(name = "알체라 API 요청/응답")
    public AlcHttpResponseEntity execute(AlcHttpRequestEntity entity) {
        AlcHttpResponseEntity responseEntity = null;
        try {

            String method = entity.getMethod();
            // POST 형식
            if ("POST".equalsIgnoreCase(method)) {
                // 알체라 서버 API 통신 요청
                responseEntity = new AlcHttpResponseEntity(post(entity.getUrl(), entity));
            } else {
                // 지원하지 않는 METHOD TYPE입니다.
                throw new PRCServiceException(AlcClient.ERR_MA005, AlcClient.ERR_MA005_MSG);
            }

        } catch (PRCServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PRCServiceException(AlcClient.ERR_MA999, e.getMessage(), e);
        }
        return responseEntity;
    }

    /**
     * 알체라 서버 통신
     * 
     * @param url    알체라 서버 URL
     * @param entity 요청 정보
     * @return
     */
    private HttpResponse post(String url, AlcHttpRequestEntity entity) {

        try {
            AlcHttpJsonObject jData = AlcHttpJsonObject.fromObject((JSONObject) entity.getBodyParameters());
            // 인식할 신분증 이미지 암호화(AES256) 값
            String base64jpg = jData.getAsString("base64jpg", "");
            log.debug("###### AlcApiClientImpl ###### base64jpg : {}", base64jpg);

            // 통신 폼데이터 생성
            String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
            StringBuilder bodyBuilder = new StringBuilder();

            bodyBuilder.append("--").append(boundary).append("\r\n");
            bodyBuilder.append("Content-Disposition:form-data; name=\"base64jpg\"\r\n\r\n");
            // 인식할 신분증 사진 복호화(AES256) 처리
            bodyBuilder.append(AlcAES256.decrypt(base64jpg)).append("\r\n");

            // 옵션설정("서버 이미지 저장", "원본이미지", "증명이미지", "마스킹이미지")
            for (int i = 0; i < options.length; i++) {
                String option = options[i];
                bodyBuilder.append("--").append(boundary).append("\r\n");
                bodyBuilder.append("Content-Disposition:form-data; name=\"config\"\r\n\r\n");
                bodyBuilder.append(option).append("\r\n");
            }

            bodyBuilder.append("Content-Type:image/jpeg\r\n\r\n");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            outputStream.write(bodyBuilder.toString().getBytes("UTF-8"));

            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes("UTF-8"));

            // 통신 헤더 및 파라미터 셋팅
            HttpPost httpPost = new HttpPost(url);

            RequestConfig requestConfig = RequestConfig.custom()
                    // .setConnectTimeout(Timeout.of(Duration.ofSeconds(60)))
                    .setConnectionRequestTimeout(Timeout.of(Duration.ofSeconds(60)))
                    .setResponseTimeout(Timeout.of(Duration.ofSeconds(60)))
                    .build();
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + boundary + "; charset-UTF-8");
            httpPost.setEntity(new ByteArrayEntity(outputStream.toByteArray(),
                    ContentType.create("multipart/form-data", StandardCharsets.UTF_8)));

            log.debug("#### ALCHREA API CALL METHOD #### : " + entity.getMethod());
            log.debug("#### ALCHREA API CALL URL    #### : " + entity.getUrl());
            log.debug("#### ALCHREA API ENTITY      #### : " + httpPost.getEntity());

            // 통신 요청
            return this.httpClient.execute(httpPost);
        } catch (IOException e) {
            throw new PRCServiceException(e);
        } catch (Exception e) {
            throw new PRCServiceException(e);
        }
    }
}
