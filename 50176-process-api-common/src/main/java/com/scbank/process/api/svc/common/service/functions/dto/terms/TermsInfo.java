package com.scbank.process.api.svc.common.service.functions.dto.terms;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class TermsInfo implements IMessageObject {

    @MessageField(id = "prvsnCd", name = "약관 코드")
    private String prvsnCd;

    @MessageField(id = "prvsnNm", name = "약관 제목")
    private String prvsnNm;

    // @MessageField(id = "prvsnShortExpln", name = "")
    // private String prvsnShortExpln;

    @MessageField(id = "prvsnLongExpln", name = "약관 내용(HTML Contents)")
    private String prvsnLongExpln;

    // @MessageField(id = "prvsnMk", name = "약관 분류") // 동의서, 상품설명서 등등 (ATF001 ....)
    // private String prvsnMk;

    @MessageField(id = "attFileNm", name = "약관PDF파일명")
    private String attFileNm;

    @MessageField(id = "attFileUrl", name = "약관PDF파일경로")
    private String attFileUrl;

    // @MessageField(id = "loctnCd", name = "언어 코드") // LN1001 : 한국어 / LN1002 : 영어
    // private String loctnCd;

    // @MessageField(id = "prdctId", name = "상품코드")
    // private String prdctId;

    // @MessageField(id = "prdctmkCd", name = "상품분류코드") // 예금, 대출 등등 (BP1001 ....)
    // private String prdctmkCd;

    // @MessageField(id = "registDt", name = "등록일")
    // private String registDt;

    // @MessageField(id = "registId", name = "")
    // private String registId;

    // @MessageField(id = "updDt", name = "수정일")
    // private String updDt;

    // @MessageField(id = "updId", name = "")
    // private String updId;

    // @MessageField(id = "sortOrd", name = "")
    // private String sortOrd;

    // @MessageField(id = "oldPrvsnCd", name = "")
    // private String oldPrvsnCd;

    // @MessageField(id = "prvsnBtnNm", name = "약관 버튼명")
    // private String prvsnBtnNm;

}
