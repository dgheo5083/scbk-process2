package com.scbank.process.api.fw.base.integration.system.mci.vo;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.SegmentField;
import com.scbank.process.api.fw.message.annotation.VariableLengthField;
import com.scbank.process.api.fw.message.enums.DelimiterPosition;
import com.scbank.process.api.fw.message.enums.DelimiterType;
import com.scbank.process.api.fw.message.enums.VariableFieldType;

import lombok.Data;

/**
 * MCI 메시지 공통부
 * 
 * @author sungdon.choi
 * @version 1.0	
 * @since 25. 4. 18.
 */
@Data
public class MciMsgInfo implements IMessageObject {

    private static final long serialVersionUID = 1L;

    @MessageField(id = "msgInfo")
    @SegmentField(type = DelimiterType.DELIMITER, position = DelimiterPosition.SUFFIX, delimiter = { 0x1E })
    private MsgInfo msgInfo;

    @MessageField(id = "adtnMsgInfo")
    @SegmentField(type = DelimiterType.DELIMITER, position = DelimiterPosition.SUFFIX, delimiter = { 0x1E })
    private AdtnMsgInfo adtnMsgInfo;
    
    @MessageField(id = "newErrMsgInfo")
    @SegmentField(type = DelimiterType.DELIMITER, position = DelimiterPosition.SUFFIX, delimiter = { 0x1E })
    private NewErrMsgInfo newErrMsgInfo;
    
    @Data
    public static class MsgInfo implements IMessageObject {
    	
    	private static final long serialVersionUID = 1L;

		@MessageField(id = "msgCd")
        @VariableLengthField(lengthType = VariableFieldType.DELIMITER, delimiter = 0x1F)
        private String msgCd;

        @MessageField(id = "msgCtt", multiBytes = true)
        @VariableLengthField(lengthType = VariableFieldType.DELIMITER, delimiter = 0x1F)
        private String msgCtt;
    }
    
    @Data
    public static class AdtnMsgInfo implements IMessageObject {
    	
    	private static final long serialVersionUID = 1L;

		@MessageField(id = "adtnMsgCd")
        @VariableLengthField(lengthType = VariableFieldType.DELIMITER, delimiter = 0x1F)
        private String adtnMsgCd;

        @MessageField(id = "adtnMsg", multiBytes = true)
        @VariableLengthField(lengthType = VariableFieldType.DELIMITER, delimiter = 0x1F)
        private String adtnMsg;
    }
    
    @Data
    public static class NewErrMsgInfo implements IMessageObject {
    	
    	private static final long serialVersionUID = 1L;

		@MessageField(id = "newErrMsgCd")
        @VariableLengthField(lengthType = VariableFieldType.DELIMITER, delimiter = 0x1F)
        private String newErrMsgCd;

        @MessageField(id = "newErrMsgCtt", multiBytes = true)
        @VariableLengthField(lengthType = VariableFieldType.DELIMITER, delimiter = 0x1F)
        private String newErrMsgCtt;
    }
}
