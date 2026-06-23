package com.scbank.process.api.svc.common.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.scbank.process.api.edmi.dto.mci.MciIbAddr0101Req;
import com.scbank.process.api.edmi.dto.mci.MciIbAddr0101Res;
import com.scbank.process.api.edmi.dto.mci.MciIbAddr0104Req;
import com.scbank.process.api.edmi.dto.mci.MciIbAddr0104Res;
import com.scbank.process.api.edmi.dto.mci.MciTm1517090001Req;
import com.scbank.process.api.edmi.dto.mci.MciTm1517090001Res;
import com.scbank.process.api.svc.common.dao.dto.BranchInfoResult;
import com.scbank.process.api.svc.common.dao.dto.EmployeeInfoResult;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindAddressRefineRequest;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindAddressRefineResponse;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindAddressRequest;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindAddressResponse;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindBranchResponse;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindEmployeeResponse;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindOfficeRequest;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindOfficeResponse;

@Mapper(componentModel = "spring")
public interface FunctionsSearchMapper {

    @Mapping(source = "tisidona", target = "TISIDONA")
    @Mapping(source = "tisiggna", target = "TISIGGNA")
    @Mapping(source = "tijsojh", target = "TIJSOJH")
    public MciIbAddr0104Req toMciIbAddr0104Req(FncSchFindAddressRequest req);

    @Mapping(source = "TOTOTGSU", target = "tototgsu")
    @Mapping(source = "TOJRPGSU", target = "tojrpgsu")
    @Mapping(source = "TOJHNYKINF", target = "addressInfo")
    public FncSchFindAddressResponse toFunctionsAddressSearchResponse(MciIbAddr0104Res res);

    @Mapping(source = "TOZIPCD3", target = "tozipcd3")
    @Mapping(source = "TOOLDJSO1", target = "tooldjso1")
    @Mapping(source = "TOOLDJSO2", target = "tooldjso2")
    @Mapping(source = "TONEWJSO18", target = "tonewjso18")
    @Mapping(source = "TONEWJSO20", target = "tonewjso20")
    @Mapping(source = "TONENEWJSO1", target = "tonenewjso1")
    public FncSchFindAddressResponse.AddressInfo toAddressInfo(MciIbAddr0104Res.TOJHNYKINF list);

    @Mapping(source = "tinewjsogb2", target = "TINEWJSOGB2")
    @Mapping(source = "tiupno", target = "TIUPNO")
    @Mapping(source = "tinewjso1", target = "TINEWJSO1")
    @Mapping(source = "tinewjso4", target = "TINEWJSO4")
    public MciIbAddr0101Req toMciIbAddr0101Req(FncSchFindAddressRefineRequest req);

    @Mapping(source = "TONEWJSOGB2", target = "tonewjsogb2")
    @Mapping(source = "TONEWJSOGB", target = "tonewjsogb")
    @Mapping(source = "TONEWJSO1", target = "tonewjso1")
    @Mapping(source = "TONEWJSO4", target = "tonewjso4")
    @Mapping(source = "TOZIPCD1", target = "tozipcd1")
    @Mapping(source = "TOZIPCD2", target = "tozipcd2")
    @Mapping(source = "TONEWJSO2", target = "tonewjso2")
    @Mapping(source = "TONEWJSO5", target = "tonewjso5")
    @Mapping(source = "TOROADNACOD", target = "toroadnacod")
    @Mapping(source = "TOROADSEQ3", target = "toroadseq3")
    @Mapping(source = "TOJIHAYB", target = "tojihayb")
    @Mapping(source = "TOBLDNO", target = "tobldno")
    @Mapping(source = "TOJRPGSU", target = "tojrpgsu")
    @Mapping(source = "TOJHNYKINF", target = "addressRefineInfo")
    public FncSchFindAddressRefineResponse toFunctionsAddressRefineResponse(MciIbAddr0101Res res);

    @Mapping(source = "TOZIPCD1", target = "tozipcd1")
    @Mapping(source = "TOZIPCD2", target = "tozipcd2")
    @Mapping(source = "TONEWJSO3", target = "tonewjso3")
    @Mapping(source = "TONEWJSO5", target = "tonewjso5")
    @Mapping(source = "TOROADNACOD", target = "toroadnacod")
    @Mapping(source = "TOROADSEQ3", target = "toroadseq3")
    @Mapping(source = "TOJIHAYB", target = "tojihayb")
    @Mapping(source = "TOBLDNO", target = "tobldno")
    public FncSchFindAddressRefineResponse.AddressRefineInfo toAddressInfo(MciIbAddr0101Res.TOJHNYKINF list);

    public List<FncSchFindEmployeeResponse.EmployeeInfo> toEmployeeInfoList(List<EmployeeInfoResult> list);

    public List<FncSchFindBranchResponse.BranchInfo> toBranchInfoList(List<BranchInfoResult> list);

    @Mapping(source = "sibrno", target = "SIBRNO")
    @Mapping(source = "sitaxid", target = "SITAXID")
    @Mapping(source = "siupchnm", target = "SIUPCHNM")
    @Mapping(source = "sinxttbl", target = "SINXTTBL")
    public MciTm1517090001Req toMciTm1517090001Req(FncSchFindOfficeRequest req);

    @Mapping(source = "SOJHINF", target = "officeInfoList")
    public FncSchFindOfficeResponse toFncSchFindOfficeResponse(MciTm1517090001Res res);

    @Mapping(source = "SOEBRMUPCCOD", target = "soebrmupccod")
    @Mapping(source = "SOUPCHNM", target = "soupchnm")
    @Mapping(source = "SOTAXID", target = "sotaxid")
    public FncSchFindOfficeResponse.OfficeInfo toOfficeInfo(MciTm1517090001Res.SOJHINF info);
}
