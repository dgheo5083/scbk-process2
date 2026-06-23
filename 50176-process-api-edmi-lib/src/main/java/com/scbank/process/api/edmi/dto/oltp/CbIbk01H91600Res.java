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
@IntegrationMessage(id = "CbIbk01H91600Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "웹 회원가입 응답부")
public class CbIbk01H91600Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "Status", name = "이용자상태", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Status;

    @MessageField(id = "CustName", name = "성명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "PerNo", name = "주민등록번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerNo;

    @MessageField(id = "PerCode", name = "주민사업자구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerCode;

    @MessageField(id = "CustGubun", name = "거래고객 여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustGubun;

    @MessageField(id = "IdGubun", name = "이용자존재여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IdGubun;

    @MessageField(id = "SUserID", name = "찾은 이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SUserID;

    @MessageField(id = "Email", name = "이메일", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Email;

    @MessageField(id = "Tel1", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Tel1;

    @MessageField(id = "Tel2", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Tel2;

    @MessageField(id = "Tel3", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Tel3;

    @MessageField(id = "RegDate", name = "등록일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String RegDate;

    @MessageField(id = "YOADRGB1", name = "신구주소사용여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOADRGB1;

    @MessageField(id = "ZipCode", name = "구우편번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ZipCode;

    @MessageField(id = "Address", name = "자택주소1", length = 62, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Address;

    @MessageField(id = "YOZIPCD40", name = "신우편번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZIPCD40;

    @MessageField(id = "YOZIPD41", name = "신도로명코드", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZIPCD41;

    @MessageField(id = "YOZIPD42", name = "신일련번호", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZIPCD42;

    @MessageField(id = "YOZIPD43", name = "신지하여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZIPCD43;

    @MessageField(id = "YOZIPD44", name = "신건물번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZIPCD44;

    @MessageField(id = "YOSJUSO4", name = "신도로명", length = 102, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSJUSO4;

}
