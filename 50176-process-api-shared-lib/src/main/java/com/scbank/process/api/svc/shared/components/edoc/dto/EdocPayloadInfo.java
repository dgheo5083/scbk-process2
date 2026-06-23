package com.scbank.process.api.svc.shared.components.edoc.dto;

import java.util.List;

import lombok.Data;

@Data
public class EdocPayloadInfo {

    private String action;

    private String custNo;

    private String tradNo;

    private String bizType;

    private List<EdocInfo> edocList;

    private String lpcHost;

    private String lpcMeta;

    /**
     * 대출 접수번호
     */
    private String loanAccptNo;

    /**
     * 대출 신청번호
     */
    private String headerApplicationId;

    /**
     * 발송할 이메일 주소
     */
    private String emailAddr;

    /* 퍼스트홈론 담보/소득제공자 파라미터 (S) */
    private String fidjbwgb;

    private String spouseCustNo;

    private String spouseTradNo;

    private String spouseTradNo2;
    /* 퍼스트홈론 담보/소득제공자 파라미터 (E) */

    private String endDate;

    private String ssnNo;

    private String isAddScanYn;

    /**
     * 쓰는 파라미터 인지 확인 불가
     */
    private String testKey;

    /* 전자문서 EDMS 문서 다운로드 서비스 (S) */

    /**
     * EDMS XVARM KEY
     */
    private String elementId;

    /**
     * 계약서 계좌번호
     */
    private String acctNo;

    /**
     * 계약서 주민번호
     */
    private String custSsn;

    /**
     * 계약서 문서코드
     */
    private String docCode;

    /**
     * PDF원천 채널코드
     */
    private String chnnlMk;

    /**
     * 업무구분(여신,펀드)
     */
    private String jobMk;

    /**
     * 펀드바코드데이터
     */
    private String barcodeData;

    /* 전자문서 EDMS 문서 다운로드 서비스 (E) */

    /* 전자문서 EDMS 문서 확인 서비스 (S) */
    private String edmsXvarmDocMeta;
    /* 전자문서 EDMS 문서 확인 서비스 (E) */
}
