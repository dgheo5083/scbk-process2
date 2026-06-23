package com.scbank.process.api.svc.shared.components.customer.dto;

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

@Data
@IntegrationMessage(id = "CustInfoInquiryResponse", type = Type.RESPONSE, description = "고객정보조회 응답")
public class CustCddInfoInquiryResponse implements IMessageObject {
	
	@MessageField(id = "userCiInfo", name = "사용자 CI")
    private String userCiInfo;

    @MessageField(id = "yoUsId", name = "이용자번호", length = 10, masking = true, maskingType = "01")
    private String yoUsId;

    @MessageField(id = "yoIdGb", name = "로그온구분", length = 1)
    private String yoIdGb;

    @MessageField(id = "yoJhGb", name = "거래구분", length = 1)
    private String yoJhGb;

    @MessageField(id = "yoJmNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
    private String yoJmNo;

    @MessageField(id = "yoExist", name = "당행고객임", length = 1)
    private String yoExist;

    @MessageField(id = "yoWorker", name = "급여생활자임", length = 1)
    private String yoWorker;

    @MessageField(id = "yoCddOk", name = "CDD 정상", length = 1)
    private String yoCddOk;

    @MessageField(id = "yoOjum", name = "신규고객점번", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String yoOjum;

    @MessageField(id = "yoOcmfNo", name = "신규고객번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String yoOcmfNo;

    @MessageField(id = "yoSusin", name = "수신계좌여부", length = 1)
    private String yoSusin;

    @MessageField(id = "yoIbUser", name = "인터넷뱅킹고객여부", length = 1)
    private String yoIbUser;

    @MessageField(id = "yoEmail", name = "EMAIL", length = 60, masking = true, maskingType = "07")
    private String yoEmail;

    @MessageField(id = "yoHddd", name = "휴대폰통신회사", length = 4)
    private String yoHddd;

    @MessageField(id = "yoHguk", name = "휴대폰국번", length = 4)
    private String yoHguk;

    @MessageField(id = "yoHtel", name = "휴대폰전화번호", length = 4, masking = true, maskingType = "03")
    private String yoHtel;

    @MessageField(id = "yoFxYn", name = "외환고객여부유무", length = 1)
    private String yoFxYn;

    @MessageField(id = "yoGjGb", name = "거주구분/거주국", length = 1)
    private String yoGjGb;

    @MessageField(id = "yoCcGb", name = "개인사업자/CC구분", length = 1)
    private String yoCcGb;

    @MessageField(id = "yoPagiHj", name = "파기해제여부", length = 1)
    private String yoPagiHj;

    @MessageField(id = "yoDummy", name = "더미", length = 1)
    private String yoDummy;

    @MessageField(id = "yoOGkCd", name = "거주국", length = 3)
    private String yoOGkCd;

    @MessageField(id = "yoOCddNi", name = "국적", length = 3)
    private String yoOCddNi;

    @MessageField(id = "yoOBirNc", name = "출생국", length = 3)
    private String yoOBirNc;

    @MessageField(id = "yoOFaGkCd", name = "해외주소국가코드", length = 3)
    private String yoOFaGkCd;

    @MessageField(id = "yoOFtGkCd", name = "해외전화국가코드", length = 3)
    private String yoOFtGkCd;

    @MessageField(id = "yoFirst", name = "CIF 최초생성일자", length = 8)
    private String yoFirst;

    @MessageField(id = "yoCddPgj", name = "Y:CDD평가중임", length = 1)
    private String yoCddPgj;

    @MessageField(id = "yoCddKycLv", name = "CDD리스크등급", length = 1)
    private String yoCddKycLv;

    @MessageField(id = "yoCddKycLc", name = "CDD유효일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String yoCddKycLc;

    @MessageField(id = "yoNtbYn", name = "HI통장_NTB우대여부", length = 1)
    private String yoNtbYn;

    @MessageField(id = "yoCddRv", name = "CDD Product  대상여부 (Y:대상, 공백:비대상)", length = 1)
    private String yoCddRv;

    @MessageField(id = "yoCddIlja", name = "CDD 이행일자", length = 8)
    private String yoCddIlja;
    
    @MessageField(id = "yoCddIlFlag", name = "CDD 일자도래여부")
    private String yoCddIlFlag;

    @MessageField(id = "yoDummy2", name = "더미2", length = 157)
    private String yoDummy2;

    @MessageField(id = "yoEAdGb", name = "영문주소연결주소코드", length = 1)
    private String yoEAdGb;

    @MessageField(id = "yoEJuso1", name = "영문/외국주소1", length = 45, multiBytes = true)
    private String yoEJuso1;

    @MessageField(id = "yoEJuso2", name = "영문/외국주소2", length = 45, multiBytes = true)
    private String yoEJuso2;

    @MessageField(id = "yoGeoju", name = "거주구분(1:거주, 2:비거주)", length = 1)
    private String yoGeoju;

    @MessageField(id = "yoGeore", name = "고객구분(5:개사)", length = 2)
    private String yoGeore;

    @MessageField(id = "yoIbJmGb", name = "고객업무구분(1:개인, 2:개사, 3:법인)", length = 1)
    private String yoIbJmGb;

    @MessageField(id = "yoGunsu", name = "등록개인사업자수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String yoGunsu;

    @MessageField(id = "yoGunsuRec", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CustCddInfoInquiryResponse/yoGunsu")
    private List<yoGunsuRecGrid> yoGunsuRec;

    @Getter
    @Setter
    public static class yoGunsuRecGrid implements IMessageObject {

        @MessageField(id = "yoSaSaup", name = "개인사업자번호", length = 10)
        private String yoSaSaup;

        @MessageField(id = "yoSaName", name = "개인사업자상호한글", length = 32)
        private String yoSaName;

        @MessageField(id = "yoSaEname", name = "개인사업자상호영문", length = 50)
        private String yoSaEname;

        @MessageField(id = "yoSaSinUp", name = "개인사업산업분류코드", length = 6)
        private String yoSaSinUp;

    }

    @MessageField(id = "yoMssu", name = "출력조립수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String yoMssu;

    @MessageField(id = "yoMssuRec", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CustCddInfoInquiryResponse/yoMssu")
    private List<yoMssuRecGrid> yoMssuRec;

    @Getter
    @Setter
    public static class yoMssuRecGrid implements IMessageObject {

        @MessageField(id = "yoACmfBr", name = "점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String yoACmfBr;

        @MessageField(id = "yoACmfNo", name = "고객번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String yoACmfNo;

        @MessageField(id = "yoADummy", name = "더미", length = 20)
        private String yoADummy;

    }
}
