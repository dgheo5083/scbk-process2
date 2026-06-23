package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciGp0000020001Res", type = Type.RESPONSE, captureSystem = "MCI", description = "영문주소조회")
public class MciGp0000020001Res implements IMessageObject {
	
    @MessageField(id = "TOZIPCD3", name = "새우편번호", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TOZIPCD3;
    
    @MessageField(id = "TONEWJSO18", name = "한글신주소1", length = 80, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TONEWJSO18;
    
    @MessageField(id = "TOENGJSOLG", name = "영문 신주소1 길이", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TOENGJSOLG;
    
    @MessageField(id = "TOENGJSONA", name = "영문 신주소1", length = 100, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TOENGJSONA;
    
    @MessageField(id = "TOJIHAYB", name = "건물본번", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TOJIHAYB;
    
    @MessageField(id = "TONEWJSOBON", name = "새우편번호", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TONEWJSOBON;
    
    @MessageField(id = "TONEWJSOBUN", name = "건물부번", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TONEWJSOBUN;
    
    @MessageField(id = "TOENGROADNA", name = "영문 도로명", length = 80, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TOENGROADNA;
    
    @MessageField(id = "TOENGJSONA1", name = "영문 읍/면 명", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TOENGJSONA1;
    
    @MessageField(id = "TOENGJSONA2", name = "영문 군/구 명", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TOENGJSONA2;
    
    @MessageField(id = "TOENGJSONA3", name = "영문 시/군/구 명", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TOENGJSONA3;
    
    @MessageField(id = "TOENGJSONA4", name = "영문 시/도 명", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TOENGJSONA4;
    
    @MessageField(id = "TOENGKKNA", name = "영문 국가명", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TOENGKKNA;

    
}
