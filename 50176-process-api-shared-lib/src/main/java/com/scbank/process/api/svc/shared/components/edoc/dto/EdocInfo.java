package com.scbank.process.api.svc.shared.components.edoc.dto;

import lombok.Data;

@Data
public class EdocInfo {

    private String edocCode;

    private String edocHash;

    private String signedHash;

    private String edocFilePath;

    private String elementId;

    private String edocSignYn;

}
