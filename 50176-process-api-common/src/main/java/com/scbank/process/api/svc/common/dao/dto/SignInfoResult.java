package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class SignInfoResult {
    private String userId;
    private String userName;
    private String regDate;
    private String juminNo;
    private String sysIp;
    private String sysName;
    private String gubunCode;
    private String txCode;
    private String pageLang;
    private String regSite;
    private byte[] encSign;
    private String delFlag;
    private String regDay;
}