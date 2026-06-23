package com.scbank.process.api.svc.common.service.mydata.dto;

import java.math.BigDecimal;
import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

@Data
@IntegrationMessage(id = "ListJoinOrganInfoResponse", type = Type.RESPONSE, description = "마이데이터 > 현대카드 이용내역조회 API")
public class ListJoinOrganInfoResponse implements IMessageObject {

    @MessageField(id = "nextKey", name = "다음 페이지 KEY")
    private String nextKey;

    @MessageField(id = "limitCnt", name = "페이지건수")
    private String limitCnt;

    @MessageField(id = "orgCd", name = "기관코드")
    private String orgCd;

    @MessageField(id = "orgName", name = "기관명")
    private String orgName;

    @MessageField(id = "orgImg", name = "기관이미지")
    private String orgImg;

    @MessageField(id = "blingYn", name = "수신 구분")
    private String blingYn;

    @MessageField(id = "reqNo", name = "")
    private String reqNo;

    @MessageField(id = "cardNo", name = "카드번호")
    private String cardNo;

    @MessageField(id = "cardPpId", name = "선불카드식별자")
    private String cardPpId;

    @MessageField(id = "consentMrchntNmYn", name = "가맹점명여부")
    private String consentMrchntNmYn;

    @MessageField(id = "hdFlag", name = "현대카드 플래그")
    private String hdFlag;

    @MessageField(id = "eCode", name = "에러 코드")
    private String eCode;

    @MessageField(id = "errMsg", name = "에러 메시지")
    private String errMsg;

    @MessageField(id = "cardId", name = "카드 id")
    private String cardId;

    @MessageField(id = "tranList", name = "거래내역목록")
    @RepeatedField
    private List<TranListDto> tranList;

    @Data
    public static class TranListDto implements IMessageObject {

        @MessageField(id = "apprvlTimestamp", name = "승인일자")
        private String apprvlTimestamp;

        @MessageField(id = "apprvlDt", name = "승인시간")
        private String apprvlDt;

        @MessageField(id = "tranTimestamp", name = "거래시간")
        private String tranTimestamp;

        @MessageField(id = "tranDt", name = "거래일자")
        private String tranDt;

        @MessageField(id = "mrchntNm", name = "가맹점명")
        private String mrchntNm;

        @MessageField(id = "mrchntRegno", name = "mrchnt_regno")
        private String mrchntRegno;

        @MessageField(id = "approvedAmt", name = "승인금액")
        private BigDecimal approvedAmt;

        @MessageField(id = "modifiedAmt", name = "modified_amt")
        private BigDecimal modifiedAmt;

        @MessageField(id = "totalInstallCnt", name = "total_install_cnt")
        private String totalInstallCnt;

        @MessageField(id = "stlmtStsCd", name = "상태코드")
        private String stlmtStsCd;

        @MessageField(id = "stlmtStsNm", name = "상태명")
        private String stlmtStsNm;

        @MessageField(id = "stlmtNtnlCd", name = "stlmt_ntnl_cd")
        private String stlmtNtnlCd;

        @MessageField(id = "stlmtCrncyCd", name = "stlmt_crncy_cd")
        private String stlmtCrncyCd;

        @MessageField(id = "buyDt", name = "buy_dt")
        private String buyDt;

        @MessageField(id = "saleDt", name = "sale_dt")
        private String saleDt;

        @MessageField(id = "priApprvlTimestamp", name = "pri_apprvl_timestamp")
        private String priApprvlTimestamp;

        @MessageField(id = "priApprivlAmt", name = "pri_apprivl_amt")
        private String priApprivlAmt;

        @MessageField(id = "priKrwAmt", name = "pri_krw_amt")
        private String priKrwAmt;

        @MessageField(id = "priCrncyCd", name = "pri_crncy_cd")
        private String priCrncyCd;

        @MessageField(id = "wonAmt", name = "won_amt")
        private String wonAmt;

        @MessageField(id = "tranTypeCd", name = "tran_type_cd")
        private String tranTypeCd;

        @MessageField(id = "tranTypeNm", name = "tran_type_nm")
        private String tranTypeNm;

        @MessageField(id = "tranAmt", name = "tran_amt")
        private BigDecimal tranAmt;

        @MessageField(id = "balanceAmt", name = "balance_amt")
        private BigDecimal balanceAmt;

        @MessageField(id = "tranOrgCd", name = "tran_org_cd")
        private String tranOrgCd;

        @MessageField(id = "tranId", name = "tran_id")
        private String tranId;

        @MessageField(id = "approvedNum", name = "approved_num")
        private String approvedNum;

        @MessageField(id = "nextKey", name = "next_key")
        private String nextKey;

        @MessageField(id = "totalCnt", name = "total_cnt")
        private String totalCnt;

    }

}
