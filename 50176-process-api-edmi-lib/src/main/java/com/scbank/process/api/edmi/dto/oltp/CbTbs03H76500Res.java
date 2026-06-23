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
@IntegrationMessage(id = "CbTbs03H76500Res", type = Type.RESPONSE, description = "KCB CI 조회 응답부")
public class CbTbs03H76500Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOCINO", name = "CINO", length = 88, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCINO;

    @MessageField(id = "YOTCOM", name = "통신사구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTCOM;

    @MessageField(id = "YOTEL1", name = "모바일지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTEL1;

    @MessageField(id = "YOTEL2", name = "모바일국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTEL2;

    @MessageField(id = "YOTEL3", name = "모바일전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTEL3;

    @MessageField(id = "YOMINO", name = "MB CINO", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMINO;

    @MessageField(id = "YOSO1", name = "한글시작", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSO1;

    @MessageField(id = "YONAME", name = "H고객명", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YONAME;

    @MessageField(id = "YOSI1", name = "한글끝", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSI1;

    @MessageField(id = "YORESULT", name = "CI번호 일치여부 Y:일치", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YORESULT;

}
