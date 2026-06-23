package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 마케팅 혜택 안내 팝업
 */
@Data
@IntegrationMessage(id = "SupPntSetMarketingNoticePopResponse", type = Type.RESPONSE)
public class SupPntSetMarketingNoticePopResponse implements IMessageObject {

    @MessageField(id = "termsInfo", name = "")
    @RepeatedField
    private List<TermsInfo> termsInfo;

    @MessageField(id = "viewDepth", name = "")
    private String viewDepth;

    @MessageField(id = "benefitAgreeFlag", name = "")
    private String benefitAgreeFlag;

    @MessageField(id = "serno", name = "고객일련번호")
    private String serno;

    @MessageField(id = "benefitFlag", name = "")
    private String benefitFlag;

    @MessageField(id = "financeFlag", name = "")
    private String financeFlag;

    @MessageField(id = "financeVal", name = "")
    private String financeVal;

    @MessageField(id = "wmloungeFlag", name = "")
    private String wmloungeFlag;

    @MessageField(id = "iotranListFlag", name = "")
    private String iotranListFlag;

    @MessageField(id = "notyExrateFlag", name = "")
    private String notyExrateFlag;

    @MessageField(id = "agrmntMk", name = "")
    private String agrmntMk;

    @MessageField(id = "agreeFlag", name = "")
    private String agreeFlag;

    @Data
    public static class TermsInfo implements IMessageObject {

        @MessageField(id = "attFileNm", name = "")
        private String attFileNm;

        @MessageField(id = "attFileUrl", name = "")
        private String attFileUrl;

        @MessageField(id = "loctnCd", name = "")
        private String loctnCd;

        @MessageField(id = "oldPrvsnCd", name = "")
        private String oldPrvsnCd;

        @MessageField(id = "prdctId", name = "")
        private String prdctId;

        @MessageField(id = "prdctmkCd", name = "")
        private String prdctmkCd;

        @MessageField(id = "prvsnBtnNm", name = "")
        private String prvsnBtnNm;

        @MessageField(id = "prvsnCd", name = "")
        private String prvsnCd;

        @MessageField(id = "prvsnLongExpln", name = "")
        private String prvsnLongExpln;

        @MessageField(id = "prvsnMk", name = "")
        private String prvsnMk;

        @MessageField(id = "prvsnNm", name = "")
        private String prvsnNm;

        @MessageField(id = "prvsnShortExpln", name = "")
        private String prvsnShortExpln;

        @MessageField(id = "registDt", name = "")
        private String registDt;

        @MessageField(id = "registId", name = "")
        private String registId;

        @MessageField(id = "sortOrd", name = "")
        private String sortOrd;

        @MessageField(id = "updDt", name = "")
        private String updDt;

        @MessageField(id = "updId", name = "")
        private String updId;

        @MessageField(id = "useYn", name = "")
        private String useYn;

    }

}
