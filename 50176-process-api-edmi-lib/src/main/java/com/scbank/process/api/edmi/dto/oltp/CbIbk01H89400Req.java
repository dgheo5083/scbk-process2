package com.scbank.process.api.edmi.dto.oltp;

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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@IntegrationMessage(id = "CbIbk01H89400Req", type = Type.REQUEST, description = "파기대상고객 해제거래 및 국가코드등록 응답부 요청부")
public class CbIbk01H89400Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIJMNO", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
    private String YIJMNO;

    @MessageField(id = "YIGRGB", name = "거래구분", length = 1)
    private String YIGRGB;

    @MessageField(id = "YIKUK", name = "국가코드", length = 3)
    private String YIKUK;

    @MessageField(id = "YICDDGB", name = "CDD정보입력구분Y", length = 1)
    private String YICDDGB;

    @MessageField(id = "YIJAGSRC", name = "자금원천", length = 2)
    private String YIJAGSRC;

    @MessageField(id = "YIUSECD", name = "거래목적", length = 2)
    private String YIUSECD;

    @MessageField(id = "YIYSGUPPR", name = "예상가입상품", length = 1)
    private String YIYSGUPPR;

    @MessageField(id = "YIYSAUM", name = "AUM", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIYSAUM;

    @MessageField(id = "YIISIC", name = "ISIC(상세직업코드)", length = 4)
    private String YIISIC;

    @MessageField(id = "YIWTYPE", name = "WORKTYPE(직업구분)", length = 1)
    private String YIWTYPE;

    @MessageField(id = "YIDTABYN", name = "당타발송금거래YN", length = 1)
    private String YIDTABYN;

    @MessageField(id = "YIDTABAUM", name = "당타발예상금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIDTABAUM;

    @MessageField(id = "YIDTABSU", name = "당타발예상건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIDTABSU;

    @MessageField(id = "YICHANGB", name = "신청채널구분", length = 1)
    private String YICHANGB;

    @MessageField(id = "YISAUPIL", name = "개인사업설립일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YISAUPIL;

    @MessageField(id = "YIIPJIGS", name = "예상월평균입금건수　", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIIPJIGS;

    @MessageField(id = "YIIPJIAK", name = "예상월평균입금금액　", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIIPJIAK;

    @MessageField(id = "YIMAECAK", name = "연간매출액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIMAECAK;

    @MessageField(id = "YISASAUP", name = "개인사업자번호", length = 10)
    private String YISASAUP;

    @MessageField(id = "YIENAYN", name = "영문명수기입력여부", length = 1)
    private String YIENAYN;

    @MessageField(id = "YIENAME", name = "네이버영문명", length = 50)
    private String YIENAME;

    @MessageField(id = "YIRSDYN", name = "해외장기거주여부 Y/N", length = 1)
    private String YIRSDYN;

    @MessageField(id = "YIFATYN", name = "해외납세의무신고대상", length = 1)
    private String YIFATYN;

    @MessageField(id = "YIFNAME", name = "FATCA보고영문명", length = 50)
    private String YIFNAME;

    @MessageField(id = "YIGKCD", name = "해외납세국가코드", length = 3)
    private String YIGKCD;

    @MessageField(id = "YIAUSCD", name = "추가거래목적", length = 2)
    private String YIAUSCD;

    @MessageField(id = "YIJJSRC", name = "자금원천-자금제공자", length = 1)
    private String YIJJSRC;

    @MessageField(id = "YIJISIC", name = "자금원천-자금제공자직업코드", length = 4)
    private String YIJISIC;

    @MessageField(id = "YIBISIC", name = "자금원천-이전직장직업코드", length = 4)
    private String YIBISIC;

    @MessageField(id = "YIMAUM", name = "월소득금액구간코드", length = 1)
    private String YIMAUM;
}
