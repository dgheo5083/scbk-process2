package com.scbank.process.api.svc.shared.components.account;

import java.math.BigDecimal;

import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequest;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.rebound.OltpReboundStrategy;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpReqHeader;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;
import com.scbank.process.api.fw.core.utils.ReflectionUtils;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 계좌조회 리바운드용 Strategy
 */
@RequiredArgsConstructor
@Slf4j
public class AccountReboundStrategy extends OltpReboundStrategy {
    @Override
    public OltpRequest<? extends IMessageObject> handleData(OltpRequest<? extends IMessageObject> request,
            OltpResponse<? extends IMessageObject> response) {

        OltpResHeader hostHeader = response.getHeader();
        IMessageObject responseBody = response.getResponse();
        OltpCommon hostCommon = this.getHostCommon(hostHeader);

        OltpReqHeader requestHeader = request.getHeader();
        IMessageObject requestBody = request.getRequestMessage();
        OltpCommon hostReqCommon = requestHeader.getOltpCommon();

        String processTp = this.getProcessTp(hostCommon); // 처리구분

        String continueInfo = ReflectionUtils.getFieldValue(responseBody, "ContinueInfo");
        String continueType = ReflectionUtils.getFieldValue(responseBody, "ContinueType");
        String availBalSign = ReflectionUtils.getFieldValue(responseBody, "AvailBalSign");
        BigDecimal availBalance = ReflectionUtils.getFieldValue(responseBody, "AvailBalance");
        String continueYn = ReflectionUtils.getFieldValue(responseBody, "ContinueYn");
        String referOrder = ReflectionUtils.getFieldValue(responseBody, "ReferOrder");
        String continueAssort = ReflectionUtils.getFieldValue(responseBody, "ContinueAssort");
        String acctServiceCode = ReflectionUtils.getFieldValue(responseBody, "AcctServiceCode");
        String depPayPart = ReflectionUtils.getFieldValue(responseBody, "DepPayPart");
        String drawBankCode = ReflectionUtils.getFieldValue(responseBody, "DrawBankCode");
        String drawAcctNum = ReflectionUtils.getFieldValue(responseBody, "DrawAcctNum");
        String depBankCode = ReflectionUtils.getFieldValue(responseBody, "DepBankCode");
        String depAcctNum = ReflectionUtils.getFieldValue(responseBody, "DepAcctNum");
        Integer printCount = ReflectionUtils.getFieldValue(responseBody, "PrintCount");
        Integer referCount = ReflectionUtils.getFieldValue(responseBody, "ReferCount");
        Integer payCount = ReflectionUtils.getFieldValue(responseBody, "PayCount");
        String currentPage = ReflectionUtils.getFieldValue(responseBody, "CurrentPage");
        Integer totPageCnt = ReflectionUtils.getFieldValue(responseBody, "TotPageCnt");
        Integer totCount = ReflectionUtils.getFieldValue(responseBody, "TotCount");
        Integer cardContinueInfo = ReflectionUtils.getFieldValue(responseBody, "CardContinueInfo");
        String dumy02 = ReflectionUtils.getFieldValue(responseBody, "dumy02");

        hostReqCommon.setProcessTp(processTp);

        ReflectionUtils.setFieldValue(requestBody, "ContinueInfo", continueInfo);
        ReflectionUtils.setFieldValue(requestBody, "ContinueType", continueType);
        ReflectionUtils.setFieldValue(requestBody, "AvailBalSign", availBalSign);
        ReflectionUtils.setFieldValue(requestBody, "AvailBalance", availBalance);
        ReflectionUtils.setFieldValue(requestBody, "ContinueYn", continueYn);
        ReflectionUtils.setFieldValue(requestBody, "ReferOrder", referOrder);
        ReflectionUtils.setFieldValue(requestBody, "ContinueAssort", continueAssort);
        ReflectionUtils.setFieldValue(requestBody, "AcctServiceCode", acctServiceCode);
        ReflectionUtils.setFieldValue(requestBody, "DepPayPart", depPayPart);
        ReflectionUtils.setFieldValue(requestBody, "DrawBankCode", drawBankCode);
        ReflectionUtils.setFieldValue(requestBody, "DrawAcctNum", drawAcctNum);
        ReflectionUtils.setFieldValue(requestBody, "DepBankCode", depBankCode);
        ReflectionUtils.setFieldValue(requestBody, "DepAcctNum", depAcctNum);
        ReflectionUtils.setFieldValue(requestBody, "PrintCount", printCount);
        ReflectionUtils.setFieldValue(requestBody, "ReferCount", referCount);
        ReflectionUtils.setFieldValue(requestBody, "PayCount", payCount);
        ReflectionUtils.setFieldValue(requestBody, "CurrentPage", currentPage);
        ReflectionUtils.setFieldValue(requestBody, "TotPageCnt", totPageCnt);
        ReflectionUtils.setFieldValue(requestBody, "TotCount", totCount);
        ReflectionUtils.setFieldValue(requestBody, "CardContinueInfo", cardContinueInfo);
        ReflectionUtils.setFieldValue(requestBody, "dumy02", dumy02);

        return request;
    }
}