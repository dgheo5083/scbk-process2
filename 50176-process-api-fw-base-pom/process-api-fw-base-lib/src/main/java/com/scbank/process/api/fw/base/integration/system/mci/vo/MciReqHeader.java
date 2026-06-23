package com.scbank.process.api.fw.base.integration.system.mci.vo;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.SegmentField;
import com.scbank.process.api.fw.message.enums.DelimiterPosition;
import com.scbank.process.api.fw.message.enums.DelimiterType;

import lombok.Builder;
import lombok.Data;

/**
 * @author sungdon.choi
 */
@Data
@Builder
@IntegrationMessage(id = "mciReqHeader")
public class MciReqHeader implements IMessageObject {

    private static final long serialVersionUID = 1L;
    
    @MessageField(id = "mciTotalLength", name = "MCI_TOTAL_LENGTH", length = 8)
    private int mciTotalLength;

    @MessageField(id = "mciSystemHeader")
    private MciSystemHeader mciSystemHeader;

    @MessageField(id = "mciTranCommHeader")
    @SegmentField(type = DelimiterType.DELIMITER, position = DelimiterPosition.SUFFIX, delimiter = { 0x1E })
    private MciTranCommHeader mciTranCommHeader;

    @MessageField(id = "mciContTran")
    @SegmentField(type = DelimiterType.DELIMITER, position = DelimiterPosition.SUFFIX, delimiter = { 0x1E })
    private MciContTran mciContTran;
}