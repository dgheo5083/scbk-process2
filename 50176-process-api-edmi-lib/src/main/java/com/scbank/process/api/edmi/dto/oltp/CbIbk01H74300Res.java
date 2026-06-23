package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;
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
@IntegrationMessage(id = "CbIbk01H74300Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "증명서>연말정산증명서-장기주택저당 차입금이자상환증명서")
public class CbIbk01H74300Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "PerBusNo", name = "주민/사업자번호", length = 13, masking = true, maskingType = "01")
    private String PerBusNo;

    @MessageField(id = "Hname", name = "성명", length = 32, masking = true, maskingType = "03")
    private String Hname;

    @MessageField(id = "Hjuso", name = "주소", length = 100, masking = true, maskingType = "05")
    private String Hjuso;

    @MessageField(id = "FSTIL", name = "최초대출일", length = 8)
    private String FSTIL;

    @MessageField(id = "LASGI", name = "대출기한", length = 8)
    private String LASGI;

    @MessageField(id = "GEOCI", name = "거치기간", length = 5)
    private String GEOCI;

    @MessageField(id = "IJNIL", name = "주택취득임차일", length = 8)
    private String IJNIL;

    @MessageField(id = "SULIL", name = "근저당설정일", length = 8)
    private String SULIL;

    @MessageField(id = "SIZE", name = "전용면적", length = 5)
    private String SIZE;

    @MessageField(id = "GJNO", name = "계좌번호", length = 11, masking = true, maskingType = "02")
    private String GJNO;

    @MessageField(id = "BNKNM", name = "금융기관명", length = 32)
    private String BNKNM;

    @MessageField(id = "BFSIL", name = "대환신규일", length = 8)
    private String BFSIL;

    @MessageField(id = "BLSGI", name = "대환만기일", length = 8)
    private String BLSGI;

    @MessageField(id = "CHIAK", name = "차입금액", length = 13)
    private BigDecimal CHIAK;

    @MessageField(id = "SHAMT", name = "대출금액", length = 13)
    private BigDecimal SHAMT;

    @MessageField(id = "GIJAK", name = "기준싯가", length = 13)
    private BigDecimal GIJAK;

    @MessageField(id = "DSIJA", name = "공제대상이자합계액", length = 13)
    private BigDecimal DSIJA;

    @MessageField(id = "YGTOT", name = "년간원리합계", length = 13)
    private BigDecimal YGTOT;

    @MessageField(id = "DSAMT", name = "공제대상액", length = 13)
    private BigDecimal DSAMT;

    @MessageField(id = "GRYY", name = "거래년도", length = 4)
    private String GRYY;

    @MessageField(id = "TYPE1", name = "상환유형ㅡ고정금리", length = 1)
    private String TYPE1;

    @MessageField(id = "TYPE2", name = "상환유형ㅡ비거치식", length = 1)
    private String TYPE2;

    @MessageField(id = "TYPE3", name = "상환유형ㅡ기타", length = 1)
    private String TYPE3;

    @MessageField(id = "DCHGB", name = "대출구분", length = 1)
    private String DCHGB;

    @MessageField(id = "FSHDG", name = "고정금리최초차입액", length = 13)
    private BigDecimal FSHDG;

    @MessageField(id = "FAMT", name = "고정금리방식차입액", length = 13)
    private BigDecimal FAMT;

    @MessageField(id = "FYEAR", name = "고정금리기간", length = 5)
    private String FYEAR;

    @MessageField(id = "NSHDG", name = "비거치식최초차입액", length = 13)
    private BigDecimal NSHDG;

    @MessageField(id = "NAMT", name = "비거치식차입액", length = 13)
    private BigDecimal NAMT;

    @MessageField(id = "NYEAR", name = "비거치식기간", length = 5)
    private String NYEAR;

    @MessageField(id = "JUSO2", name = "주택물건지", length = 102)
    private String JUSO2;

    @MessageField(id = "YGWON", name = "연간원금합계액", length = 13)
    private BigDecimal YGWON;

    @MessageField(id = "RCount", name = "출력명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "TYPE4", name = "1800만원ㅡ고정금리", length = 1)
    private String TYPE4;

    @MessageField(id = "TYPE5", name = "1800만원ㅡ비거치식", length = 1)
    private String TYPE5;

    @MessageField(id = "FSHDG4", name = "최초차입액", length = 13)
    private BigDecimal FSHDG4;

    @MessageField(id = "FAMT4", name = "고정금리방식차입액", length = 13)
    private BigDecimal FAMT4;

    @MessageField(id = "FYEAR4", name = "고정금리기간（년）", length = 5)
    private String FYEAR4;

    @MessageField(id = "NSHDG4", name = "최초차입액", length = 13)
    private BigDecimal NSHDG4;

    @MessageField(id = "NAMT4", name = "비거치식ㅡ차입액", length = 13)
    private BigDecimal NAMT4;

    @MessageField(id = "NYEAR4", name = "비거치식기간（년）", length = 5)
    private String NYEAR4;

    @MessageField(id = "YGWON4", name = "연간원금합계액", length = 13)
    private BigDecimal YGWON4;

    @MessageField(id = "TYPE6", name = "300만원 고정금리", length = 1)
    private String TYPE6;

    @MessageField(id = "TYPE7", name = "300만원 비거치식", length = 1)
    private String TYPE7;

    @MessageField(id = "FSHDG6", name = "최초차입액", length = 13)
    private BigDecimal FSHDG6;

    @MessageField(id = "FAMT6", name = "고정금리방식차입액", length = 13)
    private BigDecimal FAMT6;

    @MessageField(id = "FYEAR6", name = "고정금리기간（년）", length = 5)
    private String FYEAR6;

    @MessageField(id = "NSHDG6", name = "최초차입액", length = 13)
    private BigDecimal NSHDG6;

    @MessageField(id = "NAMT6", name = "비거치식ㅡ차입액", length = 13)
    private BigDecimal NAMT6;

    @MessageField(id = "NYEAR6", name = "비거치식기간（년）", length = 5)
    private String NYEAR6;

    @MessageField(id = "YGWON6", name = "연간원금합계액", length = 13)
    private BigDecimal YGWON6;

    @MessageField(id = "YBYEAR", name = "대환대출기간（년）", length = 5)
    private String YBYEAR;

    @MessageField(id = "TR743Rec1", name = "접수번호별")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H74300Res/RCount")
    private List<TR743Rec1> TR743Rec1;

    @Getter
    @Setter
    // 접수번호별 - 반복부
    public static class TR743Rec1 implements IMessageObject {
        @MessageField(id = "GRIL", name = "상환일자", length = 8)
        private String GRIL;

        @MessageField(id = "IJAMT", name = "이자", length = 13)
        private BigDecimal IJAMT;
    }

    @MessageField(id = "YOGJEHD1", name = "공제한도1", length = 13)
    private BigDecimal YOGJEHD1;

    @MessageField(id = "YOGJEHD2", name = "공제한도2", length = 13)
    private BigDecimal YOGJEHD2;

    @MessageField(id = "YOGJEHD3", name = "공제한도3", length = 13)
    private BigDecimal YOGJEHD3;

    @MessageField(id = "YOGJEHD4", name = "공제한도4", length = 13)
    private BigDecimal YOGJEHD4;
}