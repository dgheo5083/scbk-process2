package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * UI 진행상태 업데이트 처리
 */
@Data
@IntegrationMessage(id = "OngoingProcessCancelResponse", type = Type.RESPONSE)
public class OngoingProcessCancelResponse implements IMessageObject {
	@MessageField(id = "successYn", name = "")
    private String successYn;

    @MessageField(id = "errType", name = "")
    private String errType;

    @MessageField(id = "foreserve5", name = "API HEAD OUT", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String foreserve5;

    @MessageField(id = "foerrcod", name = "에러코드", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String foerrcod;

    @MessageField(id = "foerrname", name = "ERROR한글명30", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String foerrname;

    @MessageField(id = "fototcnt", name = "전체건수", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String fototcnt;

    @MessageField(id = "fonxttbl", name = "연속거래", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String fonxttbl;

    @MessageField(id = "fogunsu", name = "건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String fogunsu;

    @MessageField(id = "fojumin", name = "주민등록번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String fojumin;

    @MessageField(id = "fochjna", name = "차주명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String fochjna;

    @MessageField(id = "fodcscak", name = "대출신청금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String fodcscak;

    @MessageField(id = "foceutelno", name = "전화번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String foceutelno;

    @MessageField(id = "fojningb", name = "진행정보", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String fojningb;

    @MessageField(id = "foccnchshb", name = "취소자행번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String foccnchshb;

    @MessageField(id = "foccnchsnm", name = "취소자성명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String foccnchsnm;

    @MessageField(id = "fojubno", name = "접수번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String fojubno;

}
