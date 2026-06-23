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
@IntegrationMessage(id = "CbIbk01H72200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "펀드 통장상세조회")
public class CbIbk01H72200Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIGJNO", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String YIGJNO;

    @MessageField(id = "YIGPASS", name = "계좌비밀번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String YIGPASS;

    @MessageField(id = "YIGRGBN", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGRGBN;

    @MessageField(id = "YIJUGBN", name = "1.주민3.계좌", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUGBN;

    @MessageField(id = "YIMSSU", name = "요구명세수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIMSSU;

    @MessageField(id = "YIJMGB", name = "주민구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJMGB;

    @MessageField(id = "YICMCGB", name = "연속구분", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICMCGB;

    @MessageField(id = "YIHPAGE", name = "현재요청페이지", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIHPAGE;

    @MessageField(id = "YITPAGE", name = "연속KEY전체페이지수", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITPAGE;

    @MessageField(id = "YIBLKGB", name = "Y:캐나다고객블로킹여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIBLKGB;

    @MessageField(id = "YIDUMMY", name = "연속정보BYTE맞춤", length = 46, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;

}