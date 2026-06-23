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
@IntegrationMessage(id = "MciIbCidi001Req", type = Type.REQUEST, description = "CI 연계정보 조회 요청개별부")
public class MciIbCidi001Req implements IMessageObject {

    @MessageField(id = "AIGRGB", name = "거래구분", length = 1)
    private String AIGRGB;

    @MessageField(id = "AISMNO", name = "실명번호", length = 13)
    private String AISMNO;

    @MessageField(id = "AIGUBUN", name = "등록구분", length = 1)
    private String AIGUBUN;

    @MessageField(id = "AICRGB", name = "처리구분", length = 1)
    private String AICRGB;

    @MessageField(id = "AICINOINF", name = "CI정보", length = 88)
    private String AICINOINF;

    @MessageField(id = "AICIFNO", name = "CIF번호", length = 12)
    private String AICIFNO;

    @MessageField(id = "AIJUMNO", name = "점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AIJUMNO;

    @MessageField(id = "AIKMCOD", name = "과목코드", length = 2)
    private String AIKMCOD;

    @MessageField(id = "AIGJWNO", name = "계좌번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AIGJWNO;

    @MessageField(id = "AIFTRCOD", name = "생성업무코드", length = 6)
    private String AIFTRCOD;

    @MessageField(id = "FLDdelimiter1", name = "FLDdelimiter1", length = 1, defaultValue = "0x1F")
    private String FLDdelimiter1;

    @MessageField(id = "SEGdelimiter1", name = "SEGdelimiter1", length = 1, defaultValue = "0x1E")
    private String SEGdelimiter1;

    @MessageField(id = "ENDdelimiter1", name = "ENDdelimiter1", length = 1, defaultValue = "0xFF")
    private String ENDdelimiter1;

    @MessageField(id = "ENDdelimiter2", name = "ENDdelimiter2", length = 1, defaultValue = "0xFF")
    private String ENDdelimiter2;

}