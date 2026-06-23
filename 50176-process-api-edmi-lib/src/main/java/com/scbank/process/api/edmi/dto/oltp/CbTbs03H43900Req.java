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
@IntegrationMessage(id = "CbTbs03H43900Req", type = Type.REQUEST, description = "은행연합회조회 요청부", captureSystem = "OLTP")
public class CbTbs03H43900Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "InquiryGubun", name = "조회구분(1:한도조회,2:전금융기관조회,3:계좌별한도조회)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InquiryGubun;

    @MessageField(id = "InquiryCnt", name = "조회건수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer InquiryCnt;

    @MessageField(id = "NextDivision", name = "연속구분", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String NextDivision;

    @MessageField(id = "NextCountNo", name = "연속전문번호", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String NextCountNo;

    @MessageField(id = "YIGUBN", name = "구분 0:세금우대, 1:생계형, 2: 청년도약계좌", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGUBN;

    @MessageField(id = "YIJUMIN", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMIN;

    @MessageField(id = "YIGJNO", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO;

}
