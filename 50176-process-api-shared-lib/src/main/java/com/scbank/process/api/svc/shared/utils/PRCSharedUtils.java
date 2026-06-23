package com.scbank.process.api.svc.shared.utils;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import com.scbank.process.api.fw.base.store.ThreadLocalStoreDelegator;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.constants.PRCSharedConstants;
import com.scbank.process.api.svc.shared.constants.PRCSharedEnums.ChannelType;
import com.scbank.process.api.svc.shared.constants.PRCSharedEnums.LangType;
import com.scbank.process.api.svc.shared.constants.PRCSharedEnums.OsType;
import com.scbank.process.api.svc.shared.constants.PRCSharedEnums.VanType;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PRCSharedUtils {

    public static String getScBankName() {
        return StringUtils.nvl(CodeUtils.getCodeValue("BKCODE_OBS", PRCSharedConstants.SC_BANK_CODE), ";;")
                .split(";;", 2)[0];
    }

    public static String getAccountName(@NonNull String kwamok, String assort) {
        Locale locale = LocaleContextHolder.getLocale();
        String langCode = locale.getLanguage();

        return getAccountName(langCode, kwamok, assort);
    }

    /**
     * 과목코드와 종별 코드를 이용하여 계좌의 계좌명을 가지고 온다.
     * 
     * @title 과목코드와 종별 코드를 이용하여 계좌의 계좌명을 가지고 온다.
     * @param language
     * @param kwamok   계좌번호의 점번호(3) + 과목코드(2)
     * @param assort
     * @return
     */
    public static String getAccountName(String language, @NonNull String kwamok,
            String assort) {

        // if (kwamok.isBlank()) {
        // return "code값이 없습니다.";
        // }

        final String ASSORT_FF = "FF";
        final String CODE_KEY_ACCTNM = "ACCTNM";

        String acctName = "";
        if (assort != null) {
            // 과목명 종별 코드를 이용하여 코드값 조회(code=getKwamokCode(code, assort);)
            acctName = CodeUtils.getCodeValue(CODE_KEY_ACCTNM, kwamok + assort, language);
            // 값이 없을 경우에는 종별코드+"FF" 값을 이용하여 처리
            if (acctName == null || acctName.length() == 0) {
                acctName = CodeUtils.getCodeValue(CODE_KEY_ACCTNM, kwamok + ASSORT_FF, language);
            }
            // 그래도 값이 없을 경우에는 과목만 이용하여 처리
            if (acctName == null || acctName.length() == 0) {
                acctName = CodeUtils.getCodeValue(CODE_KEY_ACCTNM, kwamok, language);
            }
        }

        return acctName;
    }

    /**
     * 카드의 종류에 따른 카드명을 반환한다.
     * 
     * @title 카드의 종류에 따른 카드명을 반환한다.
     * @param kind
     * @return
     */
    public static String getCardNameByKind(String kind) {
        String result = "";
        if (kind == null || kind.equals("")) {
            return result;
        }

        if (kind.equals("1")) {
            result = "BC";
        } else if (kind.equals("2")) {
            result = "VISA";
        } else if (kind.equals("3")) {
            result = "JCB";
        } else if (kind.equals("4")) {
            result = "MASTER";
        } else if (kind.equals("6")) {
            result = "CUP";
        } else if (kind.equals("7")) {
            result = "GLOBAL";
        }
        return result;
    }

    /**
     * 과목코드와 종별 코드를 이용하여 계좌의 계좌명을 가지고 온다.
     * 
     * @param kwamok       계좌번호의 점번호(3) + 과목코드(2)
     * @param assrot
     * @param loanKind
     * @param LoanAcctKmCD
     * @return
     */
    public static String getLoanAccountName(String kwamok, String assrot, String loanKind, String loanAcctKmCd) {

        Locale locale = LocaleContextHolder.getLocale();
        String langCode = locale.getLanguage();

        return getLoanAccountName(langCode, kwamok, assrot, loanKind, loanAcctKmCd);
    }

    /**
     * 과목코드와 종별 코드를 이용하여 계좌의 계좌명을 가지고 온다.
     * 
     * @param language
     * @param kwamok       계좌번호의 점번호(3) + 과목코드(2)
     * @param assrot
     * @param loanKind
     * @param LoanAcctKmCD
     * @return
     */
    public static String getLoanAccountName(String language, String kwamok, String assort, String loanKind,
            String loanAcctKmCd) {
        if (kwamok == null || kwamok.length() == 0) {
            return "code값이 없습니다.";
        }
        String code = kwamok;
        String loanKindCode = StringUtils.defaultString(loanKind);
        String loanAcctkmCdCode = StringUtils.defaultString(loanAcctKmCd);
        String acctName = "";

        if (assort != null) {
            // 과목명 종별 코드를 이용하여 코드값 조회
            // code = getKwamokCode(code, assort);

            // 2020.06.18 대출 계정코드 및 자금용도 필드 추가
            if ((!"".equalsIgnoreCase(loanKindCode) && loanKindCode.length() > 0)
                    && (!"".equalsIgnoreCase(loanAcctkmCdCode) && loanAcctkmCdCode.length() > 0)) {
                acctName = CodeUtils.getCodeValue("OGYECDACCTNM", loanAcctKmCd + "|" + loanKind);
            }
            // 202308정기 대출 상품명노출추가
            if (acctName == null || acctName.length() == 0) {
                acctName = CodeUtils.getCodeValue("OGYECDACCTNM", loanAcctKmCd + "|FFF");
            }

            if (acctName == null || acctName.length() == 0) {
                acctName = CodeUtils.getCodeValue("ACCTNM", code + assort);
            }
            // 값이 없을 경우에는 종별코드 + &apos;FF&apos;값을 이용하여 처리
            if (acctName == null || acctName.length() == 0) {
                acctName = CodeUtils.getCodeValue("ACCTNM", kwamok + "FF");
            }
            // 그래도 값이 없을 경우에는 과목만 이용하여 처리
            if (acctName == null || acctName.length() == 0) {
                acctName = CodeUtils.getCodeValue("ACCTNM", kwamok);
            }
        }

        return acctName;
    }

    public static boolean isKorean() {
        if (LangType.KOREAN == LangType.fromCode(getLanguageHeader())) {
            return true;
        }
        return false;
    }
    
    public static boolean isEnglish() {
    	if (LangType.ENGLISH == LangType.fromCode(getLanguageHeader())) {
    		return true;
    	}
    	return false;
    }

    /**
     * VAN_TYPE의 값을 가지고 온다
     * 
     * @return vanTypeCode
     */
    public static String getVanType() {
        return getVanType(getChannelId(), getLanguageHeader());
    }

    /**
     * VAN_TYPE의 값을 가지고 온다
     * 
     * @param langCode
     * @param channelName
     * @return
     */
    public static String getVanType(String langCode, String channelId) {
        String returnValue = "";

        if (ChannelType.SMART == ChannelType.fromId(channelId)) {
            ibVanType(langCode);
        } else if (ChannelType.INTERNET == ChannelType.fromId(channelId)) {
            sbVanType(langCode);
        }
        // else if (CSLSharedConstants.CIB_CHANNEL_NAME.equals(channelName)) {
        // returnValue = VanType.KR_CIB_VAN_TYPE.getCode();
        // }

        return returnValue;
    }

    public static String ibVanType(String langCode) {
        if (LangType.KOREAN == LangType.fromCode(langCode)) {
            return VanType.KR_IB_VAN_TYPE.getCode();
        } else if (LangType.ENGLISH == LangType.fromCode(langCode)) {
            return VanType.EN_IB_VAN_TYPE.getCode();
        }
        return "";
    }

    public static String sbVanType(String langCode) {
        if (LangType.KOREAN == LangType.fromCode(langCode)) {
            return VanType.KR_SB_VAN_TYPE.getCode();
        } else if (LangType.ENGLISH == LangType.fromCode(langCode)) {
            return VanType.EN_SB_VAN_TYPE.getCode();
        }

        return "";
    }

    public static boolean isSB() {
        if (ChannelType.SMART == ChannelType.fromId(getChannelId())) {
            return true;
        }
        return false;
    }

    public static boolean isIB() {
        if (ChannelType.INTERNET == ChannelType.fromId(getChannelId())) {
            return true;
        }
        return false;
    }

    public static boolean isAndroid() {
        if (OsType.ANDROID == OsType.fromValue(StringUtils.defaultString(PRCSharedUtils.getOsType()))) {
            return true;
        }
        return false;
    }

    // 헤더정보 작업

    /**
     * Language Header 문자열을 가져온다.
     * 
     * @return Language Header 문자열
     */
    public static String getLanguageHeader() {
        return ThreadLocalStoreDelegator.getLanguageHeader();
    }

    /**
     * 요청 채널ID 문자열을 가져온다.
     * 
     * @return 요청 채널ID 문자열
     */
    public static String getChannelId() {
        return ThreadLocalStoreDelegator.getChannelId();
    }

    /**
     * 요청 디바이스ID를 가져온다.
     * 
     * @return 요청 디바이스ID 문자열
     */
    public static String getDeviceId() {
        return ThreadLocalStoreDelegator.getDeviceId();
    }

    /**
     * 앱 버전 문자열을 가져온다.
     * 
     * @return 앱 버전 문자열
     */
    public static String getAppVersion() {
        return ThreadLocalStoreDelegator.getAppVersion();
    }

    /**
     * OS 타입 문자열을 가져온다.
     * - android/ios/windows
     * 
     * @return OS 타입 문자열
     */
    public static String getOsType() {
        return StringUtils.defaultString(ThreadLocalStoreDelegator.getOsType()).toLowerCase();
    }

    /**
     * OS Version 문자열을 가져온다.
     * 
     * @return OS Version 문자열
     */
    public static String getOsVersion() {
        return ThreadLocalStoreDelegator.getOsVersion();
    }

    /**
     * 요청 화면ID를 가져온다.
     * 
     * @return 요청 화면ID
     */
    public static String getScreenId() {
        return ThreadLocalStoreDelegator.getScreenId();
    }

    /**
     * 요청 메뉴ID를 가져온다.
     * 
     * @return 요청 메뉴ID
     */
    public static String getMenuId() {
        return ThreadLocalStoreDelegator.getMenuId();
    }

    /**
     * IPINSIDE IP 문자열을 획득한다.
     * 
     * @return IPINSIDE IP 문자열
     */
    public static String getIpinsideIp() {
        return ThreadLocalStoreDelegator.getIpinsideIp();
    }

    /**
     * IPINSIDE AX 문자열을 획득한다.
     * 
     * @return IPINSIDE AX 문자열
     */
    public static String getIpinsideAx() {
        return ThreadLocalStoreDelegator.getIpinsideAx();
    }

    /**
     * IPINSIDE MAC 문자열을 획득한다.
     * 
     * @return IPINSIDE MAC 문자열
     */
    public static String getIpinsideMac() {
        return ThreadLocalStoreDelegator.getIpinsideMac();
    }

    /**
     * IPINSIDE HDD 문자열을 획득한다.
     * 
     * @return IPINSIDE HDD 문자열
     */
    public static String getIpinsideHdd() {
        return ThreadLocalStoreDelegator.getIpinsideHdd();
    }
    
    /**
     * 
     * @return
     */
    public static String getDeviceUUID() {
    	return ThreadLocalStoreDelegator.getDeviceUUID();
    }
    
    /**
     * 
     * @return
     */
    public static String getSimSerial() {
    	return ThreadLocalStoreDelegator.getSimSerial();
    }

}
