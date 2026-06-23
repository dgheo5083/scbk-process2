package com.scbank.process.api.svc.shared.components.frs;

public interface IFrsClient {
	
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_AUTHORIZATION_TYPE_FORMAT = "Bearer %s";

	public static final String ERR_MA001 = "KFTC-MA001";
	public static final String ERR_MA001_MSG = "JSON DATA를 확인해 주세요.";
	public static final String ERR_MA002 = "KFTC-MA002";
	public static final String ERR_MA002_MSG = "RESPONSE DATA NOT FOUND";
	public static final String ERR_MA003 = "KFTC-MA003";
	public static final String ERR_MA003_MSG = "해당 경로가 존재하지 않습니다.";
	public static final String ERR_MA004 = "KFTC-MA004";
	public static final String ERR_MA004_MSG = "연동 정보가 유효하지 않습니다.";
	public static final String ERR_MA005 = "KFTC-MA005";
	public static final String ERR_MA005_MSG = "-";
	public static final String ERR_MA006 = "MA006";
	public static final String ERR_MA006_MSG = "MA006";
	public static final String ERR_MA007 = "MA007";
	public static final String ERR_MA007_MSG = "MA007";
	public static final String ERR_MA008 = "MA008";
	public static final String ERR_MA008_MSG = "-";
	public static final String ERR_MA999 = "MA999"; // 기타

	
	public static final String ERR_MA101 = "MA101";
	public static final String ERR_MA101_MSG = "관리자에게 문의하세요(접속정보 오류)";
	public static final String ERR_MA102 = "MA002";
	public static final String ERR_MA102_MSG = "RESPONSE DATA NOT FOUND";
	public static final String ERR_MA103 = "MA003";
	public static final String ERR_MA103_MSG = "MA003";
	
}
