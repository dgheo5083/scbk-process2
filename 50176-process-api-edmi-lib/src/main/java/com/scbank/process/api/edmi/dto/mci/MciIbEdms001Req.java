package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@IntegrationMessage(id = "MciIbEdms001Req", type = Type.REQUEST, captureSystem = "MCI", description = "EDMS 계약서류조회")
public class MciIbEdms001Req implements IMessageObject {

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