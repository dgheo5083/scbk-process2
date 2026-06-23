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
@IntegrationMessage(id = "CbTbs07H53700Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "지방세통합")
public class CbTbs07H53700Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "W5312_JHGB", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_JHGB;

    @MessageField(id = "GB024O_BNCD", name = "분류코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB024O_BNCD;

    @MessageField(id = "GB024O_GIRO", name = "지로번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB024O_GIRO;

    @MessageField(id = "W5312_NASENO", name = "납세번호", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_NASENO;

    @MessageField(id = "W5312_NAPNO", name = "전자납부번호", length = 19, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_NAPNO;

    @MessageField(id = "W5312_ILNO", name = "일련번호", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_ILNO;

    @MessageField(id = "W5312_EASYNO", name = "간편납부번호", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_EASYNO;

    @MessageField(id = "W5312_IDSANO", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_IDSANO;

    @MessageField(id = "W5312_SEIPGN", name = "지방세입금구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_SEIPGN;

    @MessageField(id = "W5312_GOJIGN", name = "고지구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_GOJIGN;

    @MessageField(id = "W5312_SEMKNM", name = "세목명", length = 52, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_SEMKNM;

    @MessageField(id = "W5312_NAPNM", name = "납부자성명", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_NAPNM;

    @MessageField(id = "W5312_GJTYPE", name = "고지서서식구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_GJTYPE;

    @MessageField(id = "W5312_NBGEUM", name = "납기내금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_NBGEUM;

    @MessageField(id = "W5312_NAGEUM", name = "납기후금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_NAGEUM;

    @MessageField(id = "W5312_SSMK1GMK", name = "상세세목1금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_SSMK1GMK;

    @MessageField(id = "W5312_SSMK1GAK", name = "상세세목1가산금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_SSMK1GAK;

    @MessageField(id = "W5312_SSMK2GMK", name = "상세세목2금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_SSMK2GMK;

    @MessageField(id = "W5312_SSMK2GAK", name = "상세세목2가산금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_SSMK2GAK;

    @MessageField(id = "W5312_SSMK3GMK", name = "상세세목3금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_SSMK3GMK;

    @MessageField(id = "W5312_SSMK3GAK", name = "상세세목3가산금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_SSMK3GAK;

    @MessageField(id = "W5312_SSMK4GMK", name = "상세세목4금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_SSMK4GMK;

    @MessageField(id = "W5312_SSMK4GAK", name = "상세세목4가산금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_SSMK4GAK;

    @MessageField(id = "W5312_SSMK5GMK", name = "상세세목5금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_SSMK5GMK;

    @MessageField(id = "W5312_SSMK5GAK", name = "상세세목5가산금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_SSMK5GAK;

    @MessageField(id = "W5312_SSMK6GMK", name = "지방세3+5 금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_SSMK6GMK;

    @MessageField(id = "W5312_SSMK6GAK", name = "지방세 3+5 가산금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_SSMK6GAK;

    @MessageField(id = "W5312_KSESTN", name = "과세표준금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_KSESTN;

    @MessageField(id = "W5312_NBILJA", name = "납기일(납기내)", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_NBILJA;

    @MessageField(id = "W5312_NAILJA", name = "납기일(납기후)", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_NAILJA;

    @MessageField(id = "W5312_KSESH", name = "과세대상", length = 132, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_KSESH;

    @MessageField(id = "W5312_GBILJA", name = "고지자료발생일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_GBILJA;

    @MessageField(id = "W5312_ICHYN", name = "자동이체등록여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_ICHYN;

    @MessageField(id = "W5312_SBJMCD", name = "출금은행점별코드", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5312_SBJMCD;

    @MessageField(id = "W5312_NBILSI", name = "납부일시", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_NBILSI;

    @MessageField(id = "W5312_NBAGB", name = "납기내후구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_NBAGB;

    @MessageField(id = "W5312_GIROH", name = "지로번호한글", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_GIROH;

    @MessageField(id = "W5312_DUMY", name = "예비", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5312_DUMY;

    @MessageField(id = "W5313_EASYNO", name = "간편납부번호", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5313_EASYNO;

    @MessageField(id = "W5313_IDSANO", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5313_IDSANO;

    @MessageField(id = "W5313_CHONGNO", name = "총괄납부고유번호", length = 19, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5313_CHONGNO;

    @MessageField(id = "W5313_NAPNM", name = "납부자성명", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5313_NAPNM;

    @MessageField(id = "W5313_CTOTGN", name = "총괄납부총고지건수", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5313_CTOTGN;

    @MessageField(id = "W5313_CTOTGMK", name = "총괄납부총납부금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5313_CTOTGMK;

    @MessageField(id = "W5313_DUMY", name = "예비", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5313_DUMY;

    @MessageField(id = "YSINFO", name = "연속정보", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YSINFO;

    @MessageField(id = "YSINFODUMMY", name = "연속정보(더미)", length = 47, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YSINFODUMMY;

    @MessageField(id = "W5311_EASYNO", name = "간편납부번호", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5311_EASYNO;

    @MessageField(id = "W5311_IDSANO", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5311_IDSANO;

    @MessageField(id = "W5311_NAPNM", name = "납부자성명", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String W5311_NAPNM;

    @MessageField(id = "W5311_TOTGN", name = "총고지건수", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5311_TOTGN;

    @MessageField(id = "W5311_TOTGMK", name = "총납부금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5311_TOTGMK;

    @MessageField(id = "W5311_DATAGN", name = "데이터건수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String W5311_DATAGN;

    @MessageField(id = "W5311_JHINF", name = "반복부", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbTbs07H53700Res/W5311_DATAGN")
    private List<W5311_JHINF> W5311_JHINF;

    @Data
    public static class W5311_JHINF implements IMessageObject {

        @MessageField(id = "W5311_BKCD", name = "발행기관분류코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String W5311_BKCD;

        @MessageField(id = "W5311_GIRO", name = "이용기관지로번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String W5311_GIRO;

        @MessageField(id = "W5311_JIBGN", name = "지방세입금구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String W5311_JIBGN;

        @MessageField(id = "W5311_NASENO", name = "납세번호", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String W5311_NASENO;

        @MessageField(id = "W5311_NAPNO", name = "전자납부번호", length = 19, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String W5311_NAPNO;

        @MessageField(id = "W5311_ILNO", name = "일련번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String W5311_ILNO;

        @MessageField(id = "W5311_SEMKNM", name = "세목명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String W5311_SEMKNM;

        @MessageField(id = "W5311_GOJIGN", name = "고지구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String W5311_GOJIGN;

        @MessageField(id = "W5311_NGEUM", name = "납부금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String W5311_NGEUM;

        @MessageField(id = "W5311_NBAGB", name = "납기내후구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String W5311_NBAGB;

        @MessageField(id = "W5311_NAPGH", name = "납부기한", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String W5311_NAPGH;

        @MessageField(id = "W5311_ICHYN", name = "자동이체등록여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String W5311_ICHYN;

        @MessageField(id = "W5311_GIROH", name = "지로번호한글", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String W5311_GIROH;
    }

}
