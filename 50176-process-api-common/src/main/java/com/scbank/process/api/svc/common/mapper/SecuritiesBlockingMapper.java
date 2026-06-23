package com.scbank.process.api.svc.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01D96000Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D96000Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H96100Res;
import com.scbank.process.api.svc.common.service.securities.dto.blocking.SecBlkApplyBlockingForeignIpMainRequest;
import com.scbank.process.api.svc.common.service.securities.dto.blocking.SecBlkApplyBlockingForeignIpMainResponse;
import com.scbank.process.api.svc.common.service.securities.dto.blocking.SecBlkGetBlockingForeignIpResponse;

@Mapper(componentModel = "spring")
public interface SecuritiesBlockingMapper {

        @Mapping(source = "YOTOYB", target = "yoTOYB")
        @Mapping(source = "YONTSU", target = "yoNTSU")
        SecBlkGetBlockingForeignIpResponse toSecBlkGetBlockingForeignIpResponse(
                        CbIbk01H96100Res dto);

        @Mapping(source = "YONTNM", target = "yoNTNM")
        @Mapping(source = "YONTCD", target = "yoNTCD")
        SecBlkGetBlockingForeignIpResponse.ComNameReqRltDto toSecBlkGetBlockingForeignIpResponse(
                        CbIbk01H96100Res.ComNameReqRltDto dto);

        @Mapping(source = "YOSTGB", target = "yoSTGB")
        @Mapping(source = "YOGUNSU", target = "yoGUNSU")
        SecBlkApplyBlockingForeignIpMainResponse toSecBlkApplyBlockingForeignIpMainResponse(
                        CbIbk01D96000Res dto);

        @Mapping(source = "YONTNM", target = "yoNTNM")
        @Mapping(source = "YONTCD", target = "yoNTCD")
        SecBlkApplyBlockingForeignIpMainResponse.ComNameReqRltDto toSecBlkApplyBlockingForeignIpMainResponse(
                        CbIbk01D96000Res.ComNameReqRltDto dto);

        @Mapping(source = "yiSCGB", target = "YISCGB")
        @Mapping(source = "yiSTGB", target = "YISTGB")
        @Mapping(source = "yiGUNSU", target = "YIGUNSU")
        @Mapping(source = "yiNTNM0", target = "YINTNM0")
        @Mapping(source = "yiNTCD0", target = "YINTCD0")
        @Mapping(source = "yiNTNM1", target = "YINTNM1")
        @Mapping(source = "yiNTCD1", target = "YINTCD1")
        @Mapping(source = "yiNTNM2", target = "YINTNM2")
        @Mapping(source = "yiNTCD2", target = "YINTCD2")
        @Mapping(source = "yiNTNM3", target = "YINTNM3")
        @Mapping(source = "yiNTCD3", target = "YINTCD3")
        @Mapping(source = "yiNTNM4", target = "YINTNM4")
        @Mapping(source = "yiNTCD4", target = "YINTCD4")
        @Mapping(source = "yiNTNM5", target = "YINTNM5")
        @Mapping(source = "yiNTCD5", target = "YINTCD5")
        @Mapping(source = "yiNTNM6", target = "YINTNM6")
        @Mapping(source = "yiNTCD6", target = "YINTCD6")
        @Mapping(source = "yiNTNM7", target = "YINTNM7")
        @Mapping(source = "yiNTCD7", target = "YINTCD7")
        @Mapping(source = "yiNTNM8", target = "YINTNM8")
        @Mapping(source = "yiNTCD8", target = "YINTCD8")
        @Mapping(source = "yiNTNM9", target = "YINTNM9")
        @Mapping(source = "yiNTCD9", target = "YINTCD9")
        CbIbk01D96000Req toCbIbk01D96000Req(
                        SecBlkApplyBlockingForeignIpMainRequest dto);
}
