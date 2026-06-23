package com.scbank.process.api.fw.channel.message;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

/**
 * <pre>
 * packageName    : co.kr.framework.channel.message
 * fileName       : ValidationErrors
 * author         : gasigol
 * date           : 25. 4. 11.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 4. 11.        gasigol       최초 생성
 * </pre>
 */
@Data
@IntegrationMessage(id = "ValidationErrorMessage", type = Type.RESPONSE, description = "유효성 검증 오류 응답 메시지")
public class ValidationErrorMessage implements IMessageObject {

    private static final long serialVersionUID = 1L;

    @MessageField(id = "errorCnt", name = "유효성 검증 오류 목록 개수", length = 2)
    private int errorCnt;

    @MessageField(id = "errors", name = "유효성 검증 오류 목록")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "ValidationErrorMessage/errorCnt")
    private List<ValidationError> errors;

    @Data
    public static class ValidationError implements IMessageObject {

        private static final long serialVersionUID = 1L;

        @MessageField(id = "field", name = "필드명")
        private String field;

        @MessageField(id = "message", name = "오류 메시지")
        private String message;
    }
}
