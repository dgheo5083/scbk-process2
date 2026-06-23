package com.scbank.process.api.edmi.dto.mci;

import java.math.BigDecimal;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "MciIbFundN070Req", type = Type.REQUEST, captureSystem = "MCI", description = "외화펀드 선취수수료 조회")
public class MciIbFundN070Req implements IMessageObject {
    @MessageField(id = "AIJUMNO", name = "점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AIJUMNO;

    @MessageField(id = "AIKMCOD", name = "과목코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIKMCOD;

    @MessageField(id = "AIGJWPW4", name = "계좌비밀번호4", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String AIGJWPW4;

    @MessageField(id = "AIGJWNO", name = "계좌번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "02")
    private String AIGJWNO;

    @MessageField(id = "AICMFNO", name = "고객번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AICMFNO;

    @MessageField(id = "AIIPHCH", name = "입금회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AIIPHCH;

    @MessageField(id = "AIJHIL", name = "조회일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AIJHIL;

    @MessageField(id = "AIJHCOD", name = "조회코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AIJHCOD;

    @MessageField(id = "AIOUTORD", name = "출력순위", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIOUTORD;

    @MessageField(id = "AIPAGEJGASU", name = "PAGE증가수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIPAGEJGASU;

    @MessageField(id = "AIJHSTAIL", name = "조회시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIJHSTAIL;

    @MessageField(id = "AICHEJRT", name = "체증율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AICHEJRT;

    @MessageField(id = "AIKMJJMKCOD", name = "국민주종목코드", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIKMJJMKCOD;

    @MessageField(id = "AIKKCOD", name = "국가코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIKKCOD;

    @MessageField(id = "AIFNDCOD1", name = "펀드코드1", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIFNDCOD1;

    @MessageField(id = "AIFNDCOD2", name = "펀드코드2", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIFNDCOD2;

    @MessageField(id = "AIFNDCOD3", name = "펀드코드3", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIFNDCOD3;

    @MessageField(id = "AICONGGMMS", name = "계약기간월수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AICONGGMMS;

    @MessageField(id = "AIYNGJIGGG", name = "연금지급기간", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYNGJIGGG;

    @MessageField(id = "AIJHENDIL", name = "조회종료일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIJHENDIL;

    @MessageField(id = "AIGISIL", name = "기산일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIGISIL;

    @MessageField(id = "AIGRAK", name = "거래금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AIGRAK;

    @MessageField(id = "AIWLJUKAK", name = "월적립금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AIWLJUKAK;

    @MessageField(id = "AIYNGGGMAK", name = "연금기금금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AIYNGGGMAK;

    @MessageField(id = "AIMPOAK", name = "목표금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AIMPOAK;

    @MessageField(id = "AIGRCOD", name = "거래코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIGRCOD;

    @MessageField(id = "AIJHGB", name = "조회구분", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIJHGB;

    @MessageField(id = "AIGJGKCD", name = "계정과목코드", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIGJGKCD;

    @MessageField(id = "AIJHCOD1", name = "조회코드1", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIJHCOD1;

    @MessageField(id = "AIYNGJIGJGI", name = "연금지급주기", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYNGJIGJGI;

    @MessageField(id = "AIYNGJIGJR", name = "연금지급종류", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYNGJIGJR;

    @MessageField(id = "AIGRJASU", name = "거래좌수", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AIGRJASU;

    @MessageField(id = "AIJHMM", name = "조회월", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIJHMM;

    @MessageField(id = "AIJYIY50", name = "적용이율50", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIJYIY50;

    @MessageField(id = "AIJHGIJHM", name = "조회기준시분", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIJHGIJHM;

    @MessageField(id = "AISBUJHGB", name = "세부조회구분", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AISBUJHGB;

    @MessageField(id = "AIERSEQ", name = "에러일련번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIERSEQ;

    @MessageField(id = "AIJHYY", name = "조회년도", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIJHYY;

    @MessageField(id = "AISTJBCOD", name = "신탁종별코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AISTJBCOD;

    @MessageField(id = "AIKMJTONNO", name = "국민주통장번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIKMJTONNO;

    @MessageField(id = "AIGJUGB", name = "거주자구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIGJUGB;

    @MessageField(id = "AIGSEGB", name = "과세구분", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIGSEGB;

    @MessageField(id = "AIDCAK", name = "대출금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AIDCAK;

    @MessageField(id = "AIFXGRJASU", name = "외화거래좌수", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AIFXGRJASU;

    @MessageField(id = "AIFNDJHGB", name = "펀드조회구분", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIFNDJHGB;

    @MessageField(id = "AIFNDGGMMS", name = "펀드기간월수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIFNDGGMMS;

    @MessageField(id = "AIFXGRAK", name = "외화거래금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AIFXGRAK;

    @MessageField(id = "AIJHSYUCOD", name = "조회사유코드", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIJHSYUCOD;

    @MessageField(id = "AICURCOD", name = "통화코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AICURCOD;

    @MessageField(id = "AICYAK", name = "청약금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AICYAK;

    @MessageField(id = "AISALESSR", name = "판매수수료", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISALESSR;

    @MessageField(id = "AIJHGB2", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIJHGB2;

    @MessageField(id = "AIJHGBN1", name = "조회구분명1", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIJHGBN1;

    @MessageField(id = "AIJHGBN2", name = "조회구분명2", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIJHGBN2;

    @MessageField(id = "AIJMSUPNO", name = "주민사업자번호", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String AIJMSUPNO;

    @MessageField(id = "AIFXJHNJUSU", name = "외화전환주식수", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AIFXJHNJUSU;

    @MessageField(id = "AIRELGB", name = "관계인구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIRELGB;

    @MessageField(id = "AIYEAR0", name = "기준년도", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR0;

    @MessageField(id = "AISODGJEAK0", name = "소득공제금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK0;

    @MessageField(id = "AIYEAR1", name = "기준년도1", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR1;

    @MessageField(id = "AISODGJEAK1", name = "소득공제금액1", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK1;

    @MessageField(id = "AIYEAR2", name = "기준년도2", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR2;

    @MessageField(id = "AISODGJEAK2", name = "소득공제금액2", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK2;

    @MessageField(id = "AIYEAR3", name = "기준년도3", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR3;

    @MessageField(id = "AISODGJEAK3", name = "소득공제금액3", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK3;

    @MessageField(id = "AIYEAR4", name = "기준년도4", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR4;

    @MessageField(id = "AISODGJEAK4", name = "소득공제금액4", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK4;

    @MessageField(id = "AIYEAR5", name = "기준년도5", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR5;

    @MessageField(id = "AISODGJEAK5", name = "소득공제금액5", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK5;

    @MessageField(id = "AIYEAR6", name = "기준년도6", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR6;

    @MessageField(id = "AISODGJEAK6", name = "소득공제금액6", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK6;

    @MessageField(id = "AIYEAR7", name = "기준년도7", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR7;

    @MessageField(id = "AISODGJEAK7", name = "소득공제금액7", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK7;

    @MessageField(id = "AIYEAR8", name = "기준년도8", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR8;

    @MessageField(id = "AISODGJEAK8", name = "소득공제금액8", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK8;

    @MessageField(id = "AIYEAR9", name = "기준년도9", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR9;

    @MessageField(id = "AISODGJEAK9", name = "소득공제금액9", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK9;

    @MessageField(id = "AIYEAR10", name = "기준년도10", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR10;

    @MessageField(id = "AISODGJEAK10", name = "소득공제금액10", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK10;

    @MessageField(id = "AIYEAR11", name = "기준년도11", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR11;

    @MessageField(id = "AISODGJEAK11", name = "소득공제금액11", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK11;

    @MessageField(id = "AIYEAR12", name = "기준년도12", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR12;

    @MessageField(id = "AISODGJEAK12", name = "소득공제금액12", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK12;

    @MessageField(id = "AIYEAR13", name = "기준년도13", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR13;

    @MessageField(id = "AISODGJEAK13", name = "소득공제금액13", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK13;

    @MessageField(id = "AIYEAR14", name = "기준년도14", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIYEAR14;

    @MessageField(id = "AISODGJEAK14", name = "소득공제금액14", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AISODGJEAK14;

    @MessageField(id = "AIGRCHANN", name = "거래채널구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIGRCHANN;

    @MessageField(id = "AIJHGBN3", name = "조회구분명3", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIJHGBN3;

    @MessageField(id = "AISAGUBUN", name = "1:실행2:미실행", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AISAGUBUN;

    @MessageField(id = "AICMFGB1", name = "필드입력여부01", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB1;

    @MessageField(id = "AICMFGB3", name = "필드입력여부03", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB3;

    @MessageField(id = "AICMFGB4", name = "필드입력여부04", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB4;

    @MessageField(id = "AICMFGB5", name = "필드입력여부05", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB5;

    @MessageField(id = "AICMFGB6", name = "필드입력여부06", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB6;

    @MessageField(id = "AICMFGB7", name = "필드입력여부07", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB7;

    @MessageField(id = "AICMFGB8", name = "필드입력여부08", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB8;

    @MessageField(id = "AICMFGB9", name = "필드입력여부09", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB9;

    @MessageField(id = "AICMFGB10", name = "필드입력여부10", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB10;

    @MessageField(id = "AICMFGB11", name = "필드입력여부11", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB11;

    @MessageField(id = "AICMFGB12", name = "필드입력여부12", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB12;

    @MessageField(id = "AICMFGB13", name = "필드입력여부13", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB13;

    @MessageField(id = "AICMFGB14", name = "필드입력여부14", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB14;

    @MessageField(id = "AICMFGB15", name = "필드입력여부15", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB15;

    @MessageField(id = "AICMFGB16", name = "필드입력여부16", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB16;

    @MessageField(id = "AICMFGB17", name = "필드입력여부17", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB17;

    @MessageField(id = "AICMFGB18", name = "필드입력여부18", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFGB18;

    @MessageField(id = "AIGRGGMM", name = "거래기간（월）", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIGRGGMM;

    @MessageField(id = "AICBNCHATY", name = "예외승인여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICBNCHATY;

    @MessageField(id = "AICNFRBN", name = "확인자행번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AICNFRBN;

    @MessageField(id = "AIMGRPW", name = "수수승인비번", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String AIMGRPW;

    @MessageField(id = "AICYGRGB", name = "취약고객여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICYGRGB;

    @MessageField(id = "AICIPGRGB", name = "CIP고객구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICIPGRGB;

    @MessageField(id = "AICIPILJA", name = "CIP판정일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AICIPILJA;

    @MessageField(id = "AICIPRISKGN", name = "CIP리스크등급", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICIPRISKGN;

    @MessageField(id = "AITOGB", name = "통화구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AITOGB;

    @MessageField(id = "AICIPNARA", name = "CIP 국적", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICIPNARA;

    @MessageField(id = "AIOPPRBGB", name = "조작자PRB여", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIOPPRBGB;

    @MessageField(id = "AICIPGB", name = "CIP 장애여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICIPGB;

    @MessageField(id = "AITJAJAGSH", name = "조작자PRB여", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AITJAJAGSH;

}