package com.scbank.process.api.fw.base.gateway.prc.base.codec;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.gateway.prc.base.exception.PRCFeignException;
import com.scbank.process.api.fw.core.error.FrameworkErrorCode;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * 프로세스 API 호출 오류응답 디코더 클래스
 * 
 * @author sungdon.choi
 */
public class PRCExceptionDecoder extends ErrorDecoder.Default {

	private static final String FLD_HEADER = "header";
	private static final String FLD_RES_CODE = "resCode";
	private static final String FLD_ERRORCODE = "errorCode";
	private static final String FLD_ERROR_LOCATION = "errorLocation";
	private static final String FLD_ERROR_MODULE = "errorModule";
	private static final String FLD_ERROR_MESSAGE = "errorMessage";

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Exception decode(String methodKey, Response response) {
		try {
			Response.Body responseBody = response.body();
			String body = IOUtils.toString(responseBody.asInputStream(), StandardCharsets.UTF_8);
			int status = response.status();

			if (status != HttpStatus.OK.value()) {
				JsonNode node = objectMapper.readTree(body);
				JsonNode headerNode = node.findValue(FLD_HEADER);

				if (Objects.isNull(headerNode)) {
					return this.handleDefaultException();
				}

				JsonNode resCode = headerNode.get(FLD_RES_CODE);
				if ("00".equals(resCode.asText())) {
					return this.handleDefaultException();
				}

				JsonNode errCode = headerNode.get(FLD_ERRORCODE);
				JsonNode errLocation = headerNode.get(FLD_ERROR_LOCATION);
				JsonNode errModule = headerNode.get(FLD_ERROR_MODULE);
				JsonNode errMessage = headerNode.get(FLD_ERROR_MESSAGE);

				// TOOD 에러 가이드 메시지 처리
				return this.handleException(errCode.asText(), errLocation.asText(), errModule.asText(),
						errMessage.asText());
			}
			return super.decode(methodKey, response);
		} catch (Exception e) {
			throw new PRCFeignException(e);
		}
	}

	/**
	 * 기본 내부 에러코드로 예외를 발생시킨다.
	 * 
	 * @return
	 */
	private Exception handleDefaultException() {
		return new PRCServiceException(FrameworkErrorCode.INTERNAL_ERROR);
	}

	/**
	 * 
	 * @param errorCode
	 * @param errorLocation
	 * @param errorModule
	 * @param errorMessage
	 * @return
	 */
	private Exception handleException(String errorCode, String errorLocation, String errorModule, String errorMessage) {
		PRCFeignException ex = new PRCFeignException();
		ex.setErrorCode(errorCode);
		ex.setErrorLocation(errorLocation);
		ex.setErrorModule(errorModule);
		ex.setErrorMessage(errorMessage);
		ex.setErrorGuideMessage("");
		return ex;
	}
}