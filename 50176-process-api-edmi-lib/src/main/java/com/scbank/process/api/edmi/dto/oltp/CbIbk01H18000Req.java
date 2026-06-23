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
@IntegrationMessage(id = "CbIbk01H18000Req", type = Type.REQUEST, description = "영업점 입금계좌등록 조회 요청 전문", captureSystem = "OLTP")
public class CbIbk01H18000Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "MSList", name = "요구명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer MSList;

    @MessageField(id = "YIJHGB", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJHGB;

    @MessageField(id = "YICONT", name = "연속거래여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICONT;

    @MessageField(id = "YIMSVCD", name = "서비스코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMSVCD;

    @MessageField(id = "YIMIPJI", name = "계좌부입지급구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMIPJI;

    @MessageField(id = "YIMGBNK", name = "계좌부은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMGBNK;

    @MessageField(id = "YIMGGJN", name = "계좌부계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMGGJN;

    @MessageField(id = "YIMIBNK", name = "입금계좌은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMIBNK;

    @MessageField(id = "YIMIGJN", name = "입금계좌계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMIGJN;
}
