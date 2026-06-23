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
@IntegrationMessage(id = "CbIbk01H29200Req", type = Type.REQUEST, description = "등록된 예약이체 조회 요청 전문")
public class CbIbk01H29200Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "ServicePart", name = "서비스구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ServicePart;

    @MessageField(id = "JhPart", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JhPart;

    @MessageField(id = "JhSDate", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JhSDate;

    @MessageField(id = "JhSTime", name = "조회시작시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JhSTime;

    @MessageField(id = "JsNum", name = "접수번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JsNum;

    @MessageField(id = "JhEDate", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JhEDate;

    @MessageField(id = "YgMsCnt", name = "요구명세건수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YgMsCnt;

    @MessageField(id = "KTransDate", name = "연속-이체지정일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KTransDate;

    @MessageField(id = "KTransTime", name = "연속-이체시각", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KTransTime;

    @MessageField(id = "KJsNum", name = "연속-접수번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KJsNum;
}
