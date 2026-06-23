package com.scbank.process.api.svc.common.service.settings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92500Res;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.NoticeComponent;
import com.scbank.process.api.svc.common.dao.SmtPhoneBankingVsnMgtDao;
import com.scbank.process.api.svc.common.dao.dto.SmtPhoneBankingInfoResult;
import com.scbank.process.api.svc.common.mapper.SettingsAppMapper;
import com.scbank.process.api.svc.common.service.settings.dto.app.SetAppGetAccountViewYnRequest;
import com.scbank.process.api.svc.common.service.settings.dto.app.SetAppGetAccountViewYnResponse;
import com.scbank.process.api.svc.common.service.settings.dto.app.SetAppGetInitInfoResponse;
import com.scbank.process.api.svc.common.service.settings.dto.app.SetAppSendMalWareResultRequest;
import com.scbank.process.api.svc.common.service.settings.dto.app.SetAppSetGlobalInfoRequest;
import com.scbank.process.api.svc.common.service.settings.dto.emergency.SetEmgSearchResponse;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.E2EUtil;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("SettingsAppService")
class SettingsAppServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private IServiceContext ctx;

    @Mock
    private HostClient hostClient;

    @Mock
    private ISessionContextManager sessionManager;

    @Mock
    private NoticeComponent noticeComponent;

    @Mock
    private IpinsideComponent ipinsideComponent;

    @Mock
    private SmtPhoneBankingVsnMgtDao smtPhoneBankingVsnMgtDao;

    @Mock
    private SettingsAppMapper settingsAppMapper;

    @InjectMocks
    private SettingsAppService settingsAppService;

    @Nested
    @DisplayName("getInitInfo")
    class GetInitInfo {

        @Test
        @DisplayName("앱 초기화 정보 조회")
        void getInitInfoTest() {
            SetEmgSearchResponse emgResponse = new SetEmgSearchResponse();
            emgResponse.setNtcShowYn("Y");
            emgResponse.setNtcTitle("긴급공지");

            SmtPhoneBankingInfoResult iosVersion = new SmtPhoneBankingInfoResult();
            iosVersion.setVsn("1.0.0");
            SmtPhoneBankingInfoResult androidVersion = new SmtPhoneBankingInfoResult();
            androidVersion.setVsn("2.0.0");

            when(noticeComponent.getPmsNotice("Z04001")).thenReturn(emgResponse);
            when(smtPhoneBankingVsnMgtDao.selectLatestAppVersion("M4")).thenReturn(iosVersion);
            when(smtPhoneBankingVsnMgtDao.selectLatestAppVersion("M5")).thenReturn(androidVersion);

            Map<String, String> nKeyMap = new HashMap<>();
            nKeyMap.put(E2EUtil.NFILTER_PUBLICKEY, "public-key");

            try (MockedStatic<E2EUtil> e2e = mockStatic(E2EUtil.class);
                    MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
                e2e.when(() -> E2EUtil.getNkey(any())).thenReturn(nKeyMap);
                props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("https://test.com");
                props.when(() -> PropertiesUtils.getString("V3_MOBILE_DETECT_AI")).thenReturn("Y");
                props.when(() -> PropertiesUtils.getString("V3_MOBILE_DETECT_RC")).thenReturn("N");
                props.when(() -> PropertiesUtils.getString("V3_MOBILE_DETECT_TI")).thenReturn("N");
                props.when(() -> PropertiesUtils.getString("V3_MOBILE_REALTIME_AI")).thenReturn("N");
                props.when(() -> PropertiesUtils.getString("V3_MOBILE_REALTIME_RC")).thenReturn("N");

                SetAppGetInitInfoResponse response = settingsAppService.getInitInfo(ctx);

                assertThat(response.getNfilterPublicKey()).isEqualTo("public-key");
                assertThat(response.getNtcShowYn()).isEqualTo("Y");
                assertThat(response.getSvrIosVersion()).isEqualTo("1.0.0");
                assertThat(response.getSvrAndroidVersion()).isEqualTo("2.0.0");
                assertThat(response.getHomeMenuInfo()).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("getAccountViewYn")
    class GetAccountViewYn {

        @ParameterizedTest
        @ValueSource(strings = { "0", "" })
        @DisplayName("계좌노출여부")
        void shouldGetAccountViewYn(String acctNumType) {
            SetAppGetAccountViewYnRequest input = new SetAppGetAccountViewYnRequest();
            input.setAcctNumType(acctNumType);
            OltpResponse<CbIbk01H92500Res> hostResponse = new OltpResponse<>();
            SetAppGetAccountViewYnResponse response = new SetAppGetAccountViewYnResponse();
            OltpRequestOptions hostCfg = mock(OltpRequestOptions.class);
            when(hostClient.getOltpRequestOptions("CB_IBK01_H925", "", "")).thenReturn(hostCfg);
            when(hostClient.sendOltp(any(), any(), eq(CbIbk01H92500Res.class))).thenReturn(hostResponse);
            when(settingsAppMapper.toSetAppGetAccountViewYnResponse(hostResponse.getResponse())).thenReturn(response);

            SetAppGetAccountViewYnResponse actualValue = settingsAppService.getAccountViewYn(ctx, input);

            assertThat(actualValue).isNotNull();
        }
    }

    @Nested
    @DisplayName("setGloblaInfo")
    class SetGlobalInfo {

        @Test
        @DisplayName("게이트웨어 세션정보 저장")
        void setGlobalInfoTest() {
            SetAppSetGlobalInfoRequest request = new SetAppSetGlobalInfoRequest();
            request.setCnnCtnWay("PARTNER01");
            request.setClerkNo("12345");
            request.setReaCdn("REA01");

            settingsAppService.setGloblaInfo(ctx, request);

            verify(sessionManager).setGlobalValue("CNNCTN_WAY", "PARTNER01");
            verify(sessionManager).setGlobalValue("CLERK_NO", "12345");
            verify(sessionManager).setGlobalValue("REACDN", "REA01");
        }
    }

    @Nested
    @DisplayName("sendMalWareResult")
    class SendMalWareResult {

        @Test
        @DisplayName("iOS이면 V3 복호화 생략")
        void sendMalWareResultIosTest() {
            SetAppSendMalWareResultRequest request = new SetAppSendMalWareResultRequest();
            request.setIosYn("Y");
            request.setV3EncryptData("");
            request.setIpinsideEncryptData("invalid");

            try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
                props.when(() -> PropertiesUtils.getString("V3_MOBILE_SYMMETRIC_KEY")).thenReturn("1234567890123456");

                settingsAppService.sendMalWareResult(ctx, request);
            }
        }
    }
}
