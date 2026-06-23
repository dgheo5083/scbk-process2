package com.scbank.process.api.edmi.dto.mci;

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
@IntegrationMessage(id = "MciIbEdms001Res", type = Type.RESPONSE, captureSystem = "MCI", description = "EDMS 계약서류조회")
public class MciIbEdms001Res implements IMessageObject {

    @MessageField(id = "DUMMY_TEMP1", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DUMMY_TEMP1;

    @MessageField(id = "RESCODE", name = "정상코드", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RESCODE;

    @MessageField(id = "BODY_LENGTH", name = "BODY부LENGTH", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int BODY_LENGTH;

    @MessageField(id = "AOMACRONM", name = "마크로명으로 보임", length = 21, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOMACRONM;

    @MessageField(id = "ROWCOUNT", name = "조립건수", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int ROWCOUNT;

    @MessageField(id = "DOCULIST", name = "계약서류리스트")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbEdms001Res/ROWCOUNT")
    private List<DOCU> DOCULIST;

    @Data
    public static class DOCU implements IMessageObject {
        @MessageField(id = "ELEMENT_ID", name = "엘리먼트 ID", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String ELEMENT_ID;

        @MessageField(id = "TRAN_DT", name = "계좌신규일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TRAN_DT;

        @MessageField(id = "LOAN_REQ_NO", name = "여신신청번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String LOAN_REQ_NO;

        @MessageField(id = "ACCT_NO", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String ACCT_NO;

        @MessageField(id = "CUST_SSN", name = "주민사업자번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CUST_SSN;

        @MessageField(id = "DOC_CODE", name = "문서코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DOC_CODE;

        @MessageField(id = "DOC_NAME", name = "문서명", length = 100, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DOC_NAME;

        @MessageField(id = "BRCH_NO", name = "계약문서 생성 영업점코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BRCH_NO;

        @MessageField(id = "EMP_NO", name = "계약문서 생성 행번", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String EMP_NO;

        @MessageField(id = "CHNNL_MK", name = "최종 채널구분", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CHNNL_MK;

        @MessageField(id = "JOB_MK", name = "업무구분", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String JOB_MK;

        @MessageField(id = "CREATE_DOC_DT", name = "계약문서 생성일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CREATE_DOC_DT;

    }

    @MessageField(id = "Dummy", name = "더미", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;
}