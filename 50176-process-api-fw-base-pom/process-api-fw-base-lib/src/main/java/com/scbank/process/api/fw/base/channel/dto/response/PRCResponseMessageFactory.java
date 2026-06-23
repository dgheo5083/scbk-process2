package com.scbank.process.api.fw.base.channel.dto.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.base.channel.dto.PRCResponseHeader;
import com.scbank.process.api.fw.base.channel.dto.PRCResponseHeader.NextPageParameter;
import com.scbank.process.api.fw.base.channel.dto.PRCResponseHeader.ErrorPageParameter;
import com.scbank.process.api.fw.base.constant.PRCBaseConstants;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.channel.message.IResponseMessageFactory;
import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.error.IErrorMessage;
import com.scbank.process.api.fw.core.error.IErrorMessageResolver;
import com.scbank.process.api.fw.core.exception.IExceptionInfo;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.LocaleUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프로세스API 응답 메시지 Factory 구현 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PRCResponseMessageFactory<B extends IMessageObject> implements
        IResponseMessageFactory<PRCResponseHeader, B, PRCResponseMessage<B>> {

    /**
     * 에러메시지 획득 리졸버
     */
    private final IErrorMessageResolver errorMessageResolver;

    @SuppressWarnings("unchecked")
    @Override
    public PRCResponseMessage<B> ok(B body) {
        IServiceContext sc = ServiceContextHolder.getContext();
        PRCResponseMessage<B> responseMessage = (PRCResponseMessage<B>) PRCResponseMessage.builder()
                .header(PRCResponseHeader.builder()
                        .requestUId(sc != null ? sc.requestUId() : StringUtils.EMPTY)
                        .resCode(PRCBaseConstants.SUCCESS_RES_CODE)
                        .build())
                .body(body)
                .build();
        return responseMessage;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PRCResponseMessage<B> fail(Throwable cause) {
        IServiceContext sc = ServiceContextHolder.getContext();
        Locale locale = sc == null ? LocaleUtils.toLocale(RuntimeContext.getDefaultLocale()) : sc.locale();
        if (locale == null) {
            locale = LocaleUtils.toLocale(RuntimeContext.getDefaultLocale());
        }

        // 기본 에러코드 획득
        String defaultErrorCode = RuntimeContext.getDefaultErrorCode();
        if (StringUtils.isEmpty(defaultErrorCode)) {
            defaultErrorCode = FrameworkErrorCode.INTERNAL_ERROR.getCode();
        }

        // 기본 오류 메시지 획득
        String defaultErrorMessage = RuntimeContext.getDefaultErrorMessage();

        String errorCode = defaultErrorCode;
        String message = cause.getMessage();
        String errorLocation = "";
        String errorModule = "";
        List<Object> messageArgs = List.of();
        List<String> guideMessages = new ArrayList<>();
        
        String nextPage = "";
        List<NextPageParameter> nextPageParameters = List.of();
        
        List<ErrorPageParameter> errorPageParameters = List.of();

        // 프레임워크 예외인경우 처리
        if (cause instanceof IExceptionInfo ex) {
            errorCode = ex.getErrorCode();
            message = ex.getErrorMessage();
            messageArgs = ex.getMessageArgs() != null ? ex.getMessageArgs() : List.of();

            //[2026.04.10 최성돈] 에러가이드 메시지 처리 수정
            String errorGuideMessage = ex.getErrorGuideMessage();
            // 예외 객체에서 에러 가이드 메시지를 가져온다.
            if (StringUtils.hasText(errorGuideMessage)) {
            	// '^' 구분자 처리 추가
                guideMessages = Arrays.stream(errorGuideMessage.split("\\^"))
                		.map(String::trim)
                		.filter(StringUtils::hasText)
                		.toList();
            }
            
            errorLocation = ex.getErrorLocation();
            errorModule = ex.getErrorModule();
            
            //[2026.02.11] 최성돈 추가
            nextPage = ex.getNextPage();
            nextPageParameters = this.convertNextPageParameters(ex.getNextPageParameters());
            
            //[2026.04.01] 권태민 추가
            errorPageParameters = this.convertErrorPageParameters(ex.getErrorPageParameters());
        }

        IErrorMessage errorMessageObj = this.errorMessageResolver.resolveMessage(errorCode,
                messageArgs.toArray(Object[]::new), locale);
        // 에러메시지 정보 획득
        if (errorMessageObj != null) {
            String errMsg = errorMessageObj.getErrorMessage();
            if (StringUtils.hasLength(errMsg)) {
                message = errMsg;
            }

            // 에러 가이드 메시지 추가
            if (!CollectionUtils.isEmpty(errorMessageObj.getErrorGuideMessages())) {
                guideMessages.addAll(errorMessageObj.getErrorGuideMessages());
            }
        }

        // 에러메시지가 없는 경우 기본 오류 메시지로 설정하여 처리
        if (StringUtils.isEmpty(message)) {
            if (log.isDebugEnabled()) {
                log.debug("# 에러메시지 문자열 공백, 어플리케이션 기본 에러메시로 설정");
            }
            message = defaultErrorMessage;
        }

        // 오류 응답 헤더 설정
        PRCResponseHeader responseHeader = PRCResponseHeader.builder()
                .requestUId(sc != null ? sc.requestUId() : StringUtils.EMPTY)
                .resCode(PRCBaseConstants.FAILED_RES_CODE)
                .errorCode(errorCode)
                .errorMessage(message)
                .errorGuideMessages(guideMessages)
                .errorLocation(errorLocation)
                .errorModule(errorModule)
                //[2026.02.11] 추가
                .nextPage(nextPage)
                .nextPageParameters(nextPageParameters)
                //[2026.04.01] 권태민 추가
                .errorPageParameters(errorPageParameters)
                .build();

        PRCResponseMessage<B> responseMessage = (PRCResponseMessage<B>) PRCResponseMessage.builder()
                .header(responseHeader)
                .build();
        return responseMessage;
    }

    @Override
    public PRCResponseMessage<B> fail(ConstraintViolationException cause) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Map 타입의 nextPageParameters 를 List 타입으로 변환한다.
     * @param map nextPageParameters
     * @return List 타입으로 변환된 nextPageParameters
     */
    private List<NextPageParameter> convertNextPageParameters(Map<String, Object> map) {
    	if (CollectionUtils.isEmpty(map)) {
    		return List.of();
    	}
    	
    	return map.entrySet().stream()
    			.filter(e -> e.getValue() != null)
    			.map(e -> {
    				NextPageParameter p = new NextPageParameter();
    				p.setName(e.getKey());
    				p.setValue(String.valueOf(e.getValue()));
    				
    				return p;
    			})
    			.collect(Collectors.toList());
    }
    
    /**
     * Map 타입의 errorPageParameters 를 List 타입으로 변환한다.
     * @param map errorPageParameters
     * @return List 타입으로 변환된 errorPageParameters
     */
    private List<ErrorPageParameter> convertErrorPageParameters(Map<String, Object> map) {
    	if (CollectionUtils.isEmpty(map)) {
    		return List.of();
    	}
    	
    	return map.entrySet().stream()
    			.filter(e -> e.getValue() != null)
    			.map(e -> {
    				ErrorPageParameter p = new ErrorPageParameter();
    				p.setName(e.getKey());
    				p.setValue(String.valueOf(e.getValue()));
    				
    				return p;
    			})
    			.collect(Collectors.toList());
    }
}
