package com.scbank.process.api.svc.shared.components.edoc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.edoc.dto.EdocInfo;
import com.scbank.process.api.svc.shared.components.edoc.dto.EdocPayloadInfo;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class EdocAPConnector {

    private final EdocHelper helper;

    @ComponentOperation(name = "Edoc AP 요청")
    public JSONObject request(EdocPayloadInfo edocPayloadInfo) {

        try {

            JSONObject payload = buildPayload(edocPayloadInfo);

            log.debug("전자문서 EdocAPConnector [{}] 전송: \n {}", edocPayloadInfo.getAction(), payload.toString());

            JSONObject response = this.httpEdocAPConnector(payload.toString());

            log.debug("전자문서 EdocAPConnector [{}] 결과: \n {}", edocPayloadInfo.getAction(), response.toString());

            return response;

        } catch (Exception e) {
            log.error("EdocAPComponent {} Error : {}", edocPayloadInfo.getAction(), e);
            return new JSONObject();
        }

    }

    @ComponentOperation(name = "Edoc AP 요청")
    public JSONObject request(EdocPayloadInfo edocPayloadInfo, String errorModule) {

        try {

            JSONObject payload = buildPayload(edocPayloadInfo);

            log.debug("전자문서 EdocAPConnector [{}] 전송: \n {}", edocPayloadInfo.getAction(), payload.toString());

            JSONObject response = this.httpEdocAPConnector(payload.toString());

            log.debug("전자문서 EdocAPConnector [{}] 결과: \n {}", edocPayloadInfo.getAction(), response.toString());

            if (!"Y".equals(response.getString("successYN"))) {
                helper.sendLpcError(edocPayloadInfo, errorModule);
            }

            return response;

        } catch (Exception e) {
            log.error("EdocAPComponent {} Error : {}", edocPayloadInfo.getAction(), e);

            helper.sendLpcError(edocPayloadInfo, errorModule);
            return new JSONObject();
        }

    }

    private JSONObject httpEdocAPConnector(String jsonString) {

        // TODO: String relayServerURL = EdocServerFactory.getAvailableEdocServer(); //
        // SCFBCodeUtil.getSystemCodeValue("EDOC_RELAY_SERVER_URL");

        String relayServerURL = EdocServerFactory.getAvailableEdocServer(); // SCFBCodeUtil.getSystemCodeValue("EDOC_RELAY_SERVER_URL");

        String url = relayServerURL + PropertiesUtils.getString("EDOC_RELAY_SERVER_URL_ACTION");

        return this.httpEdocAPConnector(url, jsonString);
    }

    private JSONObject httpEdocAPConnector(String url, String jsonString) {

        HttpURLConnection conn = null;

        try {
            URI uri = URI.create(url);
            conn = (HttpURLConnection) uri.toURL().openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);

            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // 요청 전송
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonString.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // 응답 읽기
            int status = conn.getResponseCode();
            InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

            StringBuilder result = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }

            // JSON 변환
            return this.getEdocResponseData(result.toString());

        } catch (Exception e) {
            return new JSONObject();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Edoc서버 response Body 영역만 추출하여 리턴
     * ASIS : EdocAPConnectService.edocResponseDataToMa30
     * 
     * @param edocResult
     * @return
     */
    private JSONObject getEdocResponseData(String edocResult) {

        try {
            JSONObject jsonResult = new JSONObject(edocResult);

            JSONObject root = jsonResult.optJSONObject("_msg_");

            if (root == null) {
                return new JSONObject();
            }

            JSONObject body = root.optJSONObject("_body_");

            return body != null ? body : new JSONObject();
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private JSONObject buildPayload(EdocPayloadInfo edocPayloadInfo) {

        JSONObject root = new JSONObject();

        defaultPayload(root, "action", edocPayloadInfo.getAction());
        defaultPayload(root, "custNo", edocPayloadInfo.getCustNo());
        defaultPayload(root, "tradNo", edocPayloadInfo.getTradNo());
        defaultPayload(root, "bizType", edocPayloadInfo.getBizType());

        if (StringUtils.isNotBlank(edocPayloadInfo.getLpcMeta())) {
            root.put("lpcMeta",
                    edocPayloadInfo.getLpcMeta().replace("SCNF_CUST_SSN", SessionUtils.getSessionValue("PerBusNo")));
        }

        defaultPayload(root, "HEADER_RECEPTIONID", edocPayloadInfo.getLoanAccptNo());
        defaultPayload(root, "HEADER_APPLICATIONID", edocPayloadInfo.getHeaderApplicationId());
        defaultPayload(root, "emailAddr", edocPayloadInfo.getEmailAddr());

        /* 퍼스트홈론 담보/소득제공자 파라미터 (S) */
        if ("FHLS".equals(edocPayloadInfo.getBizType())) {
            defaultPayload(root, "FIDJBWGB", edocPayloadInfo.getFidjbwgb());
            defaultPayload(root, "SPOUSE_CUST_NO", edocPayloadInfo.getSpouseCustNo());
            defaultPayload(root, "SPOUSE_TRAD_NO", edocPayloadInfo.getSpouseTradNo());
            defaultPayload(root, "SPOUSE_TRAD_NO2", edocPayloadInfo.getSpouseTradNo2());
        }
        /* 퍼스트홈론 담보/소득제공자 파라미터 (E) */

        defaultPayload(root, "endDate", edocPayloadInfo.getEndDate());
        defaultPayload(root, "ssnNo", edocPayloadInfo.getSsnNo());
        defaultPayload(root, "testKey", edocPayloadInfo.getTestKey());

        /* 전자문서 EDMS 문서 다운로드 서비스 (S) */
        defaultPayload(root, "elementId", edocPayloadInfo.getElementId());
        defaultPayload(root, "acctNo", edocPayloadInfo.getAcctNo());
        defaultPayload(root, "custSsn", edocPayloadInfo.getCustSsn());
        defaultPayload(root, "docCode", edocPayloadInfo.getDocCode());
        defaultPayload(root, "chnnlMk", edocPayloadInfo.getChnnlMk());
        defaultPayload(root, "jobMk", edocPayloadInfo.getJobMk());
        defaultPayload(root, "barcodeData", edocPayloadInfo.getBarcodeData());
        /* 전자문서 EDMS 문서 다운로드 서비스 (E) */

        /* 전자문서 EDMS 문서 확인 서비스 (S) */
        defaultPayload(root, "edmsXvarmDocMeta", edocPayloadInfo.getEdmsXvarmDocMeta());
        /* 전자문서 EDMS 문서 확인 서비스 (E) */

        JSONArray edocListArray = new JSONArray();

        if (edocPayloadInfo.getEdocList() != null) {
            for (EdocInfo edoc : edocPayloadInfo.getEdocList()) {
                edocListArray.put(convertEdoc(edoc));
            }
        }

        root.put("edocList", edocListArray);

        return root;
    }

    private JSONObject convertEdoc(EdocInfo edoc) {
        JSONObject obj = new JSONObject();

        defaultPayload(obj, "edocCode", edoc.getEdocCode());
        defaultPayload(obj, "edocHash", edoc.getEdocHash());
        defaultPayload(obj, "signedHash", edoc.getSignedHash());
        defaultPayload(obj, "edocFilePath", edoc.getEdocFilePath());
        defaultPayload(obj, "elementID", edoc.getElementId());
        defaultPayload(obj, "edocSignYN", edoc.getEdocSignYn());

        return obj;
    }

    private void defaultPayload(JSONObject obj, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            obj.put(key, value);
        }
    }

}
