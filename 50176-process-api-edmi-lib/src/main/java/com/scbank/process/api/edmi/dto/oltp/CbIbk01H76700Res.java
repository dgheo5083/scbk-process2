package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;
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
@IntegrationMessage(id = "CbIbk01H76700Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "대출 철회대상 여부조회 응답부")
public class CbIbk01H76700Res implements IMessageObject {

    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSID;

    @MessageField(id = "YOJMNO", name = "주민／사업자번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJMNO;

    @MessageField(id = "YOCOUNT", name = "배열갯수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOCOUNT;

    @MessageField(id = "YODDATA", name = "계좌배열")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H76700Res/YOCOUNT")
    private List<YODDATA> YODDATA;

    @Data
    public static class YODDATA implements IMessageObject {
        @MessageField(id = "YOGEJWA", name = "대출계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGEJWA;

        @MessageField(id = "YORVKGB", name = "계약철회대상", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YORVKGB;

        @MessageField(id = "YOYEOGB", name = "여신구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOYEOGB;

        @MessageField(id = "YOSNAMT", name = "승인금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal YOSNAMT;

    }
}
