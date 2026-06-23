package com.scbank.process.api.svc.shared.components.edoc;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EdocConstant {
	public final String EDOC_AP_ACTION_CREATE_SIGN_PDF = "CreateSignPDF"; // 전자서명 PDF 생성
	public final String EDOC_AP_ACTION_CREATE_ESL_SIGN_PDF = "ELSCreateSignPDFService"; // ELS 서명주입
	public final String EDOC_AP_ACTION_AMS_HOST = "AMSHostTransfer"; // AMS HOST 전송
	public final String EDOC_AP_ACTION_LPC_IMAGE_TRANSFER = "LPCImageTransfer"; // LPC 전자문서 전송
	public final String EDOC_AP_ACTION_LPC_IMAGE_META_TRANSFER = "LPCImageMetaTransfer"; // LPC 대출 상품 정보 전송
	public final String EDOC_AP_ACTION_LPC_RECOVERY = "LPCRecovery"; // LPC 복구
	public final String EDOC_AP_ACTION_OFFICIAL_RECOVERY = "OfficialRecovery"; // 공전소 복구
	public final String EDOC_AP_ACTION_LPC_ADD_DOCS = "AddDocRegist"; // LPC 추가서류전송
	public final String EDOC_AP_ACTION_LPC_INTERNET_LOAN_IMAGE_META_TRANSFER = "InternetLoanImageMetaTransfer"; // 인터넷대출
																												// 전자문서
																												// 정보 전송
	public final String EDOC_AP_ACTION_LPC_ADD_TRANS = "AddDocTransfer"; // LPC 추가전송
	public final String EDOC_AP_ACTION_OFFICIAL_TRANSFER_REGIST = "OfficialTransferRegist"; // TSA 대상 TSA 수행 -> 공전소 발송
																							// 등록까지 진행
	public final String EDOC_AP_ACTION_WARRENTPOST = "WarrentPost"; // 담보대출 후처리 요청(LPC 이미지 전송 , LPC META 전송 , TSA 대상 TSA
																	// 프로세스 수행 , 공전소 발송 등록)
	public final String EDOC_AP_ACTION_SEND_EMAIL = "EmailSend"; // 이메일 발송
	public final String EDOC_AP_ACTION_SEND_EMAIL_TERMS = "EmailTermsSend"; // 이메일 발송(약관)
	public final String EDOC_AP_ACTION_LPC_IMAGE_TRANSFER_BACKUP = "LPCImageTransferBackUp"; // LPC 복구
	public final String EDOC_AP_ACTION_LPC_IMAGE_META_TRANSFER_BACKUP = "LPCImageMetaTransferBackUp"; // 공전소 복구

	public final String EDOC_AP_ACTION_SEND_LPC_META_TRANSFER = "LPCMetaTransfer"; // LPC 상품메타만 전송(담보대출용)
	public final String EDOC_AP_ACTION_SEND_PSH_SAMPLE_TRANSFER = "PSHSample"; // PSH Sample 서비스 테스트용
	public final String EDOC_AP_ACTION_HEALTH_CHECK = "healthCheck"; // EDOC 서버 HealthCheck 용도

	public final String EDOC_AP_ACTION_EDMS_DOC_DOWNLOAD = "ContractDocDown"; // EDOC 서버 EDMS 계약문서 다운로드 서비스

	// public final String EDOC_AP_ACTION_BARCODE_SERVICE = "BarcodeService"; //
	// EDOC 서버 EDMS 계약문서중 UI에서 생성한 문서는 바코드를 입혀야함.

	public final String EDOC_AP_ACTION_EDMS_DOC_CHECK = "ContractFileCheckService"; // EDOC 서버 EDMS 계약문서 파일 존재 확인

	// TODO 확인필요 : MA30 Framework MessageConstants MSG_ROOT/MSG_BODY EDOC 응답 파싱시 필요
	public final String MessageConstants_MSG_ROOT = "_msg_";
	public final String MessageConstants_MSG_BODY = "_body_";

}
