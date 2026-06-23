package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;
import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbf01D10600Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "환전가능 영업점 조회")
public class CbIbf01D10600Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOTIME", name = "조회 시각", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOTIME;

    @MessageField(id = "YOTONG", name = "통화코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOTONG;

    @MessageField(id = "YOGUNSU", name = "입력 영업점 개수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOGUNSU;

    @MessageField(id = "YOJHINF", name = "점별잔액명세")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbf01D10600Res/YOGUNSU")
    private List<YOJHINF> YOJHINF;

    @Data
    public static class YOJHINF implements IMessageObject {

        @MessageField(id = "YOBRNO", name = "점번호", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOBRNO;

        @MessageField(id = "YOGMAK", name = "외화금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal YOGMAK;

    }
}
