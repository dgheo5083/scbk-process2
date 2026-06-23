package com.scbank.process.api.fw.common.errorcode;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.i18n.LocaleContextHolder;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.lifecycle.IReloadable;

/**
 * 프레임워크 에러코드 매니저 인터페이스
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public interface IErrorCodeManager extends IReloadable, InitializingBean {

    @Override
    default void afterPropertiesSet() {
        init();
    }

    /**
     * 초기화 수행
     */
    void init();

    /**
     * 에러코드로 등록된 에러코드 정보를 가져온다.
     *
     * @param errorCode 에러코드
     * @return 에러코드 정보
     */
    default IErrorCodeInfo getErrorCodeInfo(String errorCode) {
        Locale locale = LocaleContextHolder.getLocale();
        String langCode = locale.getLanguage();
        return this.getErrorCodeInfo(errorCode, langCode);
    }

    /**
     * 에러코드 + 언어코드로 등록된 에러코드 정보를 가져온다.
     *
     * @param errorCode 에러코드
     * @param langCode  언어코드
     * @return 에러코드 정보
     */
    IErrorCodeInfo getErrorCodeInfo(String errorCode, String langCode);

    /**
     * 에러코드로 등록된 에러 메시지를 가져온다.
     *
     * @param errorCode 에러코드
     * @return 에러메시지
     */
    @ComponentOperation(name = "에러메시지 획득")
    default String getErrorMessage(String errorCode) {
        Locale locale = LocaleContextHolder.getLocale();
        String langCode = locale.getLanguage();
        return this.getErrorMessage(errorCode, langCode);
    }
    
    /**
     * 에러코드로 등록된 에러메시지를 가져온다.
     * @param errorCode
     * @param args
     * @return
     */
    @ComponentOperation(name = "에러메시지 획득 With args")
    default String getErrorMessage(String errorCode, Object...args) {
    	Locale locale = LocaleContextHolder.getLocale();
        String langCode = locale.getLanguage();
        return this.getErrorMessage(errorCode, langCode, args);
    }

    /**
     * 에러코드 + 언어코드로 등록된 에러 메시지를 가져온다.
     *
     * @param errorCode 에러코드
     * @param langCode  언어코드
     * @return 에러 메시지
     */
    String getErrorMessage(String errorCode, String langCode);
    
    /**
     * 
     * @param errorCode
     * @param langCode
     * @param args
     * @return
     */
    String getErrorMessage(String errorCode, String langCode, Object...args);

    /**
     * 에러코드에 등록된 가이드 메시지 목록을 가져온다.
     *
     * @param errorCode 에러코드
     * @return 에러코드에 등록된 가이드 메시지 목록
     */
    default List<IErrorGuideMessageInfo> getErrorGuideMessages(String errorCode) {
        Locale locale = LocaleContextHolder.getLocale();
        String langCode = locale.getLanguage();
        return this.getErrorGuideMessages(errorCode, langCode);
    }

    /**
     * 에러코드 + 언어코드로 에러코드정보 하위 가이드 메시지 목록을 가져온다.
     *
     * @param errorCode 에러코드
     * @param langCode  언어코드
     * @return 에러코드정보에 등록된 가이드 메시지 목록
     */
    List<IErrorGuideMessageInfo> getErrorGuideMessages(String errorCode, String langCode);

}
