package com.scbank.process.api.svc.shared.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.integration.system.edmi.EdmiManager;
import com.scbank.process.api.fw.base.integration.system.edmi.EdmiRequestOptions;
import com.scbank.process.api.fw.base.integration.system.edmi.EdmiResponse;
import com.scbank.process.api.fw.base.integration.system.mci.MciManager;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequestOptions;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpManager;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.store.ThreadLocalStoreDelegator;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.svc.shared.components.auth.SecureDataComponent;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class HostClientTest {

//	@Mock private OltpManager hostManager;
//	@Mock private EdmiManager edmiManager;
//	@Mock private MciManager mciManager;
//	@Mock private SecureDataComponent secureData;
//
//	@InjectMocks private HostClient hostClient;
//
//	private MockedStatic<ThreadLocalStoreDelegator> store;
//
//	@BeforeEach
//	void setUp() {
//		store = Mockito.mockStatic(ThreadLocalStoreDelegator.class);
//		store.when(ThreadLocalStoreDelegator::getTrackingId).thenReturn("TR0001");
//	}
//
//	@org.junit.jupiter.api.AfterEach
//	void tearDown() {
//		store.close();
//	}
//
//	@Nested
//	@DisplayName("RequestOptions 생성")
//	class RequestOptions {
//
//		@Test
//		@DisplayName("OLTP 옵션 (단일 인자 / 3 인자)")
//		public void oltpOptionsTest() {
//			assertNotNull(hostClient.getOltpRequestOptions("IF001"));
//			assertNotNull(hostClient.getOltpRequestOptions("IF001", "OLTP", "TI1IBK01"));
//		}
//
//		@Test
//		@DisplayName("EDMI / MCI 옵션")
//		public void edmiMciOptionsTest() {
//			assertNotNull(hostClient.getEdmiRequestOptions("IF002"));
//			assertNotNull(hostClient.getMciRequestOptions("IF003"));
//		}
//	}
//
//	@Nested
//	@DisplayName("전문 송수신")
//	class Send {
//
//		@SuppressWarnings("unchecked")
//		@Test
//		@DisplayName("sendOltp / sendOltpWithSecure")
//		public void oltpSendTest() {
//			OltpRequestOptions cfg = mock(OltpRequestOptions.class);
//			IMessageObject input = mock(IMessageObject.class);
//			OltpResponse<IMessageObject> response = mock(OltpResponse.class);
//			when(hostManager.send(any(), any(), any())).thenReturn(response);
//
//			assertNotNull(hostClient.sendOltp(cfg, input, IMessageObject.class));
//			assertNotNull(hostClient.sendOltpWithSecure(cfg, input, IMessageObject.class));
//			verify(secureData).verifyVerification(any());
//		}
//
////		@SuppressWarnings("unchecked")
////		@Test
////		@DisplayName("sendOltpWithRebound (기본/전략지정/null전략)")
////		public void oltpReboundTest() {
////			OltpRequestOptions cfg = mock(OltpRequestOptions.class);
////			IMessageObject input = mock(IMessageObject.class);
////			OltpResponse<IMessageObject> response = mock(OltpResponse.class);
////			when(hostManager.sendWithRebound(any(), any(), any(), any())).thenReturn(response);
////
////			//assertNotNull(hostClient.sendOltpWithRebound(cfg, input, IMessageObject.class));
////			//assertNotNull(hostClient.sendOltpWithRebound(cfg, input, IMessageObject.class, null));
////		}
//
//		@SuppressWarnings("unchecked")
//		@Test
//		@DisplayName("sendEdmi")
//		public void edmiSendTest() {
//			EdmiRequestOptions cfg = mock(EdmiRequestOptions.class);
//			IMessageObject input = mock(IMessageObject.class);
//			EdmiResponse<IMessageObject> response = mock(EdmiResponse.class);
//			when(edmiManager.send(any(), any(), any())).thenReturn(response);
//
//			assertNotNull(hostClient.sendEdmi(cfg, input, IMessageObject.class));
//		}
//
//		@SuppressWarnings("unchecked")
//		@Test
//		@DisplayName("sendMci / sendMciWithSecure / sendMciWithRebound")
//		public void mciSendTest() {
//			MciRequestOptions cfg = mock(MciRequestOptions.class);
//			IMessageObject input = mock(IMessageObject.class);
//			MciResponse<IMessageObject> response = mock(MciResponse.class);
//			when(mciManager.send(any(), any(), any())).thenReturn(response);
//			when(mciManager.sendWithRebound(any(), any(), any(), any())).thenReturn(response);
//
//			assertNotNull(hostClient.sendMci(cfg, input, IMessageObject.class));
//			assertNotNull(hostClient.sendMciWithSecure(cfg, input, IMessageObject.class));
//			//assertNotNull(hostClient.sendMciWithRebound(cfg, input, IMessageObject.class));
//			//assertNotNull(hostClient.sendMciWithRebound(cfg, input, IMessageObject.class, null));
//		}
//	}
}
