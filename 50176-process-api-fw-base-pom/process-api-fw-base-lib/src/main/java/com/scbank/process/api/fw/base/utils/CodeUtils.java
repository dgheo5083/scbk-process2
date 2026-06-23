package com.scbank.process.api.fw.base.utils;

import java.util.List;
import java.util.Locale;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.common.code.ICodeManager;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.LocaleUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CodeUtils {

    /**
     * 
     */
    private static ICodeManager codeManager;

    private static String getDefaultLanguage() {
        String localeStr = RuntimeContext.getDefaultLocale();
        Locale locale = LocaleUtils.toLocale(localeStr);
        return locale.getLanguage();
    }

    private static String getLanguage() {
        IServiceContext sc = ServiceContextHolder.getContext();
        if (sc == null) {
            return getDefaultLanguage();
        }

        Locale locale = sc.locale();
        if (locale == null) {
            return getDefaultLanguage();
        }

        return locale.getLanguage();
    }

    /**
     * <pre>
     * 지정한 공통코드 카테고리 하위 코드목록을 지정한 코드 항목키로 검색하여 코드항목 값을 가져온다.
     * 
     * &#64;param category 공통코드 카테고리 키
     * &#64;param code 공통코드 항목 키
     * @return 지정한 코드키로 검색하여 코드 값
     * </pre>
     */
    public static String getCodeValue(String category, String code) {
        return getCodeValue(category, code, getLanguage());
    }

    /**
     * <pre>
     * 지정한 공통코드 카테고리 하위 코드목록을 지정한 코드 항목키로 검색하여 코드항목 값을 가져온다.
     * 
     * &#64;param category 공통코드 카테고리 키
     * &#64;param code 공통코드 항목 키
     * &#64;param metaSequencesFlag 개행문자 치환여부 true or false
     * @return 지정한 코드키로 검색하여 코드 값
     * </pre>
     */
    public static String getCodeValue(String category, String code, boolean metaSequencesFlag) {
        String langCode = getLanguage();
        String result = new String();
        if (metaSequencesFlag) {
            result = getCodeValue(category, code, langCode);
            if (result.indexOf("&#10;") != -1) {
                return result.replace("&#10;", "\n");
            }
        }
        return getCodeValue(category, code, langCode);
    }

    /**
     * <pre>
     * 지정한 공통코드 카테고리 하위 코드항목 목록을 가져온다.
     * 현재 요청의 언어코드로 등록된 코드항목 목록을 반환하며, 코드항목 목록이 없는 경우
     * 어플리케이션에서 기본으로 지정된 언어코드(ma30.default.lang.code)로 검색하여 코드항목 목록을 반환한다.
     * &#64;param category 공통코드 카테고리
     * @return 지정한 공통코드 카테고리 하위 코드항목 목록
     * </pre>
     */
    public static List<ICodeItemInfo> getCodes(String category) {
        String langCode = getLanguage();
        return getCodes(category, langCode, true);
    }

    /**
     * <pre>
     * 지정한 공통코드 카테고리 하위 코드목록을 지정한 코드 항목키로 검색하여 코드항목 값을 가져온다.
     * 
     * &#64;param category 공통코드 카테고리 키
     * &#64;param code 공통코드 항목 키
     * &#64;param langCode 언어코드(ko, en)
     * &#64;param isDefaultLanguage 기본 언어코드 저장된 공통코드 검색여부
     * @return 지정한 코드키로 검색하여 코드 값
     * </pre>
     */
    private static String getCodeValue(String category, String code, String langCode, boolean isDefaultLanguage) {
        if (isDefaultLanguage) {
            langCode = getDefaultLanguage();
        }
        return getCodeManager().getCodeItem(category, code, langCode);
    }

    /**
     * <pre>
     * 지정한 공통코드 카테고리 하위 코드목록을 지정한 코드 항목키로 검색하여 코드항목 값을 가져온다.
     * 
     * &#64;param category 공통코드 카테고리 키
     * &#64;param code 공통코드 항목 키
     * &#64;param langCode 언어코드(ko, en)
     * @return 지정한 코드키로 검색하여 코드 값
     * </pre>
     */
    public static String getCodeValue(String category, String code, String langCode) {
        return getCodeValue(category, code, langCode, true);
    }

    /**
     * <pre>
     * 
     * 지정한 공통코드 카테고리 하위 코드항목 목록을 가져온다.
     * 현재 요청의 언어코드로 등록된 코드항목 목록을 반환하며, 코드항목 목록이 없는 경우
     * isDefaultLanguege 여부에 따라 true인 경우 어플리케이션에서 기본으로 지정된 언어코드(ma30.default.lang.code)로 
     * 검색하여 코드항목 목록을 반환하며, false 인 경우 null을 반환한다.
     * 
     * &#64;param category 공통코드 카테고리
     * &#64;param langCode 언어코드 (ko, en)
     * &#64;param isDefaultLanguege 기본으로 지정된 언어설정으로 공통코드 검색 여부
     * @return 지정한 공통코드 카테고리 하위 코드항목 목록
     * </pre>
     */
    private static List<ICodeItemInfo> getCodes(String category, String langCode, boolean isDefaultLanguage) {
        if (isDefaultLanguage) {
            langCode = getDefaultLanguage();
        }
        return getCodeManager().getCodeItemList(category, langCode);
    }

    private static ICodeManager getCodeManager() {
        if (codeManager == null) {
            codeManager = RuntimeContext.getBean(ICodeManager.class);
        }
        return codeManager;
    }
}
