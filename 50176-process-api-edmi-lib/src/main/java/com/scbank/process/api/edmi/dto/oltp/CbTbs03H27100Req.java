package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@IntegrationMessage(id = "CbTbs03H27100Req", type = Type.REQUEST, captureSystem = "OLTP", description = "역송금발송 요청부")
public class CbTbs03H27100Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, masking = true, maskingType = "03")
    private String TransPassword;

    @MessageField(id = "ApplNum", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO )
    private String ApplNum;
    
    @MessageField(id = "CGAcctNum", name = "출금계좌번호", length = 14, masking = true, maskingType = "02" )
    private String CGAcctNum;
    
    @MessageField(id = "CGPassword", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CGPassword;
    
    @MessageField(id = "YIJGGBN", name = "지급구분", length = 1)
    private String YIJGGBN;
    
//    @MessageField(id = "YISO1", name = "한글시작", length = 1)
//    private String YISO1;
    
    @MessageField(id = "YIONAME", name = "비고란", length = 26, sosi = true)
    private String YIONAME;
    
//    @MessageField(id = "YISI1", name = "한글끝", length = 1)
//    private String YISI1;
    
    @MessageField(id = "YIGRNO", name = "관리번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGRNO;
    
    @MessageField(id = "YIICGB", name = "즉시／예약구분", length = 1)
    private String YIICGB;
    
    @MessageField(id = "YIIBKCD", name = "입금은행코드", length = 3)
    private String YIIBKCD;
    
    @MessageField(id = "YIIGJNO", name = "입금계좌번호", length = 16)
    private String YIIGJNO;
    
    @MessageField(id = "YITICGM", name = "총이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YITICGM;
    
    @MessageField(id = "YITSSR", name = "총수수료금액", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YITSSR;
    
    @MessageField(id = "YIICGM", name = "이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIICGM;
    
    @MessageField(id = "YISSR", name = "수수료금액", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YISSR;
    
    @MessageField(id = "YISSRGB", name = "수수료구분", length = 1)
    private String YISSRGB;
    
    @MessageField(id = "YIICJR", name = "이체종류", length = 1)
    private String YIICJR;
    
    @MessageField(id = "YIJGCD", name = "증권／보험사코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJGCD;
    
    @MessageField(id = "YICMSCD", name = "의뢰인코드", length = 24)
    private String YICMSCD;
    
//    @MessageField(id = "YISO2", name = "한글시작", length = 1)
//    private String YISO2;
    
    @MessageField(id = "YISCNM", name = "수취인명", length = 22, masking = true, maskingType = "04", sosi = true)
    private String YISCNM;
    
//    @MessageField(id = "YISI2", name = "한글끝", length = 1)
//    private String YISI2;
    
    @MessageField(id = "YIYICIL", name = "예약이체일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIYICIL;
    
    @MessageField(id = "YIYICSI", name = "예약이체시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIYICSI;
    
    @MessageField(id = "YINEWIPGJ", name = "신입금계좌이체여부", length = 1)
    private String YINEWIPGJ;
    
    @MessageField(id = "YISIMPLE", name = "간편이체여부", length = 1)
    private String YISIMPLE;
    
    @MessageField(id = "YIINNUM", name = "인증번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIINNUM;
    
    @MessageField(id = "YIIDUMMY", name = "더미", length = 196)
    private String YIIDUMMY;
    
    @MessageField(id = "YIIPMAC", name = "거래명세IPMAC추가", length = 170)
    private String YIIPMAC;

}