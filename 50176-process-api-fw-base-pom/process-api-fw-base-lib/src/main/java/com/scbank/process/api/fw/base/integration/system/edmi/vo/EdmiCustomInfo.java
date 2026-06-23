package com.scbank.process.api.fw.base.integration.system.edmi.vo;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 채널 출력전문 고객정보부 출력 데이터 VO
 */
@Data
public class EdmiCustomInfo implements IMessageObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@MessageField(id = "CUST_INFO_DV_CD", name = "CUST_INFO_DV_CD", length = 1)
	private String custInfoDvcd;
	
	@MessageField(id = "CUST_INFO_SRCH_KEY", name = "CUST_INFO_SRCH_KEY", length = 20)
	private String custInfoSrchKey;

}
