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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@IntegrationMessage(id = "CbIbk01D91200Res", type = Type.REQUEST, captureSystem = "OLTP", description = "조회회원가입 응답부")
public class CbIbk01D91200Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TrxDate", name = "거래일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TrxDate;

    @MessageField(id = "TrxTime", name = "거래시각", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TrxTime;

    @MessageField(id = "CustName1", name = "성명1", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName1;

    @MessageField(id = "CustName2", name = "성명2", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName2;

    @MessageField(id = "PerNo", name = "주민등록번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerNo;

    @MessageField(id = "AcctCardNum", name = "기본계좌", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctCardNum;

    @MessageField(id = "Tele", name = "전화번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Tele;

    @MessageField(id = "HomePostNum", name = "우편번호", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HomePostNum;

    @MessageField(id = "HomeAddr", name = "주소(자택)", length = 62, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HomeAddr;

    @MessageField(id = "JobPostNum", name = "우편번호(직장)", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JobPostNum;

    @MessageField(id = "JobAddr", name = "주소(직장)", length = 62, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JobAddr;

    @MessageField(id = "YOADRGB1", name = "신구주소사용여부(자택)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOADRGB1;

    @MessageField(id = "YOZIPTXT1", name = "구주소1(자택)", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZIPTXT1;

    @MessageField(id = "YOZIPCD40", name = "신우편번호(자택)", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZIPCD40;

    @MessageField(id = "YOZIPCD41", name = "신도로명코드(자택)", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZIPCD41;

    @MessageField(id = "YOZIPCD42", name = "신일련번호(자택)", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZIPCD42;

    @MessageField(id = "YOZIPCD43", name = "신지하여부(자택)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZIPCD43;

    @MessageField(id = "YOZIPCD44", name = "신건물번호(자택)", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZIPCD44;

    @MessageField(id = "YOSJUSO4", name = "신도로명(자택)", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSJUSO4;

    @MessageField(id = "YOJUSO4", name = "신상세(자택)", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJUSO4;

    @MessageField(id = "YOJIKGB", name = "신구주소사용여부(직장)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJIKGB;

    @MessageField(id = "YOJIKTXT1", name = "구주소1(직장)", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJIKTXT1;

    @MessageField(id = "YOJIKCD40", name = "신우편번호(직장)", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJIKCD40;

    @MessageField(id = "YOJIKCD41", name = "신도로명코드(직장)", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJIKCD41;

    @MessageField(id = "YOJIKCD42", name = "신일련번호(직장)", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJIKCD42;

    @MessageField(id = "YOJIKCD43", name = "신지하여부(직장)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJIKCD43;

    @MessageField(id = "YOJIKCD44", name = "신건물번호(직장)", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJIKCD44;

    @MessageField(id = "YOJIKDORO", name = "신도로명(직장)", length = 72, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJIKDORO;

    @MessageField(id = "YOJIKSANG", name = "신상세(직장)", length = 102, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJIKSANG;

}
