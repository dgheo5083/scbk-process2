package com.scbank.process.api.svc.common.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;

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

import com.scbank.process.api.edmi.dto.mci.MciIbCidi001Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Res;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.dto.VerificationGetFindUserIdDto;
import com.scbank.process.api.svc.common.components.dto.VerificationGetTelNoDto;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.dao.DeviceAuthUserDao;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("VerificationAdditionalComponent")
class VerificationAdditionalComponentTest {

    @Mock
    private HostClient hostClient;

    @Mock
    private ISessionContextManager sessionManager;

    @Mock
    private IpinsideComponent ipinside;

    @Mock
    private DeviceAuthUserDao deviceAuthUserDao;

    @Mock
    private IpinsideComponent ipinsideComponent;

    private VerificationAdditionalComponent component;

    @BeforeEach
    void setUp() {
        component = new VerificationAdditionalComponent(
                hostClient,
                sessionManager,
                ipinside,
                deviceAuthUserDao,
                ipinsideComponent);

        lenient().when(sessionManager.getGlobalValue(anyString(), eq(String.class))).thenReturn(null);
    }

    @Nested
    @DisplayName("getAdditionalType")
    class GetAdditionalType {

        @Test
        @DisplayName("additionalType 미입력 시 기본값 A:B 반환")
        void defaultAdditionalTypeTest() {
            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class)) {
                shared.when(PRCSharedUtils::isSB).thenReturn(true);
                shared.when(PRCSharedUtils::isIB).thenReturn(false);

                String result = component.getAdditionalType(null, "LOGIN");

                assertEquals("A:B", result);
            }
        }

        @Test
        @DisplayName("SB 앱이 아니면 간편인증 A 제외")
        void removeSimpleAuthWhenNotSbTest() {
            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class)) {
                shared.when(PRCSharedUtils::isSB).thenReturn(false);
                shared.when(PRCSharedUtils::isIB).thenReturn(false);

                String result = component.getAdditionalType("A:B:D", "LOGIN");

                assertEquals(":B:D", result);
            }
        }

        @Test
        @DisplayName("해외출국 인증 F 포함 + 해외 아님이면 F 제외")
        void removeAbroadAuthWhenNotAbroadTest() {
            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class)) {
                shared.when(PRCSharedUtils::isSB).thenReturn(true);
                shared.when(PRCSharedUtils::isIB).thenReturn(false);
                when(ipinside.getAbroadYn()).thenReturn("N");

                String result = component.getAdditionalType("A:B:F", "LOGIN");

                assertEquals("A:B:", result);
            }
        }
    }

    @Nested
    @DisplayName("getFindUserIdInfo")
    class GetFindUserIdInfo {

        @Test
        @DisplayName("사용자ID찾기 전화번호 세션이 있으면 DTO 세팅")
        void findUserIdInfoExistsTest() {
            when(sessionManager.getGlobalValue("FIND_USER_ID_TRCD", String.class)).thenReturn("TR001");
            when(sessionManager.getGlobalValue("FIND_USER_ID_TEL1", String.class)).thenReturn("010");
            when(sessionManager.getGlobalValue("FIND_USER_ID_TEL2", String.class)).thenReturn("1234");
            when(sessionManager.getGlobalValue("FIND_USER_ID_TEL3", String.class)).thenReturn("5678");

            VerificationGetFindUserIdDto result = component.getFindUserIdInfo();

            assertEquals("Y", result.getFindUserIdYn());
            assertEquals("TR001", result.getWasTranNo());
            assertEquals("01012345678", result.getPhoneNo());
            assertEquals("010-1234-****", result.getMaskPhoneNo());
        }

        @Test
        @DisplayName("사용자ID찾기 전화번호 세션이 없으면 빈 DTO 반환")
        void findUserIdInfoNotExistsTest() {
            VerificationGetFindUserIdDto result = component.getFindUserIdInfo();

            assertNull(result.getFindUserIdYn());
            assertNull(result.getPhoneNo());
        }
    }

    @Nested
    @DisplayName("getGuestUserInfo")
    class GetGuestUserInfo {

        @Test
        @DisplayName("비로그인 전화번호 세션이 있으면 guestUserYn Y")
        void guestUserInfoExistsTest() {
            when(sessionManager.getGlobalValue("GUEST_USER_TRCD", String.class)).thenReturn("GUEST_TR");
            when(sessionManager.getGlobalValue("GUEST_USER_TEL1", String.class)).thenReturn("010");
            when(sessionManager.getGlobalValue("GUEST_USER_TEL2", String.class)).thenReturn("2222");
            when(sessionManager.getGlobalValue("GUEST_USER_TEL3", String.class)).thenReturn("3333");

            HashMap<String, String> result = component.getGuestUserInfo();

            assertEquals("Y", result.get("guestUserYn"));
            assertEquals("GUEST_TR", result.get("trCd"));
            assertEquals("01022223333", result.get("phoneNo"));
            assertEquals("010-2222-****", result.get("maskPhoneNo"));
        }

        @Test
        @DisplayName("비로그인 전화번호 세션이 없으면 guestUserYn N")
        void guestUserInfoNotExistsTest() {
            HashMap<String, String> result = component.getGuestUserInfo();

            assertEquals("N", result.get("guestUserYn"));
        }
    }

    @Nested
    @DisplayName("getTelNo")
    class GetTelNo {

        @Test
        @DisplayName("사용자ID찾기 정보가 우선 적용")
        void getTelNoFromFindUserIdInfoTest() {
            when(sessionManager.getGlobalValue("FIND_USER_ID_TRCD", String.class)).thenReturn("TR_FIND");
            when(sessionManager.getGlobalValue("FIND_USER_ID_TEL1", String.class)).thenReturn("010");
            when(sessionManager.getGlobalValue("FIND_USER_ID_TEL2", String.class)).thenReturn("1111");
            when(sessionManager.getGlobalValue("FIND_USER_ID_TEL3", String.class)).thenReturn("2222");

            VerificationGetTelNoDto result = component.getTelNo();

            assertEquals("TR_FIND", result.getWasTranNo());
            assertEquals("01011112222", result.getPhoneNo());
            assertEquals("010-1111-****", result.getMaskPhoneNo());
            verifyNoInteractions(hostClient);
        }

        @Test
        @DisplayName("비로그인 사용자 정보가 두 번째로 적용")
        void getTelNoFromGuestUserInfoTest() {
            when(sessionManager.getGlobalValue("GUEST_USER_TRCD", String.class)).thenReturn("TR_GUEST");
            when(sessionManager.getGlobalValue("GUEST_USER_TEL1", String.class)).thenReturn("010");
            when(sessionManager.getGlobalValue("GUEST_USER_TEL2", String.class)).thenReturn("3333");
            when(sessionManager.getGlobalValue("GUEST_USER_TEL3", String.class)).thenReturn("4444");

            VerificationGetTelNoDto result = component.getTelNo();

            assertEquals("TR_GUEST", result.getWasTranNo());
            assertEquals("01033334444", result.getPhoneNo());
            assertEquals("010-3333-****", result.getMaskPhoneNo());
            verifyNoInteractions(hostClient);
        }

        @Test
        @DisplayName("로그인 상태이면 H920 전문 조회 결과로 전화번호 세팅")
        void getTelNoFromHostWhenLoginTest() {
            when(sessionManager.isLogin()).thenReturn(true);

            OltpRequestOptions options = new OltpRequestOptions();
            when(hostClient.getOltpRequestOptions("CB_IBK01_H920")).thenReturn(options);

            CbIbk01H92000Res body = new CbIbk01H92000Res();
            body.setHandPhone1("010");
            body.setHandPhone2("5555");
            body.setHandPhone3("6666");
            body.setHomeTele1("02");
            body.setHomeTele2("7777");
            body.setHomeTele3("8888");
            body.setJobTele1("031");
            body.setJobTele2("9999");
            body.setJobTele3("0000");

            OltpCommon common = new OltpCommon();
            common.setTrCd("TR_HOST");
            OltpResHeader header = new OltpResHeader();
            header.setOltpCommon(common);

            OltpResponse<CbIbk01H92000Res> hostResponse = new OltpResponse<>();
            hostResponse.setHeader(header);
            hostResponse.setResponse(body);

            doReturn(hostResponse).when(hostClient)
                    .sendOltp(eq(options), any(CbIbk01H92000Req.class), eq(CbIbk01H92000Res.class));

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("UserID")).thenReturn("USER01");
                session.when(() -> SessionUtils.getSessionValue("TSPassword")).thenReturn("PWD");

                VerificationGetTelNoDto result = component.getTelNo();

                assertEquals("TR_HOST", result.getWasTranNo());
                assertEquals("01055556666", result.getPhoneNo());
                assertEquals("010-5555-****", result.getMaskPhoneNo());
                assertEquals("0277778888", result.getHomeTelNo());
                assertEquals("02-7777-****", result.getMaskHomeTelNo());
                assertEquals("03199990000", result.getJobTelNo());
                assertEquals("031-9999-****", result.getMaskJobTelNo());
            }
        }

        @Test
        @DisplayName("비로그인이나 사용자/비밀번호/주민번호 세션이 있으면 H920 전문 조회")
        void getTelNoFromHostWhenNotLoginButHasSessionValuesTest() {
            when(sessionManager.isLogin()).thenReturn(false);
            when(sessionManager.getGlobalValue("UserID", String.class)).thenReturn("USER01");
            when(sessionManager.getGlobalValue("TSPassword", String.class)).thenReturn("PWD");
            when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("9001011234567");

            OltpRequestOptions options = new OltpRequestOptions();
            when(hostClient.getOltpRequestOptions("CB_IBK01_H920")).thenReturn(options);

            CbIbk01H92000Res body = new CbIbk01H92000Res();
            body.setHandPhone1("010");
            body.setHandPhone2("1212");
            body.setHandPhone3("3434");
            body.setHomeTele1("02");
            body.setHomeTele2("0000");
            body.setHomeTele3("1111");
            body.setJobTele1("031");
            body.setJobTele2("2222");
            body.setJobTele3("3333");

            OltpCommon common = new OltpCommon();
            common.setTrCd("TR_NON_LOGIN");
            OltpResHeader header = new OltpResHeader();
            header.setOltpCommon(common);

            OltpResponse<CbIbk01H92000Res> hostResponse = new OltpResponse<>();
            hostResponse.setHeader(header);
            hostResponse.setResponse(body);

            doReturn(hostResponse).when(hostClient)
                    .sendOltp(eq(options), any(CbIbk01H92000Req.class), eq(CbIbk01H92000Res.class));

            VerificationGetTelNoDto result = component.getTelNo();

            assertEquals("TR_NON_LOGIN", result.getWasTranNo());
            assertEquals("01012123434", result.getPhoneNo());
        }
    }

    private MciResponse<MciIbCidi001Res> mciResponse(
            String confirm,
            String status,
            String cif,
            String smno,
            String name,
            String ci) {
        MciIbCidi001Res body = new MciIbCidi001Res();
        body.setAOCONFIRM(confirm);
        body.setAOSTSGB(status);
        body.setAOCIFNO(cif);
        body.setAOSMNO(smno);
        body.setAOCMFNA(name);
        body.setAOCINOINF(ci);

        MciResponse<MciIbCidi001Res> response = new MciResponse<>();
        response.setResponse(body);
        return response;
    }
}
