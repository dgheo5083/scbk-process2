package com.scbank.process.api.svc.common.dao.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class NonFaceTblParameter {
	private String seqNo;
	private String applyId;
	private String productCode;//Integer
	private String productName;
	private String acctNo;
	private String bankName;
	private String anotherAcctNo;
	private BigDecimal deposit;
	private BigDecimal invest;
	private BigDecimal property;
	private BigDecimal assets;
	private String assetsFlag;
	private BigDecimal mortgage;
	private BigDecimal loan;
	private BigDecimal debt;
	private String detbFlag;
	private String idcdDsCd;
	private String psno;
	private String cusNm;
	private String idcdIsuDt;
	private String opLicNo;
	private String reqBrCd;
	private String reqBrNm;
	private String agtYn;
	private String jobTypeCd;
	private String fileName;
	private String filePath;
	private String sendYn;
	private String passCode;
	private String imgIdxNo;
	private String phone;
	private String errMsg;
	private String businessDate;
	private String photoInfo;
	private String photoInfoSize;//Integer
	private String extractScore;//Integer
	private String fngrInfo;
	private String fngrInfoSize;//Integer
	private String custNoChgYn;
	private String custNmChgYn;
	private String custIssDtChgYn;
	private String idvErrMsg;
	private String idvType;
	private String idvSave;
	private String idvRih;
	private String idcddsImg;
	private String pssprtNo;
	private String mrtsNo;
	private String pssprtNoChgYn;
	private String mrtsNoChgYn;
}
