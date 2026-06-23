package com.scbank.process.api.svc.common.components.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 편한뱅킹 조회 응답
 */
@Data
@IntegrationMessage(id = "GetComfyBankReseponse", type = Type.REQUEST)
public class GetComfyBankResponse implements IMessageObject {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MessageField(id = "ssnOrTaxid", name = "주민/사업자번호")
    private String ssnOrTaxid; // 주민/사업자번호
    
    @MessageField(id = "usrNo", name = "이용자번호")
	private String usrNo; // 이용자번호
    
    @MessageField(id = "partyNm", name = "고객명")
	private String partyNm; // 고객명
    
    @MessageField(id = "registDt", name = "등록일자")
	private String registDt; // 등록일자
    
    @MessageField(id = "registTm", name = "등록시간")
	private String registTm; // 등록시간
    
    @MessageField(id = "questRply1", name = "설문답변1")
	private String questRply1; // 설문답변1
    
    @MessageField(id = "questRply2", name = "설문답변2")
	private String questRply2; // 설문답변2
    
    @MessageField(id = "questRply3", name = "설문답변3")
	private String questRply3; // 설문답변3
    
    @MessageField(id = "questRply4", name = "설문답변4")
	private String questRply4; // 설문답변4
    
    @MessageField(id = "questRply5", name = "설문답변5")
	private String questRply5; // 설문답변5
    
    @MessageField(id = "othrInfo1", name = "기타정보1")
	private String othrInfo1; // 기타정보1
    
    @MessageField(id = "othrInfo2", name = "기타정보2")
	private String othrInfo2; // 기타정보2
    
    @MessageField(id = "emailAddr", name = "이메일주소")
	private String emailAddr; // 이메일주소
    
    @MessageField(id = "cntctPlce", name = "연락처")
	private String cntctPlce; // 연락처
   
}
