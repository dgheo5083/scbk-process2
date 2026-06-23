package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H866Req", type = Type.REQUEST, captureSystem = "OLTP", description = "보유계좌조회 요청 전문")
public class CbIbk01H866Req implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "YIJMNO", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
	private String YIJMNO; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "YIBIDAE", name = "비대면실명승인계좌조회", length = 1)
	private String YIBIDAE;

	@MessageField(id = "YIJHUSID", name = "IB조회용　이용자ID", length = 10)
	private String YIJHUSID;

	@MessageField(id = "YIHANDO", name = "한도제한조회여부(Y, 공백)", length = 1)
	private String YIHANDO;

	@MessageField(id = "YIMKT", name = "마케팅동의존재조회(Y, N)", length = 1)
	private String YIMKT;

	@MessageField(id = "YISMSYN", name = "퍼스트알리미(Y, N)", length = 1)
	private String YISMSYN;

	@MessageField(id = "YIOIHWA", name = "외화보통예금조회(Y, 공백)", length = 1)
	private String YIOIHWA;

	@MessageField(id = "YIMBSINGO", name = "모바일제신고여부YN", length = 1)
	private String YIMBSINGO;

	@MessageField(id = "YIGIBONCH", name = "Y : 기본계좌변경용", length = 1)
	private String YIGIBONCH;

}
