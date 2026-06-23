package com.scbank.process.api.fw.base.gateway.edmi.base.filters;

import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_PAYLOAD_ROOT;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_RESPONSE_MESSAGE;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_ROOT;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_SCBML_PAYLOAD;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Hex;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.base.exception.EDMiFeignException;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.integration.client.filter.FeignFilter;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;

import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * EDMi 메시지 응답 Hex 디코더 필터
 */
@Slf4j
@RequiredArgsConstructor
public class EDMiMessageHexDecodeFilter implements FeignFilter {

    /**
     * Object Mapper
     */
    private final ObjectMapper objectMapper;

    @Override
    public Response afterResponse(Response response, FeignFilterContext ctx) {
    	if (!IntegrationConstant.SYSTEM_ID_HOST.equals(ctx.getSystemId())) {
    		return response;
    	}
    	
        try {
            String responseBody = StreamUtils.copyToString(response.body().asInputStream(), StandardCharsets.UTF_8);
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode responseMessage = root
                    .path(FLD_ROOT)
                    .path(FLD_PAYLOAD_ROOT)
                    .path(FLD_SCBML_PAYLOAD)
                    .path(FLD_RESPONSE_MESSAGE)
                    .path("TRANDATA")
                    .path("MESSAGE");

            byte[] decodedResponseMessage = Hex.decodeHex(responseMessage.textValue());

            return Response.builder()
                    .status(response.status())
                    .reason(response.reason())
                    .request(response.request())
                    .body(decodedResponseMessage)
                    .build();
        } catch (Exception e) {
            throw new EDMiFeignException(e);
        }
    }
}
