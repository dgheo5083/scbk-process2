package com.scbank.process.api.svc.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import com.scbank.process.api.svc.common.service.mydata.dto.GetJoinInfoRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.GetJoinInfoResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.ListCardOrganInfoRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.ListCardOrganInfoResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.ListCardManageInfoRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.ListCardManageInfoResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.ListCardLoanRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.ListCardLoanResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.ListJoinOrganInfoRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.ListJoinOrganInfoResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.GetPaymentLastFiveDaysRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.GetPaymentLastFiveDaysResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.GetMostUsedThreeMonthRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.GetMostUsedThreeMonthResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.GetMostSpentLastMonthRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.GetMostSpentLastMonthResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.GetCardBasicInfoRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.GetCardBasicInfoResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MydataCardMapper {

        MydataCardMapper INSTANCE = Mappers.getMapper(MydataCardMapper.class);

        /**
         * 마이데이터 > 가입유무 조회 API
         */

        GetJoinInfoResponse toGetJoinInfoResponse(GetJoinInfoRequest dto);

        /**
         * 마이데이터 > 현대카드목록 조회 API
         */

        ListCardOrganInfoResponse toListCardOrganInfoResponse(ListCardOrganInfoRequest dto);

        /**
         * 마이데이터 > 현대카드 관리 조회 API
         */

        ListCardManageInfoResponse toListCardManageInfoResponse(ListCardManageInfoRequest dto);

        /**
         * 마이데이터 > 장기카드대출 API
         */

        ListCardLoanResponse toListCardLoanResponse(ListCardLoanRequest dto);

        /**
         * 마이데이터 > 현대카드 이용내역 조회 API
         */

        ListJoinOrganInfoResponse toListJoinOrganInfoResponse(ListJoinOrganInfoRequest dto);

        /**
         * 마이데이터 > 최근 5일 결제금액 조회 API
         */
        GetPaymentLastFiveDaysResponse toGetPaymentLastFiveDaysResponse(
                        GetPaymentLastFiveDaysRequest dto);

        /**
         * 마이데이터 > 가장 많이 쓴 3일 결제금액 조회 API
         */
        GetMostUsedThreeMonthResponse toGetMostUsedThreeDaysResponse(
                        GetMostUsedThreeMonthRequest dto);

        /**
         * 마이데이터 > 지난달 가장 많이 쓴 가맹점 및 금액 조회 API
         */
        GetMostSpentLastMonthResponse toGetMostSpentLastMonthResponse(
                        GetMostSpentLastMonthRequest dto);

        /**
         * 마이데이터 > 결제예상금액 (기본 정보) 조회 API
         */
        GetCardBasicInfoResponse toGetCardBasicInfoResponse(
                        GetCardBasicInfoRequest dto);

}
