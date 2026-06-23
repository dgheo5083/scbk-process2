package com.scbank.process.api.svc.common.service.securities.dto.blocking;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * PRC 서비스 요청 정보 클래스
 * 해외IP차단서비스 본거래
 */
@Data
@IntegrationMessage(id = "SecBlkApplyBlockingForeignIpMainRequest", description = "해외IP차단서비스 본거래 요청 DTO", type = Type.REQUEST)
public class SecBlkApplyBlockingForeignIpMainRequest implements IMessageObject {

    @MessageField(id = "yiSCGB", name = "신청구분값")
    private String yiSCGB; // selectMenu

    @MessageField(id = "yiSTGB", name = "전체/특정")
    private String yiSTGB; // SelectPart

    @MessageField(id = "yiGUNSU", name = "국가갯수")
    private Integer yiGUNSU;

    @MessageField(id = "yiNTNM0", name = "국가이름")
    private String yiNTNM0;

    @MessageField(id = "yiNTCD0", name = "국가코드")
    private String yiNTCD0;

    @MessageField(id = "yiNTNM1", name = "국가이름")
    private String yiNTNM1;

    @MessageField(id = "yiNTCD1", name = "국가코드")
    private String yiNTCD1;

    @MessageField(id = "yiNTNM2", name = "국가이름")
    private String yiNTNM2;

    @MessageField(id = "yiNTCD2", name = "국가코드")
    private String yiNTCD2;

    @MessageField(id = "yiNTNM3", name = "국가이름")
    private String yiNTNM3;

    @MessageField(id = "yiNTCD3", name = "국가코드")
    private String yiNTCD3;

    @MessageField(id = "yiNTNM4", name = "국가이름")
    private String yiNTNM4;

    @MessageField(id = "yiNTCD4", name = "국가코드")
    private String yiNTCD4;

    @MessageField(id = "yiNTNM5", name = "국가이름")
    private String yiNTNM5;

    @MessageField(id = "yiNTCD5", name = "국가코드")
    private String yiNTCD5;

    @MessageField(id = "yiNTNM6", name = "국가이름")
    private String yiNTNM6;

    @MessageField(id = "yiNTCD6", name = "국가코드")
    private String yiNTCD6;

    @MessageField(id = "yiNTNM7", name = "국가이름")
    private String yiNTNM7;

    @MessageField(id = "yiNTCD7", name = "국가코드")
    private String yiNTCD7;

    @MessageField(id = "yiNTNM8", name = "국가이름")
    private String yiNTNM8;

    @MessageField(id = "yiNTCD8", name = "국가코드")
    private String yiNTCD8;

    @MessageField(id = "yiNTNM9", name = "국가이름")
    private String yiNTNM9;

    @MessageField(id = "yiNTCD9", name = "국가코드")
    private String yiNTCD9;
}
