package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@IntegrationMessage(id = "CbIbf01H10200Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "목표환율매매 본거래 응답")

public class CbIbf01H10200Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOREFNO", name = "예약번호", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOREFNO;

    @MessageField(id = "YOGUBUN", name = "흐름분류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGUBUN;

    @MessageField(id = "YOGOGAK", name = "고객번호", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGOGAK;

    @MessageField(id = "YODRIL", name = "등록일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODRIL;

    @MessageField(id = "YOTIME", name = "등록시간", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTIME;

    @MessageField(id = "YOBRNO", name = "영업점코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBRNO;

    @MessageField(id = "YOYYJR", name = "예약구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYYJR;

    @MessageField(id = "YOTONG", name = "예약통화", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTONG;

    @MessageField(id = "YOGMAK", name = "외화예약금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGMAK;

    @MessageField(id = "YOWGMAK", name = "원화예약금액", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWGMAK;

    @MessageField(id = "YORATE", name = "예약환율", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YORATE;

    @MessageField(id = "YOEXDAY", name = "예약만기일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOEXDAY;

    @MessageField(id = "YOWGJNO", name = "원화지급계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWGJNO;

    @MessageField(id = "YOFGJNO", name = "외화지급계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOFGJNO;

    @MessageField(id = "YOEMAIL", name = "E-mail", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOEMAIL;

    @MessageField(id = "YODLRATE", name = "체결된환율", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODLRATE;

    @MessageField(id = "YOCRGBN", name = "처리여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCRGBN;

    @MessageField(id = "YOUDYUL", name = "환율우대율", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUDYUL;

}
