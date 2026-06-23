package com.scbank.process.api.fw.base.channel.message;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.common.errorcode.DefaultErrorMessage;
import com.scbank.process.api.fw.common.errorcode.IErrorCodeInfo;
import com.scbank.process.api.fw.common.errorcode.IErrorCodeManager;
import com.scbank.process.api.fw.common.errorcode.IErrorGuideMessageInfo;
import com.scbank.process.api.fw.core.error.IErrorMessage;
import com.scbank.process.api.fw.core.error.IErrorMessageResolver;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.RequiredArgsConstructor;

/**
 * 제일은행 프로세스API 기본 오류 응답 메시지 처리 Resolver
 * 
 * @author sungdon.choi
 */
@Component
@RequiredArgsConstructor
public class DefaultErrorMessageResolver implements IErrorMessageResolver {

    /**
     * 프레임워크 오류코드 매니저 인터페이스
     */
    private final IErrorCodeManager errorCodeManager;

    @Override
    public IErrorMessage resolveMessage(String errorCode, Object[] args, Locale locale) {
        // 에러코드정보 획득
        IErrorCodeInfo errorCodeInfo = this.getErrorCodeInfo(errorCode, locale);
        // 에러메시지 획득
        String errorMessage = errorCodeInfo != null ? errorCodeInfo.getMessage() : StringUtils.EMPTY;
        
        //[2026.04.22 최성돈] 오류메시지 args 처리 추가
        if (args != null && args.length > 0) {
        	errorMessage = MessageFormat.format(errorMessage, args);
        }

        // 에러 가이드 메시지 목록 획득
        List<String> errorGuideMessageList = List.of();
        if (errorCodeInfo != null) {
            List<IErrorGuideMessageInfo> errorGuideMessageInfoList = errorCodeInfo.getErrorGuideMessages();
            if (!CollectionUtils.isEmpty(errorGuideMessageInfoList)) {
                errorGuideMessageList = errorGuideMessageInfoList.stream().map((v) -> {
                    return v.getMessage();
                }).toList();
            }
        }

        return DefaultErrorMessage.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .errorGuideMessages(errorGuideMessageList)
                .build();
    }

    /**
     * 프레임워크에 등록된 오류코드정보를 획득한다.
     * 
     * @param errorCode 에러코드 문자열
     * @param locale    로케일 Locale
     * @return IErrorCodeInfo
     */
    private IErrorCodeInfo getErrorCodeInfo(String errorCode, Locale locale) {
        IErrorCodeInfo errorCodeInfo = errorCodeManager.getErrorCodeInfo(errorCode, locale.getLanguage());
        return errorCodeInfo;
    }
}
