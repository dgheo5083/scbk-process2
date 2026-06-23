package com.scbank.process.api.svc.shared.components.frs.kftc.model;

import java.util.Date;

import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpJsonObject;
import com.scbank.process.api.svc.shared.components.frs.util.FrsDateUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class KftcFrsAuthVo {
	private String accessToken;
	private String tokenType;
	private long expiresIn;
	private Date expiresDate;
	private String scope;

	private Object logData;

	public KftcFrsAuthVo clone(KftcFrsAuthVo info) {

		setAccessToken(info.getAccessToken());
		setTokenType(info.getTokenType());
		setExpiresIn(info.getExpiresIn());
		setScope(info.getScope());
		setExpiresDate(info.getExpiresDate());

		return this;
	}

	public KftcFrsAuthVo clone(FrsHttpJsonObject responseJson) {

		setAccessToken(responseJson.getAsString("access_token", ""));
		setTokenType(responseJson.getAsString("token_type", ""));
		setExpiresIn(Long.parseLong(responseJson.getAsString("expires_in", "0")));
		setScope(responseJson.getAsString("scope", ""));
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

		log.debug("### 안면인식 금결원 Access Token CHECK ### ");

		if (this.accessToken != null && !"".equals(this.accessToken.trim())) {
			// access token에 값이 있으면 유효기간를 확인한다.
			int dtAcssYear = FrsDateUtils.getYear(getExpiresDate()) * 100;
			int checkAccess = dtAcssYear + FrsDateUtils.getMonth(getExpiresDate());

			int dtCurrYear = FrsDateUtils.getYear(new Date()) * 100;
			int checkCurr = dtCurrYear + FrsDateUtils.getMonth(new Date());

			log.debug("### 안면인식 금결원 - 만료월 -1: [" + (checkAccess - 1) + "] 현재월: [" + checkCurr + "] > accessToken : " + this.accessToken);

			if (checkAccess - 1 <= checkCurr) {
				log.debug("### 안면인식 금결원 - FALSE (기간만류) ");
				return false;
			}

			log.debug("### 안면인식 금결원 - TRUE (유효한 상태) ");

			return true;
		}

		log.debug(" - FALSE (보유키 없음: )");

		return false;
	}

}
