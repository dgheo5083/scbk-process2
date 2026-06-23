package com.scbank.process.api.fw.base.integration.system.edmi.exception;

import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiResHeader;
import com.scbank.process.api.fw.integration.exception.IntegrationSystemException;

import lombok.Getter;
import lombok.Setter;

/**
 * 호스트 외 거래 시스템 예외객체
 */
public class EdmiSystemException extends IntegrationSystemException{

	private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private EdmiResHeader header;
    
    @Getter
    @Setter
    private String captureSystem;

    public EdmiSystemException(EdmiResHeader header, String errorCode, String errorMessage) {
        super(IntegrationConstant.SYSTEM_ID_EDMI, errorCode);
        this.header = header;
        this.setErrorMessage(errorMessage);
        
    }
}
