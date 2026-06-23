package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

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
@IntegrationMessage(id = "CbIbk01H93l00Res", type = Type.RESPONSE, description = "키보드뱅킹 공통 관리 전문 요청부")
public class CbIbk01H93l00Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGUBN", name = "처리구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGUBN;

    @MessageField(id = "YOSVCYES", name = "가입고객", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSVCYES;

    @MessageField(id = "YOTOHAN", name = "전체이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOTOHAN;

    @MessageField(id = "YOREMAIN", name = "당일이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOREMAIN;

    @MessageField(id = "YOCGEJA", name = "지정출금계좌", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCGEJA;

    @MessageField(id = "YOPASSCH", name = "계좌비밀번호 사용여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPASSCH;

    @MessageField(id = "YOSVCD", name = "키보드뱅킹 서비스코드(전화번호카톡이체 : 805)", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSVCD;

    @MessageField(id = "YOSVC731YN", name = "즉시이체 출금계좌 여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSVC731YN;

    @MessageField(id = "YOBOANYN", name = "보안계좌여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBOANYN;

    @MessageField(id = "YODUMMY", name = "Dummy(2019.03.05 100->95)", length = 95, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

    // @MessageField(id = "YOARRAY", name = "출력계좌반복부")
    // @RepeatedField(repeatType = RepeatType.REFERENCE)
    // private List<YOARRAY> YOARRAY;

    // @Data
    // public static class YOARRAY implements IMessageObject {

    // @MessageField(id = "YOIBANK", name = "은행코드")
    // private String YOIBANK;

    // @MessageField(id = "YOIGEJA", name = "계좌번호")
    // private String YOIGEJA;

    // @MessageField(id = "YORESULT", name = "정상여부")
    // private String YORESULT;

    // @MessageField(id = "YOADUMMY", name = "어레이더미")
    // private String YOADUMMY;
    // }

}
