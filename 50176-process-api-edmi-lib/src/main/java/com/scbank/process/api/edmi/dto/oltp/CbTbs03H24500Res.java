package com.scbank.process.api.edmi.dto.oltp;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbTbs03H24500Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "오픈뱅킹 자동이체 정보 조회 응답 전문")
public class CbTbs03H24500Res implements IMessageObject {

    @MessageField(id = "YOUSID", name = "이용자번호", length = 10)
    private String YOUSID;

    @MessageField(id = "YOCJOKYB", name = "충전계좌등록가능여부(Y:충전계좌사용가능)", length = 1)
    private String YOCJOKYB;

    @MessageField(id = "YOCJGDR", name = "충전계좌기등록여부(Y:충전계좌로이미사용중)", length = 1)
    private String YOCJGDR;

    @MessageField(id = "YOCJHD", name = "충전계좌의한도대출계좌여부(Y:한도대출계좌임)", length = 1)
    private String YOCJHD;

    @MessageField(id = "YODRST", name = "등록상태", length = 1)
    private String YODRST;

    @MessageField(id = "YODUMY1", name = "예비", length = 50)
    private String YODUMY1;

    @MessageField(id = "YOARRSU", name = "배열내조립건수", length = 3)
    private Integer YOARRSU;

    @MessageField(id = "YOARR", name = "계좌정보")
    @RepeatedField
    private List<YOARR> YOARR;

    @Data
    public static class YOARR implements IMessageObject {

        @MessageField(id = "YOIGJNO", name = "입금계좌번호", length = 11)
        private String YOIGJNO;

        @MessageField(id = "YOCGJNO", name = "출금계좌번호", length = 20)
        private String YOCGJNO;

        @MessageField(id = "YOICJR", name = "자동이체종류", length = 1)
        private String YOICJR;

        @MessageField(id = "YOUSID", name = "이용자번호", length = 10)
        private String YOUSID;

        @MessageField(id = "YODRST", name = "등록상태", length = 1)
        private String YODRST;

        @MessageField(id = "YOJJECD", name = "자동중지응답코드", length = 5)
        private String YOJJECD;

        @MessageField(id = "YOCBKCD", name = "출금은행코드", length = 3)
        private String YOCBKCD;

        @MessageField(id = "YOCIFNO", name = "CIF번호", length = 13)
        private String YOCIFNO;

        @MessageField(id = "YOICIL", name = "매월자동이체일자", length = 2)
        private String YOICIL;

        @MessageField(id = "YOICGM", name = "이체금액", length = 15)
        private String YOICGM;

        @MessageField(id = "YOICSTA", name = "자동이체시작일", length = 8)
        private String YOICSTA;

        @MessageField(id = "YOICEND", name = "자동이체종료일", length = 8)
        private String YOICEND;

        @MessageField(id = "YOSO1", name = "SO", length = 1)
        private String YOSO1;

        @MessageField(id = "YOJUKYO", name = "입금계좌표기내용", length = 20)
        private String YOJUKYO;

        @MessageField(id = "YOSI1", name = "SI", length = 1)
        private String YOSI1;

        @MessageField(id = "YOSO2", name = "SO", length = 1)
        private String YOSO2;

        @MessageField(id = "YOSGNM", name = "송금인명", length = 24)
        private String YOSGNM;

        @MessageField(id = "YOSI2", name = "SI", length = 1)
        private String YOSI2;

        @MessageField(id = "YOSO3", name = "SO", length = 1)
        private String YOSO3;

        @MessageField(id = "YOSCNM", name = "수취인명", length = 24)
        private String YOSCNM;

        @MessageField(id = "YOSI3", name = "SI", length = 1)
        private String YOSI3;

        @MessageField(id = "YODRIL", name = "등록일자", length = 6)
        private String YODRIL;

        @MessageField(id = "YODRTM", name = "등록시각", length = 6)
        private String YODRTM;

        @MessageField(id = "YOHJIL", name = "해지일자", length = 8)
        private String YOHJIL;

        @MessageField(id = "YOHJTM", name = "해지시각", length = 6)
        private String YOHJTM;
    }

    @MessageField(id = "YOYSYB", name = "연속여부(Y:DATA더있음)", length = 1)
    private String YOYSYB;

    @MessageField(id = "YOYSKEY", name = "계좌정보")
    @RepeatedField
    private List<YOYSKEY> YOYSKEY;

    @Data
    public static class YOYSKEY implements IMessageObject {

        @MessageField(id = "YOYSIGJ", name = "연속키－입금계좌", length = 11)
        private String YOYSIGJ;

        @MessageField(id = "YOYSCJG", name = "연속키－출금계좌", length = 20)
        private String YOYSCJG;

        @MessageField(id = "YOYSICJR", name = "연속키－이체종류", length = 1)
        private String YOYSICJR;
    }

    @MessageField(id = "YODUMY2", name = "예비", length = 90)
    private String YODUMY2;

}
