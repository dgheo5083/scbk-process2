package com.scbank.process.api.edmi.dto.edmi;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(
		id = "CbIbk01003Req", 
		type = Type.REQUEST, 
		description = "파기대상신규등록 요청부[AS-IS EDMI_CB_IBK01_003]",
		captureSystem = "OLTP", 
		typeName = "CoreBanking:smartPhoneBankingCommonRoute",
		messageSenderBody = "SmartPhoneBanking",
		senderDomainBody = "CoreBanking")
public class CbIbk01003Req implements IMessageObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MessageField(id = "YIPBGTB", name = "요청 업무헤더")
	private YiPBGTBHeader YIPBGTB;
	
	@MessageField(id = "YIPUPMU", name = "업무DATA")
	private YIPUPMU_REC YIPUPMU;
	
	public static class YIPUPMU_REC implements IMessageObject {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@MessageField(id = "YIUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
		private String YIUSID; 
		
		@MessageField(id = "YIPASS", name = "통신비밀번호", length = 8, masking = true, maskingType = "03")
		private String YIPASS;
		
		@MessageField(id = "YIJMNO", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
		private String YIJMNO;
		
		@MessageField(id = "YIKUK", name = "국가코드", length = 3)
		private String YIKUK;
		
		@MessageField(id = "YICDDGB", name = "정보입력구분Y")
		private String YICDDGB;
		
		@MessageField(id = "YIJAGSRC", name = "자금원천", length = 2)
		private String YIJAGSRC;
		
		@MessageField(id = "YIUSECD", name = "거래목적", length = 2)
		private String YIUSECD;
		
		@MessageField(id = "YIYSGIPPR", name = "예상가입상품", length = 1)
		private String YIYSGIPPR;
		
		@MessageField(id = "YIYSAUM", name = "AUM", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private String YIYSAUM;
		
		@MessageField(id = "YIISIC", name = "ISIC", length = 4)
		private String YIISIC;
		
		@MessageField(id = "YIWTYPE", name = "WORK TYPE", length = 1)
		private String YIWTYPE;
		
		@MessageField(id = "YIDTABYN", name = "당타발송금거래Y/N", length = 1)
		private String YIDTABYN;
		
		@MessageField(id = "YIDTABAUM", name = "당타발예상금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private String YIDTABAUM;
		
		@MessageField(id = "YIDTABSU", name = "당타발예상건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private String YIDTABSU;
		
		@MessageField(id = "YICHANGB", name = "신청채널구분", length = 1)
		private String YICHANGB;
	}

}
