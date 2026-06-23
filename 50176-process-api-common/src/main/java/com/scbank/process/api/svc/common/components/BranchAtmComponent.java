package com.scbank.process.api.svc.common.components;

import org.apache.commons.lang3.StringUtils;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.common.components.dto.GetForeignBranchAtmRequest;
import com.scbank.process.api.svc.common.components.dto.GetForeignBranchAtmResponse;
import com.scbank.process.api.svc.common.dao.GlbAtmMgtDao;
import com.scbank.process.api.svc.common.dao.dto.GetForeignBranchAtmCountParameter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "영업점/ATM찾기 컴포넌트")
public class BranchAtmComponent {

    private final GlbAtmMgtDao glbAtmMgtDao;

    /**
     * 해외영업점 찾기
     *
     * @param findForeignBranchAtm
     * @return
     * @throws Exception
     * @description MA3CSTATM005_109S
     */
    @ComponentOperation(name = "해외영업점 찾기 [ASIS:MA3CSTATM005_109S]", author = "송지섭")
    public GetForeignBranchAtmResponse getForeignBranchAtm(GetForeignBranchAtmRequest input) {
        GetForeignBranchAtmResponse response = new GetForeignBranchAtmResponse();

        String keyword = StringUtils.defaultIfEmpty(input.getKeyWord(), "");
        String NTNL = StringUtils.defaultIfEmpty(input.getNtnl(), "");
        String CITY = StringUtils.defaultIfEmpty(input.getCity(), "");
        String LOCAL = StringUtils.defaultIfEmpty(input.getLocal(), "");

        GetForeignBranchAtmCountParameter param = new GetForeignBranchAtmCountParameter();
        param.setKeyWord(keyword);
        param.setNtnl(NTNL);
        param.setCity(CITY);
        param.setLocal(LOCAL);

        int CountData = 0;
        try {
            CountData = glbAtmMgtDao.selectGetForeignBranchAtmCount(param);
        } catch (PRCServiceException e) {
            throw e;
        } finally {
            if (CountData > 0) {
                response.setCountData(CountData);
            }
        }

        return response;
    }

}
