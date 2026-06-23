package com.scbank.process.api.edmi.dto.mci;

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
@IntegrationMessage(id = "MciIbCidi001Res", type = Type.RESPONSE, description = "CI 연계정보 조회 응답개별부")
public class MciIbCidi001Res implements IMessageObject {

    @MessageField(id = "AOOUTMACNA", name = "출력마크로명", length = 8)
    private String AOOUTMACNA;

    @MessageField(id = "AOPRODCOD", name = "상품코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AOPRODCOD;

    @MessageField(id = "AOJPNO", name = "전표번호", length = 20)
    private String AOJPNO;

    @MessageField(id = "AOENGCMFNA1", name = "영문고객명1", length = 50)
    private String AOENGCMFNA1;

    @MessageField(id = "AOCMFNA", name = "고객명", length = 30)
    private String AOCMFNA;

    @MessageField(id = "AOBTHIL", name = "출생일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AOBTHIL;

    @MessageField(id = "AOSGMNT", name = "세그먼트", length = 3)
    private String AOSGMNT;

    @MessageField(id = "AOGSEGB", name = "과세구분", length = 2)
    private String AOGSEGB;

    @MessageField(id = "AOCMPMNOPN", name = "주요영업국", length = 3)
    private String AOCMPMNOPN;

    @MessageField(id = "AOCMPMNOPN2", name = "주요영업국2", length = 3)
    private String AOCMPMNOPN2;

    @MessageField(id = "AOCMPMNOPN3", name = "주요영업국3", length = 3)
    private String AOCMPMNOPN3;

    @MessageField(id = "AOCMPMNOPN4", name = "주요영업국4", length = 3)
    private String AOCMPMNOPN4;

    @MessageField(id = "AOKKCOD", name = "국가코드", length = 3)
    private String AOKKCOD;

    @MessageField(id = "AOKKCOD2", name = "국가코드2", length = 3)
    private String AOKKCOD2;

    @MessageField(id = "AOKKCOD3", name = "국가코드3", length = 3)
    private String AOKKCOD3;

    @MessageField(id = "AOKKCOD4", name = "국가코드4", length = 3)
    private String AOKKCOD4;

    @MessageField(id = "AOSMNOGB", name = "실명번호구분", length = 1)
    private String AOSMNOGB;

    @MessageField(id = "AOSMNO", name = "실명번호", length = 13)
    private String AOSMNO;

    @MessageField(id = "AOCDDBR", name = "CDD평가점", length = 3)
    private String AOCDDBR;

    @MessageField(id = "AOCDDLV", name = "CDD등급", length = 1)
    private String AOCDDLV;

    @MessageField(id = "AOCDDIL", name = "CDD이행일자", length = 9)
    private String AOCDDIL;

    @MessageField(id = "AOCDDLC", name = "CDD유효일자", length = 9)
    private String AOCDDLC;

    @MessageField(id = "AOCONFIRM", name = "CI등록여부", length = 1)
    private String AOCONFIRM;

    @MessageField(id = "AOCINOINF", name = "CI정보", length = 88)
    private String AOCINOINF;

    @MessageField(id = "AOCIFNO", name = "CIF번호", length = 12)
    private String AOCIFNO;

    @MessageField(id = "AOINFORGB", name = "정보구분", length = 2)
    private String AOINFORGB;

    @MessageField(id = "AOMGTNO", name = "관리번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AOMGTNO;

    @MessageField(id = "AOSTSGB", name = "사활구분", length = 1)
    private String AOSTSGB;

    @MessageField(id = "AOFSTSSIL10", name = "생성일자", length = 10)
    private String AOFSTSSIL10;

    @MessageField(id = "AOJYENDIL", name = "폐기일자", length = 10)
    private String AOJYENDIL;

    @MessageField(id = "AOFTRCOD", name = "생성업무코드", length = 6)
    private String AOFTRCOD;

}