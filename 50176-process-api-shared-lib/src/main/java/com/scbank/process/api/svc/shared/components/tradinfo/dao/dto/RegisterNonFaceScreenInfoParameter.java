package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 비대면 화면정보관리 등록 Parameter
 */
@Data
public class RegisterNonFaceScreenInfoParameter {
    private String custNo;
    private String tradNo;
    private String bizType;
    private String scrnDataInfo;
}