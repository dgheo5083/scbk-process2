package com.scbank.process.api.fw.common.errorcode;

import java.util.List;

import com.scbank.process.api.fw.core.error.IErrorMessage;

import lombok.Builder;
import lombok.Data;

/**
 * <pre>
 * packageName    : co.kr.framework.common.errorcode
 * fileName       : DefaultErrorMessage
 * author         : gasigol
 * date           : 25. 4. 9.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 4. 9.        gasigol       최초 생성
 * </pre>
 */
@Data
@Builder
public class DefaultErrorMessage implements IErrorMessage {

    private static final long serialVersionUID = 1L;

    private String errorCode;

    private String errorMessage;

    private List<String> errorGuideMessages;
}
