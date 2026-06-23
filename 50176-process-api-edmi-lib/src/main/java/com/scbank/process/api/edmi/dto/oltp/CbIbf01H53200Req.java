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
@IntegrationMessage(id = "CbIbf01H53200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "외화정기예금조회해지")
public class CbIbf01H53200Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자 ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TransPassword; // APP 이중암호화 대응 _DNFE2E_ _/DNFE2E_, WEB 이중암호화 대응 _DVKEY_ _/DVKEY_

    @MessageField(id = "YIJSNO", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJSNO;

    @MessageField(id = "YIJGJNO", name = "해약계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJGJNO;

    @MessageField(id = "YIJGJPW", name = "해약계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "03")
    private String YIJGJPW; // APP 이중암호화 대응 _DNFE2E_ _/DNFE2E_, WEB 이중암호화 대응 _DVKEY_ _/DVKEY_

    @MessageField(id = "YIUDAE", name = "우대구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUDAE;

    @MessageField(id = "YIINGUBUN", name = "보험구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIINGUBUN;

    @MessageField(id = "YIINILJA", name = "보험시작일자", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIINILJA;

    @MessageField(id = "YIIGJNO", name = "입금계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIIGJNO;

    @MessageField(id = "YICRATE", name = "적용환율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YICRATE;

    @MessageField(id = "YIDUMY1", name = "더미1", length = 425, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMY1;

    @MessageField(id = "YIIPN", name = "IP 정보", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC 정보", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

    @MessageField(id = "YIDUMY2", name = "더미2", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMY2;

}
