package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H29600Req", type = Type.REQUEST, description = "출금계좌등록및삭제")
public class CbIbk01H29600Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "GLGubun", name = "거래구분 (J read D등록 H삭제)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GLGubun;

    @MessageField(id = "AcctNum", name = "등록계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "AcctNumPassword", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNumPassword;

    @MessageField(id = "ReqDetailCnt", name = "요구명세건수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer ReqDetailCnt;

    @MessageField(id = "YSAcctNum", name = "연속출금계좌번호", length = 14, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YSAcctNum;
}
