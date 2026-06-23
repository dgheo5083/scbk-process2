package com.scbank.process.api.svc.shared.components.mydata.mdc.model;

import java.util.Date;

import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpJsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MdcMyDataAuthVo {
	private String accessToken;
	private String tokenType;
	private long expiresIn;
	private Date expiresDate;
	private String refreshToken;
	private String tokenId;

	private Object logData;

	public MdcMyDataAuthVo() {

	}

	public MdcMyDataAuthVo clone(MdcMyDataAuthVo info) {
		log.debug("::::: MdcMyDataAuthVo clone MdcMyDataAuthVo ::::: ");

		setAccessToken(info.getAccessToken());
		setTokenType(info.getTokenType());
		setExpiresIn(info.getExpiresIn());
		setExpiresDate(info.getExpiresDate());
		setRefreshToken(info.getRefreshToken());
		setTokenId(info.getTokenId());

		return this;
	}

	public MdcMyDataAuthVo clone(MyDataHttpJsonObject responseJson) {
		log.debug("::::: MdcMyDataAuthVo clone MyDataHttpJsonObject ::::: ");

		setAccessToken(responseJson.getAsString("access_token", ""));
		setTokenType(responseJson.getAsString("token_type", ""));
		setExpiresIn(Long.parseLong(responseJson.getAsString("expires_in", "0")));
		setExpiresDate(responseJson.getAsString("expires_date", "19700101"));

		return this;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Object getLogData() {
		return this.logData;
	}

	public void setLogData(Object logData) {
		this.logData = logData;
	}

	/**
	 * 밀리세컨드 / 1000 된 expiresIn의 데이타를 day로 바꾼다.
	 * 
	 * @return
	 */
	public long getExpiresDay() {
		return (this.expiresIn / (60 * 60 * 24 * 9));
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

		log.debug("##### MdcMyDataAuthVo isAccessToken CHECK ##### ");

		if (this.accessToken != null && !"".equals(this.accessToken.trim())) { // access token에 값이 있으면 유효기간를 확인한다.
			long epoch = getExpiresIn();
			epoch = epoch - (60 * 60 * 24 * 10); // 만료일 10일전 날짜 계산

			String acssDay = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date(epoch * 1000));
			String currDay = DateUtils.getCurrentDate("yyyyMMdd");

			// access token이 없거나 유효기간이 10 전일짜 와 같으면 바로 access키를 다시 가져온다.
			log.debug(" - 만료일 -10: [" + (acssDay) + "] 현재일: [" + currDay + "]");
			log.debug(" - accessToken : " + this.accessToken);

			if (Integer.parseInt(acssDay) == Integer.parseInt(currDay)) {
				log.debug(" - FALSE (기간만료) ");
				return false;
			}

			log.debug(" - TRUE (유효한 상태) ");

			return true;
		}

		log.debug(" - FALSE (보유키 없음: )");

		return false;
	}

}
