package com.scbank.process.api.fw.base.integration.system.oltp.exception;

import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpError;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;
import com.scbank.process.api.fw.integration.exception.IntegrationSystemException;

import lombok.Getter;
import lombok.Setter;

/**
 * 호스트 시스템 예외 객체
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 21.
 */
public class OltpSystemException extends IntegrationSystemException {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private OltpResHeader header;
    
    @Getter
    @Setter
    private OltpError error;

    public OltpSystemException(OltpResHeader header, String errorCode, String errorMessage) {
        super(IntegrationConstant.SYSTEM_ID_HOST, errorCode);
        this.header = header;
        this.setErrorMessage(errorMessage);
        
    }
}
