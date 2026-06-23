package com.scbank.process.api.svc.shared.components.account.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Res.YOMYINF_REC;
import com.scbank.process.api.svc.shared.components.account.dto.ListAccountHeldInfo;
import com.scbank.process.api.svc.shared.components.account.dto.ListAccountHeldInfo.yoMyInfRecList;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ListAccountHeldMapper {
	@Mapping(source = "YOIBID", target = "yoIbId")
    @Mapping(source = "YOIBGB", target = "yoIbGb")
    @Mapping(source = "YOTBGB", target = "yoTbGb")
    @Mapping(source = "YOFBGB", target = "yoFbGb")
    @Mapping(source = "YOPSOGB", target = "yoPsoGb")
    @Mapping(source = "YOTSOGB", target = "yoTsoGb")
    @Mapping(source = "YODSGB", target = "yoDsGb")
    @Mapping(source = "YOPJANGO", target = "yoPjango")
    @Mapping(source = "YONAME", target = "yoName")
    @Mapping(source = "YOHDDD", target = "yoHddd")
    @Mapping(source = "YOHGUK", target = "yoHguk")
    @Mapping(source = "YOHTEL", target = "yoHtel")
    @Mapping(source = "YOJMNO", target = "yoJmNo")
    @Mapping(source = "YOFATIL", target = "yoFatIl")
    @Mapping(source = "YOCIFNO", target = "yoCifNo")
    @Mapping(source = "YOMKTYN", target = "yoMktYn")
    @Mapping(source = "YOSMSYN", target = "yoSmsYn")
    @Mapping(source = "YOHUMGB", target = "yoHumGb")
    @Mapping(source = "YOPAYDD", target = "yoPayDd")
    @Mapping(source = "YOTSUPGB", target = "yoTsupGb")
    @Mapping(source = "YOTSCNOD", target = "yoTscNod")
    @Mapping(source = "YOTSIRCD", target = "yoTsirCd")
    @Mapping(source = "YOPSCST", target = "yoPscSt")
    @Mapping(source = "YOTSCST", target = "yoTscSt")
    @Mapping(source = "YODUMMY", target = "yoDummy")
    @Mapping(source = "YOGUNSU", target = "yoGunsu")
    @Mapping(source = "YOMYINF_REC", target = "yoMyInfRec")
	ListAccountHeldInfo toListAccountHeldInfo(CbIbk01H86600Res dto);

    @Mapping(source = "YOMYGJ", target = "yoMyGj")
    @Mapping(source = "YOZONG", target = "yoZong")
    @Mapping(source = "YOBSJUM", target = "yoBsJum")
    @Mapping(source = "YOSIIL", target = "yoSiIl")
    @Mapping(source = "YOMKJANSIGN", target = "yoMkJanSign")
    @Mapping(source = "YOMKJAN", target = "yoMkJan")
    @Mapping(source = "YOPAYGB", target = "yoPayGb")
    @Mapping(source = "YOHDGB", target = "yoHdGb")
    @Mapping(source = "YONHDGB", target = "yoNhdGb")
    @Mapping(source = "YOTONM", target = "yoToNm")
    @Mapping(source = "YOHUMYN", target = "yoHumYn")
    @Mapping(source = "YOGRJJ", target = "yoGrJj")
    @Mapping(source = "YOTJDC", target = "yoTjDc")
    @Mapping(source = "YOHDLMGB", target = "yoHdlmGb")
    yoMyInfRecList toListAccountHeldInfo(YOMYINF_REC dtoList);
}
