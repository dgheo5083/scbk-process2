package com.scbank.process.api.svc.shared.integration.interceptor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequest;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequest;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.sign.SignComponent;
import com.scbank.process.api.svc.shared.components.sign.constants.SignConstants;
import com.scbank.process.api.svc.shared.components.sign.dto.SignVerifyInfo;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignActionType;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignVerifyType;
import com.scbank.process.api.svc.shared.constants.PRCSharedConstants;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class SignVerifyIntegrationInterceptorTest {

	@Mock private ISessionContextManager sessionManager;
	@Mock private SignComponent signComponent;

	@InjectMocks private SignVerifyIntegrationInterceptor interceptor;

	private IntegrationContext contextWithType(SignVerifyType verifyType, SignActionType actionType) {
		IntegrationContext ctx = mock(IntegrationContext.class);
		when(ctx.getAttribute(SignConstants.VERIFY_TYPE, SignVerifyType.class)).thenReturn(verifyType);
		when(ctx.getAttribute(SignConstants.ACTION_TYPE, SignActionType.class)).thenReturn(actionType);
		return ctx;
	}

	@Nested
	@DisplayName("전자서명 검증 before")
	class Before {

		@Test
		@DisplayName("서명 비대상(verifyType null) 거래는 검증하지 않음")
		public void nullVerifyTypeTest() {
			IntegrationContext ctx = contextWithType(null, null);

			interceptor.before(ctx, new Object());

			verify(signComponent, never()).verifySign(any(), any());
		}

		@Test
		@DisplayName("서명 비대상(verifyType NONE) 거래는 검증하지 않음")
		public void noneVerifyTypeTest() {
			IntegrationContext ctx = contextWithType(SignVerifyType.NONE, SignActionType.SIGN);

			interceptor.before(ctx, new Object());

			verify(signComponent, never()).verifySign(any(), any());
		}

		@Test
		@DisplayName("성공_OLTP 요청 서명 검증")
		public void oltpVerifyTest() {
			IntegrationContext ctx = contextWithType(SignVerifyType.VERIFY, SignActionType.SIGN);
			OltpRequest oltpRequest = mock(OltpRequest.class, RETURNS_DEEP_STUBS);
			when(oltpRequest.getHeader().getOltpCommon().getImsTranCd()).thenReturn("IMS01");
			when(oltpRequest.getHeader().getOltpCommon().getInClassCd()).thenReturn("IN01");
			when(oltpRequest.getHeader().getOltpCommon().getSvcCd()).thenReturn("SVC01");
			when(oltpRequest.getHeader().getOltpCommon().getJobTp()).thenReturn("JOB01");
			doNothing().when(signComponent).verifySign(any(), any(SignVerifyInfo.class));

			interceptor.before(ctx, oltpRequest);

			verify(signComponent, times(1)).verifySign(any(), any(SignVerifyInfo.class));
		}

		@Test
		@DisplayName("성공_MCI 요청 서명 검증")
		public void mciVerifyTest() {
			IntegrationContext ctx = contextWithType(SignVerifyType.VERIFY_N_SAVE, SignActionType.SIGN);
			when(ctx.getAttribute("tranCd")).thenReturn("TR001");
			MciRequest mciRequest = mock(MciRequest.class, RETURNS_DEEP_STUBS);
			doNothing().when(signComponent).verifySign(any(), any(SignVerifyInfo.class));

			interceptor.before(ctx, mciRequest);

			verify(signComponent, times(1)).verifySign(any(), any(SignVerifyInfo.class));
		}

		@Test
		@DisplayName("OLTP/MCI 외 요청도 검증(requestMessage null) 수행")
		public void otherRequestVerifyTest() {
			IntegrationContext ctx = contextWithType(SignVerifyType.VERIFY, SignActionType.SIGN);
			doNothing().when(signComponent).verifySign(any(), any(SignVerifyInfo.class));

			interceptor.before(ctx, new Object());

			verify(signComponent, times(1)).verifySign(any(), any(SignVerifyInfo.class));
		}

		@Test
		@DisplayName("실패_PRCServiceException 발생 시 재던짐")
		public void prcExceptionTest() {
			IntegrationContext ctx = contextWithType(SignVerifyType.VERIFY, SignActionType.SIGN);
			doThrow(new PRCServiceException("MA3SGN0001", "서명검증실패"))
					.when(signComponent).verifySign(any(), any(SignVerifyInfo.class));

			PRCServiceException ex = assertThrows(PRCServiceException.class,
					() -> interceptor.before(ctx, new Object()));
			assertTrue(ex.getErrorCode().contains("MA3SGN0001"));
		}

		@Test
		@DisplayName("실패_기타 Exception 발생 시 MA3CMM0001로 변환")
		public void genericExceptionTest() {
			IntegrationContext ctx = contextWithType(SignVerifyType.VERIFY, SignActionType.SIGN);
			doThrow(new RuntimeException("unexpected"))
					.when(signComponent).verifySign(any(), any(SignVerifyInfo.class));

			PRCServiceException ex = assertThrows(PRCServiceException.class,
					() -> interceptor.before(ctx, new Object()));
			assertTrue(ex.getErrorCode().contains("MA3CMM0001"));
		}
	}

	@Test
	@DisplayName("after - 전자서명 성공코드 globalValue 제거")
	public void afterTest() {
		IntegrationContext ctx = mock(IntegrationContext.class);

		interceptor.after(ctx, new Object(), new Object());

		verify(sessionManager).removeGlobalValue(PRCSharedConstants.SIGN_SUCCESS_CODE);
	}
}
