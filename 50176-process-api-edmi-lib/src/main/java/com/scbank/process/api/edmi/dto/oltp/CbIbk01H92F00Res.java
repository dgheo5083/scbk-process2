package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H92F00Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "외화고객정보 전문 응답")
public class CbIbk01H92F00Res implements IMessageObject {
	@MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;
    
    @MessageField(id = "YOGRGB", name = "거래구분", length = 1)
    private String YOGRGB;
    
    @MessageField(id = "YOBRNO", name = "고객점번호", length = 3)
    private String YOBRNO;
    
    @MessageField(id = "YOJMNO", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
    private String YOJMNO;
    
    @MessageField(id = "YOEJUSO1", name = "영문주소1", length = 45)
    private String YOEJUSO1;
    
    @MessageField(id = "YOEJUSO2", name = "영문주소2", length = 45)
    private String YOEJUSO2;
    
    @MessageField(id = "YOEBUNHO", name = "영문전화번호", length = 22, masking = true, maskingType = "05")
    private String YOEBUNHO;
    
    @MessageField(id = "YOENAME", name = "영문성명", length = 50)
    private String YOENAME;
    
    @MessageField(id = "YOKNAME", name = "한글성명", length = 30, masking = true, maskingType = "04")
    private String YOKNAME;
    
    @MessageField(id = "YOROKYN", name = "정상여부:Y/N", length = 1)
    private String YOROKYN;
    
    @MessageField(id = "YOFXYN", name = "외화고객여부", length = 1)
    private String YOFXYN;
    
    @MessageField(id = "YOIDUMMY", name = "YOIDUMMY", length = 41)
    private String YOIDUMMY;
}
