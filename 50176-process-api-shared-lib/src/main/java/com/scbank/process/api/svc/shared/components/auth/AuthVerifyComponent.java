package com.scbank.process.api.svc.shared.components.auth;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.auth.dto.NonFaceAuthCompleteYnRequest;
import com.scbank.process.api.svc.shared.components.auth.dto.NonFaceAuthCompleteYnResponse;
import com.scbank.process.api.svc.shared.dao.NfTradinfoMgtDao;
import com.scbank.process.api.svc.shared.dao.dto.NfAuthCompleteYnParameter;
import com.scbank.process.api.svc.shared.dao.dto.NfAuthCompleteYnResult;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class AuthVerifyComponent {

    private final NfTradinfoMgtDao nfTradinfoMgtDao;

    @ComponentOperation(name = "비대면 인증 완료 여부 조회[MA3ACTOPN001_A12S]")
    public NonFaceAuthCompleteYnResponse getNonFaceAuthCompleteYn(NonFaceAuthCompleteYnRequest input) {
        NonFaceAuthCompleteYnResponse result = new NonFaceAuthCompleteYnResponse();

        String custNo = StringUtils.defaultIfEmpty(input.getCustNo(), SessionUtils.getSessionValue("CUST_NO"));
        String tradNo = StringUtils.defaultIfEmpty(input.getTradNo(), SessionUtils.getSessionValue("TRAD_NO"));

        NfAuthCompleteYnParameter authCinoketeParam = new NfAuthCompleteYnParameter();

        authCinoketeParam.setCustNo(custNo);
        authCinoketeParam.setTradNo(tradNo);

        NfAuthCompleteYnResult authCinoketeYnResult = nfTradinfoMgtDao.selectNfAuthCompleteYn(authCinoketeParam);

        if (authCinoketeYnResult != null) {
            result.setAuthntIndCd(authCinoketeYnResult.getAuthntIndCd());
            result.setCddReqCd(authCinoketeYnResult.getCddReqCd());
            result.setDocEvdcCd(authCinoketeYnResult.getDocEvdcCd());
            result.setIdCardCd(authCinoketeYnResult.getIdCardCd());
        }

        return result;
    }

}
