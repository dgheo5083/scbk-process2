package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbk01H50700Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "대출이동제-상환확인증 조회")
public class CbIbk01H50700Res implements IMessageObject {
    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSID;

    @MessageField(id = "YOOTCNT", name = "데이터건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOOTCNT;

    @MessageField(id = "YOOTREC", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H50700Res/YOOTCNT")
    private List<YOOTREC> YOOTREC;

    @Data
    public static class YOOTREC implements IMessageObject {

        @MessageField(id = "YOJUMIN", name = "주민등록번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOJUMIN;

        @MessageField(id = "YOGRGIG", name = "거래기관구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGRGIG;

        @MessageField(id = "YOOLDBK", name = "보유기관금융회사코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOOLDBK;

        @MessageField(id = "YOOLDHK", name = "하위보유기관코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOOLDHK;

        @MessageField(id = "YOACNO", name = "대출식별번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOACNO;

        @MessageField(id = "YOSHBKCD", name = "상환계좌금융회사코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOSHBKCD;

        @MessageField(id = "YOSHACNO", name = "상환계좌번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOSHACNO;

        @MessageField(id = "YODCJR", name = "대출종류", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YODCJR;

        @MessageField(id = "YOAMTYN", name = "상환금액유무", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOAMTYN;

        @MessageField(id = "YOTOAMT", name = "상환총액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOTOAMT;

        @MessageField(id = "YOWONGM", name = "상환원금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOWONGM;

        @MessageField(id = "YOIJA", name = "이자금액", length = 16, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOIJA;

        @MessageField(id = "YOSUAMT", name = "중도상환수수료", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOSUAMT;

        @MessageField(id = "YOETC", name = "기타비용", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOETC;

        @MessageField(id = "YOJMNOV", name = "가상계좌거래추적번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOJMNOV;

        @MessageField(id = "YOSNAMT", name = "약정액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOSNAMT;

        @MessageField(id = "YOSHIYL", name = "이율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOSHIYL;

        @MessageField(id = "YOSTATU", name = "진행상태코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOSTATU;

        @MessageField(id = "YOMSAMT", name = "말소예상비용", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOMSAMT;

        @MessageField(id = "YOMSCGB", name = "말소처리주체", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOMSCGB;

        @MessageField(id = "YOMSHYN", name = "총액합산여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOMSHYN;

        @MessageField(id = "BankName", name = "은행명")
        private String BankName;

        @MessageField(id = "LoanCategoryName", name = "대출종류명")
        private String LoanCategoryName;

        @MessageField(id = "LoanCategory", name = "대출종류")
        private String LoanCategory;

        @MessageField(id = "YOMSCGBNM", name = "말소처리주체명")
        private String YOMSCGBNM;
    }
}