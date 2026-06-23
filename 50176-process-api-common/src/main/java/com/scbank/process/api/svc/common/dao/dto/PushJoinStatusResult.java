package com.scbank.process.api.svc.common.dao.dto;


import lombok.Data;

@Data
public class PushJoinStatusResult {

	private String serNo;
	private String bnkingId;
	private String pushSrvcApprvlFlg;
	private String benefitFlag;			// 상품 및 서비스 알림 신청 여부 ( Y : 신청 , N : 미신청 )
	private String financeFlag;			// 금융시장알림 서비스 신청 여부 ( Y : 신청 , N : 미신청 )
	private String financeVal;			// 금융시장정보 value값(1:전체, 2:주간정보, 3:월간정보)
	private String wmloungeFlag;		// WMLOUNGE 서비스 신청 여부 ( Y : 신청 , N : 미신청 )
	private String iotranlistFlag;		// 입출금내역 서비스 신청 여부 ( Y : 신청 , N : 미신청 )
	private String exrateFlg;			// 환율푸시알림 신청 여부 ( Y : 신청 ,  N or '' : 미신청)
	private String notyExrateFlg;
	private String cnfrmNo;
	private String appGb;

}
