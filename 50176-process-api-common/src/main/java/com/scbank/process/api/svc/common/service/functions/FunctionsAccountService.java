package com.scbank.process.api.svc.common.service.functions;

import java.util.List;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Req;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.svc.common.mapper.FunctionsAccountMapper;
import com.scbank.process.api.svc.common.service.functions.dto.account.FncAccListResponse;
import com.scbank.process.api.svc.shared.components.account.AccountListComponent;
import com.scbank.process.api.svc.shared.components.account.dto.AllAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.ListAccountHeldInfo;
import com.scbank.process.api.svc.shared.components.account.dto.OpenbankingAccountInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통기능 - 계좌", url = "/functions/account")
public class FunctionsAccountService {

    private final AccountListComponent accountListComponent;

    private final FunctionsAccountMapper mapper;

    @ServiceEndpoint(url = "/list", name = "계좌목록조회")
    public FncAccListResponse list(IServiceContext serviceContext) {

        FncAccListResponse response = new FncAccListResponse();

        List<AllAccountInfo> depositAccountList = mapper.toDepositAccountList(accountListComponent.getAllAccountList());

        List<AllAccountInfo> loanAccountList = mapper.toLoanAccountList(accountListComponent.getAllAccountList());

        List<OpenbankingAccountInfo> openbankingAccountList = accountListComponent.getOpenbankingAccountList();

        response.setDepositAccountList(depositAccountList);
        response.setLoanAccountList(loanAccountList);
        response.setObsAccountList(openbankingAccountList);

        return response;
    }

    @ServiceEndpoint(url = "/holdingsList", name = "보유계좌조회 [ASIS:MA3CMMBIZ001_115S]")
    public ListAccountHeldInfo listAccountHoldingsInquiry(IServiceContext ctx, CbIbk01H86600Req request)
            throws PRCServiceException {

        return accountListComponent.getListAccountHeld(request);

    }
}
