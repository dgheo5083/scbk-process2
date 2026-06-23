package com.scbank.process.api.svc.shared.components.customer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.scbank.process.api.svc.shared.components.customer.dao.dto.NonFaceCustomerInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.customer.dto.NonFaceCustomerInfoInquiryResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NonFaceCustomerInfoMapper {
	
	
	@Mapping(source = "ssn", target = "ssn")
	@Mapping(source = "custNo", target = "custNo")
	@Mapping(source = "mblphnNo", target = "mblphnNo")
	@Mapping(source = "userNm", target = "userNm")
	@Mapping(source = "cmpndCheckKey", target = "cmpndCheckKey")
	@Mapping(source = "srcNewUserFlg", target = "srcNewUserFlg")
	@Mapping(source = "newUserFlg", target = "newUserFlg")
	@Mapping(source = "srcDelObjFlg", target = "srcDelObjFlg")
	@Mapping(source = "delObjFlg", target = "delObjFlg")
	@Mapping(source = "initCnnctnDt", target = "initCnnctnDt")
	@Mapping(source = "lstCnnctnDt", target = "lstCnnctnDt")
	NonFaceCustomerInfoInquiryResponse toNonFaceCustInfoResponseDto(NonFaceCustomerInfoInquiryResult result);

}
