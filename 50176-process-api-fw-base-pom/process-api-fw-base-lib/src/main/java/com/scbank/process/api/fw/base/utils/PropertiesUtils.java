package com.scbank.process.api.fw.base.utils;

import com.scbank.process.api.fw.common.property.IPropertyManager;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 공통 프로퍼티 유틸리티 클래스
 */
@Slf4j
@UtilityClass
public class PropertiesUtils {

    private static IPropertyManager propertyManager;

    /**
     * 지정된 키에 해당되는 프라퍼티 값을 반환한다.
     * 
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getString(key, "");
    }

    /**
     * 지정된 키에 해당되는 프라퍼티 값을 반환한다.
     * 
     * @param key          프로퍼티 키
     * @param defaultValue 기본값
     * @return
     */
    public static String getString(String key, String defaultValue) {
        IPropertyManager propertyManager = getPropertyManager();
        return propertyManager.getString(key, defaultValue);
    }

    /**
     * 지정된 키에 해당되는 프라퍼티 값을 반환한다.
     * 
     * @param key 프로퍼티 키
     * @return
     */
    public static int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * 지정된 키에 해당되는 프라퍼티 값을 반환한다.
     * 
     * @param key          프로퍼티 키
     * @param defaultValue 기본 값
     * @return
     */
    public static int getInt(String key, int defaultValue) {
        IPropertyManager propertyManager = getPropertyManager();
        return propertyManager.getInt(key, defaultValue);
    }

    private static IPropertyManager getPropertyManager() {
        if (propertyManager == null) {
            propertyManager = RuntimeContext.getBean(IPropertyManager.class);
        }
        return propertyManager;
    }
}
