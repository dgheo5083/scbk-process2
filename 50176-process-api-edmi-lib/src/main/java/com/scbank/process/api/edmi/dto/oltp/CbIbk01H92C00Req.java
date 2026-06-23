package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbk01H92C00Req", type = Type.REQUEST, captureSystem = "OLTP", description = "고객정보조회 요청 전문")
public class CbIbk01H92C00Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "로그인비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIIDGB", name = "로그인 구분", length = 1)
    private String YIIDGB;

    @MessageField(id = "YIJHGB", name = "거래구분", length = 1)
    private String YIJHGB;

    @MessageField(id = "YIJMNO", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
    private String YIJMNO;

    @MessageField(id = "YICMFJN", name = "고객점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YICMFJN;

    @MessageField(id = "YISMNOGB", name = "실명번호구분", length = 1)
    private String YISMNOGB;

    @MessageField(id = "YICMFNA", name = "H고객명 시작", length = 32, masking = true, maskingType = "04", sosi = true)
    private String YICMFNA;

    @MessageField(id = "YIENGNA", name = "영문고객명1", length = 50)
    private String YIENGNA;

    @MessageField(id = "YIUPSRGB", name = "우편수령구분", length = 1)
    private String YIUPSRGB;

    @MessageField(id = "YIHMUPNO", name = "자택우편번호", length = 6)
    private String YIHMUPNO;

    @MessageField(id = "YIHMJSO1", name = "H자택주소1", length = 42, masking = true, maskingType = "06", sosi = true)
    private String YIHMJSO1;

    @MessageField(id = "YIHMJSO2", name = "H자택주소2", length = 62, sosi = true)
    private String YIHMJSO2;

    @MessageField(id = "YITELRGN1", name = "전화지역번호1", length = 4)
    private String YITELRGN1;

    @MessageField(id = "YITELK1", name = "전화국번1", length = 4)
    private String YITELK1;

    @MessageField(id = "YITEL1", name = "전화번호1", length = 4, masking = true, maskingType = "03")
    private String YITEL1;

    @MessageField(id = "YIOFIUPNO", name = "직장우편번호", length = 6)
    private String YIOFIUPNO;

    @MessageField(id = "YIOFIJSO1", name = "H직장주소1", length = 42, masking = true, maskingType = "06", sosi = true)
    private String YIOFIJSO1;

    @MessageField(id = "YIOFIJSO2", name = "H직장주소2", length = 62, masking = true, maskingType = "06", sosi = true)
    private String YIOFIJSO2;

    @MessageField(id = "YITELRGN2", name = "전화지역번호2", length = 4)
    private String YITELRGN2;

    @MessageField(id = "YITELK2", name = "전화국번2", length = 4)
    private String YITELK2;

    @MessageField(id = "YITEL2", name = "전화번호2", length = 4, masking = true, maskingType = "03")
    private String YITEL2;

    @MessageField(id = "YIMBRGN", name = "핸드폰지역번호", length = 4)
    private String YIMBRGN;

    @MessageField(id = "YIMBK", name = "핸드폰국번", length = 4)
    private String YIMBK;

    @MessageField(id = "YIMBTEL", name = "핸드폰전화번호", length = 4, masking = true, maskingType = "03")
    private String YIMBTEL;

    @MessageField(id = "YIEMALJSO", name = "이메일주소", length = 60, masking = true, maskingType = "07")
    private String YIEMALJSO;

    @MessageField(id = "YIGSEGB", name = "과세구분", length = 2)
    private String YIGSEGB;

    @MessageField(id = "YIGRJGB", name = "거래자구분", length = 2)
    private String YIGRJGB;

    @MessageField(id = "YIJOBCOD", name = "직업코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJOBCOD;

    @MessageField(id = "YISDJGB", name = "고객소득자", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YISDJGB;

    @MessageField(id = "YICLDGBCOD", name = "양력음력구분", length = 1)
    private String YICLDGBCOD;

    @MessageField(id = "YIBTHIL", name = "출생일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBTHIL;

    @MessageField(id = "YIWEDYMCOD", name = "결혼유무코드", length = 1)
    private String YIWEDYMCOD;

    @MessageField(id = "YIJSCDGB1", name = "자택 신주소", length = 1)
    private String YIJSCDGB1;

    @MessageField(id = "YIJSCD4", name = "주소코드", length = 6)
    private String YIJSCD4;

    @MessageField(id = "YIJUSO4", name = "상세", length = 100, masking = true, maskingType = "06")
    private String YIJUSO4;

    @MessageField(id = "YIZIP41", name = "YIZIP41", length = 12)
    private String YIZIP41;

    @MessageField(id = "YIZIP42", name = "YIZIP42", length = 2)
    private String YIZIP42;

    @MessageField(id = "YIZIP43", name = "YIZIP43", length = 1)
    private String YIZIP43;

    @MessageField(id = "YIZIP44", name = "YIZIP44", length = 10)
    private String YIZIP44;

    @MessageField(id = "YIJSCDGB2", name = "직장 신주소", length = 1)
    private String YIJSCDGB2;

    @MessageField(id = "YIJSCD5", name = "YIJSCD5", length = 6)
    private String YIJSCD5;

    @MessageField(id = "YIJUSO5", name = "YIJUSO5", length = 100, masking = true, maskingType = "06")
    private String YIJUSO5;

    @MessageField(id = "YIZIP51", name = "YIZIP51", length = 12)
    private String YIZIP51;

    @MessageField(id = "YIZIP52", name = "YIZIP52", length = 2)
    private String YIZIP52;

    @MessageField(id = "YIZIP53", name = "YIZIP53", length = 1)
    private String YIZIP53;

    @MessageField(id = "YIZIP54", name = "YIZIP54", length = 10)
    private String YIZIP54;

    @MessageField(id = "YISMS", name = "YISMS", length = 1)
    private String YISMS;

    @MessageField(id = "YIKUK", name = "국적", length = 3)
    private String YIKUK;

    @MessageField(id = "YIOFINA", name = "H직장명", length = 30)
    private String YIOFINA;

    @MessageField(id = "YIDPTNA", name = "H부서명", length = 30)
    private String YIDPTNA;

    @MessageField(id = "YIJWINA1", name = "H직위명1", length = 10)
    private String YIJWINA1;

    @MessageField(id = "YIPAGI", name = "NTB고객신규", length = 1)
    private String YIPAGI;

    @MessageField(id = "YIWCMFJM", name = "신규점(반영처리용)", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIWCMFJM;

    @MessageField(id = "YIWCMFNO", name = "신규CMF(반영처리용)", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIWCMFNO;

    @MessageField(id = "YIWCIFNO", name = "신규CIF(반영처리용)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIWCIFNO;

    @MessageField(id = "YIEADGB", name = "영문주소구분", length = 1)
    private String YIEADGB;

    @MessageField(id = "YIEJUSO1", name = "영문/외국주소1", length = 45)
    private String YIEJUSO1;

    @MessageField(id = "YIEJUSO2", name = "영문/외국주소2", length = 45)
    private String YIEJUSO2;

    @MessageField(id = "YIEBUNHO", name = "해외전화번호", length = 22)
    private String YIEBUNHO;

    @MessageField(id = "YIFIRST", name = "CIF 최초생성일자구분", length = 1)
    private String YIFIRST;

    @MessageField(id = "YIIDUMMY", name = "더비", length = 289)
    private String YIIDUMMY;

}
