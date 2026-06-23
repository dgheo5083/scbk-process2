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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H71700Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "예금이율조회 응답 전문")
public class CbIbk01H71700Res implements IMessageObject {

    @MessageField(id = "YOUSID", name = "이용자ID", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSID;

    @MessageField(id = "YOPASS", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPASS;

    @MessageField(id = "YOSPNAME", name = "상품명", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSPNAME;

    @MessageField(id = "YOGGNSU", name = "기본이율건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOGGNSU;

    @MessageField(id = "YOGINFO", name = "기본이율정보")
    @RepeatedField(repeatType = RepeatType.FIXED, repeatCount = "20")
    private List<YOGINFO> YOGINFO;

    @Data
    public static class YOGINFO implements IMessageObject {
        @MessageField(id = "YOGSTAIL", name = "기본이율시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOGSTAIL;

        @MessageField(id = "YOGENDIL", name = "기본이율종료일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOGENDIL;

        @MessageField(id = "YOGIYUL", name = "기본이율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOGIYUL;

        @MessageField(id = "YOGJUKYO", name = "기본이율적요", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGJUKYO;

        @MessageField(id = "YOGJUKY2", name = "기본이율적요2", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGJUKY2;

    }

    @MessageField(id = "YOUGNSU", name = "우대이율건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUGNSU;

    @MessageField(id = "YOUINFO", name = "우대이율정보")
    @RepeatedField(repeatType = RepeatType.FIXED, repeatCount = "20")
    private List<YOUINFO> YOUINFO;

    @Data
    public static class YOUINFO implements IMessageObject {
        @MessageField(id = "YOUSTAIL", name = "우대이율시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOUSTAIL;

        @MessageField(id = "YOUENDIL", name = "우대이율종료일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOUENDIL;

        @MessageField(id = "YOUIYUL", name = "우대이율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOUIYUL;

        @MessageField(id = "YOUJUKYO", name = "우대이율적요", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOUJUKYO;

        @MessageField(id = "YOUJUKY2", name = "우대이율적요2", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOUJUKY2;

    }

}
