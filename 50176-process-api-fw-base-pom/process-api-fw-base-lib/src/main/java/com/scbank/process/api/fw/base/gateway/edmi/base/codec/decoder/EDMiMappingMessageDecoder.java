package com.scbank.process.api.fw.base.gateway.edmi.base.codec.decoder;

import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_PAYLOAD_ROOT;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_RESPONSE_MESSAGE;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_ROOT;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_SCBML_PAYLOAD;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiResponseMessage;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext.FeignFilterContextHolder;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.context.IntegrationContextHolder;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import lombok.RequiredArgsConstructor;

/**
 * EDMi 전문 수신 메시지 디코더 구현 클래스
 * 
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public class EDMiMappingMessageDecoder implements IEDMiMessageDecoder {

	private final ObjectMapper objectMapper;
	
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.body() == null) {
            return null;
        }

        try {
            String responseBody = StreamUtils.copyToString(response.body().asInputStream(), StandardCharsets.UTF_8);
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode responseMessage = root
                    .path(FLD_ROOT)
                    .path(FLD_PAYLOAD_ROOT)
                    .path(FLD_SCBML_PAYLOAD)
                    .path(FLD_RESPONSE_MESSAGE);
            
            ObjectNode systemHeader = (ObjectNode)responseMessage.path(EDMiConstants.FLD_SYSTEMHEADER);
        	if (systemHeader != null && !systemHeader.isMissingNode()) {
        		systemHeader.put(EDMiConstants.FLD_PROC_RSLT_DV_CD, this.isErrorResponse(responseMessage) ? "1" : "0");
        	}

            byte[] responseMessageBytes = objectMapper.writeValueAsBytes(responseMessage);
            return EDMiResponseMessage.builder()
                    .responseMessage(responseMessageBytes)
                    .build();
        } catch (DecodeException e) {
            throw e;
        } catch (FeignException e) {
            throw e;
        } catch (Exception e) {
            throw new DecodeException(response.status(), e.getMessage(), response.request(), e);
        }
    }

	@Override
	public boolean supported(Response response, Type type) {
		FeignFilterContext ctx = FeignFilterContextHolder.get();
		if (ctx == null) {
			return false;
		}
		
		String systemId = ctx.getSystemId();
		return !IntegrationConstant.SYSTEM_ID_HOST.equals(systemId);
	}
	
	private static final String EDMI_CAPTURE_TARGET_SYSTEM_ERROR_SKIP = "EDMI_CAPTURE_TARGET_SYSTEM_ERROR_SKIP";
	
	/**
	 * 
	 * @param response
	 * @return
	 */
	private boolean isErrorResponse(JsonNode response) {
		IntegrationContext ctx = IntegrationContextHolder.get();
		String captureSystem = ctx != null ? ctx.getCaptureSystem() : StringUtils.EMPTY;
		
		String errorCheckSkipTargetSystem = PropertiesUtils.getString(EDMI_CAPTURE_TARGET_SYSTEM_ERROR_SKIP);
		if (errorCheckSkipTargetSystem.indexOf(captureSystem) > -1) {
			return false;
		}
		
		String procRsltDvCd = response.at(EDMiConstants.PATH_PROC_RSLT_DV_CD).asText(StringUtils.EMPTY);
		if (StringUtils.hasLength(procRsltDvCd)) {
			return !"0".equals(procRsltDvCd);
		}
		
		//TODO MSG_INFO를 봐야하나.. 확인필요
		
		String errorCode = response.at(EDMiConstants.PATH_YOGHERR).asText(StringUtils.EMPTY);
		if (!StringUtils.hasLength(errorCode)) {
			errorCode = response.at(EDMiConstants.PATH_FOERRCOD).asText(StringUtils.EMPTY);
		}
		
		return !"0000".equals(errorCode); 
	}
}
