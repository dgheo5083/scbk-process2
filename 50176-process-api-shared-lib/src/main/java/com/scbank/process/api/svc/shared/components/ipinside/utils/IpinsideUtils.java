package com.scbank.process.api.svc.shared.components.ipinside.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class IpinsideUtils {

    public String getCouturyCode(String code) {
        String res = "";
        // 대한민국 - 국가코드(107)
        // SC제일은행 등록코드 - 단순코드
        String[] SCFBOrderList = { "001", "002", "003", "004", "005", "006", "007", "008", "009", "010" };
        // 인터리젠 등록코드 - 국가코드{중국,미국,일본,캐나다,대만,필리핀,태국,말레이지아,베트남,러시아}
        String[] InterezenCountryList = { "44", "206", "100", "35", "200", "160", "191", "141", "214", "171" };

        log.debug("@@@ getCouturyCode code[" + code + "]");
        for (int i = 0; i < SCFBOrderList.length; i++) {
            if (SCFBOrderList[i].equals(code)) {
                res = InterezenCountryList[i];
                break;
            }
        }
        log.debug("@@@ getCouturyCode code[" + code + "], res[" + res + "]");
        return res;
    }

}
