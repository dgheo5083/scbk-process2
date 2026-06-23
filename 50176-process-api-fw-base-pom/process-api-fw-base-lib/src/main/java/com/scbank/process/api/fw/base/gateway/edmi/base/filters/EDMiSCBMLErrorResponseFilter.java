package com.scbank.process.api.fw.base.gateway.edmi.base.filters;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants;
import com.scbank.process.api.fw.base.gateway.edmi.base.exception.EDMiFeignException;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.client.filter.FeignFilter;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;
import com.scbank.process.api.fw.integration.exception.IntegrationException;

import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@RequiredArgsConstructor
public class EDMiSCBMLErrorResponseFilter implements FeignFilter {

    private static final String IME_EXCEPTION = "IMSException: ";
    private static final String SERVICE_EXCEPTION = "ServiceException: ";

    private final ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Override
    public Response afterResponse(Response response, FeignFilterContext ctx) {
        try {
            String systemId = StringUtils.defaultIfEmpty(ctx.getSystemId(), StringUtils.EMPTY);
            String responseBody = StreamUtils.copyToString(response.body().asInputStream(), StandardCharsets.UTF_8);

            Map<String, Object> responseMap = this.objectMapper.readValue(responseBody,
                    new TypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> root = (Map<String, Object>) responseMap.get(EDMiConstants.FLD_ROOT);
            Map<String, Object> header = (Map<String, Object>) root.get(EDMiConstants.FLD_HEADER);
            Map<String, Object> exceptions = (Map<String, Object>) header.get(EDMiConstants.FLD_EXCEPTIONS);

            if (CollectionUtils.isEmpty(exceptions) || !exceptions.containsKey(EDMiConstants.FLD_EXCEPTION)) {
                return response.toBuilder()
                        .body(responseBody, StandardCharsets.UTF_8)
                        .build();
            }

            List<Map<String, Object>> exception = (List<Map<String, Object>>) exceptions
                    .get(EDMiConstants.FLD_EXCEPTION);
            if (exception.size() == 0) {
                return response.toBuilder()
                        .body(responseBody, StandardCharsets.UTF_8)
                        .build();
            }

            Map<String, Object> errorMap = exception.get(0);
            String errorCode = "";
            String errorMessage = "";
            String errorMessage2 = "";
            String errorCustomerMessage = "";
            String warnMessage = "";
            String errorModule = "";
            String captureSystem = "";
            String description = "";
            boolean isException = false;

            if (header.get(EDMiConstants.FLD_CAPTURESYSTEM) != null) {
                captureSystem = (String) header.get(EDMiConstants.FLD_CAPTURESYSTEM);
            }

            if (errorMap.containsKey(EDMiConstants.FLD_CODE)) {
                Map<String, String> codeMap = (Map<String, String>) errorMap.get(EDMiConstants.FLD_CODE);
                errorCode = codeMap.get(EDMiConstants.FLD_BODY);
            } // end if

            if (errorMap.containsKey(EDMiConstants.FLD_DESCRIPTION)) {
                description = StringUtils.defaultIfEmpty((String) errorMap.get(EDMiConstants.FLD_DESCRIPTION), "");
            } // end if

            if (!EDMiConstants.VAL_COMPLETED.equals(errorCode)) {
                log.debug("### error code: {}", errorCode);

                isException = true;

             // IMSException인 경우 CAPERROR로 판단한다.
                if (description.indexOf("IMSException:") > -1) {
                    errorCode = IntegrationConstant.ERRCODE_HOST_ECAP;
                    errorModule = IntegrationConstant.ERRMESG_HOST_ECAP;
                    errorMessage = this.getHostCapErrorMessage(description);
                    errorMessage2 = IntegrationConstant.ERRMESG_HOST_ECAP1;
                    warnMessage = IntegrationConstant.ERRMESG_HOST_ECAP2;
                }
                // 연결 오류
                else if (description.indexOf("Connection refused") > -1
                        || description.indexOf("java.net.SocketException") > -1) {
                    errorCode = IntegrationConstant.ERRCODE_HOST_FAIL;
                    errorModule = IntegrationConstant.ERRMESG_HOST_FAIL;
                    errorMessage = IntegrationConstant.ERRMESG_HOST_FAIL1;
                    errorMessage2 = IntegrationConstant.ERRMESG_HOST_FAIL2;
                    warnMessage = IntegrationConstant.ERRMESG_HOST_FAIL3;
                } else if (description.indexOf("Connection timeed out") > -1) {
                    errorCode = IntegrationConstant.ERRCODE_HOST_FAIL;
                    errorModule = IntegrationConstant.ERRMESG_HOST_FAIL;
                    errorMessage = IntegrationConstant.ERRMESG_HOST_FAIL1;
                    errorMessage2 = IntegrationConstant.ERRMESG_HOST_FAIL2;
                    warnMessage = IntegrationConstant.ERRMESG_HOST_FAIL3;
                } else if (description.indexOf("wait timed out") > -1
                        || description.indexOf("Read timed out") > -1) {
                    errorCode = IntegrationConstant.ERRCODE_HOST_TIMEOUT;
                    errorModule = IntegrationConstant.ERRMESG_HOST_TIMEOUT;
                    errorMessage = IntegrationConstant.ERRMESG_HOST_TIMEOUT1;
                    errorMessage = IntegrationConstant.ERRMESG_HOST_TIMEOUT2;
                } else {
                    // 기타오류는 EDMI ETC 오류로 처리
                    errorCode = IntegrationConstant.ERRCODE_EDMI_ETC;
                    errorModule = IntegrationConstant.ERRMESG_EDMI_ETC;
                    errorMessage = this.resolveModeMessage(description);
                    errorMessage2 = IntegrationConstant.ERRMESG_EDMI_ETC1;
                    warnMessage = IntegrationConstant.ERRMESG_EDMI_ETC2;
                }
            }

            // 예외처리
            if (isException) {
                if (log.isDebugEnabled()) {
                    log.debug("##### responsefilter captureSystem: [{}], errorCode: [{}], isException :[{}]",
                            captureSystem, errorCode, isException);
                }

                if (StringUtils.isNotEmpty(errorMessage)) {
                    errorCustomerMessage += errorMessage;
                }

                if (StringUtils.isNotEmpty(errorMessage2)) {
                    errorCustomerMessage += "^" + errorMessage2;
                }

                if (StringUtils.isNotEmpty(warnMessage)) {
                    errorCustomerMessage += "^" + warnMessage;
                }

                IntegrationException ex = new IntegrationException(errorCode);
                ex.setErrorModule(errorModule);
                ex.setErrorLocation(systemId);
                ex.setErrorMessage(errorCustomerMessage);

                throw ex;
            }

            return response.toBuilder()
                    .body(responseBody.getBytes())
                    .build();
        } catch (EDMiFeignException e) {
            throw e;
        } catch (IntegrationException ie) {
        	throw ie;
        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR, e);
        }
    }

    /**
     * 로컬일 때를 제외한 러닝모드의 상태에 따라 errorMessage를 정의한다.
     * 에러메시지 획득 이후 70자리로 절삭한다.
     * 
     * @param errorMessage
     * @return errorMessage
     */
    private String resolveModeMessage(String description) {
        String errorMessage = description;
        int index = description.indexOf(SERVICE_EXCEPTION);
        if (index == -1) {
            errorMessage = description;
        } else {
            index += SERVICE_EXCEPTION.length();
            errorMessage = description.substring(index, description.indexOf("\n")).replace("\r", "").replace("\n", "");

        }

        if (StringUtils.isNotEmpty(errorMessage)) {
            // 에러메시지 자름
            if (errorMessage.length() > 70) {
                errorMessage = errorMessage.substring(0, 70);
            }
        }
        return errorMessage;
    }

    /**
     * 에러메시지 획득 이후 70자리로 절삭한다.
     * 
     * @param description
     * @return
     */
    private String getHostCapErrorMessage(String description) {
        String errorMessage = description;
        int index = description.indexOf(IME_EXCEPTION);
        if (index == -1) {
            errorMessage = description;
            if (description.length() > 70) {
                errorMessage = description.substring(0, 70);
            }
        } else {
            index += IME_EXCEPTION.length();

            errorMessage = description.substring(index, description.indexOf("\n")).replace("\r", "").replace("\n", "");
            if (StringUtils.isEmpty(errorMessage)) {
                errorMessage = this.resolveModeMessage(description);
            } else {
                if (errorMessage.length() > 70) {
                    errorMessage = errorMessage.substring(0, 70);
                }
            }
        }
        return errorMessage;
    }
}
