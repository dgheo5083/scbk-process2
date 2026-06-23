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
@IntegrationMessage(id = "CbIbk01H29600Res", type = Type.RESPONSE, description = "출금계좌등록및삭제")
public class CbIbk01H29600Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "GLGubun", name = "거래구분 (J read D등록 H삭제)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GLGubun;

    @MessageField(id = "AcctNum", name = "등록계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "YSAcctNum", name = "연속출금계좌번호", length = 14, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YSAcctNum;

    @MessageField(id = "RCount", name = "명세건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "TR296Rec", name = "반복부1")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H296Res/RCount")
    private List<TR296Rec> TR296Rec;

    @Data
    public static class TR296Rec implements IMessageObject {
        @MessageField(id = "Assort", name = "출금계좌종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Assort;

        @MessageField(id = "DrawAcctNum", name = "출금계좌번호", length = 14, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DrawAcctNum;
    }
}
