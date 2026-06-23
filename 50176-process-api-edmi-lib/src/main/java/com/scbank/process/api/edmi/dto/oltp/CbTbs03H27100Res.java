package com.scbank.process.api.edmi.dto.oltp;

import java.util.List;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H93600Res.REC_01;
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
@IntegrationMessage(id = "CbTbs03H27100Res", type = Type.RESPONSE, description = "역송금발송 응답 전문", captureSystem = "OLTP")
public class CbTbs03H27100Res implements IMessageObject {

    @MessageField(id = "UserID", name = "사용자ID", length = 10)
    private String UserID;

    @MessageField(id = "YOGRIL", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOGRIL;
    
    @MessageField(id = "CGAcctNum", name = "출금계좌번호", length = 14, masking = true, maskingType = "02")
    private String CGAcctNum;
    
    @MessageField(id = "YOONAME", name = "입금통장표시내용", length = 26)
    private String YOONAME;
    
    @MessageField(id = "YOTRADE", name = "상호명", length = 22)
    private String YOTRADE;
    
    @MessageField(id = "YOJGGBN", name = "지급구분", length = 1)
    private String YOJGGBN;
    
    @MessageField(id = "YOICGB", name = "즉시예약구분", length = 1)
    private String YOICGB;
    
    @MessageField(id = "YOIBKCD", name = "입금은행코드", length = 3)
    private String YOIBKCD;
    
    @MessageField(id = "YOBKNM", name = "입금은행명", length = 6)
    private String YOBKNM;
    
    @MessageField(id = "YOIGJNO", name = "입금계좌번호", length = 16)
    private String YOIGJNO;
    
    @MessageField(id = "YOTICGM", name = "총이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOTICGM;
    
    @MessageField(id = "YOTSSR", name = "총수수료", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOTSSR;
    
    @MessageField(id = "YOSSRGB", name = "수수료구분", length = 1)
    private String YOSSRGB;
    
    @MessageField(id = "YOSSRMSG", name = "수수료안내메세지", length = 62)
    private String YOSSRMSG;
    
    @MessageField(id = "YOICJR", name = "이체종류", length = 1)
    private String YOICJR;
    
    @MessageField(id = "YOJGCD", name = "증권／보험사코드", length = 2)
    private String YOJGCD;
    
    @MessageField(id = "YOCMSCD", name = "CMS코드", length = 24)
    private String YOCMSCD;
    
    @MessageField(id = "YOCMSDIS", name = "CMS코드처리여부", length = 1)
    private String YOCMSDIS;
    
    @MessageField(id = "YOCMSYB", name = "의뢰인코드사용여부", length = 1)
    private String YOCMSYB;
    
    @MessageField(id = "YOCMSG1", name = "의뢰인코드설명", length = 62)
    private String YOCMSG1;
    
    @MessageField(id = "YOCMSG2", name = "의뢰인코드설명2", length = 62)
    private String YOCMSG2;
    
    @MessageField(id = "YOSCYB", name = "수취인명입력여부", length = 1)
    private String YOSCYB;
    
    @MessageField(id = "YOSCNM", name = "출금통장표시내용", length = 22)
    private String YOSCNM;
    
    @MessageField(id = "YOIPNM", name = "입금인명", length = 22, masking = true, maskingType = "04")
    private String YOIPNM;
    
    @MessageField(id = "YOJINM", name = "지급인명", length = 26, masking = true, maskingType = "04")
    private String YOJINM;
    
    @MessageField(id = "YOYICIL", name = "예약이체일간", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOYICIL;
    
    @MessageField(id = "YOYICSI", name = "예약이체시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOYICSI;
    
    @MessageField(id = "YOJANAK", name = "출금후　잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOJANAK;
    
    @MessageField(id = "YOSTIIK", name = "신탁이익금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOSTIIK;
    
    @MessageField(id = "YOSEAK", name = "신탁세액", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOSEAK;
    
    @MessageField(id = "YOJAE", name = "재입금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOJAE;
    
    @MessageField(id = "YOSJNO", name = "시작접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOSJNO;

    @MessageField(id = "YOGUBUN", name = "구분자", length = 4)
    private String YOGUBUN;
    
    @MessageField(id = "YOEJNO", name = "최종접수번호", length = 5)
    private String YOEJNO;
    
    @MessageField(id = "YOMSSU", name = "분할ARRAY건수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOMSSU;
    
    @MessageField(id = "RECLIST", name = "출력계좌반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbTbs03H27100Res/YOMSSU")
    private List<REC> RECLIST;

    @Data
    public static class REC implements IMessageObject {
    	@MessageField(id = "YOJSNO", name = "이체접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOJSNO;
    	
    	@MessageField(id = "YOICGM", name = "이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOICGM;
    	
    	@MessageField(id = "YOSSR", name = "수수료", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOSSR;
    	
    	@MessageField(id = "YOJMBH", name = "전문관리번호", length = 13)
        private String YOJMBH;
    	
    	@MessageField(id = "YOERCD", name = "에러코드 : 여러계좌", length = 4)
        private String YOERCD;
    	
    	@MessageField(id = "YOERMSG", name = "처리결과내용", length = 12)
        private String YOERMSG;
    	
    	@MessageField(id = "YORESULT", name = "처리결과 1:정상", length = 1)
        private String YORESULT;
    	
    	@MessageField(id = "YODUMY", name = "DUMMY", length = 8)
        private String YODUMY;
    }

}
