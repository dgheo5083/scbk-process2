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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk03H03900Res", type = Type.RESPONSE, description = "전계좌조회 원장 Refresh 확인 거래 응답부")
public class CbIbk03H03900Res implements IMessageObject {
    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSID;

    @MessageField(id = "YODUMY1", name = "더미1", length = 47, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMY1;

    @MessageField(id = "YOMSSU", name = "명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOMSSU;

    @MessageField(id = "YOGJARR", name = "계좌번호배열")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk03H039Res/YOMSSU")
    private List<YOGJARR> YOGJARR;

    @Getter
    @Setter
    public static class YOGJARR implements IMessageObject {
        @MessageField(id = "YOGJNO", name = "외화잔액", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGJNO;

        @MessageField(id = "YOTONM", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOTONM;

        @MessageField(id = "YOKIND", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOKIND;

        @MessageField(id = "YOBALANCESign", name = "최초원장잔액부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOBALANCESign;

        @MessageField(id = "YOBALANCE", name = "최초원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOBALANCE;

        @MessageField(id = "YOBALANCE2Sign", name = "refresh원장잔액부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOBALANCE2Sign;

        @MessageField(id = "YOBALANCE2", name = "refresh원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOBALANCE2;

        @MessageField(id = "YOHJYB", name = "해지계좌여부Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOHJYB;

    }
}