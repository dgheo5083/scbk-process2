package com.scbank.process.api.svc.shared.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class SecurityFilterUtils {

    /**
     * Path Manipulation 보안취약점 필터
     * 
     * @param sInvalid
     * @return
     */
    public String getPMFilter(String sInvalid) {
        String sValid = sInvalid;

        if (sValid == null || sValid.equals(""))
            return "";

        sValid = sValid.replaceAll("\\.\\./", "");
        return sValid;
    }
}
