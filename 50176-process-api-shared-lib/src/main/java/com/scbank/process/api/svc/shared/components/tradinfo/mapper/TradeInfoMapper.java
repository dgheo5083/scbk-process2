package com.scbank.process.api.svc.shared.components.tradinfo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.scbank.process.api.edmi.dto.mci.MciDy7200170001Res;
import com.scbank.process.api.edmi.dto.mci.MciYp0049010001Res;
import com.scbank.process.api.edmi.dto.mci.MciYp0049020001Res;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.CheckOngoingTradeInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.OngoingTradeInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.TradeInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.CheckOngoingTradeInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingProcessCancelResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.ScreenAndScrapingInfoResponse;



@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TradeInfoMapper {
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse toCheckOngoingTradeInfoResponse(CheckOngoingTradeInfoInquiryResult dto);
	
	/**
	 * NF_TRADINFO_MGT_S_02 to MA3CMMBIZ010_106S
	 * @param dto OngoingTradeInfoInquiryResult
	 * @return OngoingTradeInfoInquiryResponse
	 */
	OngoingTradeInfoInquiryResponse toOngoingTradeInfoResponse(OngoingTradeInfoInquiryResult dto);
	
	/**
	 * NF_TRADINFO_MGT_S_05 to MA3CMMBIZ015_102S
	 * 
	 * @param response OngoingTradeInfoInquiryResponse
	 * @return ScreenAndScrapingInfoResponse.TradeInfoResponse
	 */
	ScreenAndScrapingInfoResponse.TradeInfoResponse toListScrapingInfoResponseDto(TradeInfoInquiryResult response);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	@Mapping(source = "FORESERVE5", target = "foreserve5")
    @Mapping(source = "FOERRCOD", target = "foerrcod")
    @Mapping(source = "FOERRNAME", target = "foerrname")
    @Mapping(source = "FOTOTCNT", target = "fototcnt")
    @Mapping(source = "FONXTTBL", target = "fonxttbl")
    @Mapping(source = "FOGUNSU", target = "fogunsu")
    @Mapping(source = "FOJUMIN", target = "fojumin")
    @Mapping(source = "FOCHJNA", target = "fochjna")
    @Mapping(source = "FODCSCAK", target = "fodcscak")
    @Mapping(source = "FOCEUTELNO", target = "foceutelno")
    @Mapping(source = "FOJNINGB", target = "fojningb")
    @Mapping(source = "FOCCNCHSHB", target = "foccnchshb")
    @Mapping(source = "FOCCNCHSNM", target = "foccnchsnm")
	OngoingProcessCancelResponse toResponseFromMciYp0049010001Res(MciYp0049010001Res dto);

	/**
	 * 
	 * @param dto
	 * @return
	 */
    @Mapping(source = "FORESERVE5", target = "foreserve5")
    @Mapping(source = "FOERRCOD", target = "foerrcod")
    @Mapping(source = "FOERRNAME", target = "foerrname")
    @Mapping(source = "FOTOTCNT", target = "fototcnt")
    @Mapping(source = "FONXTTBL", target = "fonxttbl")
    @Mapping(source = "FOJUBNO", target = "fojubno")
    OngoingProcessCancelResponse toResponseFromMciYp0049020001Res(MciYp0049020001Res dto);
    
    /**
     * 
     * @param dto
     * @return
     */
    @Mapping(source = "FORESERVE5", target = "foreserve5")
    @Mapping(source = "FOERRCOD", target = "foerrcod")
    @Mapping(source = "FOERRNAME", target = "foerrname")
    @Mapping(source = "FOTOTCNT", target = "fototcnt")
    @Mapping(source = "FONXTTBL", target = "fonxttbl")
    @Mapping(source = "FOGUNSU", target = "fogunsu")
    OngoingProcessCancelResponse toResponseFromMciDy7200170001Res(MciDy7200170001Res dto);

}
