package com.scbank.process.api.svc.common.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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

import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.components.ipinside.dto.DFinderApiInfo;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("VerificationFDSComponent")
class VerificationFDSComponentTest {

    @Mock
    private ISessionContextManager sessionManager;

    @Mock
    private IpinsideComponent ipinside;

    private VerificationFDSComponent component;

    @BeforeEach
    void setUp() {
        component = new VerificationFDSComponent(sessionManager, ipinside);
        lenient().when(sessionManager.getGlobalValue(anyString(), eq(String.class))).thenReturn(null);
    }

    private void initWithVfdsSwitch(String vfdsSwYn) {
        try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
            props.when(() -> PropertiesUtils.getString("VFDS_SW_YN")).thenReturn(vfdsSwYn);
            component.init();
        }
    }

    private void stubDFinderApi(String rspActnMethCd) {
        DFinderApiInfo info = new DFinderApiInfo();
        info.setRspActnMethCd(rspActnMethCd);
        when(ipinside.dFinderApi(eq("USER01"), eq("R"))).thenReturn(info);
    }

    private void stubDFinderApiNull() {
        when(ipinside.dFinderApi(eq("USER01"), eq("R"))).thenReturn(null);
    }

    private void triggerInitialization() {
        try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
            session.when(() -> SessionUtils.getSessionValue("UserID")).thenReturn("USER01");
            component.getRspActnMethCd();
        }
    }

    @Nested
    @DisplayName("init")
    class Init {

        @Test
        @DisplayName("VFDS_SW_YN 프로퍼티 로드")
        void initLoadsVfdsSwitchTest() {
            initWithVfdsSwitch("Y");
            stubDFinderApi("03");
            triggerInitialization();
            assertEquals("03", component.getRspActnMethCd());
        }
    }

    @Nested
    @DisplayName("initialized")
    class Initialized {

        @Test
        @DisplayName("VFDS 스위치 N이면 dFinderApi 미호출")
        void skipDFinderWhenVfdsOffTest() {
            initWithVfdsSwitch("N");
            triggerInitialization();
            verifyNoInteractions(ipinside);
        }

        @Test
        @DisplayName("VFDS 스위치 Y이면 dFinderApi 호출")
        void callDFinderWhenVfdsOnTest() {
            initWithVfdsSwitch("Y");
            stubDFinderApi("03");
            triggerInitialization();
            verify(ipinside).dFinderApi("USER01", "R");
        }

        @Test
        @DisplayName("dFinderApi 응답 null이면 기본 상태코드 유지")
        void keepDefaultWhenDFinderReturnsNullTest() {
            initWithVfdsSwitch("Y");
            stubDFinderApiNull();
            triggerInitialization();
            assertEquals("00", component.getRspActnMethCd());
        }

        @Test
        @DisplayName("이미 초기화된 경우 dFinderApi 재호출 안함")
        void initializedOnlyOnceTest() {
            initWithVfdsSwitch("Y");
            stubDFinderApi("03");
            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("UserID")).thenReturn("USER01");
                component.isWdrawFreeze();
                component.isTranBlock();
            }
            verify(ipinside, times(1)).dFinderApi("USER01", "R");
        }
    }

    @Nested
    @DisplayName("isWdrawFreeze")
    class IsWdrawFreeze {

        @Test
        @DisplayName("VFDS Y이고 응답코드 03이면 true")
        void freezeWhenCode03Test() {
            initWithVfdsSwitch("Y");
            stubDFinderApi("03");
            triggerInitialization();
            assertTrue(component.isWdrawFreeze());
        }

        @Test
        @DisplayName("VFDS N이면 false")
        void noFreezeWhenVfdsOffTest() {
            initWithVfdsSwitch("N");
            triggerInitialization();
            assertFalse(component.isWdrawFreeze());
        }

        @Test
        @DisplayName("응답코드 03이 아니면 false")
        void noFreezeWhenCodeNot03Test() {
            initWithVfdsSwitch("Y");
            stubDFinderApi("04");
            triggerInitialization();
            assertFalse(component.isWdrawFreeze());
        }
    }

    @Nested
    @DisplayName("isTranBlock")
    class IsTranBlock {

        @Test
        @DisplayName("VFDS Y이고 응답코드 04이면 true")
        void blockWhenCode04Test() {
            initWithVfdsSwitch("Y");
            stubDFinderApi("04");
            triggerInitialization();
            assertTrue(component.isTranBlock());
        }

        @Test
        @DisplayName("VFDS N이면 false")
        void noBlockWhenVfdsOffTest() {
            initWithVfdsSwitch("N");
            triggerInitialization();
            assertFalse(component.isTranBlock());
        }

        @Test
        @DisplayName("응답코드 04가 아니면 false")
        void noBlockWhenCodeNot04Test() {
            initWithVfdsSwitch("Y");
            stubDFinderApi("03");
            triggerInitialization();
            assertFalse(component.isTranBlock());
        }
    }

    @Nested
    @DisplayName("isIDCapture")
    class IsIDCapture {

        @Test
        @DisplayName("조건 미충족 시 false")
        void noCaptureWhenConditionsNotMetTest() {
            initWithVfdsSwitch("N");
            triggerInitialization();
            assertFalse(component.isIDCapture());
        }

        @Test
        @DisplayName("출금정지+이체거래이고 인증 미완료 시 true")
        void captureWhenAuthIncompleteTest() {
            initWithVfdsSwitch("Y");
            stubDFinderApi("06");
            when(sessionManager.getGlobalValue("FDS_SERVICE_CODE", String.class)).thenReturn("211");
            when(sessionManager.getGlobalValue("ADD_AUTH_ID_CAPTURE_FLAG", String.class)).thenReturn("N");

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("UserID")).thenReturn("USER01");
                session.when(() -> SessionUtils.getSessionValue("idRecogResultYn")).thenReturn(null);
                session.when(() -> SessionUtils.getSessionValue("idTruthResultYn")).thenReturn(null);
                session.when(() -> SessionUtils.getSessionValue("faceResultYn")).thenReturn(null);

                assertTrue(component.isIDCapture());
            }

            verify(sessionManager).removeGlobalValue("idRecogResultYn");
            verify(sessionManager).removeGlobalValue("idTruthResultYn");
            verify(sessionManager).removeGlobalValue("faceResultYn");
            verify(sessionManager).removeGlobalValue("videoResultYn");
        }

        @Test
        @DisplayName("비대면실명인증 완료 시 false 및 성공 플래그 설정")
        void noCaptureWhenAuthCompleteTest() {
            initWithVfdsSwitch("Y");
            stubDFinderApi("06");
            when(sessionManager.getGlobalValue("FDS_SERVICE_CODE", String.class)).thenReturn("211");
            when(sessionManager.getGlobalValue("ADD_AUTH_ID_CAPTURE_FLAG", String.class)).thenReturn("Y");

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("UserID")).thenReturn("USER01");
                session.when(() -> SessionUtils.getSessionValue("idRecogResultYn")).thenReturn("Y");
                session.when(() -> SessionUtils.getSessionValue("idTruthResultYn")).thenReturn("Y");
                session.when(() -> SessionUtils.getSessionValue("faceResultYn")).thenReturn("Y");

                assertFalse(component.isIDCapture());
            }

            verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
            verify(sessionManager).removeGlobalValue("ADD_AUTH_ID_CAPTURE_FLAG");
        }

        @Test
        @DisplayName("fdsServiceCode가 211이 아니면 false")
        void noCaptureWhenServiceCodeNot211Test() {
            initWithVfdsSwitch("Y");
            stubDFinderApi("06");
            when(sessionManager.getGlobalValue("FDS_SERVICE_CODE", String.class)).thenReturn("999");
            triggerInitialization();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("idRecogResultYn")).thenReturn("N");
                session.when(() -> SessionUtils.getSessionValue("idTruthResultYn")).thenReturn("N");
                session.when(() -> SessionUtils.getSessionValue("faceResultYn")).thenReturn("N");
                assertFalse(component.isIDCapture());
            }
        }
    }

    @Nested
    @DisplayName("getRspActnMethCd")
    class GetRspActnMethCd {

        @Test
        @DisplayName("dFinderApi 응답 상태코드 반환")
        void returnsRspActnMethCdTest() {
            initWithVfdsSwitch("Y");
            stubDFinderApi("05");
            triggerInitialization();
            assertEquals("05", component.getRspActnMethCd());
        }
    }
}
