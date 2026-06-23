package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class ListNoticeRecordParameter {

    private String nowdate;
    private String language;
    private String ctgry_cd;
    private String paging;
    private String pageSize;

}
