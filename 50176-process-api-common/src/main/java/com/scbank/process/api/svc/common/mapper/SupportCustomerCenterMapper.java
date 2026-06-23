package com.scbank.process.api.svc.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.scbank.process.api.edmi.dto.mci.MciHpCuCusCosRes;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D75400Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H75400Res;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscApplyPhoneConsultationResponse;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscSetMarketingTermsPageResponse;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscUpdateMarketingTermsResponse;

@Mapper(componentModel = "spring")
public interface SupportCustomerCenterMapper {

    @Mapping(source = "YOUSID", target = "yoUSID")
    @Mapping(source = "YOGRGJIL", target = "yoGRGJIL")
    @Mapping(source = "YOGRJN", target = "yoGRJN")
    @Mapping(source = "YOPHONE01", target = "yoPHONE01")
    @Mapping(source = "YOPHONE02", target = "yoPHONE02")
    @Mapping(source = "YOPHONE03", target = "yoPHONE03")
    @Mapping(source = "YOPHONE04", target = "yoPHONE04")
    @Mapping(source = "YOPHONE05", target = "yoPHONE05")
    @Mapping(source = "YOPHONE06", target = "yoPHONE06")
    @Mapping(source = "YOPHONE07", target = "yoPHONE07")
    @Mapping(source = "YOPHONE08", target = "yoPHONE08")
    @Mapping(source = "YOPHONE09", target = "yoPHONE09")
    @Mapping(source = "YOPHONE10", target = "yoPHONE10")
    @Mapping(source = "YOMAIL01", target = "yoMAIL01")
    @Mapping(source = "YOMAIL02", target = "yoMAIL02")
    @Mapping(source = "YOMAIL03", target = "yoMAIL03")
    @Mapping(source = "YOMAIL04", target = "yoMAIL04")
    @Mapping(source = "YOMAIL05", target = "yoMAIL05")
    @Mapping(source = "YOMAIL06", target = "yoMAIL06")
    @Mapping(source = "YOMAIL07", target = "yoMAIL07")
    @Mapping(source = "YOMAIL08", target = "yoMAIL08")
    @Mapping(source = "YOMAIL09", target = "yoMAIL09")
    @Mapping(source = "YOMAIL10", target = "yoMAIL10")
    @Mapping(source = "YOMUSE3", target = "yoMUSE3")
    @Mapping(source = "YOMUSEM3", target = "yoMUSEM3")
    @Mapping(source = "YOLUSE22S", target = "yoLUSE22S")
    @Mapping(source = "YODUMMY", target = "yoDUMMY")
    SupCscSetMarketingTermsPageResponse toSupCscSetMarketingTermsPageResponse(CbIbk01H75400Res dto);

    @Mapping(source = "YOUSID", target = "yoUSID")
    @Mapping(source = "YOGRGJIL", target = "yoGRGJIL")
    @Mapping(source = "YOGRJN", target = "yoGRJN")
    @Mapping(source = "YOPHONE01", target = "yoPHONE01")
    @Mapping(source = "YOPHONE02", target = "yoPHONE02")
    @Mapping(source = "YOPHONE03", target = "yoPHONE03")
    @Mapping(source = "YOPHONE04", target = "yoPHONE04")
    @Mapping(source = "YOPHONE05", target = "yoPHONE05")
    @Mapping(source = "YOPHONE06", target = "yoPHONE06")
    @Mapping(source = "YOPHONE07", target = "yoPHONE07")
    @Mapping(source = "YOPHONE08", target = "yoPHONE08")
    @Mapping(source = "YOPHONE09", target = "yoPHONE09")
    @Mapping(source = "YOPHONE10", target = "yoPHONE10")
    @Mapping(source = "YOMAIL01", target = "yoMAIL01")
    @Mapping(source = "YOMAIL02", target = "yoMAIL02")
    @Mapping(source = "YOMAIL03", target = "yoMAIL03")
    @Mapping(source = "YOMAIL04", target = "yoMAIL04")
    @Mapping(source = "YOMAIL05", target = "yoMAIL05")
    @Mapping(source = "YOMAIL06", target = "yoMAIL06")
    @Mapping(source = "YOMAIL07", target = "yoMAIL07")
    @Mapping(source = "YOMAIL08", target = "yoMAIL08")
    @Mapping(source = "YOMAIL09", target = "yoMAIL09")
    @Mapping(source = "YOMAIL10", target = "yoMAIL10")
    @Mapping(source = "YOMUSE3", target = "yoMUSE3")
    @Mapping(source = "YOMUSEM3", target = "yoMUSEM3")
    @Mapping(source = "YOLUSE22S", target = "yoLUSE22S")
    @Mapping(source = "YODUMMY", target = "yoDUMMY")
    SupCscUpdateMarketingTermsResponse toSupCscUpdateMarketingTermsResponse(CbIbk01D75400Res dto);

    @Mapping(source = "TORESULT", target = "toRESULT")
    SupCscApplyPhoneConsultationResponse toSupCscApplyPhoneConsultationResponse(MciHpCuCusCosRes dto);

}
