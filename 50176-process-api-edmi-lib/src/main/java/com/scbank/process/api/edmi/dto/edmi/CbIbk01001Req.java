package com.scbank.process.api.edmi.dto.edmi;



import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(
		id = "CbIbk01001Req", 
		type = Type.REQUEST, 
		description = "FATCA등록/조회 요청부[AS-IS EDMI_CB_IBK01_001]",
		captureSystem = "OLTP", 
		typeName = "CoreBanking:smartPhoneBankingCommonRoute",
		messageSenderBody = "SmartPhoneBanking",
		senderDomainBody = "CoreBanking")
public class CbIbk01001Req implements IMessageObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MessageField(id = "YIPBGTB", name = "요청 업무헤더")
	private YiPBGTBHeader YIPBGTB;
	
	@MessageField(id = "YIPUPMU", name = "업무DATA")
	private YIPUPMU_REC YIPUPMU;
	
	public static class YIPUPMU_REC {
		@MessageField(id = "YIUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
		private String YIUSID; 
		
		@MessageField(id = "YIPASS", name = "통신비밀번호", length = 8, masking = true, maskingType = "03")
		private String YIPASS; 
		
		@MessageField(id = "YIJMNO", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
		private String YIJMNO;
		
		@MessageField(id = "YIITM01", name = "FATCA 평가항목1", length = 1)
		private String YIITM01;
		
		@MessageField(id = "YIITM02", name = "FATCA 평가항목2", length = 1)
		private String YIITM02;
		
		@MessageField(id = "YIITM03", name = "FATCA 평가항목3", length = 1)
		private String YIITM03;
		
		@MessageField(id = "YIITM04", name = "FATCA 평가항목4", length = 1)
		private String YIITM04;
		
		@MessageField(id = "YIITM05", name = "FATCA 평가항목5", length = 1)
		private String YIITM05;
		
		@MessageField(id = "YIITM06", name = "FATCA 평가항목6", length = 1)
		private String YIITM06;
		
		@MessageField(id = "YIITM07", name = "FATCA 평가항목7", length = 1)
		private String YIITM07;
		
		@MessageField(id = "YIITM08", name = "FATCA 평가항목8", length = 1)
		private String YIITM08;
		
		@MessageField(id = "YIITM09", name = "FATCA 평가항목9", length = 1)
		private String YIITM09;
		
		@MessageField(id = "YIFGUBN", name = "FATCA/USP 조회구분", length = 1)
		private String YIFGUBN;
	}
}
