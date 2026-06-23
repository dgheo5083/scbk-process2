package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H22900Req", type = Type.REQUEST, description = "예약이체 상세 요청 전문")
public class CbIbk01H22900Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransPassword; // _DNFE2E_ _/DNFE2E_

    @MessageField(id = "TransJJDate", name = "이체지정일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransJJDate;

    @MessageField(id = "TransJJTime", name = "이체지정시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransJJTime;

    @MessageField(id = "SerialNum", name = "일련번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SerialNum;

    @MessageField(id = "YgMsno", name = "요구명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YgMsno;

    @MessageField(id = "KTransJJDate", name = "연속키-이체지정일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KTransJJDate;

    @MessageField(id = "KTransJJTime", name = "연속키-이체지정시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KTransJJTime;

    @MessageField(id = "KSerialNum", name = "연속키-일련번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KSerialNum;
}
