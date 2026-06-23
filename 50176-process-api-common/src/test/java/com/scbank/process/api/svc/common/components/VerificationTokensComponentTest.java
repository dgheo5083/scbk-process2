package com.scbank.process.api.svc.common.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("VerificationTokensComponent")
class VerificationTokensComponentTest {

    @Mock
    private ISessionContextManager sessionManager;

    @Mock
    private IpinsideComponent ipinsideService;

    private VerificationTokensComponent component;

    @BeforeEach
    void setUp() {
        component = new VerificationTokensComponent(sessionManager, ipinsideService);
        lenient().when(sessionManager.isLogin()).thenReturn(true);
    }

    @Nested
    @DisplayName("isMyAcctTran")
    class IsMyAcctTran {

        @Test
        @DisplayName("내계좌송금이면 true")
        void myAcctTranTest() {
            Map<String, Object> tranInfo = new HashMap<>();
            tranInfo.put("JgCode", "M");
            List<Map<String, Object>> tranInfoList = Collections.singletonList(tranInfo);

            when(sessionManager.getGlobalValue("addCertTranInfoList", List.class)).thenReturn(tranInfoList);

            assertTrue(component.isMyAcctTran("TRAN_IMMEDIATE"));
        }

        @Test
        @DisplayName("TRAN_ 타입이 아니면 false")
        void notTranTypeTest() {
            assertFalse(component.isMyAcctTran("CERT_LOGIN"));
        }

        @Test
        @DisplayName("JgCode가 M이 아니면 false")
        void notMyAcctTest() {
            Map<String, Object> tranInfo = new HashMap<>();
            tranInfo.put("JgCode", "O");
            when(sessionManager.getGlobalValue("addCertTranInfoList", List.class))
                    .thenReturn(Collections.singletonList(tranInfo));

            assertFalse(component.isMyAcctTran("TRAN_IMMEDIATE"));
        }
    }

    @Nested
    @DisplayName("isAdditional")
    class IsAdditional {

        @Test
        @DisplayName("additionalYn Y이면 true")
        void additionalYnYTest() {
            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                stubDefaultSession(session);
                assertTrue(component.isAdditional("Y", "TRAN_IMMEDIATE", 0));
            }
        }

        @Test
        @DisplayName("CERT_ 거래타입이면 추가인증 필수")
        void certTranTypeTest() {
            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                stubDefaultSession(session);
                assertTrue(component.isAdditional("", "CERT_CENTER", 0));
            }
        }

        @Test
        @DisplayName("이체금액이 한도 이상이면 추가인증")
        void tranAmountOverLimitTest() {
            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                stubDefaultSession(session);
                session.when(() -> SessionUtils.getSessionValue("PcFixValue")).thenReturn("0");
                session.when(() -> SessionUtils.getSessionValue("SafeCardKind")).thenReturn("0");
                when(sessionManager.isLogin()).thenReturn(true);
                session.when(() -> SessionUtils.getSessionValue("FlagIsAbroadYN", String.class)).thenReturn("N");

                assertTrue(component.isAdditional("", "TRAN_IMMEDIATE", 2000000));
            }
        }

        @Test
        @DisplayName("지정PC이면 추가인증 pass")
        void pcFixServiceTest() {
            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("ConnectType")).thenReturn("1");
                session.when(() -> SessionUtils.getSessionValue("PcFixValue", String.class)).thenReturn("2");
                session.when(() -> SessionUtils.getSessionValue("OtherPCYes", String.class)).thenReturn("N");

                assertFalse(component.isAdditional("", "OTHER_TYPE", 0));
            }
        }
    }

    @Nested
    @DisplayName("isPcFixService")
    class IsPcFixService {

        @Test
        @DisplayName("미지정PC거래 금지 시 예외")
        void pcFixNotAllowedTest() {
            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("PcFixValue", String.class)).thenReturn("1");
                session.when(() -> SessionUtils.getSessionValue("OtherPCYes", String.class)).thenReturn("N");

                assertThrows(PRCServiceException.class, () -> component.isPcFixService());
            }
        }

        @Test
        @DisplayName("지정PC이면 true")
        void designatedPcTest() {
            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("PcFixValue", String.class)).thenReturn("2");
                session.when(() -> SessionUtils.getSessionValue("OtherPCYes", String.class)).thenReturn("N");

                assertTrue(component.isPcFixService());
            }
        }
    }

    @Nested
    @DisplayName("getAbroadYn")
    class GetAbroadYn {

        @Test
        @DisplayName("로그인 상태이면 세션값 반환")
        void loginAbroadYnTest() {
            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                when(sessionManager.isLogin()).thenReturn(true);
                session.when(() -> SessionUtils.getSessionValue("FlagIsAbroadYN", String.class)).thenReturn("Y");

                assertEquals("Y", component.getAbroadYn());
            }
        }
    }

    private void stubDefaultSession(MockedStatic<SessionUtils> session) {
        session.when(() -> SessionUtils.getSessionValue("ConnectType")).thenReturn("1");
        session.when(() -> SessionUtils.getSessionValue("PcFixValue")).thenReturn("0");
        session.when(() -> SessionUtils.getSessionValue("SafeCardKind")).thenReturn("0");
        session.when(() -> SessionUtils.getSessionValue("PcFixValue", String.class)).thenReturn("0");
        session.when(() -> SessionUtils.getSessionValue("OtherPCYes", String.class)).thenReturn("N");
        session.when(() -> SessionUtils.getSessionValue("FlagIsAbroadYN", String.class)).thenReturn("N");
    }
}
