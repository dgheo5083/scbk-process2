package com.scbank.process.api.svc.common.service.functions.dto.terms;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductTermsInfo implements IMessageObject {

    @MessageField(id = "prdctPrvsnId", name = "상품약관 아이디")
    private String prdctPrvsnId;

    @MessageField(id = "prdctPrvsnCd", name = "상품약관 종류")
    private String prdctPrvsnCd;

    @MessageField(id = "prdctPrvsnNm", name = "상품약관 제목")
    private String prdctPrvsnNm;

    @MessageField(id = "attFileNm", name = "상품약관 PDF파일명")
    private String attFileNm;

}
