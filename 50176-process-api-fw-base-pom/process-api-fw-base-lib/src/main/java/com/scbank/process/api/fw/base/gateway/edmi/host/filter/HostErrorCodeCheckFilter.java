package com.scbank.process.api.fw.base.gateway.edmi.host.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;

import com.scbank.process.api.fw.base.gateway.edmi.base.exception.EDMiFeignException;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.integration.client.filter.FeignFilter;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;

import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 호스트 응답 전문 오류코드 체크 필터
 * 
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
public class HostErrorCodeCheckFilter implements FeignFilter {

    private static final int OFFSET_FLD_ERROR_CODE = 145;
    private static final int LENGTH_FLD_ERROR_CODE = 4;
    private static final int OFFSET_FLD_CHECK_FLAG = 43;
    private static final int LENGTH_FLD_CHECK_FLAG = 4;
    private static final String DEFAULT_ERROR_CODE = "0000";
    private static final String ECAP_ERROR_CODE = "****";

    @Value("${csl.integration.system.host.charset}")
    private String charset;

    @Override
    public Response afterResponse(Response response, FeignFilterContext ctx) {
    	if (!IntegrationConstant.SYSTEM_ID_HOST.equals(ctx.getSystemId())) {
    		return response;
    	}
        //
        byte[] checkFlagBytes = new byte[LENGTH_FLD_CHECK_FLAG];
        byte[] errorCodeBytes = new byte[LENGTH_FLD_ERROR_CODE];

        try {
            byte[] responseMessageBytes = StreamUtils.copyToByteArray(response.body().asInputStream());
            // -----------------------------------------------------
            // 호스트 전문 응답 메시지에서 체크플래그 4byte를 추출한다.
            // -----------------------------------------------------
            System.arraycopy(responseMessageBytes, OFFSET_FLD_CHECK_FLAG, checkFlagBytes, 0, checkFlagBytes.length);

            // -----------------------------------------------------
            // CAP ABEND FLAG 확인하여 errorCode 'ECAP'으로 설정
            // -----------------------------------------------------
            if (checkFlagBytes[0] == (byte) 0x82) {
                errorCodeBytes = ECAP_ERROR_CODE.getBytes(charset);
                System.arraycopy(errorCodeBytes, 0, responseMessageBytes, OFFSET_FLD_ERROR_CODE, errorCodeBytes.length);
            }

            // -----------------------------------------------------
            // 호스트 전문 응답 메시지에서 에러코드 4byte를 추출한다.
            // -----------------------------------------------------
            System.arraycopy(responseMessageBytes, OFFSET_FLD_ERROR_CODE, errorCodeBytes, 0, errorCodeBytes.length);
            String errorCode = new String(errorCodeBytes, charset);

            if (log.isDebugEnabled()) {
                log.debug("HostErrorCodeFilter extract errorCode -> [{}]", errorCode);
            }

            // -----------------------------------------------------
            // 에러코드가 4byte 공백이 오는경우 정상응답으로 판단하여
            // 정상 오류코드 '0000' 으로 값을 다시 채워준다.
            // -----------------------------------------------------
            if (errorCodeBytes[0] == 0x00 &&
                    errorCodeBytes[1] == 0x00 &&
                    errorCodeBytes[2] == 0x00 &&
                    errorCodeBytes[3] == 0x00) {
                errorCodeBytes = DEFAULT_ERROR_CODE.getBytes(charset);
                System.arraycopy(errorCodeBytes, 0, responseMessageBytes, OFFSET_FLD_ERROR_CODE, errorCodeBytes.length);
            }

            return response.toBuilder().body(responseMessageBytes).build();
        } catch (EDMiFeignException e) {
            throw e;
        } catch (Exception e) {
            throw new EDMiFeignException(e);
        }
    }
}
