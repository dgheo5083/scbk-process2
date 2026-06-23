package com.scbank.process.api.svc.common.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.scbank.process.api.svc.common.components.dto.SupPntListPushCustomerRateRegistInfoResponse;
import com.scbank.process.api.svc.common.dao.dto.SelectExchangeInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SelectPushAgreeInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SelectPushServiceCdResult;
import com.scbank.process.api.svc.common.dao.dto.TermsInfoResult;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntConfirmPushJoinYnResponse;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntListPushRateResponse;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntSetMarketingNoticePopResponse;

@Mapper(componentModel = "spring")
public interface SupportPushNotificationMapper {

    List<SupPntSetMarketingNoticePopResponse.TermsInfo> toTermsInfoList(List<TermsInfoResult> termsInfoResult);

    List<SupPntListPushRateResponse.ExchangeInfo> toExchangeInfoList(List<SelectExchangeInfoResult> exchangeInfoList);

    List<SupPntListPushRateResponse.ExrateData> toExrateDataList(
            List<SupPntListPushCustomerRateRegistInfoResponse.ExrateData> exrateDataList);

    List<SupPntListPushRateResponse.ExchangeInfo> toExchangeInfo(List<SelectExchangeInfoResult> exchangeInfoResult);

    List<SupPntConfirmPushJoinYnResponse.PushServiceCd> toPushServiceCd(
            List<SelectPushServiceCdResult> selectpPushServiceCdResult);

    List<SupPntConfirmPushJoinYnResponse.PushAgreeInfo> toPushAgreeInfo(
            List<SelectPushAgreeInfoResult> selectPushAgreeInfoResult);

}
