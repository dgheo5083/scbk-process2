package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class UpdateNfInfoParameter {
	private String idCdDsCd;
	private String idCdIsuDt;
	private String oplicNo;
	private String fileName;
	private String filePath;
	private String sendYn;
	private String businessDate;
	private String custIssDtChgYn;
	private String custNmChgYn;
	private String custNoChgYn;
	private String extractScore;
	private String fngrInfo;
	private String fngrInfoSize;
	private String photoInfo;
	private String photoInfoSize;
	private String mrtsNoChgYn;
	private String pssprtNoChgYn;
	private String pssprtNo;
	private String mrtsNo;
	private String idCdDsImg;
	private String passCode;
	private String psNo;
	private String seqNo;
	private String action;
	private String errorCode;
}
