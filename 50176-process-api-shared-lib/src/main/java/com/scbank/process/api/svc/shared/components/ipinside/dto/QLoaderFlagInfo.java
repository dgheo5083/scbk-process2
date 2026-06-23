package com.scbank.process.api.svc.shared.components.ipinside.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "QLoaderFlagInfo", type = Type.RESPONSE, description = "QLoader Flag 정보")
public class QLoaderFlagInfo {

    String abroadYn;

    String blackListYn;

    String bciCountryCode;

    int flagValue;
}