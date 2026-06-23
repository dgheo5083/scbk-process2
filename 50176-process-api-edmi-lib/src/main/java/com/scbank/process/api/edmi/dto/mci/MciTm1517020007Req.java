package com.scbank.process.api.edmi.dto.mci;

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
@AllArgsConstructor
@NoArgsConstructor
@IntegrationMessage(id = "MciTm1517020007Req", type = Type.REQUEST, description = "급여통장해지 인증 요청부")
public class MciTm1517020007Req implements IMessageObject {

    @MessageField(id = "SIGJWJN", name = "계좌점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SIGJWJN;

    @MessageField(id = "SIGJWKM", name = "계좌과목", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIGJWKM;

    @MessageField(id = "SIGJWNO", name = "계좌번호", length = 7, masking = true, maskingType = "02", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SIGJWNO;

    @MessageField(id = "SIHMKCOD", name = "항목코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SIHMKCOD;

    @MessageField(id = "SIHMKNY6", name = "항목내용6", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SIHMKNY6;

    @MessageField(id = "SIHMKNY10", name = "항목내용10", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SIHMKNY10;

    @MessageField(id = "SIHMKNY", name = "항목내용", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIHMKNY;

    @MessageField(id = "SIGISIL", name = "기산일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SIGISIL;

    @MessageField(id = "SIHMCJMKCOD", name = "환매채종목코드", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIHMCJMKCOD;

    @MessageField(id = "SIHMKJUMNO", name = "항목점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SIHMKJUMNO;

    @MessageField(id = "SIHMKKMCOD", name = "항목과목코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIHMKKMCOD;

    @MessageField(id = "SIHMKGJWNO", name = "항목계좌번호", length = 6, masking = true, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SIHMKGJWNO;

    @MessageField(id = "SIGJWPW", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SIGJWPW;

    @MessageField(id = "SIGRGB", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIGRGB;

    @MessageField(id = "SIDRSYUCOD", name = "등록사유코드", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIDRSYUCOD;

    @MessageField(id = "SITELRGNNO", name = "전화지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SITELRGNNO;

    @MessageField(id = "SITELKNO", name = "전화국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SITELKNO;

    @MessageField(id = "SITELNO1", name = "전화번호1", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SITELNO1;

    @MessageField(id = "SITELRGNNO2", name = "전화지역번호2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SITELRGNNO2;

    @MessageField(id = "SITELKNO2", name = "전화국번2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SITELKNO2;

    @MessageField(id = "SITELNO2", name = "전화번호2", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SITELNO2;

    @MessageField(id = "SIMAECHE", name = "이용매체", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIMAECHE;

    @MessageField(id = "SIEMALJSO", name = "이메일주소", length = 60, masking = true, maskingType = "07", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIEMALJSO;

    @MessageField(id = "SIHDPNO1", name = "휴대폰1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIHDPNO1;

    @MessageField(id = "SIHDPNO2", name = "휴대폰2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIHDPNO2;

    @MessageField(id = "SIHDPNO3", name = "휴대폰3", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIHDPNO3;

    @MessageField(id = "SIDAERINM", name = "수신대리인성명", length = 30, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIDAERINM;

    @MessageField(id = "SIDRJMNO", name = "수신대리인주민번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIDRJMNO;

    @MessageField(id = "SIGJWUPCCOD", name = "계좌업체코드", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIGJWUPCCOD;

    @MessageField(id = "SIUPCHNM", name = "업체명", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIUPCHNM;

    @MessageField(id = "SINEWJSOGB1", name = "신주소구분1", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SINEWJSOGB1;

    @MessageField(id = "SIKYUBN", name = "권유자행번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SIKYUBN;

    @MessageField(id = "SITBGJL", name = "잔액통보거절", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SITBGJL;

    @MessageField(id = "FLDdelimiter1", name = "FLDdelimiter1", length = 1, defaultValue = "0x1F")
    private String FLDdelimiter1;

    @MessageField(id = "SEGdelimiter1", name = "SEGdelimiter1", length = 1, defaultValue = "0x1E")
    private String SEGdelimiter1;

    @MessageField(id = "ENDdelimiter1", name = "ENDdelimiter1", length = 1, defaultValue = "0xFF")
    private String ENDdelimiter1;

    @MessageField(id = "ENDdelimiter2", name = "ENDdelimiter2", length = 1, defaultValue = "0xFF")
    private String ENDdelimiter2;
}
