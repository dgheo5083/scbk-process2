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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H92C00Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "고객정보조회 응답 전문")
public class CbIbk01H92C00Res implements IMessageObject {

    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
    private String YOUSID;

    @MessageField(id = "YOIDGB", name = "로그온구분", length = 1)
    private String YOIDGB;

    @MessageField(id = "YOJHGB", name = "거래구분", length = 1)
    private String YOJHGB;

    @MessageField(id = "YOJMNO", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
    private String YOJMNO;

    @MessageField(id = "YOEXIST", name = "당행고객임", length = 1)
    private String YOEXIST;

    @MessageField(id = "YOWORKER", name = "급여생활자임", length = 1)
    private String YOWORKER;

    @MessageField(id = "YOCDDOK", name = "CDD 정상", length = 1)
    private String YOCDDOK;

    @MessageField(id = "YOOJUM", name = "신규고객점번", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOOJUM;

    @MessageField(id = "YOOCMFNO", name = "신규고객번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOOCMFNO;

    @MessageField(id = "YOSUSIN", name = "수신계좌여부", length = 1)
    private String YOSUSIN;

    @MessageField(id = "YOIBUSER", name = "인터넷뱅킹고객여부", length = 1)
    private String YOIBUSER;

    @MessageField(id = "YOEMAIL", name = "EMAIL", length = 60, masking = true, maskingType = "07")
    private String YOEMAIL;

    @MessageField(id = "YOHDDD", name = "휴대폰통신회사", length = 4)
    private String YOHDDD;

    @MessageField(id = "YOHGUK", name = "휴대폰국번", length = 4)
    private String YOHGUK;

    @MessageField(id = "YOHTEL", name = "휴대폰전화번호", length = 4, masking = true, maskingType = "03")
    private String YOHTEL;

    @MessageField(id = "YOFXYN", name = "외환고객여부유무", length = 1)
    private String YOFXYN;

    @MessageField(id = "YOGJGB", name = "거주구분/거주국", length = 1)
    private String YOGJGB;

    @MessageField(id = "YOCCGB", name = "개인사업자/CC구분", length = 1)
    private String YOCCGB;

    @MessageField(id = "YOPAGIHJ", name = "파기해제여부", length = 1)
    private String YOPAGIHJ;

    @MessageField(id = "YODUMMY", name = "더미", length = 1)
    private String YODUMMY;

    @MessageField(id = "YOOGKCD", name = "거주국", length = 3)
    private String YOOGKCD;

    @MessageField(id = "YOOCDDNI", name = "국적", length = 3)
    private String YOOCDDNI;

    @MessageField(id = "YOOBIRNC", name = "출생국", length = 3)
    private String YOOBIRNC;

    @MessageField(id = "YOOFAGKCD", name = "해외주소국가코드", length = 3)
    private String YOOFAGKCD;

    @MessageField(id = "YOOFTGKCD", name = "해외전화국가코드", length = 3)
    private String YOOFTGKCD;

    @MessageField(id = "YOFIRST", name = "CIF 최초생성일자", length = 8)
    private String YOFIRST;

    @MessageField(id = "YOCDDPGJ", name = "Y:CDD평가중임", length = 1)
    private String YOCDDPGJ;

    @MessageField(id = "YOCDDKYCLV", name = "CDD리스크등급", length = 1)
    private String YOCDDKYCLV;

    @MessageField(id = "YOCDDKYCLC", name = "CDD유효일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOCDDKYCLC;

    @MessageField(id = "YONTBYN", name = "HI통장_NTB우대여부", length = 1)
    private String YONTBYN;

    @MessageField(id = "YOCDDRV", name = "CDD Product  대상여부 (Y:대상, 공백:비대상)", length = 1)
    private String YOCDDRV;

    @MessageField(id = "YOCDDILJA", name = "CDD 이행일자", length = 8)
    private String YOCDDILJA;

    @MessageField(id = "YODUMMY2", name = "더미2", length = 157)
    private String YODUMMY2;

    @MessageField(id = "YOEADGB", name = "영문주소연결주소코드", length = 1)
    private String YOEADGB;

    @MessageField(id = "YOEJUSO1", name = "영문/외국주소1", length = 45, multiBytes = true)
    private String YOEJUSO1;

    @MessageField(id = "YOEJUSO2", name = "영문/외국주소2", length = 45, multiBytes = true)
    private String YOEJUSO2;

    @MessageField(id = "YOGEOJU", name = "거주구분(1:거주, 2:비거주)", length = 1)
    private String YOGEOJU;

    @MessageField(id = "YOGEORE", name = "고객구분(5:개사)", length = 2)
    private String YOGEORE;

    @MessageField(id = "YOIBJMGB", name = "고객업무구분(1:개인, 2:개사, 3:법인)", length = 1)
    private String YOIBJMGB;

    @MessageField(id = "YOGUNSU", name = "등록개인사업자수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int YOGUNSU;

    @MessageField(id = "YOGUNSU_REC", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H92C00Res/YOGUNSU")
    private List<YOGUNSU_REC_GRID> YOGUNSU_REC;

    @Getter
    @Setter
    public static class YOGUNSU_REC_GRID implements IMessageObject {

        @MessageField(id = "YOSASAUP", name = "개인사업자번호", length = 10)
        private String YOSASAUP;

        @MessageField(id = "YOSANAME", name = "개인사업자상호한글", length = 32)
        private String YOSANAME;

        @MessageField(id = "YOSAENAME", name = "개인사업자상호영문", length = 50)
        private String YOSAENAME;

        @MessageField(id = "YOSASINUP", name = "개인사업산업분류코드", length = 6)
        private String YOSASINUP;

    }

    @MessageField(id = "YOMSSU", name = "출력조립수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int YOMSSU;

    @MessageField(id = "YOMSSU_REC", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H92C00Res/YOMSSU")
    private List<YOMSSU_REC_GRID> YOMSSU_REC;

    @Getter
    @Setter
    public static class YOMSSU_REC_GRID implements IMessageObject {

        @MessageField(id = "YOACMFBR", name = "점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOACMFBR;

        @MessageField(id = "YOACMFNO", name = "고객번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOACMFNO;

        @MessageField(id = "YOADUMMY", name = "더미", length = 20)
        private String YOADUMMY;

    }

}
