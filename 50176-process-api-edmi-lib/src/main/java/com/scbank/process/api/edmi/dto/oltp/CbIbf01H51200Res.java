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
@IntegrationMessage(id = "CbIbf01H51200Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "인터넷환전 내역조회")
public class CbIbf01H51200Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOREFNO", name = "환전취결번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOREFNO;

    @MessageField(id = "YOGRIL", name = "거래일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGRIL;

    @MessageField(id = "YONAME", name = "고객명(한글)", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YONAME;

    @MessageField(id = "YOJUMNO", name = "영업점코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJUMNO;

    @MessageField(id = "YOJUMNM", name = "실물수령영업점(한글)", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJUMNM;

    @MessageField(id = "YOYGNO", name = "여권번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYGNO;

    @MessageField(id = "YOSLIL", name = "실물수령희망일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSLIL;

    @MessageField(id = "YOJMNO", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJMNO;

    @MessageField(id = "YOWNGJ", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWNGJ;

    @MessageField(id = "YOSGJNO", name = "수수료계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSGJNO;

    @MessageField(id = "YOGUBN", name = "거래내용(한글)", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGUBN;

    @MessageField(id = "YOOHGJ", name = "외화예금입금계좌", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOOHGJ;

    @MessageField(id = "YOUDAE", name = "우대구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUDAE;

    @MessageField(id = "YOMLIGE", name = "우대금액", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMLIGE;

    @MessageField(id = "YOTOTAL", name = "연중환전누계", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTOTAL;

    @MessageField(id = "YOTONG", name = "통화명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTONG;

    @MessageField(id = "YOSTONG", name = "수수료통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSTONG;

    @MessageField(id = "YOHAPAK", name = "환전합계금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHAPAK;

    @MessageField(id = "YOHJAK", name = "외화현찰금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHJAK;

    @MessageField(id = "YOHJYL", name = "외화현찰적용환율", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHJYL;

    @MessageField(id = "YOHJWON", name = "외화현찰원화금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHJWON;

    @MessageField(id = "YOTCAK", name = "T/C금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTCAK;

    @MessageField(id = "YOTCYL", name = "T/C적용환율", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTCYL;

    @MessageField(id = "YOTCWON", name = "T/C원화금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTCWON;

    @MessageField(id = "YOWNAK", name = "원화출금합계", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWNAK;

    @MessageField(id = "YOMSSU", name = "출력명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOMSSU;

    @MessageField(id = "YOYILJA", name = "연속거래일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYILJA;

    @MessageField(id = "YOYJSNO", name = "연속접수번호", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYJSNO;

    @MessageField(id = "ARRLIST", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbf01H51200Res/YOMSSU")
    private List<ARRLIST> ARRLIST;

    @Data
    public static class ARRLIST implements IMessageObject {

        @MessageField(id = "YOJSNO", name = "접수번호", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOJSNO;

        @MessageField(id = "YOREFN", name = "REF-NO", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOREFN;

        @MessageField(id = "YOILJA", name = "거래일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOILJA;

        @MessageField(id = "YOTOCD", name = "통화명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOTOCD;

        @MessageField(id = "YOHWAK", name = "환전금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOHWAK;

        @MessageField(id = "YOHJ", name = "환전목적", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOHJ;

        @MessageField(id = "YOERCD", name = "오류코드", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOERCD;

        @MessageField(id = "YOMSG", name = "오류내용", length = 80, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOMSG;

        @MessageField(id = "YOHTGB", name = "권종구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOHTGB;
    }

    @MessageField(id = "ARRLIST2", name = "권종반복부2")
    @RepeatedField(repeatType = RepeatType.FIXED, repeatCount = "10")
    private List<ARRLIST2> ARRLIST2;

    @Data
    public static class ARRLIST2 implements IMessageObject {

        @MessageField(id = "YOKWJON", name = "권종", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOKWJON;

        @MessageField(id = "YOMESU", name = "매수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer YOMESU;

    }

    @MessageField(id = "YODNAME", name = "대리인성명", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODNAME;

    @MessageField(id = "YODJUMIN", name = "대리인주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODJUMIN;

    @MessageField(id = "YODTEL1", name = "대리인전화(지역)", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODTEL1;

    @MessageField(id = "YODTEL2", name = "대리인(국번)", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODTEL2;

    @MessageField(id = "YODTEL3", name = "대리인(번호)", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODTEL3;

    @MessageField(id = "YOKJNO", name = "국제학생증보유", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOKJNO;

    @MessageField(id = "YOCRGB", name = "처리구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCRGB;

    @MessageField(id = "YOJJIL", name = "정리일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJJIL;

    @MessageField(id = "YOREFNO1", name = "REFNO", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOREFNO1;

    @MessageField(id = "YOSWAK", name = "수수료원화금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSWAK;

    @MessageField(id = "YOSHAK", name = "수수료외화금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSHAK;

    @MessageField(id = "YOTIME", name = "거래시각", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOTIME;

}
