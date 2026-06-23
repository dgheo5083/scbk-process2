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
@IntegrationMessage(id = "CbTbs03H76500Req", type = Type.REQUEST, description = "KCB CI 조회 요청부")
public class CbTbs03H76500Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIJUMIN", name = "주민번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMIN;

    @MessageField(id = "YITCOM", name = "통신사구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITCOM;

    @MessageField(id = "YITEL1", name = "모바일지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITEL1;

    @MessageField(id = "YITEL2", name = "모바일국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITEL2;

    @MessageField(id = "YITEL3", name = "모바일전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITEL3;

    @MessageField(id = "YIMINO", name = "MB CINO", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMINO;

    @MessageField(id = "YISO1", name = "한글시작", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISO1;

    @MessageField(id = "YINAME", name = "H고객명", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YINAME;

    @MessageField(id = "YISI1", name = "한글끝", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISI1;

}