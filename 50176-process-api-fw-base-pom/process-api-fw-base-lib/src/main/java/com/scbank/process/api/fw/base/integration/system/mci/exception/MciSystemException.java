package com.scbank.process.api.fw.base.integration.system.mci.exception;

import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciResHeader;
import com.scbank.process.api.fw.integration.exception.IntegrationSystemException;

import lombok.Getter;
import lombok.Setter;

/**
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 21.
 */
public class MciSystemException extends IntegrationSystemException {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private MciResHeader header;

    public MciSystemException(MciResHeader header, String errorCode, String errorMessage) {
        super(IntegrationConstant.SYSTEM_ID_MCI, errorCode);
        this.header = header;
        this.setErrorMessage(errorMessage);
    }
}
