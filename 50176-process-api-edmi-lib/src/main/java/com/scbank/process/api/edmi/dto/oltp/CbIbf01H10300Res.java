package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;
import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@IntegrationMessage(id = "CbIbf01H10300Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "목표환율매매(First환전예약) 조회업무")
public class CbIbf01H10300Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YONAME", name = "고객명(CIR 고객명)", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YONAME;

    @MessageField(id = "YOYCIFNO", name = "CIF-NO", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYCIFNO;

    @MessageField(id = "YOYBRNO", name = "점번호", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYBRNO;

    @MessageField(id = "YOYGOGAK", name = "고객번호", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYGOGAK;

    @MessageField(id = "YOYREFNO", name = "환전예약번호", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYREFNO;

    @MessageField(id = "YOTIME", name = "예약시각", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTIME;

    @MessageField(id = "YOWGMAK", name = "원화입지급액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOWGMAK;

    @MessageField(id = "YOWGJNO", name = "원화결제계좌", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWGJNO;

    @MessageField(id = "YOFGMAK", name = "외화입지급액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOFGMAK;

    @MessageField(id = "YOFGJNO", name = "외화결제계좌", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOFGJNO;

    @MessageField(id = "YOSHIL", name = "예약체결일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSHIL;

    @MessageField(id = "YOSHTM", name = "예약체결시각", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSHTM;

    @MessageField(id = "YOSHRAT", name = "예약체결환율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOSHRAT;

    @MessageField(id = "YOGRGBN", name = "거래채널구분(등록단말)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGRGBN;

    @MessageField(id = "YOEMAIL", name = "이메일주소", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOEMAIL;

    @MessageField(id = "YOGUNSU", name = "출력명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOGUNSU;

    @MessageField(id = "ARRLIST", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbf01H10300Res/YOGUNSU")
    private List<ARRLIST> ARRLIST;

    @Data
    public static class ARRLIST implements IMessageObject {

        @MessageField(id = "YODRIL", name = "예약일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YODRIL;

        @MessageField(id = "YOREFNO", name = "환전예약번호", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOREFNO;

        @MessageField(id = "YOBRNO", name = "점번호", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOBRNO;

        @MessageField(id = "YOGOGAK", name = "고객번호", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGOGAK;

        @MessageField(id = "YOTONG", name = "통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOTONG;

        @MessageField(id = "YOGMAK", name = "예약금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal YOGMAK;

        @MessageField(id = "YORATE", name = "예약환율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer YORATE;

        @MessageField(id = "YOEXDAY", name = "예약기간(3개월내일자)", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOEXDAY;

        @MessageField(id = "YOCRGBN", name = "처리구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOCRGBN;

        @MessageField(id = "YOYYJR", name = "예약구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOYYJR;

    }
}