package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H12800Req", type = Type.REQUEST, captureSystem = "OLTP", description = "아이디찾기 선조회 요청")
public class CbIbk01H12800Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "이용자비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIGJCHK", name = "계좌번호체크여부", length = 1)
    private String YIGJCHK;

    @MessageField(id = "YIGJNO", name = "계좌번호", length = 11, masking = true, maskingType = "02")
    private String YIGJNO;

    @MessageField(id = "YIGPAS", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGPAS;

    @MessageField(id = "YICDCHK", name = "카드번호체크여부", length = 1)
    private String YICDCHK;

    @MessageField(id = "YICDNO", name = "입력카드번호", length = 16)
    private String YICDNO;

    @MessageField(id = "YITEL1", name = "검증전화번호1", length = 4)
    private String YITEL1;

    @MessageField(id = "YITEL2", name = "검증전화번호2", length = 4)
    private String YITEL2;

    @MessageField(id = "YITEL3", name = "검증전화번호3", length = 4)
    private String YITEL3;

}
