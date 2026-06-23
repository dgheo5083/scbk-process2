package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H13000Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "개인정보 열람")
public class CbIbk01H13000Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOINAME", name = "한글성명", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOINAME;

    @MessageField(id = "YOIJMIN", name = "주민번호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIJMIN;

    @MessageField(id = "YOIZIP1", name = "자택주소", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIZIP1;

    @MessageField(id = "YOITEL1", name = "자택전화번호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOITEL1;

    @MessageField(id = "YOIHPNO", name = "휴대폰번호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIHPNO;

    @MessageField(id = "YOIEMAL", name = "이메일주소", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIEMAL;

    @MessageField(id = "YOIZIP2", name = "직장주소", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIZIP2;

    @MessageField(id = "YOITEL2N", name = "직장전화번호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOITEL2N;

    @MessageField(id = "YOIOFF1", name = "직장명", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIOFF1;

    @MessageField(id = "YOIOFF2D", name = "근무부서", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIOFF2D;

    @MessageField(id = "YOIBRTHD", name = "생년월일", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIBRTHD;

    @MessageField(id = "YOIBRGBN", name = "생일구분 양/음", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIBRGBN;

    @MessageField(id = "YOIPASS", name = "여권번호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIPASS;

    @MessageField(id = "YOINTNL", name = "국적", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOINTNL;

    @MessageField(id = "YOIFAXNL", name = "팩스번호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIFAXNL;

    @MessageField(id = "YOIJIK1L", name = "직위", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIJIK1L;

    @MessageField(id = "YOIJIK2", name = "직업군", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIJIK2;

    @MessageField(id = "YOISNID", name = "SNS ID", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOISNID;

    @MessageField(id = "YOIENME", name = "영문성명", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIENME;

    @MessageField(id = "YOISEXT", name = "성별", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOISEXT;
}
