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

@Data
@IntegrationMessage(id = "CbTbs07H97600Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "ACCOUNT_INFO보유계좌내역일괄조회（금융회사조회）")
public class CbTbs07H97600Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOFERR", name = "FORMAT ERROR CODE", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOFERR;

    @MessageField(id = "YOERCD", name = "ERROR CODE", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOERCD;

    @MessageField(id = "YOMODN", name = "MODULE NAME", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMODN;

    @MessageField(id = "YORESCD", name = "응답코드", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YORESCD;

    @MessageField(id = "YORERR", name = "응답코드변환 ERR CODE", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YORERR;

    @MessageField(id = "YOEXCNT", name = "조회제외기관수", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOEXCNT;

    @MessageField(id = "YOEXCOD", name = "조회제외기관코드LIST", length = 63, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOEXCOD;

    @MessageField(id = "YOTOTGN", name = "계좌총건수", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTOTGN;

    @MessageField(id = "YOBUNHO", name = "지정번호", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOBUNHO;

    @MessageField(id = "YODACNT", name = "데이터건수", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YODACNT;

    @MessageField(id = "YOWGOYU", name = "조회원거래고유번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWGOYU;

    @MessageField(id = "YOYEBI1", name = "예비", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYEBI1;

    @MessageField(id = "TR97601", name = "ACCOUNT_INFO보유계좌내역일괄조회 record")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbTbs07H97600Res/YODACNT")
    private List<TR97601> TR97601;

    @Data
    public static class TR97601 implements IMessageObject {
        @MessageField(id = "YOGBKCD", name = "개설금융회사코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGBKCD;

        @MessageField(id = "YODGUBN", name = "유형구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YODGUBN;

        @MessageField(id = "YOACGR", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOACGR;

        @MessageField(id = "YOGJNO", name = "계좌번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGJNO;

        @MessageField(id = "YOAHOI", name = "예금회차정보", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOAHOI;

        @MessageField(id = "YOGBR", name = "계좌관리점코드", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGBR;

        @MessageField(id = "YOGDATE", name = "개설일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGDATE;

        @MessageField(id = "YOMDATE", name = "만기일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOMDATE;

        @MessageField(id = "YOCDATE", name = "최종입출금일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOCDATE;

        @MessageField(id = "YOPNAME", name = "상품명", length = 102, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOPNAME;

        @MessageField(id = "YOBNAME", name = "부기명", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOBNAME;

        @MessageField(id = "YOHMGB", name = "휴면계좌여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOHMGB;

        @MessageField(id = "YOJANAK", name = "계좌잔고", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOJANAK;

        @MessageField(id = "YOGTJAN", name = "금투사예수금잔고", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOGTJAN;

        @MessageField(id = "YOGTGJ1", name = "금투사잔고산출기준1", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGTGJ1;

        @MessageField(id = "YOGTGJ2", name = "금투사잔고산출기준2", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGTGJ2;

        @MessageField(id = "YOGTTJYB", name = "금투사투자재산연계", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGTTJYB;

        @MessageField(id = "YOGTBNKJ", name = "금투사은행제휴계좌", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGTBNKJ;

        @MessageField(id = "YOGTHJHJ", name = "금투사해지후잔고발생", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGTHJHJ;

        @MessageField(id = "YOSAVBNK", name = "저축은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOSAVBNK;

        @MessageField(id = "YOYEBI2", name = "예비정보필드2", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOYEBI2;
    }
}
