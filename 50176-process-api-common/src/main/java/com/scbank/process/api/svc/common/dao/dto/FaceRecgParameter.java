package com.scbank.process.api.svc.common.dao.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FaceRecgParameter {
	
	private String userCifNo; // 고객 고유식별 번호
	private String faceTransactionId; // 안면인식 거래고유번호
	private String custNo; // 고객번호
	private String tradNo; // 거래번호
	private String faceRecgYn; // 안면인식 결과
	private BigDecimal faceRecgScore; // 안면인식 점수
	private String faceRecgCd; // 안면인식 결과코드
	private String faceRecgMsg; // 안면인식 결과 메세지
	private String faceRecgData; // 안면인식 응답일시
	private String selfiePath; // 안면인식 셀피파일 경로
	private String filePath; // 안면인식 파일 경로
	private String fileName; // 안면인식 파일명
}
