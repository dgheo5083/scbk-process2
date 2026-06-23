package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciYp0049010001Res", type = Type.RESPONSE, description = "대출신청조회 및 취소")
public class MciYp0049010001Res implements IMessageObject {
    @MessageField(id = "FORESERVE5", name = "API HEAD OUT", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FORESERVE5;

    @MessageField(id = "FOERRCOD", name = "에러코드", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FOERRCOD;

    @MessageField(id = "FOERRNAME", name = "ERROR한글명30", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FOERRNAME;

    @MessageField(id = "FOTOTCNT", name = "전체건수", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FOTOTCNT;

    @MessageField(id = "FONXTTBL", name = "연속거래", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FONXTTBL;

    @MessageField(id = "FOGUNSU", name = "건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FOGUNSU;

    @MessageField(id = "FOJUMIN", name = "주민등록번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FOJUMIN;

    @MessageField(id = "FOCHJNA", name = "차주명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FOCHJNA;

    @MessageField(id = "FODCSCAK", name = "대출신청금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FODCSCAK;

    @MessageField(id = "FOCEUTELNO", name = "전화번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FOCEUTELNO;

    @MessageField(id = "FOJNINGB", name = "진행정보", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FOJNINGB;

    @MessageField(id = "FOCCNCHSHB", name = "취소자행번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FOCCNCHSHB;

    @MessageField(id = "FOCCNCHSNM", name = "취소자성명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FOCCNCHSNM;
}
