package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03H16100Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "한도대출 감액 응답부")
public class CbTbs03H16100Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOCHGU", name = "처리구분G:감 H:해", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCHGU;

    @MessageField(id = "YOBGJKM", name = "변경전한도　", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOBGJKM;

    @MessageField(id = "YOBGHKM", name = "변경후한도　", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOBGHKM;

    @MessageField(id = "YOINIL", name = "변경일　　", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOINIL;

    @MessageField(id = "YOACCT", name = "대출 계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String YOACCT;

    @MessageField(id = "YOJAMT", name = "해지시이자수입금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOJAMT;

    @MessageField(id = "YORVKGB", name = "철회대상여부 1:대상", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YORVKGB;

    @MessageField(id = "RvkakSign", name = "철회상환액 부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RvkakSign;

    @MessageField(id = "YORVKAK", name = "철회상환액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YORVKAK;

    @MessageField(id = "YODUMMY", name = "더미", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

}