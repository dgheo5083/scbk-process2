package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "SelectMydataOrgMgtResult", type = Type.RESPONSE, description = "MYDATA 업권 기관코드 리스트 응답")
public class SelectMydataOrgMgtResult {

    @MessageField(id = "orgCd", name = "기관코드")
    private String orgCd;

    @MessageField(id = "orgMkCd", name = "기관코드")
    private String orgMkCd;

    @MessageField(id = "orgName", name = "기관명")
    private String orgName;

    @MessageField(id = "orgImg", name = "기관이미지")
    private String orgImg;

    @MessageField(id = "orgImgName", name = "기관이미지명")
    private String orgImgName;

    @MessageField(id = "orgOrder", name = "")
    private String orgOrder;

    @MessageField(id = "openBnkFlag", name = "")
    private String openBnkFlag;

    @MessageField(id = "openBnkCd", name = "")
    private String openBnkCd;

    @MessageField(id = "mobileChkFlag", name = "")
    private String mobileChkFlag;

}
