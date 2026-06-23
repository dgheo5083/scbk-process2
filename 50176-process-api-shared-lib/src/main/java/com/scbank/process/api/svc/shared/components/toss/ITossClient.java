package com.scbank.process.api.svc.shared.components.toss;

import com.scbank.process.api.svc.shared.components.toss.model.TossHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.toss.model.TossHttpResponseEntity;

/**
 * 토스 연계 클라이언트 인터페이스
 */
public interface ITossClient {

    public static final String ERR_MA001 = "TOSS-MA001";
    public static final String ERR_MA001_MSG = "JSON DATA를 확인해 주세요.";
    public static final String ERR_MA002 = "TOSS-MA002";
    public static final String ERR_MA002_MSG = "RESPONSE DATA NOT FOUND";
    public static final String ERR_MA003 = "TOSS-MA003";
    public static final String ERR_MA003_MSG = "해당 경로가 존재하지 않습니다.";
    public static final String ERR_MA004 = "TOSS-MA004";
    public static final String ERR_MA004_MSG = "연동 정보가 유효하지 않습니다.";
    public static final String ERR_MA999 = "TOSS-MA999"; // 기타

    /**
     * 토스 API 서버 HTTP 송/수신 처리
     * @param entity 토스 API 요청 Entity {@link TossHttpRequestEntity}
     * @return {@link TossHttpResponseEntity}
     */
    TossHttpResponseEntity execute(TossHttpRequestEntity entity);
}
