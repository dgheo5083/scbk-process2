package com.scbank.process.api.svc.shared.components.alchera;

/**
 * 알체라 API 요청 클라이언트 인터페이스
 */
public interface AlcClient {
    public static final String ERR_MA001 = "ALC-MA001";
    public static final String ERR_MA001_MSG = "JSON DATA를 확인해 주세요.";

    public static final String ERR_MA002 = "ALC-MA002";
    public static final String ERR_MA002_MSG = "응답 데이터가 존재 하지 않습니다.";

    public static final String ERR_MA003 = "ALC-MA003";
    public static final String ERR_MA003_MSG = "해당 경로가 존재하지 않습니다.";

    public static final String ERR_MA004 = "ALC-MA004";
    public static final String ERR_MA004_MSG = "통신 정보가 유효하지 않습니다.";

    public static final String ERR_MA005 = "ALC-MA005";
    public static final String ERR_MA005_MSG = "지원하지 않는 METHOD TYPE입니다.";

    public static final String ERR_MA006 = "ALC-MA006";
    public static final String ERR_MA006_MSG = "신분증 인식 이미지가 존재 하지 않습니다.";

    public static final String ERR_MA007 = "ALC-MA007";
    public static final String ERR_MA007_MSG = "알체라 복호화 에러가 발생 하였습니다.";

    public static final String ERR_MA008 = "ALC-MA008";
    public static final String ERR_MA008_MSG = "알체라 암호화 에러가 발생 하였습니다.";

    // 안면인식 수정 건 START - 20250429
    public static final String ERR_MA009 = "ALC-MA009";
    public static final String ERR_MA009_MSG = "지원하지 않는 API 입니다.";

    public static final String ERR_MA010 = "ALC-MA010";
    public static final String ERR_MA010_MSG = "안면 인식 이미지가 존재 하지 않습니다.";

    public static final String ERR_MA998 = "ALC-MA998";
    public static final String ERR_MA998_MSG = "안면인식 압축 실패 하였습니다.";
    // 안면인식 수정 건 END - 20250429

    public static final String ERR_MA999 = "ALC-MA999";
    public static final String ERR_MA999_MSG = "알수 없는 에러. 알체라 에러코드를 확인하세요.";

}
