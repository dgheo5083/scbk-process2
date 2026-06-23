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
@IntegrationMessage(id = "CbTbs07H53700Req", type = Type.REQUEST, captureSystem = "OLTP", description = "지방세통합")
public class CbTbs07H53700Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "UMGubun", name = "업무구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UMGubun;

    @MessageField(id = "GB013I_JHGB", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB013I_JHGB;

    @MessageField(id = "GB013I_JHJR", name = "조회종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB013I_JHJR;

    @MessageField(id = "GB013I_UPJR", name = "업무종류", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB013I_UPJR;

    @MessageField(id = "GB013I_BNCD", name = "분류코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB013I_BNCD;

    @MessageField(id = "GB013I_GIRO", name = "지로번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB013I_GIRO;

    @MessageField(id = "GB013I_JUMIN", name = "주민번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB013I_JUMIN;

    @MessageField(id = "GB013I_EASYNO", name = "간편납부번호", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB013I_EASYNO;

    @MessageField(id = "GB023I_NAPNO", name = "전자납부번호", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB023I_NAPNO;

    @MessageField(id = "GB023I_ILNO", name = "일련번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB023I_ILNO;

    @MessageField(id = "YSINFO", name = "연속정보", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YSINFO;

    @MessageField(id = "YSINFODUMMY", name = "연속정보(더미)", length = 47, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YSINFODUMMY;

    @MessageField(id = "YGINT", name = "요구명세수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YGINT;

    @MessageField(id = "YITRGB", name = "이체화면구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITRGB;

    @MessageField(id = "YIOGJNO", name = "출금계좌번호", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIOGJNO;

    @MessageField(id = "YIOGPAS", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03")
    private String YIOGPAS;

}
