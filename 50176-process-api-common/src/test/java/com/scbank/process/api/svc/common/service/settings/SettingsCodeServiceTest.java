package com.scbank.process.api.svc.common.service.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.service.settings.dto.code.SetCodListBankCodeRequest;
import com.scbank.process.api.svc.common.service.settings.dto.code.SetCodListBankCodeResponse;
import com.scbank.process.api.svc.common.service.settings.dto.code.SetCodListRequest;
import com.scbank.process.api.svc.common.service.settings.dto.code.SetCodListResponse;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("SettingsCodeService")
class SettingsCodeServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private IServiceContext ctx;

    @Mock
    private ISessionContextManager sessionManager;

    @InjectMocks
    private SettingsCodeService service;

    @Nested
    @DisplayName("list")
    class List {

        @Test
        @DisplayName("코드목록 조회")
        void listTest() {
            SetCodListRequest request = new SetCodListRequest();
            request.setCategories(List.of("TEST_CAT"));

            ICodeItemInfo codeItem = mock(ICodeItemInfo.class);
            when(codeItem.getKey()).thenReturn("01");
            when(codeItem.getValue()).thenReturn("코드1");
            when(codeItem.getOrder()).thenReturn(1);

            try (MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
                codeUtils.when(() -> CodeUtils.getCodes("TEST_CAT")).thenReturn(new ArrayList<>(List.of(codeItem)));

                SetCodListResponse response = service.list(ctx, request);

                assertEquals(1, response.getCategoriesCount());
                assertEquals("TEST_CAT", response.getCategories().get(0).getCategory());
                assertEquals("01", response.getCategories().get(0).getCodes().get(0).getCode());
                assertEquals("코드1", response.getCategories().get(0).getCodes().get(0).getName());
            }
        }
    }

    @Nested
    @DisplayName("listBankCode")
    class ListBankCode {

        @Test
        @DisplayName("일반 은행코드 조회")
        void listBankCodeTest() {
            SetCodListBankCodeRequest request = new SetCodListBankCodeRequest();
            request.setTaxDisplayYn("Y");
            request.setScbankDisplayYn("Y");
            request.setCodeType("");

            when(sessionManager.getLoginValue("YODELAY", String.class)).thenReturn("N");

            ICodeItemInfo bankCode = mock(ICodeItemInfo.class);
            when(bankCode.getKey()).thenReturn("023");
            when(bankCode.getValue()).thenReturn("SC제일은행");
            when(bankCode.getOrder()).thenReturn(1);

            try (MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
                codeUtils.when(() -> CodeUtils.getCodes("BKCODE")).thenReturn(new ArrayList<>(List.of(bankCode)));

                SetCodListBankCodeResponse response = service.listBankCode(ctx, request);

                assertEquals(1, response.getCount());
                assertEquals("023", response.getCodes().get(0).getCode());
            }
        }

        @Test
        @DisplayName("obs 타입이면 url/fcode 포함")
        void listBankCodeObsTest() {
            SetCodListBankCodeRequest request = new SetCodListBankCodeRequest();
            request.setTaxDisplayYn("Y");
            request.setScbankDisplayYn("Y");
            request.setCodeType("obs");

            when(sessionManager.getLoginValue("YODELAY", String.class)).thenReturn("N");

            ICodeItemInfo bankCode = mock(ICodeItemInfo.class);
            when(bankCode.getKey()).thenReturn("088");
            when(bankCode.getValue()).thenReturn("신한은행;;https://obs.test;;F001");
            when(bankCode.getOrder()).thenReturn(1);

            try (MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
                codeUtils.when(() -> CodeUtils.getCodes("BKCODE_OBS")).thenReturn(new ArrayList<>(List.of(bankCode)));

                SetCodListBankCodeResponse response = service.listBankCode(ctx, request);

                assertEquals("신한은행", response.getCodes().get(0).getName());
                assertEquals("https://obs.test", response.getCodes().get(0).getUrl());
                assertEquals("F001", response.getCodes().get(0).getFcode());
            }
        }

        @Test
        @DisplayName("SC제일은행 미노출 필터")
        void listBankCodeHideScbankTest() {
            SetCodListBankCodeRequest request = new SetCodListBankCodeRequest();
            request.setTaxDisplayYn("Y");
            request.setScbankDisplayYn("N");
            request.setCodeType("");

            when(sessionManager.getLoginValue("YODELAY", String.class)).thenReturn("N");

            ICodeItemInfo scBank = mock(ICodeItemInfo.class);
            when(scBank.getKey()).thenReturn("023");
            when(scBank.getValue()).thenReturn("SC제일은행");
            when(scBank.getOrder()).thenReturn(1);

            ICodeItemInfo otherBank = mock(ICodeItemInfo.class);
            when(otherBank.getKey()).thenReturn("088");
            when(otherBank.getValue()).thenReturn("신한은행");
            when(otherBank.getOrder()).thenReturn(2);

            try (MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
                codeUtils.when(() -> CodeUtils.getCodes("BKCODE"))
                        .thenReturn(new ArrayList<>(List.of(scBank, otherBank)));

                SetCodListBankCodeResponse response = service.listBankCode(ctx, request);

                assertEquals(1, response.getCount());
                assertEquals("088", response.getCodes().get(0).getCode());
            }
        }

        @Test
        @DisplayName("지연이체 고객이면 국세/지방세 필터")
        void listBankCodeYoDelayTest() {
            SetCodListBankCodeRequest request = new SetCodListBankCodeRequest();
            request.setTaxDisplayYn("Y");
            request.setScbankDisplayYn("Y");
            request.setCodeType("");

            when(sessionManager.getLoginValue("YODELAY", String.class)).thenReturn("Y");

            ICodeItemInfo taxCode = mock(ICodeItemInfo.class);
            when(taxCode.getKey()).thenReturn("091");
            when(taxCode.getValue()).thenReturn("국세청");
            when(taxCode.getOrder()).thenReturn(1);

            ICodeItemInfo normalCode = mock(ICodeItemInfo.class);
            when(normalCode.getKey()).thenReturn("088");
            when(normalCode.getValue()).thenReturn("신한은행");
            when(normalCode.getOrder()).thenReturn(2);

            try (MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
                codeUtils.when(() -> CodeUtils.getCodes("BKCODE"))
                        .thenReturn(new ArrayList<>(List.of(taxCode, normalCode)));

                SetCodListBankCodeResponse response = service.listBankCode(ctx, request);

                assertEquals(1, response.getCount());
                assertFalse(response.getCodes().stream().anyMatch(c -> "091".equals(c.getCode())));
            }
        }
    }
}
