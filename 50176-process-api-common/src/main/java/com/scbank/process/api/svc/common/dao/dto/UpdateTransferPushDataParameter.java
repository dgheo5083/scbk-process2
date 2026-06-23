package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class UpdateTransferPushDataParameter {

    private String bsNotiRg;

    private String bsAmount;

    private String bsTimeFg;

    private String bsStartTm;

    private String bsEndTm;

    private String bsBalanMk;

    private String bsAcctNo;

    private String bsPuserno;

}
