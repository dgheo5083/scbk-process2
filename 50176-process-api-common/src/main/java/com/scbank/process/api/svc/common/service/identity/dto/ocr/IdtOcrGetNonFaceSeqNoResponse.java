package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import java.util.ArrayList;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrGetNonFaceSeqNoResponse", description = "비대면신청 일련번호 조회 응답 DTO", type = Type.RESPONSE)
public class IdtOcrGetNonFaceSeqNoResponse implements IMessageObject {
	
	@MessageField(id = "seqNo", name = "일련번호")
    private String seqNo;
	
	@MessageField(id = "seqNoList", name = "시퀀스리스트")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
    private ArrayList<String> seqNoList;
	
	@MessageField(id = "tradNoList", name = "거래번호리스트")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
    private ArrayList<String> tradNoList;
	
	@MessageField(id = "count", name = "횟수")
    private int count;
	
	@MessageField(id = "shootingType", name = "재촬영구분")
    private String shootingType;
}
