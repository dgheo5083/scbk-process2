package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IntegrationMessage(id = "CbIbk01D96000Req", type = Type.REQUEST, captureSystem = "OLTP", description = "해외/특정IP차단서비스 등록 요청전문")
public class CbIbk01D96000Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "로그인비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YISCGB", name = "신청구분값", length = 1)
    private String YISCGB;

    @MessageField(id = "YISTGB", name = "전체/특정", length = 1)
    private String YISTGB;

    @MessageField(id = "YIGUNSU", name = "국가갯수", length = 2)
    private Integer YIGUNSU;

    @MessageField(id = "YINTNM0", name = "국가이름", length = 12, sosi = true)
    private String YINTNM0;

    @MessageField(id = "YINTCD0", name = "국가코드", length = 3)
    private String YINTCD0;

    @MessageField(id = "YINTNM1", name = "국가이름", length = 12, sosi = true)
    private String YINTNM1;

    @MessageField(id = "YINTCD1", name = "국가코드", length = 3)
    private String YINTCD1;

    @MessageField(id = "YINTNM2", name = "국가이름", length = 12, sosi = true)
    private String YINTNM2;

    @MessageField(id = "YINTCD2", name = "국가코드", length = 3)
    private String YINTCD2;

    @MessageField(id = "YINTNM3", name = "국가이름", length = 12, sosi = true)
    private String YINTNM3;

    @MessageField(id = "YINTCD3", name = "국가코드", length = 3)
    private String YINTCD3;

    @MessageField(id = "YINTNM4", name = "국가이름", length = 12, sosi = true)
    private String YINTNM4;

    @MessageField(id = "YINTCD4", name = "국가코드", length = 3)
    private String YINTCD4;

    @MessageField(id = "YINTNM5", name = "국가이름", length = 12, sosi = true)
    private String YINTNM5;

    @MessageField(id = "YINTCD5", name = "국가코드", length = 3)
    private String YINTCD5;

    @MessageField(id = "YINTNM6", name = "국가이름", length = 12, sosi = true)
    private String YINTNM6;

    @MessageField(id = "YINTCD6", name = "국가코드", length = 3)
    private String YINTCD6;

    @MessageField(id = "YINTNM7", name = "국가이름", length = 12, sosi = true)
    private String YINTNM7;

    @MessageField(id = "YINTCD7", name = "국가코드", length = 3)
    private String YINTCD7;

    @MessageField(id = "YINTNM8", name = "국가이름", length = 12, sosi = true)
    private String YINTNM8;

    @MessageField(id = "YINTCD8", name = "국가코드", length = 3)
    private String YINTCD8;

    @MessageField(id = "YINTNM9", name = "국가이름", length = 12, sosi = true)
    private String YINTNM9;

    @MessageField(id = "YINTCD9", name = "국가코드", length = 3)
    private String YINTCD9;

}