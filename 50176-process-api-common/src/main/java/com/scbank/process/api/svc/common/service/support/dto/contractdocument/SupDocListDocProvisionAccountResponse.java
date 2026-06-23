package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.svc.shared.components.account.dto.AllAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.CardAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.DepositAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.DepositSavingAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.DepositTrustAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.FundAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.FxAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.LoanAccountInfo;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 계약서류제공 계좌목록 조회
 */
@Data
@IntegrationMessage(id = "SupDocListDocProvisionAccountResponse", type = Type.RESPONSE)
public class SupDocListDocProvisionAccountResponse implements IMessageObject {

	@MessageField(id = "depositAcctList", name = "예적금 계좌목록조회")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<AllAccountInfo> depositAcctList;

	@MessageField(id = "savingDepositAcctList", name = "예적금(10,20,30,80,85,88,90) 계좌목록조회")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<AllAccountInfo> savingDepositAcctList;

	@MessageField(id = "foreignAcctList", name = "FX 계좌목록조회")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<AllAccountInfo> foreignAcctList;

	@MessageField(id = "trustAcctList", name = "신탁 계좌목록조회")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<AllAccountInfo> trustAcctList;

	@MessageField(id = "fundAcctList", name = "펀드계좌목록조회")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<AllAccountInfo> fundAcctList;

	@MessageField(id = "loanAcctList", name = "대출계좌목록조회")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<AllAccountInfo> loanAcctList;

	@MessageField(id = "cardAcctList", name = "카드 계좌목록조회")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<AllAccountInfo> cardAcctList;

}