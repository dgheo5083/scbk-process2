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
@IntegrationMessage(id = "CbTbs07H41400Req", type = Type.REQUEST, description = "현대카드 이용내역조회 요청")
public class CbTbs07H41400Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIJHGB", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJHGB;

    @MessageField(id = "YIHCGOYU", name = "현대카드고유번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIHCGOYU;

    @MessageField(id = "YISIGB", name = "승인구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISIGB;

    @MessageField(id = "YIJHSTAIL", name = "조회시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJHSTAIL;

    @MessageField(id = "YIJHENDIL", name = "조회종료일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJHENDIL;

    @MessageField(id = "YIYSGB", name = "연속거래여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIYSGB;

    @MessageField(id = "YIPAGE", name = "페이지번호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPAGE;

}