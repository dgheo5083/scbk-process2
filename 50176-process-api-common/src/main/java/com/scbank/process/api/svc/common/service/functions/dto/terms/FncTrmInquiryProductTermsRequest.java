package com.scbank.process.api.svc.common.service.functions.dto.terms;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncTrmInquiryProductTermsRequest", type = Type.REQUEST)
public class FncTrmInquiryProductTermsRequest implements IMessageObject {

    @MessageField(id = "prdctPrvsnId", name = "상품약관 상품아이디", example = "5831,5852")
    private String prdctPrvsnId;

    @MessageField(id = "prdctPrvsnCd", name = "상품약관 상품코드", example = "")
    private String prdctPrvsnCd;

    @MessageField(id = "prdctPrvsnType", name = "상품약관 상품종류", example = "ETC_FIX") // DPST,LOAN,TRUST,CARD,INSRNC,ETC_FIX
    private String prdctPrvsnType;

    @MessageField(id = "prdctViewType", name = "상품약관 약관종류", example = "1") // 1: 약관, 2: 카테고리약관(펀드역외 , 체크카드) , 3~15: 상품설명서
    private String prdctViewType;

    @MessageField(id = "scrapFlag", name = "상품약관 구분값", example = "Y") // 공공데이터 PMS2.0 구분값
    private String scrapFlag;

}
