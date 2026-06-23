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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H87901Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "증명서>인터넷증명서조회-예금잔액증명서목록")
public class CbIbk01H87901Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;
    @MessageField(id = "YONEXTYN", name = "연속거래", length = 1)
    private String YONEXTYN;

    @MessageField(id = "YOLISTSU", name = "조립건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOLISTSU;

    @MessageField(id = "YOBGLIST", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H87901Res/YOLISTSU")
    private List<YOBGLIST> YOBGLIST;

    // 반복부
    @Getter
    @Setter
    public static class YOBGLIST implements IMessageObject {
        @MessageField(id = "YOLANGGB", name = "언어구분", length = 1)
        private String YOLANGGB;

        @MessageField(id = "YOBGIL", name = "발급일", length = 8)
        private String YOBGIL;

        @MessageField(id = "YOJJGJIL", name = "잔액증명기준일", length = 8)
        private String YOJJGJIL;

        @MessageField(id = "YOBGSEQ", name = "잔액증명발급번호", length = 17)
        private String YOBGSEQ;

        @MessageField(id = "YOFEE", name = "수수료금액", length = 15)
        private Integer YOFEE;

        @MessageField(id = "YOFEEMJYB", name = "면제여부(Y/N)", length = 1)
        private String YOFEEMJYB;

        @MessageField(id = "YOFEEPTYB", name = "포인트결제여부(Y/N)", length = 1)
        private String YOFEEPTYB;

        @MessageField(id = "YOFEESGJNO", name = "수수료출금계좌", length = 11, masking = true, maskingType = "02")
        private String YOFEESGJNO;
    }

}