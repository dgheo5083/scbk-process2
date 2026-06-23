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
@IntegrationMessage(id = "CbTbs07H41400Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "현대카드 이용내역조회 전문")
public class CbTbs07H41400Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOYSGB", name = "연속거래여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYSGB;

    @MessageField(id = "YOPAGE", name = "페이지번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOPAGE;

    @MessageField(id = "YOMSSU", name = "명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOMSSU;

    @MessageField(id = "YODATAR", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbTbs07H41400Res/YOMSSU")
    private List<CbTbs07H41400ResGrid> YODATAR;

    @Data
    public static class CbTbs07H41400ResGrid implements IMessageObject {

        @MessageField(id = "YOCADNO", name = "카드번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOCADNO;

        @MessageField(id = "YOIYIL", name = "이용일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOIYIL;

        @MessageField(id = "YOIYAK", name = "이용금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOIYAK;

        @MessageField(id = "YOGMJNM", name = "가맹점명", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGMJNM;

        @MessageField(id = "YOCNGBNM", name = "취소구분명", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOCNGBNM;

        @MessageField(id = "YOSINO", name = "승인번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOSINO;

        @MessageField(id = "YOSISTNM", name = "승인상태명", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOSISTNM;

        @MessageField(id = "YOGJGBNM", name = "결제구분명", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGJGBNM;

        @MessageField(id = "YOGMJTEL", name = "이용가맹점 TEL", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGMJTEL;

    }

}
