package com.scbank.process.api.edmi.dto.mci;

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

import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbAms003Res", type = Type.RESPONSE, captureSystem = "MCI", description = "MTS_조회거래")
public class MciIbAms003Res implements IMessageObject {
    @MessageField(id = "WSF", name = "WSF", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WSF;

    @MessageField(id = "LL", name = "LL", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String LL;

    @MessageField(id = "SFID", name = "SFID", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SFID;

    @MessageField(id = "SFTP", name = "SFTP", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SFTP;

    @MessageField(id = "ERRCOD", name = "ERRCOD", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ERRCOD;

    @MessageField(id = "ERRNAME", name = "ERRNAME", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ERRNAME;

    @MessageField(id = "SOFOTOTCNT", name = "출력명세건수(Record건수)", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer SOFOTOTCNT;

    @MessageField(id = "SOJH_REC", name = "조회내역정보")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbAms003Res/SOFOTOTCNT")
    private List<SOJH_REC> SOJH_REC;

    @Data
    public static class SOJH_REC implements IMessageObject {
        @MessageField(id = "SEND_SEG", name = "구분(발송구분)", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String SEND_SEG;

        @MessageField(id = "REGIST_DT", name = "게시일자(등록일자)", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String REGIST_DT;

        @MessageField(id = "ACCT_NO", name = "계좌번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String ACCT_NO;

        @MessageField(id = "SERNO", name = "카드일련번호", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer SERNO;

        @MessageField(id = "DLNQNCY_OCCUR_DT", name = "연체발생일", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DLNQNCY_OCCUR_DT;

        @MessageField(id = "BS_ASST_AMT", name = "채권원금금액(채권잔액)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal BS_ASST_AMT;

        @MessageField(id = "FINAL_EFFECTIVE_DT", name = "효력발생일(최종효력발생일)", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FINAL_EFFECTIVE_DT;

        @MessageField(id = "DSPL_DT", name = "처분예정일(채권양도 예정일)", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DSPL_DT;

        @MessageField(id = "BUYER", name = "양수예정인", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BUYER;

        @MessageField(id = "ADDR", name = "경매물건주소지", length = 160, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String ADDR;

        @MessageField(id = "LAPSE_PRCRPTN", name = "소멸시효완성여부", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String LAPSE_PRCRPTN;
    }
}
