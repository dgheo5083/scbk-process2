package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H92F00Req", type = Type.REQUEST, captureSystem = "OLTP", description = "외화고객정보 전문 조회")
public class CbIbk01H92F00Req implements IMessageObject {
	@MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "로그인비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;
    
    @MessageField(id = "YIGRGB", name = "거래구분", length = 1)
    private String YIGRGB;
    
    @MessageField(id = "YIBRNO", name = "고객점번호", length = 3)
    private String YIBRNO;
    
    @MessageField(id = "YIJMNO", name = "주민등록번호", length = 13)
    private String YIJMNO;
    
    @MessageField(id = "YIEJUSO1", name = "영문주소1", length = 45)
    private String YIEJUSO1;
    
    @MessageField(id = "YIEJUSO2", name = "영문주소2", length = 45)
    private String YIEJUSO2;
    
    @MessageField(id = "YIEBUNHO", name = "영문전화번호", length = 22)
    private String YIEBUNHO;
    
    @MessageField(id = "YIENAME", name = "영문성명", length = 50)
    private String YIENAME;
    
    @MessageField(id = "YIKNAME", name = "한글성명", length = 30, masking = true, maskingType = "04")
    private String YIKNAME;
    
    @MessageField(id = "YIIDUMMY", name = "YIIDUMMY", length = 43)
    private String YIIDUMMY;
}
