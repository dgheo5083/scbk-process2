package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class NoticeForPopupResult {
    private String id;
    private String title;
    private String subTitle;
    private String targetVersion;
    private String targetUrl;
    private String targetMenuId;
    private String targetParams;
    private String productGb;
}
