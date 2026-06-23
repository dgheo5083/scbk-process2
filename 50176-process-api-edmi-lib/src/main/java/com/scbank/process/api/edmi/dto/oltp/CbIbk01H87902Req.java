package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H87902Req", type = Type.REQUEST, captureSystem = "OLTP", description = "증명서>인터넷증명서조회-예금잔액증명서상세")
public class CbIbk01H87902Req implements IMessageObject {
    @MessageField(id = "YIUSID", name = "이용자번호", length = 10)
    private String YIUSID;

    @MessageField(id = "YIJMNO", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
    private String YIJMNO;

    @MessageField(id = "YIJHGB", name = "조회구분(1:발급목록2:상세)", length = 1)
    private String YIJHGB;

    @MessageField(id = "YIJHSDT", name = "조회구간시작일", length = 8)
    private String YIJHSDT;

    @MessageField(id = "YIJHEDT", name = "조회구간종료일", length = 8)
    private String YIJHEDT;

    @MessageField(id = "YIYSJHYB", name = "연속조회여부(Y/N)", length = 1)
    private String YIYSJHYB;

    @MessageField(id = "YILSTSEQ", name = "최종발급번호(연속조회시필수)", length = 17)
    private String YILSTSEQ;

    @MessageField(id = "YISEQ", name = "발급번호(상세조회)", length = 17)
    private String YISEQ;

}
