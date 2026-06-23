package com.scbank.process.api.fw.base.integration.system.edmi.vo;

import java.math.BigDecimal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 채널 출력전문 채널 데이터부 VO
 */
@Data
public class EdmiChannelData implements IMessageObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@MessageField(id = "STD_TMSG_DAT_KIND_CD", name = "데이타종류", length = 1)
	private String stdTMsgDatKindCd;
	
	@MessageField(id = "STD_TMSG_DAT_LEN", name = "데이타길이", length = 5)
	private Integer stdTMsgDatLen;
	
	@MessageField(id = "PRINT_START_LINE", name = "장표Start Line", length = 2)
	private String printStartLine;
	
	@MessageField(id = "PRINT_MAX_LINE", name = "장표Max Line", length = 2)
	private String printMaxLine;
	
	@MessageField(id = "TRAN_DIS_CODE", name = "업무식별코드", length = 2)
	private String tranDisCode;
	
	@MessageField(id = "OUTPUT_CODE", name = "출력코드", length = 6)
	private String outputCode;
	
	@MessageField(id = "PASS_NO", name = "PASS_NO", length = 7)
	private String passNo;
	
	@MessageField(id = "CASH_WITHD_COUNT", name = "현금지급건수", length = 1)
	private Integer cashWithdCount;
	
	@MessageField(id = "CASH_WITHD_AMOUNT", name = "현금지급금액", length = 15)
	private BigDecimal cashWithdAmount;
	
	@MessageField(id = "TRANS_WITHD_COUNT", name = "대체지급건수", length = 1)
	private Integer transWithdCount;
	
	@MessageField(id = "TRANS_WITHD_AMOUNT", name = "대체지급금액", length = 15)
	private BigDecimal transWithdAmount;
	
	@MessageField(id = "CASH_DEPOSIT_COUNT", name = "현금입금건수", length = 1)
	private Integer cashDepositCount;
	
	@MessageField(id = "CASH_DEPOSIT_AMOUNT", name = "현금입금금액", length = 15)
	private BigDecimal cashDepositAmount;
	
	@MessageField(id = "TRANS_DEPOSIT_COUNT", name = "대체입금건수", length = 1)
	private Integer transDepositCount;
	
	@MessageField(id = "TRANS_DEPOSIT_AMOUNT", name = "대체입금금액", length = 15)
	private BigDecimal transDepositAmount;
	
	@MessageField(id = "MSG_CD", name = "타점건수", length = 1)
	private String msgCd;
	
	@MessageField(id = "RSPS_MSG_CTT", name = "타점금액", length = 15)
	private BigDecimal rspsMsgCtt;
	
	@MessageField(id = "TRANSACTION_TIME", name = "거래입력시간", length = 6)
	private String transactionTime;
	
	@MessageField(id = "COS1HENG", name = "CTR현금금액", length = 15)
	private BigDecimal cos1heng;
	
	@MessageField(id = "COS1DAEG", name = "CTR대체금액", length = 15)
	private BigDecimal cos1daeg;
	
	@MessageField(id = "COS1CTRG", name = "CTR금액", length = 15)
	private BigDecimal cos1crtg;

}
