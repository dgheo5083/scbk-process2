package com.scbank.process.api.svc.shared.components.customer.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "RegistDestructionCustInfoRequest", type = Type.REQUEST)
public class RegistDestructionCustInfoRequest implements IMessageObject {
	@MessageField(id = "cnnctnWay", name = "접속제휴사코드")
	private String cnnctnWay;
	
	@MessageField(id = "reaCdn", name = "REA코드")
	private String reaCdn;
	
	@MessageField(id = "yiCddGb", name = "CDD구분")
	private String yiCddGb;
	
	@MessageField(id = "yiJagSrc", name = "자금원천")
	private String yiJagSrc;
	
	@MessageField(id = "yiUseCd", name = "거래목적")
	private String yiUseCd;
	
	@MessageField(id = "yiYsGupPr", name = "예상가입상품")
	private String yiYsGupPr;
	
	@MessageField(id = "yiYsAum", name = "예상가입금액")
	private String yiYsAum;
	
	@MessageField(id = "yiIsIc", name = "상세직업코드")
	private String yiIsIc;
	
	@MessageField(id = "yiWType", name = "직업구분")
	private String yiWType;
	
	@MessageField(id = "yiDtabYn", name = "당타발송금거래여부")
	private String yiDtabYn;
	
	@MessageField(id = "yiDtabAum", name = "당타발송금액")
	private String yiDtabAum;
	
	@MessageField(id = "yiDtabSu", name = "당타발송건수")
	private String yiDtabSu;
	
	@MessageField(id = "yiChanGb", name = "채널구분")
	private String yiChanGb;
	
	@MessageField(id = "yiSaupIl", name = "설립일자")
	private String yiSaupIl;
	
	@MessageField(id = "yiIpjiGs", name = "예상 월평균 입금건수")
	private String yiIpjiGs;
	
	@MessageField(id = "yiIpjiAk", name = "예상 월평균 입금금액")
	private String yiIpjiAk;
	
	@MessageField(id = "yiMaecAk", name = "연간매출액")
	private String yiMaecAk;
	
	@MessageField(id = "yiSaName", name = "개인사업자상호")
	private String yiSaName;
	
	@MessageField(id = "yiSaSaup", name = "개인사업자번호")
	private String yiSaSaup;
	
	@MessageField(id = "yiSaEname", name = "개인사업자상호영문")
	private String yiSaEname;
	
	@MessageField(id = "yiSaSinup", name = "개인사업산업분류코드")
	private String yiSaSinup;
	
	@MessageField(id = "yiENayn", name = "영문성명수기입력")
	private String yiENayn;
	
	@MessageField(id = "yiEname", name = "영문성명")
	private String yiEname;
	
	@MessageField(id = "yiRsdYn", name = "해외장기거주여부")
	private String yiRsdYn;
	
	@MessageField(id = "yiFatYn", name = "해외납세의무신고대상여부")
	private String yiFatYn;
	
	@MessageField(id = "yiFname", name = "영문성명(중간이름)")
	private String yiFname;
	
	@MessageField(id = "yiGkCd", name = "해외납세의무국가")
	private String yiGkCd;
	
	@MessageField(id = "yiAusCd", name = "거래목적 추가선택")
	private String yiAusCd;
	
	@MessageField(id = "yiJjSrc", name = "자금원천-자금제공자")
	private String yiJjSrc;
	
	@MessageField(id = "yiJiSic", name = "자금원천-자금제공자 직업코드")
	private String yiJiSic;
	
	@MessageField(id = "yiBiSic", name = "자금원천-자금제공자 이전직업코드")
	private String yiBiSic;
	
	@MessageField(id = "yiMAum", name = "월소득정보")
	private String yiMAum;
	
	@MessageField(id = "prdctNm", name = "상품명")
	private String prdctNm;
	
	@MessageField(id = "callCntrStsCd", name = "콜센터상태코드")
	private String callCntrStsCd;
	
	@MessageField(id = "prgrssStsCd", name = "진행상태코드")
	private String prgrssStsCd;
	
	@MessageField(id = "clerkNo", name = "행번")
	private String clerkNo;
	
	@MessageField(id = "gaesaCode", name = "품목코드")
	private String gaesaCode;
	
	@MessageField(id = "codeType", name = "코드타입")
	private String codeType;
	
	@MessageField(id = "custNo", name = "고객번호")
	private String custNo;
	
	@MessageField(id = "tradNo", name = "거래번호")
	private String tradNo;
}
