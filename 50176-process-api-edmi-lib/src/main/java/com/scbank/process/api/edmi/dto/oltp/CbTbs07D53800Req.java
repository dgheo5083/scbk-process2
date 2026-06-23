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
@IntegrationMessage(id = "CbTbs07D53800Req", type = Type.REQUEST, description = "지방세통합본거래 요청부")
public class CbTbs07D53800Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TransPassword; // 이중암호화 대응

    @MessageField(id = "UMGubun", name = "업무구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UMGubun;

    @MessageField(id = "GB033I_UPJR", name = "업무종류", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_UPJR;

    @MessageField(id = "GB033I_JUMIN", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_JUMIN;

    @MessageField(id = "GB033I_KSENO", name = "납세번호", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_KSENO;

    @MessageField(id = "GB033I_NAPNO", name = "전자납부번호", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_NAPNO;

    @MessageField(id = "GB033I_ILNO", name = "일련번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_ILNO;

    @MessageField(id = "GB033I_GMAK", name = "납부금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB033I_GMAK;

    @MessageField(id = "GB033I_RILJA", name = "납부일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_RILJA;

    @MessageField(id = "GB033I_GENO", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_GENO;

    @MessageField(id = "GB033I_PWD", name = "비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "03")
    private String GB033I_PWD; // 이중암호화 대응

    @MessageField(id = "GB033I_TEL", name = "전화번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_TEL;

    @MessageField(id = "GB033I_IPGB", name = "납부이용시스템", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_IPGB;

    @MessageField(id = "GB033I_NBTYPE", name = "납부형태", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_NBTYPE;

    @MessageField(id = "GB024O_BNCD", name = "분류코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB024O_BNCD;

    @MessageField(id = "GB024O_GIRO", name = "지로번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB024O_GIRO;

    @MessageField(id = "YI_RILJA", name = "예약일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YI_RILJA;

    @MessageField(id = "GB033I_CTOTGN", name = "총괄납부총고지건수", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_CTOTGN;

    @MessageField(id = "GB033I_CHONGNO", name = "총괄납부고유번호", length = 19, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_CHONGNO;

    @MessageField(id = "GB033I_TGMAK", name = "총괄납부총납부금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB033I_TGMAK;

    @MessageField(id = "GB033I_IGGB", name = "일괄구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_IGGB;

    @MessageField(id = "GB033I_DATAGN", name = "데이터건수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_DATAGN;

    @MessageField(id = "YIIPN", name = "IP정보", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC정보", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

    @MessageField(id = "YITRGB", name = "이체화면구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITRGB;

    @MessageField(id = "GB033I_IG_BNCD1", name = "발행기관분류코드1", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_BNCD1;

    @MessageField(id = "GB033I_IG_GIRO1", name = "이용기관지로번호1", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_GIRO1;

    @MessageField(id = "GB033I_IG_NAPNO1", name = "전자납부번호1", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_IG_NAPNO1;

    @MessageField(id = "GB033I_IG_NBILNO1", name = "납부순번1", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_NBILNO1;

    @MessageField(id = "GB033I_IG_GMAK1", name = "납부금액1", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB033I_IG_GMAK1;

    @MessageField(id = "GB033I_IG_GIHN1", name = "납부일자1", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_GIHN1;

    @MessageField(id = "GB033I_IG_BNCD2", name = "발행기관분류코드2", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_BNCD2;

    @MessageField(id = "GB033I_IG_GIRO2", name = "이용기관지로번호2", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_GIRO2;

    @MessageField(id = "GB033I_IG_NAPNO2", name = "전자납부번호2", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_IG_NAPNO2;

    @MessageField(id = "GB033I_IG_NBILNO2", name = "납부순번2", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_NBILNO2;

    @MessageField(id = "GB033I_IG_GMAK2", name = "납부금액2", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB033I_IG_GMAK2;

    @MessageField(id = "GB033I_IG_GIHN2", name = "납부일자2", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_GIHN2;

    @MessageField(id = "GB033I_IG_BNCD3", name = "발행기관분류코드3", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_BNCD3;

    @MessageField(id = "GB033I_IG_GIRO3", name = "이용기관지로번호3", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_GIRO3;

    @MessageField(id = "GB033I_IG_NAPNO3", name = "전자납부번호3", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_IG_NAPNO3;

    @MessageField(id = "GB033I_IG_NBILNO3", name = "납부순번3", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_NBILNO3;

    @MessageField(id = "GB033I_IG_GMAK3", name = "납부금액3", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB033I_IG_GMAK3;

    @MessageField(id = "GB033I_IG_GIHN3", name = "납부일자3", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_GIHN3;

    @MessageField(id = "GB033I_IG_BNCD4", name = "발행기관분류코드4", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_BNCD4;

    @MessageField(id = "GB033I_IG_GIRO4", name = "이용기관지로번호4", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_GIRO4;

    @MessageField(id = "GB033I_IG_NAPNO4", name = "전자납부번호4", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_IG_NAPNO4;

    @MessageField(id = "GB033I_IG_NBILNO4", name = "납부순번4", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_NBILNO4;

    @MessageField(id = "GB033I_IG_GMAK4", name = "납부금액4", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB033I_IG_GMAK4;

    @MessageField(id = "GB033I_IG_GIHN4", name = "납부일자4", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_GIHN4;

    @MessageField(id = "GB033I_IG_BNCD5", name = "발행기관분류코드5", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_BNCD5;

    @MessageField(id = "GB033I_IG_GIRO5", name = "이용기관지로번호5", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_GIRO5;

    @MessageField(id = "GB033I_IG_NAPNO5", name = "전자납부번호5", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB033I_IG_NAPNO5;

    @MessageField(id = "GB033I_IG_NBILNO5", name = "납부순번5", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_NBILNO5;

    @MessageField(id = "GB033I_IG_GMAK5", name = "납부금액5", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB033I_IG_GMAK5;

    @MessageField(id = "GB033I_IG_GIHN5", name = "납부일자5", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB033I_IG_GIHN5;

}
