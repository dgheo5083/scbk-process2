package com.scbank.process.api.svc.common.service.settings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.service.settings.dto.code.CodeCategoryDto;
import com.scbank.process.api.svc.common.service.settings.dto.code.CodeDto;
import com.scbank.process.api.svc.common.service.settings.dto.code.SetCodListBankCodeRequest;
import com.scbank.process.api.svc.common.service.settings.dto.code.SetCodListBankCodeResponse;
import com.scbank.process.api.svc.common.service.settings.dto.code.SetCodListRequest;
import com.scbank.process.api.svc.common.service.settings.dto.code.SetCodListResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "설정 - 코드관리", url = "/settings/code")
public class SettingsCodeService {

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    @ServiceEndpoint(url = "/list", name = "코드목록조회")
    public SetCodListResponse list(IServiceContext serviceContext, SetCodListRequest request) {
        SetCodListResponse response = new SetCodListResponse();

        if (log.isDebugEnabled()) {
            log.debug("categories >> " + request.getCategories());
        }

        List<CodeCategoryDto> categories = new ArrayList<CodeCategoryDto>();

        for (String category : request.getCategories()) {
            List<ICodeItemInfo> codes = new ArrayList<>(CodeUtils.getCodes(category));

            // order, key 오름차순 정렬
            // codes.sort(Comparator.comparing(ICodeItemInfo::getOrder).thenComparing(ICodeItemInfo::getKey));

            codes.sort(Comparator.comparing(ICodeItemInfo::getOrder));

            CodeCategoryDto categoryDto = new CodeCategoryDto();
            categoryDto.setCategory(category);
            categoryDto.setCount(codes.size());
            categoryDto.setCodes(codes.stream().map(code -> {
                CodeDto codeDto = new CodeDto();
                codeDto.setCode(code.getKey());
                codeDto.setName(code.getValue());
                return codeDto;
            }).toList());

            categories.add(categoryDto);
        }

        response.setCategoriesCount(categories.size());

        response.setCategories(categories);

        return response;
    }

    @ServiceEndpoint(url = "/listBankCode", name = "은행코드목록조회")
    public SetCodListBankCodeResponse listBankCode(IServiceContext serviceContext, SetCodListBankCodeRequest request) {
        SetCodListBankCodeResponse response = new SetCodListBankCodeResponse();

        String yoDelay = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("YODELAY", String.class), "N");

        String category = "obs".equals(request.getCodeType()) ? "BKCODE_OBS" : "BKCODE";

        List<CodeDto> codes = CodeUtils.getCodes(category).stream().filter(code -> {
            String codeValue = code.getKey();
            // 국세/지방세 미노출케이스 이거나, 지연이체 고객의 경우
            if ("N".equals(request.getTaxDisplayYn()) || "Y".equalsIgnoreCase(yoDelay)) {
                // 091 : 국세청, 481 : 지방세입, 485 : 국고금
                if (List.of("091", "481", "485").contains(codeValue)) {
                    return false;
                }
            }

            // SC제일은행 노출여부
            if ("N".equals(request.getScbankDisplayYn())) {
                if ("023".equals(codeValue)) {
                    return false;
                }
            }

            return true;
        }).sorted(Comparator.comparing(ICodeItemInfo::getOrder)).map(code -> {
            CodeDto codeDto = new CodeDto();

            codeDto.setCode(code.getKey());

            if ("obs".equals(request.getCodeType())) {
                String[] values = code.getValue().split(";;");
                codeDto.setName(values[0]);
                codeDto.setUrl(values[1]);
                codeDto.setFcode(values[2]);
            } else {
                codeDto.setName(code.getValue());
            }

            return codeDto;
        }).toList();

        response.setCount(codes.size());
        response.setCodes(codes);

        return response;
    }

}