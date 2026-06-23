package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class InsertTransferPushDataParameter {

    private String bsAcctNo;

    private String bsPuserno;

    private String bsAcctNm;

    private String bsNotiRg;

    private String bsAmount;

    private String bsTimeFg;

    private String bsStartTm;

    private String bsEndTm;

    private String bsBalanMk;

}
