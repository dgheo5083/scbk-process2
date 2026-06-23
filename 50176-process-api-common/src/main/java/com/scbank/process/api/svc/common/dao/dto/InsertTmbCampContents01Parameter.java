package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@IntegrationMessage(id = "InsertTmbCampContents01Parameter", type = Type.REQUEST, description = "PUSH Message 등록")
public class InsertTmbCampContents01Parameter {
    private String campSeq;
    private String title;
    private String cnts;
    private String webUrl;
    private String imgUrl;
    private String stsUrl;
    private String popupYn;
    private String stsYn;
    private String contentsMsg;
}
