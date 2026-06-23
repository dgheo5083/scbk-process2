package com.scbank.process.api.svc.shared.components.sign;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;
import com.scbank.process.api.fw.base.store.secure.vo.SignInfo;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.cert.CertVerifyComponent;
import com.scbank.process.api.svc.shared.components.cert.FinCertVerifyComponent;
import com.scbank.process.api.svc.shared.components.cert.FinTechCertVerifyComponent;
import com.scbank.process.api.svc.shared.components.fido.FidoHttpComponent;
import com.scbank.process.api.svc.shared.components.sign.constants.SignConstants;
import com.scbank.process.api.svc.shared.components.sign.dto.SignVerifyInfo;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignVerifyType;
import com.scbank.process.api.svc.shared.components.sign.utils.SignUtils;
import com.scbank.process.api.svc.shared.dao.SignTransactionDao;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class SignComponentTest {

	@Mock private ISessionContextManager sessionManager;
	@Mock private CertVerifyComponent certVerifyComponent;
	@Mock private FidoHttpComponent fidoHttpComponent;
	@Mock private FinCertVerifyComponent finCertVerifyComponent;
	@Mock private FinTechCertVerifyComponent finTechCertVerifyComponent;
	@Mock private SignTransactionDao signTransactionDao;

	@InjectMocks private SignComponent component;

	private SignVerifyInfo info(SignVerifyType type) {
		SignVerifyInfo info = new SignVerifyInfo();
		info.setSignVerifyType(type);
		return info;
	}

	@Nested
	@DisplayName("verifySign(SignVerifyInfo) - 요청 유효성")
	class VerifySignValidation {

		@Test
		@DisplayName("실패_signVerifyInfo null - PRCCMM0000")
		public void nullInfoTest() {
			assertThrows(PRCServiceException.class, () -> component.verifySign((SignVerifyInfo) null));
		}

		@Test
		@DisplayName("실패_signVerifyType null - PRCCMM0000")
		public void nullTypeTest() {
			assertThrows(PRCServiceException.class, () -> component.verifySign(info(null)));
		}

		@Test
		@DisplayName("NONE 타입은 비대상으로 통과")
		public void noneTypeTest() {
			assertDoesNotThrow(() -> component.verifySign(info(SignVerifyType.NONE)));
		}

		@Test
		@DisplayName("실패_messageMap/serviceRequest 모두 없음 - PRCCMM0000")
		public void noPayloadTest() {
			assertThrows(PRCServiceException.class, () -> component.verifySign(info(SignVerifyType.VERIFY)));
		}
	}

	@Nested
	@DisplayName("verify - secureContext 분기")
	class VerifySecureContext {

		@Test
		@DisplayName("secureContext 없음 + 비로컬 - PRCCMM0000")
		public void emptySecureContextNotLocalTest() {
			SignVerifyInfo info = info(SignVerifyType.VERIFY);

			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class);
				 MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				holder.when(ServiceContextHolder::getContext).thenReturn(mock(IServiceContext.class));
				store.when(SecureContextStore::getContext).thenReturn(Optional.empty());
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.SIT);

				assertThrows(PRCServiceException.class, () -> component.verifySign(mock(IMessageObject.class), info));
			}
		}

		@Test
		@DisplayName("secureContext 없음 + 로컬 - 통과")
		public void emptySecureContextLocalTest() {
			SignVerifyInfo info = info(SignVerifyType.VERIFY);

			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class);
				 MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				holder.when(ServiceContextHolder::getContext).thenReturn(mock(IServiceContext.class));
				store.when(SecureContextStore::getContext).thenReturn(Optional.empty());
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

				assertDoesNotThrow(() -> component.verifySign(mock(IMessageObject.class), info));
			}
		}

		@Test
		@DisplayName("messageMap 분기 - 로컬 + secureContext 없음 통과")
		public void messageMapBranchTest() {
			SignVerifyInfo info = info(SignVerifyType.VERIFY);
			HashMap<String, String> map = new HashMap<>();
			map.put("acctNo", "110");
			info.setMessageMap(map);

			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class);
				 MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				holder.when(ServiceContextHolder::getContext).thenReturn(mock(IServiceContext.class));
				store.when(SecureContextStore::getContext).thenReturn(Optional.empty());
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

				assertDoesNotThrow(() -> component.verifySign(info));
			}
		}
	}

	@Nested
	@DisplayName("verify - 서명 스킵/검증 경로")
	class VerifyFlow {

		private SecureContext secureContextWithSign(String signResultCode) {
			SecureContext secureContext = mock(SecureContext.class);
			SignInfo signInfo = mock(SignInfo.class);
			when(signInfo.getSignResultCode()).thenReturn(signResultCode);
			when(secureContext.getSign()).thenReturn(signInfo);
			return secureContext;
		}

		@Test
		@DisplayName("MOTP 간편로그인 서명 스킵")
		public void motpSkipTest() {
			SignVerifyInfo info = info(SignVerifyType.VERIFY);
			SecureContext secureContext = secureContextWithSign(SignConstants.SIGN_SKIP_RESULT_CODE);

			when(sessionManager.isLogin()).thenReturn(true);
			when(sessionManager.getLoginValue("ConnectType", String.class)).thenReturn("G");
			when(sessionManager.getGlobalValue("motpSignSkipYn", String.class)).thenReturn("Y");

			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class);
				 MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				holder.when(ServiceContextHolder::getContext).thenReturn(mock(IServiceContext.class));
				store.when(SecureContextStore::getContext).thenReturn(Optional.of(secureContext));
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.SIT);

				assertDoesNotThrow(() -> component.verifySign(mock(IMessageObject.class), info));
			}
		}

		@Test
		@DisplayName("FIDO(디지털/금융생체) 간편로그인 서명 스킵")
		public void fidoSkipTest() {
			SignVerifyInfo info = info(SignVerifyType.VERIFY);
			SecureContext secureContext = secureContextWithSign(SignConstants.SIGN_SKIP_RESULT_CODE);

			when(sessionManager.isLogin()).thenReturn(true);
			when(sessionManager.getLoginValue("ConnectType", String.class)).thenReturn("9");
			when(sessionManager.getGlobalValue("fidoSignSkipYn", String.class)).thenReturn("Y");

			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class);
				 MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				holder.when(ServiceContextHolder::getContext).thenReturn(mock(IServiceContext.class));
				store.when(SecureContextStore::getContext).thenReturn(Optional.of(secureContext));
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.SIT);

				assertDoesNotThrow(() -> component.verifySign(mock(IMessageObject.class), info));
			}
		}

		@Test
		@DisplayName("로컬/개발환경 서명 스킵")
		public void localSkipTest() {
			SignVerifyInfo info = info(SignVerifyType.VERIFY);
			SecureContext secureContext = secureContextWithSign(SignConstants.SIGN_SKIP_RESULT_CODE);

			when(sessionManager.isLogin()).thenReturn(false);

			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class);
				 MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
				 MockedStatic<com.scbank.process.api.fw.core.utils.ReflectionUtils> reflection =
						 mockStatic(com.scbank.process.api.fw.core.utils.ReflectionUtils.class)) {
				holder.when(ServiceContextHolder::getContext).thenReturn(mock(IServiceContext.class));
				store.when(SecureContextStore::getContext).thenReturn(Optional.of(secureContext));
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);
				reflection.when(() -> com.scbank.process.api.fw.core.utils.ReflectionUtils.getFieldValue(any(), anyString()))
						.thenReturn(null);

				assertDoesNotThrow(() -> component.verifySign(mock(IMessageObject.class), info));
			}
		}

		@Test
		@DisplayName("성공_검증완료(Y) 상태에서 데이터 비교 후 저장 비대상(VERIFY) 통과")
		public void verifyCompareSuccessTest() {
			SignVerifyInfo info = info(SignVerifyType.VERIFY);
			SecureContext secureContext = secureContextWithSign("0000");

			IServiceContext sCtx = mock(IServiceContext.class);
			when(sCtx.attribute(eq(SignConstants.SIGN_VERIFY_COMPLETE_YN), eq(String.class))).thenReturn("Y");
			when(sCtx.attribute(eq(SignConstants.SIGN_COMPARE_VERIFY_COUNT_KEY), eq(Integer.class))).thenReturn(null);

			when(sessionManager.isLogin()).thenReturn(false);

			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class);
				 MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
				 MockedStatic<SignUtils> signUtils = mockStatic(SignUtils.class);
				 MockedStatic<com.scbank.process.api.fw.core.utils.ReflectionUtils> reflection =
						 mockStatic(com.scbank.process.api.fw.core.utils.ReflectionUtils.class)) {
				holder.when(ServiceContextHolder::getContext).thenReturn(sCtx);
				store.when(SecureContextStore::getContext).thenReturn(Optional.of(secureContext));
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.SIT);
				reflection.when(() -> com.scbank.process.api.fw.core.utils.ReflectionUtils.getFieldValue(any(), anyString()))
						.thenReturn(null);
				signUtils.when(() -> SignUtils.getCompareMapFromOrgString(anyString()))
						.thenReturn(new HashMap<String, List<String>>());
				signUtils.when(() -> SignUtils.getCompareMapFromObject(any(), any()))
						.thenReturn(new HashMap<String, List<String>>());
				signUtils.when(() -> SignUtils.getTotalSignDataCount(any())).thenReturn(0);

				assertDoesNotThrow(() -> component.verifySign(mock(IMessageObject.class), info));
			}
		}
	}
}
