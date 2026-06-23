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
@IntegrationMessage(id = "CbIbk01H87X00Req", type = Type.REQUEST, captureSystem = "OLTP", description = "증명서>예금잔액신청")
public class CbIbk01H87X00Req implements IMessageObject {
    @MessageField(id = "YIUSID", name = "이용자번호", length = 10)
    private String YIUSID;

    @MessageField(id = "YIJMNO", name = "주민등록번호", length = 13)
    private String YIJMNO;

    @MessageField(id = "YIPTYB", name = "포인트결제여부(Y/N)", length = 1)
    private String YIPTYB;

    @MessageField(id = "YISGJNO", name = "수수료출금계좌", length = 11)
    private String YISGJNO;

    @MessageField(id = "YIGPASS", name = "출금계좌비밀번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String YIGPASS;

    @MessageField(id = "YIFEE", name = "수수료금액", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIFEE;

    @MessageField(id = "YILANGGB", name = "언어구분", length = 1)
    private String YILANGGB;

    @MessageField(id = "YIJANPRFIL", name = "잔액증명기준일", length = 8)
    private String YIJANPRFIL;

    @MessageField(id = "YIPURPOSE", name = "이용목적", length = 22, sosi = true)
    private String YIPURPOSE;

    @MessageField(id = "YICURCOD", name = "통화코드", length = 3)
    private String YICURCOD;

    @MessageField(id = "YICMFENM", name = "고객영문명", length = 30)
    private String YICMFENM;

    @MessageField(id = "YICMFENGJSO1", name = "고객영문주소", length = 45)
    private String YICMFENGJSO1;

    @MessageField(id = "YICMFENGJSO2", name = "고객영문주소2", length = 45)
    private String YICMFENGJSO2;

    @MessageField(id = "YIDUMMY1", name = "DUMMY", length = 100)
    private String YIDUMMY1;

    @MessageField(id = "YIJRPGSU", name = "조립건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIJRPGSU;

    @MessageField(id = "YIGJWNO2_01", name = "계좌번호_01", length = 11)
    private String YIGJWNO2_01;

    @MessageField(id = "YIGJWNO2_02", name = "계좌번호_02", length = 11)
    private String YIGJWNO2_02;

    @MessageField(id = "YIGJWNO2_03", name = "계좌번호_03", length = 11)
    private String YIGJWNO2_03;

    @MessageField(id = "YIGJWNO2_04", name = "계좌번호_04", length = 11)
    private String YIGJWNO2_04;

    @MessageField(id = "YIGJWNO2_05", name = "계좌번호_05", length = 11)
    private String YIGJWNO2_05;

    @MessageField(id = "YIGJWNO2_06", name = "계좌번호_06", length = 11)
    private String YIGJWNO2_06;

    @MessageField(id = "YIGJWNO2_07", name = "계좌번호_07", length = 11)
    private String YIGJWNO2_07;

}
