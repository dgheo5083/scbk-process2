package com.scbank.process.api.svc.common.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeParameter {

    // 언어코드
    private String loctnCd;

    // 카테고리 코드
    private String ctgryCd;

    // 검색어
    private String searchWord;

}
