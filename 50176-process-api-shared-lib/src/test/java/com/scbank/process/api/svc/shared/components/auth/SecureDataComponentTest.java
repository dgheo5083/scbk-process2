package com.scbank.process.api.svc.shared.components.auth;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.sign.SignComponent;
import com.scbank.process.api.svc.shared.components.sign.dto.SignVerifyInfo;
import com.scbank.process.api.svc.shared.components.verification.VerificationComponent;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationVerifyInfo;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class SecureDataComponentTest {

	@Mock private SignComponent signComponent;
	@Mock private VerificationComponent verificationComponent;
	@Mock private ISessionContextManager sessionContextManager;

	@InjectMocks private SecureDataComponent component;

	@Test
	@DisplayName("Deprecated verifyAll(3-arg)은 SIGNERROR 발생")
	public void deprecatedVerifyAll3ArgTest() {
		PRCServiceException ex = assertThrows(PRCServiceException.class,
				() -> component.verifyAll(mock(IMessageObject.class), new VerificationVerifyInfo(), new SignVerifyInfo()));
		assertSignError(ex);
	}

	@Test
	@DisplayName("verifyAll(Verification, Sign) - 추가인증/토큰/서명 검증 호출")
	public void verifyAllTwoArgTest() {
		VerificationVerifyInfo verificationVerifyInfo = new VerificationVerifyInfo();
		SignVerifyInfo signVerifyInfo = new SignVerifyInfo();

		component.verifyAll(verificationVerifyInfo, signVerifyInfo);

		verify(verificationComponent, times(1)).verifyAdditional();
		verify(verificationComponent, times(1)).verifyTokens(verificationVerifyInfo);
		verify(signComponent, times(1)).verifySign(signVerifyInfo);
	}

	@Test
	@DisplayName("Deprecated verifyAll(IMessageObject, Sign)은 SIGNERROR 발생")
	public void deprecatedVerifyAllMsgSignTest() {
		PRCServiceException ex = assertThrows(PRCServiceException.class,
				() -> component.verifyAll(mock(IMessageObject.class), new SignVerifyInfo()));
		assertSignError(ex);
	}

	@Test
	@DisplayName("verifyAll(Sign) - 내부 VerificationVerifyInfo 생성 후 검증 호출")
	public void verifyAllSignTest() {
		SignVerifyInfo signVerifyInfo = new SignVerifyInfo();

		component.verifyAll(signVerifyInfo);

		verify(verificationComponent, times(1)).verifyAdditional();
		verify(verificationComponent, times(1)).verifyTokens(org.mockito.ArgumentMatchers.any(VerificationVerifyInfo.class));
		verify(signComponent, times(1)).verifySign(signVerifyInfo);
	}

	@Test
	@DisplayName("verifyVerification() - 추가인증/토큰 검증")
	public void verifyVerificationNoArgTest() {
		
		component.verifyVerification();

		verify(verificationComponent, times(1)).verifyAdditional();
		verify(verificationComponent, times(1)).verifyTokens(org.mockito.ArgumentMatchers.any(VerificationVerifyInfo.class));
	}

	@Test
	@DisplayName("verifyVerification(info) - 추가인증/토큰 검증")
	public void verifyVerificationArgTest() {
		VerificationVerifyInfo info = new VerificationVerifyInfo();

		component.verifyVerification(info);

		verify(verificationComponent, times(1)).verifyAdditional();
		verify(verificationComponent, times(1)).verifyTokens(info);
	}

	@Test
	@DisplayName("Deprecated verifySign(IMessageObject, Sign)은 SIGNERROR 발생")
	public void deprecatedVerifySignTest() {
		PRCServiceException ex = assertThrows(PRCServiceException.class,
				() -> component.verifySign(mock(IMessageObject.class), new SignVerifyInfo()));
		assertSignError(ex);
	}

	@Test
	@DisplayName("verifySign(Sign) - 서명 검증 위임")
	public void verifySignTest() {
		SignVerifyInfo signVerifyInfo = new SignVerifyInfo();

		component.verifySign(signVerifyInfo);

		verify(signComponent, times(1)).verifySign(signVerifyInfo);
	}

	private void assertSignError(PRCServiceException ex) {
		org.junit.jupiter.api.Assertions.assertTrue(
				ex.getErrorCode().contains("SIGNERROR") || ex.getMessage().contains("SIGNERROR"));
	}
}
