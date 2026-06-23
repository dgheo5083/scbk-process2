package com.scbank.process.api.svc.shared.components.alchera.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
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
import com.scbank.process.api.svc.shared.components.alchera.AlcClient;
import com.scbank.process.api.svc.shared.components.alchera.model.AlcFaceHttpResponseEntity;
import com.scbank.process.api.svc.shared.components.alchera.model.AlcHttpJsonObject;
import com.scbank.process.api.svc.shared.components.alchera.model.AlcHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.alchera.service.AlcFaceApiClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient.HttpResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "알체라 안면인식서버 연계 공유 컴포넌트")
public class AlcFaceApiClientImpl implements AlcFaceApiClient {

    private final BaseHttpClient httpClient;

    @Override
    @ComponentOperation(name = "알체라 안면인식 서버 API 호출")
    public AlcFaceHttpResponseEntity execute(AlcHttpRequestEntity entity) {
        return executeCall(entity);
    }

    /**
     * 알체라 안면인식서버 검증 요청
     * 
     * @param requestEntity AlcHttpRequestEntity 요청엔티티
     * @return responseEntity AlcFaceHttpResponseEntity 알체라 안면인식(베스트샷 or 크롭 서비스) 응답
     *         데이터
     */
    protected AlcFaceHttpResponseEntity executeCall(AlcHttpRequestEntity entity) {
        try {
            String method = entity.getMethod();

            // 지원하지 않는 METHOD TYPE입니다.
            if (!"post".equalsIgnoreCase(method)) {
                throw new PRCServiceException(AlcClient.ERR_MA005, AlcClient.ERR_MA005_MSG);
            }

            // POST 형식
            AlcHttpJsonObject jData = AlcHttpJsonObject.fromObject((JSONObject) entity.getBodyParameters());
            String apiCode = jData.getAsString("apiCode", "");
            log.debug("###### AlcFaceApiClientImpl ###### apiCode : " + apiCode);

            if ("croppedface".equalsIgnoreCase(apiCode)) {
                // 알체라 안면인식 서버 신분증 & 얼굴 크롭 API 통신 요청
                return new AlcFaceHttpResponseEntity(croppedfacePost(entity.getUrl(), entity));
            } else if ("liveness".equalsIgnoreCase(apiCode)) {
                // 알체라 안면인식 서버 셀피 촬영 얼굴 검증 API 통신 요청
                return new AlcFaceHttpResponseEntity(post(entity.getUrl(), entity));
            } else {
                throw new PRCServiceException(AlcClient.ERR_MA009, AlcClient.ERR_MA009_MSG);
            }
        } catch (PRCServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PRCServiceException(AlcClient.ERR_MA999, e.getMessage(), e);
        }
    }

    /**
     * 안면인식 서버 베스트샷 검증 API 호출
     * 
     * @param url           String 알체라 안면인식 베스트샷 검증 API 요청 URL
     * @param requestEntity AlcHttpRequestEntity 요청엔티티
     * @return HttpClient
     */
    protected HttpResponse post(String url, AlcHttpRequestEntity entity) throws Exception {
        AlcHttpJsonObject jData = AlcHttpJsonObject.fromObject((JSONObject) entity.getBodyParameters());

        String images = jData.getAsString("images", "");
        String frameCounts = jData.getAsString("frame_counts", "");
        String filenameExtension = jData.getAsString("filename_extension", "");

        log.debug("###### AlcFaceApiClientImpl ###### images : " + images);
        log.debug("###### AlcFaceApiClientImpl ###### frameCounts : " + frameCounts);
        log.debug("###### AlcFaceApiClientImpl ###### filenameExtension : " + filenameExtension);

        String boundary = "--WebKitFormBoundary7MA4YWxkTrZu0gW";
        String LINE_FEED = "\r\n";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");

        // images(파일)
        writer.write("--" + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"images\"; filename=\"faceFile.zip\"" + LINE_FEED);
        writer.write("Content-Type: application/zip" + LINE_FEED);
        writer.write(LINE_FEED);
        writer.flush();

        File imagesFile = new File(images);
        log.debug("###### AlcFaceApiClientImpl ###### imagesFile : " + imagesFile.isFile());
        FileInputStream fis = new FileInputStream(imagesFile);
        byte[] buffer = new byte[4096];
        int byteRead;
        while ((byteRead = fis.read(buffer)) != -1) {
            outputStream.write(buffer, 0, byteRead);
        }
        fis.close();
        outputStream.write(LINE_FEED.getBytes());

        // frame_counts
        writer.write("--" + boundary + LINE_FEED);
        writer.write("Content-Disposition:form-data; name=\"frame_counts\"" + LINE_FEED);
        writer.write(LINE_FEED);
        writer.write(frameCounts + LINE_FEED);
        writer.flush();

        // filename_extenson
        writer.write("--" + boundary + LINE_FEED);
        writer.write("Content-Disposition:form-data; name=\"filename_extension\"" + LINE_FEED);
        writer.write(LINE_FEED);
        writer.write(filenameExtension + LINE_FEED);
        writer.flush();

        // 종료
        writer.write("--" + boundary + "--" + LINE_FEED);
        writer.flush();

        // 통신 헤더 및 파라미터 셋팅
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                // .setConnectTimeout(Timeout.of(Duration.ofSeconds(60)))
                .setConnectionRequestTimeout(Timeout.of(Duration.ofSeconds(60)))
                .setResponseTimeout(Timeout.of(Duration.ofSeconds(60)))
                .build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
        httpPost.setEntity(new ByteArrayEntity(outputStream.toByteArray(), ContentType.MULTIPART_FORM_DATA));

        log.debug("#### ALCHREA FACE API CALL METHOD #### : {}", entity.getMethod());
        log.debug("#### ALCHREA FACE API CALL URL    #### : {}", entity.getUrl());
        log.debug("#### ALCHREA FACE API ENTITY      #### : {}", httpPost.getEntity());

        // 통신 요청
        return httpClient.execute(httpPost);
    }

    /**
     * 안면인식 신분증 얼굴 or 셀피 얼굴 이미지 크롭 처리 API 호출
     * 
     * @param url           String 알체라 안면인식 크롭 API 요청 URL
     * @param requestEntity AlcHttpRequestEntity 요청엔티티
     * @return HttpClient
     */
    protected HttpResponse croppedfacePost(String url, AlcHttpRequestEntity entity) throws Exception {
        AlcHttpJsonObject jData = AlcHttpJsonObject.fromObject((JSONObject) entity.getBodyParameters());

        String cropTargetImage = jData.getAsString("cropTargetImage", "");
        log.debug("###### AlcFaceApiClientImp > croppedfacePost ###### cropTargetImage : " + cropTargetImage);

        File imagesFile = new File(cropTargetImage);
        log.debug(
                "###### AlcFaceApiClientImpl > croppedfacePost ###### imagesFile isFile : " + imagesFile.isFile());

        FileInputStream fis = new FileInputStream(imagesFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] data = new byte[4096];
        int nRead;
        while ((nRead = fis.read(data, 0, data.length)) != -1) {
            outputStream.write(data, 0, nRead);
        }
        fis.close();

        byte[] imageBytes = outputStream.toByteArray();

        // 통신 헤더 및 파라미터 셋팅
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                // .setConnectTimeout(Timeout.of(Duration.ofSeconds(60)))
                .setConnectionRequestTimeout(Timeout.of(Duration.ofSeconds(60)))
                .setResponseTimeout(Timeout.of(Duration.ofSeconds(60)))
                .build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "image/jpeg");
        httpPost.setEntity(new ByteArrayEntity(imageBytes, ContentType.IMAGE_JPEG));

        log.debug("#### ALCHREA FACE API CALL METHOD #### : " + entity.getMethod());
        log.debug("#### ALCHREA FACE API CALL URL    #### : " + entity.getUrl());
        log.debug("#### ALCHREA FACE API ENTITY      #### : " + httpPost.getEntity());

        // 통신 요청
        return httpClient.execute(httpPost);
    }

}
