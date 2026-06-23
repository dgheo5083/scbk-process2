package com.scbank.process.api.svc.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01D84400Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Res;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvClosePasswordErrorClearAccountResponse;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvEditNewPasswordMainResponse;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvListMyAccountsResponse;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvListPasswordErrorClearAccountResponse;

@Mapper(componentModel = "spring")
public interface SecuritiesRecoveryMapper {

    @Mapping(source = "YOIBID", target = "yoIBID")
    @Mapping(source = "YOIBGB", target = "yoIBGB")
    @Mapping(source = "YOTBGB", target = "yoTBGB")
    @Mapping(source = "YOFBGB", target = "yoFBGB")
    @Mapping(source = "YOPSOGB", target = "yoPSOGB")
    @Mapping(source = "YOTSOGB", target = "yoTSOGB")
    @Mapping(source = "YODSGB", target = "yoDSGB")
    @Mapping(source = "YOPJANGO", target = "yoPJANGO")
    @Mapping(source = "YONAME", target = "yoNAME")
    @Mapping(source = "YOHDDD", target = "yoHDDD")
    @Mapping(source = "YOHGUK", target = "yoHGUK")
    @Mapping(source = "YOHTEL", target = "yoHTEL")
    @Mapping(source = "YOJMNO", target = "yoJMNO")
    @Mapping(source = "YOFATIL", target = "yoFATIL")
    @Mapping(source = "YOCIFNO", target = "yoCIFNO")
    @Mapping(source = "YOMKTYN", target = "yoMKTYN")
    @Mapping(source = "YOSMSYN", target = "yoSMSYN")
    @Mapping(source = "YOHUMGB", target = "yoHUMGB")
    @Mapping(source = "YOPAYDD", target = "yoPAYDD")
    @Mapping(source = "YOTSUPGB", target = "yoTSUPGB")
    @Mapping(source = "YOTSCNOD", target = "yoTSCNOD")
    @Mapping(source = "YOTSIRCD", target = "yoTSIRCD")
    @Mapping(source = "YOPSCST", target = "yoPSCST")
    @Mapping(source = "YOTSCST", target = "yoTSCST")
    @Mapping(source = "YODUMMY", target = "yoDUMMY")
    @Mapping(source = "YOGUNSU", target = "yoGUNSU")
    @Mapping(source = "YOMYINF_REC", target = "yoMYINF_REC")
    SecRcvListMyAccountsResponse toSecRcvListMyAccountsResponse(
            CbIbk01H86600Res dto);

    @Mapping(source = "YOMYGJ", target = "yoMYGJ")
    @Mapping(source = "YOZONG", target = "yoZONG")
    @Mapping(source = "YOBSJUM", target = "yoBSJUM")
    @Mapping(source = "YOSIIL", target = "yoSIIL")
    @Mapping(source = "YOMKJANSIGN", target = "yoMKJANSIGN")
    @Mapping(source = "YOMKJAN", target = "yoMKJAN")
    @Mapping(source = "YOPAYGB", target = "yoPAYGB")
    @Mapping(source = "YOHDGB", target = "yoHDGB")
    @Mapping(source = "YONHDGB", target = "yoNHDGB")
    @Mapping(source = "YOTONM", target = "yoTONM")
    @Mapping(source = "YOHUMYN", target = "yoHUMYN")
    @Mapping(source = "YOGRJJ", target = "yoGRJJ")
    @Mapping(source = "YOTJDC", target = "yoTJDC")
    @Mapping(source = "YOHDLMGB", target = "yoHDLMGB")
    SecRcvListMyAccountsResponse.YOMYINF_REC toSecRcvListMyAccountsResponse(
            CbIbk01H86600Res.YOMYINF_REC dto);

    SecRcvEditNewPasswordMainResponse toSecRcvEditNewPasswordMainResponse(CbIbk01D84400Res dto);

    @Mapping(source = "YOIBID", target = "yoIBID")
    @Mapping(source = "YOIBGB", target = "yoIBGB")
    @Mapping(source = "YOTBGB", target = "yoTBGB")
    @Mapping(source = "YOFBGB", target = "yoFBGB")
    @Mapping(source = "YOPSOGB", target = "yoPSOGB")
    @Mapping(source = "YOTSOGB", target = "yoTSOGB")
    @Mapping(source = "YODSGB", target = "yoDSGB")
    @Mapping(source = "YOPJANGO", target = "yoPJANGO")
    @Mapping(source = "YONAME", target = "yoNAME")
    @Mapping(source = "YOHDDD", target = "yoHDDD")
    @Mapping(source = "YOHGUK", target = "yoHGUK")
    @Mapping(source = "YOHTEL", target = "yoHTEL")
    @Mapping(source = "YOJMNO", target = "yoJMNO")
    @Mapping(source = "YOFATIL", target = "yoFATIL")
    @Mapping(source = "YOCIFNO", target = "yoCIFNO")
    @Mapping(source = "YOMKTYN", target = "yoMKTYN")
    @Mapping(source = "YOSMSYN", target = "yoSMSYN")
    @Mapping(source = "YOHUMGB", target = "yoHUMGB")
    @Mapping(source = "YOPAYDD", target = "yoPAYDD")
    @Mapping(source = "YOTSUPGB", target = "yoTSUPGB")
    @Mapping(source = "YOTSCNOD", target = "yoTSCNOD")
    @Mapping(source = "YOTSIRCD", target = "yoTSIRCD")
    @Mapping(source = "YOPSCST", target = "yoPSCST")
    @Mapping(source = "YOTSCST", target = "yoTSCST")
    @Mapping(source = "YODUMMY", target = "yoDUMMY")
    @Mapping(source = "YOGUNSU", target = "yoGUNSU")
    @Mapping(source = "YOMYINF_REC", target = "yoMYINF_REC")
    SecRcvListPasswordErrorClearAccountResponse toSecRcvListPasswordErrorClearAccountResponse(CbIbk01H86600Res dto);

    @Mapping(source = "YOMYGJ", target = "yoMYGJ")
    @Mapping(source = "YOZONG", target = "yoZONG")
    @Mapping(source = "YOBSJUM", target = "yoBSJUM")
    @Mapping(source = "YOSIIL", target = "yoSIIL")
    @Mapping(source = "YOMKJANSIGN", target = "yoMKJANSIGN")
    @Mapping(source = "YOMKJAN", target = "yoMKJAN")
    @Mapping(source = "YOPAYGB", target = "yoPAYGB")
    @Mapping(source = "YOHDGB", target = "yoHDGB")
    @Mapping(source = "YONHDGB", target = "yoNHDGB")
    @Mapping(source = "YOTONM", target = "yoTONM")
    @Mapping(source = "YOHUMYN", target = "yoHUMYN")
    @Mapping(source = "YOGRJJ", target = "yoGRJJ")
    @Mapping(source = "YOTJDC", target = "yoTJDC")
    @Mapping(source = "YOHDLMGB", target = "yoHDLMGB")
    SecRcvListPasswordErrorClearAccountResponse.YOMYINF_REC toSecRcvListPasswordErrorClearAccountResponseYomyinfRec(
            CbIbk01H86600Res.YOMYINF_REC dto);

    SecRcvClosePasswordErrorClearAccountResponse toSecRcvClosePasswordErrorClearAccountResponse(CbIbk01D84400Res dto);
}
