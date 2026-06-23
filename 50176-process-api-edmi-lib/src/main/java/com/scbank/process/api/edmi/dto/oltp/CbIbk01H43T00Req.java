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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H43T00Req", type = Type.REQUEST, captureSystem = "OLTP", description = "모바일비과세 서류제출 및 조회 요청 전문")
public class CbIbk01H43T00Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YICRGB", name = "처리구분 (1:등록, 2:조회)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICRGB;

    @MessageField(id = "YIJUMIN", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMIN;

    @MessageField(id = "YIGAIPJA", name = "가입자구분(1:고령자, 2:장애인, 3:국가유공자, 4:기초생활수급자, 5:5.18민주화운동부상자)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGAIPJA;

}
