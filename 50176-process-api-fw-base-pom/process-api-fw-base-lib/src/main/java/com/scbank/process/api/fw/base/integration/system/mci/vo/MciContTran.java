package com.scbank.process.api.fw.base.integration.system.mci.vo;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.VariableLengthField;
import com.scbank.process.api.fw.message.enums.VariableFieldType;

import lombok.Data;

/**
 * MCI 데이터부
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 18.
 */
@Data
public class MciContTran implements IMessageObject {

    private static final long serialVersionUID = 1L;

    @MessageField(id = "contDataCd", defaultValue = StringUtils.EMPTY)
    @VariableLengthField(lengthType = VariableFieldType.DELIMITER, delimiter = 0x1F)
    private String contDataCd;

    @MessageField(id = "contDataLen", defaultValue = StringUtils.EMPTY)
    @VariableLengthField(lengthType = VariableFieldType.DELIMITER, delimiter = 0x1F)
    private String contDataLen;

    @MessageField(id = "contData", defaultValue = StringUtils.EMPTY)
    @VariableLengthField(lengthType = VariableFieldType.DELIMITER, delimiter = 0x1F)
    private String contData;

}
