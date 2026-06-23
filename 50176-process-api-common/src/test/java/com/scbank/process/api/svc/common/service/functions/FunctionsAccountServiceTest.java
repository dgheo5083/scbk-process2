package com.scbank.process.api.svc.common.service.functions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Req;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.svc.common.mapper.FunctionsAccountMapper;
import com.scbank.process.api.svc.common.service.functions.dto.account.FncAccListResponse;
import com.scbank.process.api.svc.shared.components.account.AccountListComponent;
import com.scbank.process.api.svc.shared.components.account.dto.AllAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.ListAccountHeldInfo;
import com.scbank.process.api.svc.shared.components.account.dto.OpenbankingAccountInfo;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("FunctionsAccountService")
class FunctionsAccountServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private IServiceContext ctx;

    @Mock
    private AccountListComponent accountListComponent;

    @Mock
    private FunctionsAccountMapper mapper;

    @InjectMocks
    private FunctionsAccountService service;

    @Nested
    @DisplayName("list")
    class List {

        @Test
        @DisplayName("계좌목록 조회 성공")
        void listTest() {
            List<AllAccountInfo> allAccounts = Collections.singletonList(new AllAccountInfo());
            List<AllAccountInfo> depositList = Collections.singletonList(new AllAccountInfo());
            List<AllAccountInfo> loanList = Collections.emptyList();
            List<OpenbankingAccountInfo> obsList = Collections.singletonList(new OpenbankingAccountInfo());

            when(accountListComponent.getAllAccountList()).thenReturn(allAccounts);
            when(mapper.toDepositAccountList(allAccounts)).thenReturn(depositList);
            when(mapper.toLoanAccountList(allAccounts)).thenReturn(loanList);
            when(accountListComponent.getOpenbankingAccountList()).thenReturn(obsList);

            FncAccListResponse response = service.list(ctx);

            assertThat(response.getDepositAccountList()).isEqualTo(depositList);
            assertThat(response.getLoanAccountList()).isEqualTo(loanList);
            assertThat(response.getObsAccountList()).isEqualTo(obsList);
        }
    }

    @Nested
    @DisplayName("listAccountHoldingsInquiry")
    class ListAccountHoldingsInquiry {

        @Test
        @DisplayName("보유계좌 조회 성공")
        void listAccountHoldingsInquiryTest() throws Exception {
            CbIbk01H86600Req request = new CbIbk01H86600Req();
            ListAccountHeldInfo heldInfo = new ListAccountHeldInfo();

            when(accountListComponent.getListAccountHeld(request)).thenReturn(heldInfo);

            ListAccountHeldInfo response = service.listAccountHoldingsInquiry(ctx, request);

            assertThat(response).isSameAs(heldInfo);
            verify(accountListComponent).getListAccountHeld(any(CbIbk01H86600Req.class));
        }
    }
}
