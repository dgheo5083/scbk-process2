package com.scbank.process.api.svc.shared.components.obs.kftc.model;

import java.util.Date;

import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpJsonObject;
import com.scbank.process.api.svc.shared.components.obs.utils.ObsDateUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 금결원 오픈뱅킹 연계 인증정보 VO
 */
@Slf4j
@Getter
@Setter
public class KftcObsAuthVo {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private Date expiresDate;
    private String scope;
    private String clinetUseCode;
    private Object logData;

    public KftcObsAuthVo clone(KftcObsAuthVo info) {
        setAccessToken(info.getAccessToken());
        setTokenType(info.getTokenType());
        setExpiresIn(info.getExpiresIn());
        setScope(info.getScope());
        setClinetUseCode(info.getClinetUseCode());
        setExpiresDate(info.getExpiresDate());

        return this;
    }

    public KftcObsAuthVo clone(ObsHttpJsonObject responseJson) {
        setAccessToken(responseJson.getAsString("access_token", ""));
        setTokenType(responseJson.getAsString("token_type", ""));
        setExpiresIn(Long.parseLong(responseJson.getAsString("expires_in", "0")));
        setScope(responseJson.getAsString("scope", ""));
        setClinetUseCode(responseJson.getAsString("client_use_code", ""));
        setExpiresDate(responseJson.getAsString("expires_date", "19700101"));

        return this;
    }

    /**
     * 밀리세컨드 / 1000 된 expiresIn의 데이타를 day로 바꾼다.
     * 
     * @return
     */
    public long getExpiresDay() {
        return (this.expiresIn / (60 * 60 * 24));
    }

    public Date getExpiresDate() {
        return this.expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }

    public void setExpiresDate(String expiresDate) {
        this.expiresDate = DateUtils.string2Date(expiresDate, "yyyyMMdd");
    }

    /**
     * AccessToken키를 다시 가져올지를 판단한다.
     * 
     * @return
     */
    public boolean isAccessToken() {
        log.debug("### KFTC Access Token CHECK ### ");

        if (this.accessToken != null && !"".equals(this.accessToken.trim())) {
            // access token에 값이 있으면 유효기간를 확인한다.
            int dtAccessYear = ObsDateUtils.getYear(getExpiresDate()) * 100;
            int checkAccess = dtAccessYear + ObsDateUtils.getMonth(getExpiresDate());

            int dtCurrYear = ObsDateUtils.getYear(new Date()) * 100;
            int checkCurr = dtCurrYear + ObsDateUtils.getMonth(new Date());

            // access token이 없거나 유효기간이 같으면 바로 access키를 다시 가져온다.
            log.debug(" - 만료월 -1: [{}] 현재월: [{}] > accessToken : {}", (checkAccess - 1), checkCurr, this.accessToken);

            if (checkAccess - 1 <= checkCurr) {
                log.debug(" - FALSE (기간만류) ");
                // return true; //2020.01.03 SK2.임시 삭제.
                return false;
            }

            log.debug(" - TRUE (유효한 상태) ");

            return true;
        }

        log.debug(" - FALSE (보유키 없음: )");

        return false;
    }

    /**
     * AccessToken키를 다시 가져올지를 판단한다.
     * 
     * @return
     */
    public boolean isAccessToken_pms() {
        log.debug("### KFTC Access Token CHECK ### ");
        if (this.accessToken != null && !"".equals(this.accessToken.trim())) {
            log.debug(" - TRUE (기간 만료) ");
            return true;
        }
        log.debug(" - FALSE (보유키 없음: )");
        return true;
    }
}
