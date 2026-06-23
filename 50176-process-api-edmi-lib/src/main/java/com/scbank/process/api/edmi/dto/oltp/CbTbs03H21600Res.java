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
@IntegrationMessage(id = "CbTbs03H21600Res", type = Type.RESPONSE, description = "키보드,전화번호이체 통합전문 응답 전문", captureSystem = "OLTP")
public class CbTbs03H21600Res implements IMessageObject {

    @MessageField(id = "UserID", name = "사용자ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGRIL", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOGRIL;

    @MessageField(id = "YOJSNO", name = "실제출금접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOJSNO;

    @MessageField(id = "YOOGJNO", name = "출금계좌번호", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOOGJNO;

    @MessageField(id = "YOCHDATE", name = "원거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOCHDATE;

    @MessageField(id = "YOJSNOWON", name = "원거래접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOJSNOWON;

    @MessageField(id = "YODUMY1", name = "DUMMY", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMY1;

    @MessageField(id = "YOONAME", name = "입금통장표시내용", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOONAME;

    @MessageField(id = "YOIBKCD", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIBKCD;

    @MessageField(id = "YOBKNM", name = "입금은행명", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBKNM;

    @MessageField(id = "YOIGJNO", name = "입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIGJNO;

    @MessageField(id = "YOICGM", name = "총이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOICGM;

    @MessageField(id = "YOTSSR", name = "총수수료", length = 07, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOTSSR;

    @MessageField(id = "YOSSRGB", name = "수수료구분", length = 01, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSSRGB;

    @MessageField(id = "YOICJR", name = "이체종류", length = 02, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOICJR;

    @MessageField(id = "YOCRGB", name = "처리구분", length = 01, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCRGB;

    @MessageField(id = "YOIGHPNO", name = "입금휴대폰번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIGHPNO;

    @MessageField(id = "YODUMY2", name = "DUMMY", length = 52, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMY2;

    @MessageField(id = "YOSCNM", name = "출금통장표시내용", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSCNM;

    @MessageField(id = "YOIPNM", name = "입금인명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIPNM;

    @MessageField(id = "YOJINM", name = "지급인명", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJINM;

    @MessageField(id = "YOJANAKSIGN", name = "출금후　잔액부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJANAKSIGN;

    @MessageField(id = "YOJANAK", name = "출금후　잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOJANAK;

    @MessageField(id = "YOJMBH", name = "전문관리번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJMBH;

    @MessageField(id = "YORESULT", name = "처리결과", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YORESULT;

    @MessageField(id = "YOERCD", name = "에러코드", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOERCD;

    @MessageField(id = "YOERMSG", name = "처리결과내용", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOERMSG;
}
