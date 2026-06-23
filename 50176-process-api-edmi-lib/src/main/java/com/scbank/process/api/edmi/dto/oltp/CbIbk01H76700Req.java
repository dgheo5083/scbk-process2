package com.scbank.process.api.edmi.dto.oltp;

import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
// @Builder
@IntegrationMessage(id = "CbIbk01H76700Req", type = Type.REQUEST, captureSystem = "OLTP", description = "대출 철회대상 여부조회 요청부")
public class CbIbk01H76700Req implements IMessageObject {

    @MessageField(id = "YIUSID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUSID;

    @MessageField(id = "YIPASS", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String YIPASS;

    @MessageField(id = "YICOUNT", name = "배열갯수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YICOUNT;

    @MessageField(id = "YIDDATA", name = "계좌배열")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "Ti1ibk01H767Req/YICOUNT")
    private List<YIDDATA> YIDDATA;

    @Data
    public static class YIDDATA implements IMessageObject {
        @MessageField(id = "YIGEJWA", name = "대출계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YIGEJWA;
    }
}
