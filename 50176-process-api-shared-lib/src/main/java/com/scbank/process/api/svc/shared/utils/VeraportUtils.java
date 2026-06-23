package com.scbank.process.api.svc.shared.utils;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.wizvera.util.Base64;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class VeraportUtils {

    /* Base64형태로 변환하기위한 Padding 변수처리 */
    private final String[] Base64Padding = new String[] { "", "===", "==", "=" };

    /**
     * Base64 URL Safe Decode 수행
     * STEP01 : [-] -> [+] , [_] -> [/] String Replace
     * STEP02 : Base64 Padding 추가
     * STEP03 : Base64 Decode 진행.
     * 
     * @param str
     * @return
     */
    public byte[] base64SafeDecode(String str) {
        log.debug("####PSH VeraportUtils base64SafeDecode str : {}", str);
        byte[] result = null;
        if (StringUtils.defaultIfEmpty(str, "").equals("")) { // 빈값이면 NULL 리턴한다.
            return result;
        }
        String b64 = str.replace('-', '+').replace('_', '/').replaceAll("\\r|\\n", ""); // URL Safe 변환 코드 Base64Encoding
                                                                                        // 형태로 변환
        b64 = b64 + Base64Padding[b64.length() % 4];
        log.debug("####PSH VeraportUtils base64SafeDecode b64 : {}", b64);
        result = Base64.decode(b64);
        log.debug("####PSH VeraportUtils base64SafeDecode result : {}", new String(result, 0, result.length));
        return result;
    }

    /**
     * Base64 URL Safe Decode 수행
     * STEP01 : [-] -> [+] , [_] -> [/] String Replace
     * 
     * @param str
     * @return
     */
    public String base64SafeReplace(String str) {
        log.debug("####PSH VeraportUtils base64SafeReplace str : {}", str);
        String result = "";
        if (StringUtils.defaultIfEmpty(str, "").equals("")) { // 빈값이면 NULL 리턴한다.
            return result;
        }
        result = str.replace('-', '+').replace('_', '/').replaceAll("\\r|\\n", ""); // URL Safe 변환 코드 Base64Encoding 형태로
                                                                                    // 변환
        log.debug("####PSH VeraportUtils base64SafeReplace result : {}", result);
        return result;
    }

    /**
     * Base64 URL Safe Encode 수행
     * STEP01 : [+] -> [-] , [/] -> [_] String Replace
     * 
     * @param str
     * @return
     */
    public String base64EncodeSafeReplace(String str) {
        String result = "";
        if (StringUtils.defaultIfEmpty(str, "").equals("")) { // 빈값이면 NULL 리턴한다.
            return result;
        }
        result = str.replace('+', '-').replace('/', '_').replaceAll("\\n|\\r", "");
        return result;
    }
}
