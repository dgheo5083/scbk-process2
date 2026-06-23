package com.scbank.process.api.edmi.dto.oltp;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.core.utils.StringUtils;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbf01H65300Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "영업점별 FX고객번호")
public class CbIbf01H65300Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "KorCustNm", name = "한국고객명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KorCustNm;

    @MessageField(id = "EndCustNm", name = "영문고객명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String EndCustNm;

    @MessageField(id = "ContinueFlag", name = "연속여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ContinueFlag;

    @MessageField(id = "ContinueBranchNum", name = "연속점번호", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ContinueBranchNum;

    @MessageField(id = "ContinueCustNum", name = "연속고객번호", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ContinueCustNum;

    @MessageField(id = "YOGRJSGGB", name = "근로자송금서비스구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGRJSGGB;

    @MessageField(id = "YORJSGBK", name = "근로자송금서비스변경", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YORJSGBK;

    @MessageField(id = "ReqCnt", name = "고객번호추출건수", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReqCnt;

    @MessageField(id = "ARRLIST", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbf01H65300Res/ReqCnt")
    private List<ARRLIST> ARRLIST;

    @Data
    public static class ARRLIST implements IMessageObject {

        @MessageField(id = "BranchNum", name = "점번호", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BranchNum;

        @MessageField(id = "CustNum", name = "고객번호", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CustNum;

        @MessageField(id = "KorBranchNm", name = "한글 영업점명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String KorBranchNm;

        @MessageField(id = "EngBranchNm", name = "영문 영업점명", length = 35, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String EngBranchNm;

        @MessageField(id = "FxBranchCode", name = "외환점코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FxBranchCode;

        @MessageField(id = "EDIFlag", name = "EDI 고객여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String EDIFlag;

        @MessageField(id = "WEBEDIID", name = "WEB EDI ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WEBEDIID;

        @MessageField(id = "FXFlag", name = "FX 고객여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FXFlag;

        @MessageField(id = "FXJoinDate", name = "FX 고객약정일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FXJoinDate;

        @MessageField(id = "updateD", name = "FX 고객정보수정일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String updateD;

        @MessageField(id = "FxAcct", name = "원화결제계좌", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FxAcct;

        @MessageField(id = "WonAcct", name = "외화결제계좌", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WonAcct;

        @MessageField(id = "YOECPAYK", name = "외화결제계좌", length = 17, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOECPAYK;

    }
}
