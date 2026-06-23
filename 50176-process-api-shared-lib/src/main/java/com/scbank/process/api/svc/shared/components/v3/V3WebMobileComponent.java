package com.scbank.process.api.svc.shared.components.v3;

import com.ahnlab.v3mobileplus.webinterface.LicenseKeyManager;
import com.ahnlab.v3mobileplus.webinterface.V3MobileManager;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
public class V3WebMobileComponent {

    @ComponentOperation
    public String getV3LicenseEncTime() {
        String time = ""; // 라이선스키 암호화 및 연동 시작 명령 전달 시 사용할 time 값

        try {
            time = LicenseKeyManager.getInstance().getTimeValueForGettingEncLicenseKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }

    @ComponentOperation
    public String getV3LicenseKeyEncrypt(String encTime) {

        String licenskey = PropertiesUtils.getString("V3_MOBILE_WEB_LICENSEKEY");

        String v3EncryptKey = "";

        try {
            log.debug("### License properties : []", licenskey);
            log.debug("### License time : []", encTime);

            v3EncryptKey = LicenseKeyManager.getInstance().getEncLicenseKey(licenskey, encTime);

            log.debug("### License Encrypt : []", v3EncryptKey);
        } catch (Exception e) {
            e.printStackTrace();

            log.debug("### getV3LicenseKeyEncrypt V3 Web LicenseKey 암호화시 오류발생");
        }

        return v3EncryptKey;
    }

    @ComponentOperation
    public static String getV3CryptoKeyVersion() {
        String v3CryptoKey = "";
        try {

            int cryptoKeyVersion = V3MobileManager.getInstance().getCryptoKeyVersion();
            log.debug("### cryptoKeyVersion : [" + cryptoKeyVersion + "]");

            v3CryptoKey = Integer.toString(cryptoKeyVersion);

            log.debug("### v3CryptoKey : [" + v3CryptoKey + "]");

        } catch (Exception e) {
            log.debug("### getV3CryptoKeyVersion 추출중 오류발생");
        }

        return v3CryptoKey;
    }

}
