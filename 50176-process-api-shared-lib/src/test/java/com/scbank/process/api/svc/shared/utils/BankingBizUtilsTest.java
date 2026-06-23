package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.account.dto.session.NewAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.helper.AccountHelper;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
public class BankingBizUtilsTest {

	private MockedStatic<RuntimeContext> mockRuntime(ISessionContextManager sm) {
		MockedStatic<RuntimeContext> mocked = mockStatic(RuntimeContext.class);
		mocked.when(() -> RuntimeContext.getBean(ISessionContextManager.class)).thenReturn(sm);
		return mocked;
	}

	@Mock
	private ISessionContextManager sessionManager;

	@SuppressWarnings("static-access")
	@DisplayName("예금자보호법 : 예금상품 비보호안내 표시여부")
	@ParameterizedTest
	@CsvSource({
			"'8', '', '86020192811', '20'",
			"'', '', '', ''",
			"'9', '10', '94', '10'",
			"'8', '20', '80', '91'",
			"'8', '35', '80', '10'",
			"'8', '21', '60', '10'",
			"'8', '37', '48', '10'",
			"'9', '14', '90', '10'",
			"'9', '20', '70', '10'",
			"'8', '20', '10', '10'",
			"'8', '10', '20', '10'",
			"'9', '20', '30', '10'",
			"'9', '10', '85', '10'",
			"'8', '', '59', ''",
	})
	void isUnprotectTargetActTest(String YOKWASE, String YOGEORE, String acctType, String actAssort) {
		BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
		try (
				MockedStatic<SessionUtils> mockSessionUtils = mockStatic(SessionUtils.class);) {
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOKWASE", String.class)).thenReturn(YOKWASE);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOGEORE", String.class)).thenReturn(YOGEORE);
			if ("".equals(acctType)) {
				bankingBizUtils.isUnprotectTargetAct(null, actAssort);
			}
			bankingBizUtils.isUnprotectTargetAct(acctType, actAssort);
		}
	}

	@DisplayName("accountSessionClearTest")
	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void accountSessionClearTest(boolean isLogin) {
		ISessionContextManager sessionManager = mock(ISessionContextManager.class);
		BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
		when(sessionManager.isLogin()).thenReturn(isLogin);
		ReflectionTestUtils.setField(bankingBizUtils, "sessionManager", sessionManager);

		try (MockedStatic<RuntimeContext> r = mockRuntime(sessionManager)) {
			BankingBizUtils.accountSessionClear();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@DisplayName("accountSessionClearTest(String)1")
	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void accountSessionClear_String_Test1(boolean isLogin) {
		ISessionContextManager sessionManager = mock(ISessionContextManager.class);
		BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
		AccountHelper helper = mock(AccountHelper.class);

		when(sessionManager.isLogin()).thenReturn(isLogin);
		when(helper.getNewAccountSessionList()).thenAnswer(i -> {
			List<NewAccountInfoSession> list = new ArrayList<>();
			return list;
		});

		ReflectionTestUtils.setField(bankingBizUtils, "sessionManager", sessionManager);
		ReflectionTestUtils.setField(bankingBizUtils, "helper", helper);

		try (MockedStatic<RuntimeContext> r = mockRuntime(sessionManager)) {
			BankingBizUtils.accountSessionClear("86021092811");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@DisplayName("accountSessionClearTest(String)2")
	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void accountSessionClear_String_Test2(boolean isLogin) {
		ISessionContextManager sessionManager = mock(ISessionContextManager.class);
		BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
		AccountHelper helper = mock(AccountHelper.class);

		when(sessionManager.isLogin()).thenReturn(isLogin);
		when(helper.getNewAccountSessionList()).thenAnswer(i -> {
			List<NewAccountInfoSession> list = new ArrayList<>();
			return list;
		});

		ReflectionTestUtils.setField(bankingBizUtils, "sessionManager", sessionManager);
		ReflectionTestUtils.setField(bankingBizUtils, "helper", helper);

		try (MockedStatic<RuntimeContext> r = mockRuntime(sessionManager)) {
			BankingBizUtils.accountSessionClear("");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@DisplayName("accountSessionClearTest(String)3")
	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void accountSessionClear_String_Test3(boolean isLogin) {
		ISessionContextManager sessionManager = mock(ISessionContextManager.class);
		BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
		AccountHelper helper = mock(AccountHelper.class);

		when(sessionManager.isLogin()).thenReturn(isLogin);
		when(helper.getNewAccountSessionList()).thenAnswer(i -> {
			return null;
		});

		ReflectionTestUtils.setField(bankingBizUtils, "sessionManager", sessionManager);
		ReflectionTestUtils.setField(bankingBizUtils, "helper", helper);

		try (MockedStatic<RuntimeContext> r = mockRuntime(sessionManager)) {
			BankingBizUtils.accountSessionClear("86021092811");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@DisplayName("accountSessionClearTest(List)")
	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void accountSessionClear_List_Test(boolean isLogin) {
		ISessionContextManager sessionManager = mock(ISessionContextManager.class);
		BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
		AccountHelper helper = mock(AccountHelper.class);

		when(sessionManager.isLogin()).thenReturn(isLogin);
		when(helper.getNewAccountSessionList()).thenAnswer(i -> {
			return null;
		});

		ReflectionTestUtils.setField(bankingBizUtils, "sessionManager", sessionManager);
		ReflectionTestUtils.setField(bankingBizUtils, "helper", helper);

		List<NewAccountInfoSession> l1 = null;
		try (MockedStatic<RuntimeContext> r = mockRuntime(sessionManager)) {
			BankingBizUtils.accountSessionClear(l1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@DisplayName("accountSessionClearTest(List)2")
	@Test
	void accountSessionClear_List_Test2() {
		ISessionContextManager sessionManager = mock(ISessionContextManager.class);
		BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
		AccountHelper helper = mock(AccountHelper.class);

		when(sessionManager.isLogin()).thenReturn(true);
		when(helper.getNewAccountSessionList()).thenAnswer(i -> {
			return null;
		});

		ReflectionTestUtils.setField(bankingBizUtils, "sessionManager", sessionManager);
		ReflectionTestUtils.setField(bankingBizUtils, "helper", helper);

		List<NewAccountInfoSession> l1 = new ArrayList();
		try (MockedStatic<RuntimeContext> r = mockRuntime(sessionManager)) {
			BankingBizUtils.accountSessionClear(l1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@DisplayName("accountSessionClearTest(List)3")
	@Test
	void accountSessionClear_List_Test3() {
		ISessionContextManager sessionManager = mock(ISessionContextManager.class);
		BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
		AccountHelper helper = mock(AccountHelper.class);

		when(sessionManager.isLogin()).thenReturn(true);
		when(helper.getNewAccountSessionList()).thenAnswer(i -> {
			return new ArrayList();
		});

		ReflectionTestUtils.setField(bankingBizUtils, "sessionManager", sessionManager);
		ReflectionTestUtils.setField(bankingBizUtils, "helper", helper);

		List<NewAccountInfoSession> l1 = new ArrayList();
		try (MockedStatic<RuntimeContext> r = mockRuntime(sessionManager)) {
			BankingBizUtils.accountSessionClear(l1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@DisplayName("toJSONFromStrTest")
	@Test
	void toJSONFromStrTest() {
		assertNotNull(BankingBizUtils.toJSONFromStr("{\"test\":\"1234\"}", Map.class));
		assertEquals(null, BankingBizUtils.toJSONFromStr("{\"test\":}", Map.class));
	}

	@DisplayName("toJSONStringFromObjectTest")
	@Test
	void toJSONStringFromObjectTest() {
		BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
		assertNotNull(BankingBizUtils.toJSONStringFromObject(new JSONObject("{\"test\":\"1234\"}")));

		try (
				MockedConstruction<ObjectMapper> mockObjectMapper = mockConstruction(ObjectMapper.class,
						(mock, context) -> {
							when(mock.writeValueAsString(any())).thenThrow(new JsonProcessingException("error") {
							});
						});) {

			try (MockedStatic<RuntimeContext> r = mockRuntime(sessionManager)) {
				assertEquals("", bankingBizUtils.toJSONStringFromObject(new JSONObject("{\"test\":\"1234\"}")));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@DisplayName("isAuthenticatedTest")
	@Test
	void isAuthenticatedTest() {
		BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);

		try (MockedStatic<RuntimeContext> r = mockRuntime(sessionManager)) {
			ISessionContextManager sessionManager = mock(ISessionContextManager.class);
			when(sessionManager.getGlobalValue(eq("BIZ_AUTH_FLAG"), eq(String.class))).thenReturn("Y");
			bankingBizUtils.isAuthenticated();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@DisplayName("retSplitPhoneNumberTest")
	@Test
	void retSplitPhoneNumberTest() {
		try (MockedStatic<RuntimeContext> r = mockRuntime(sessionManager)) {
			BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
			bankingBizUtils.retSplitPhoneNumber("010-1111-1111");
			bankingBizUtils.retSplitPhoneNumber("011-111-1111");
			bankingBizUtils.retSplitPhoneNumber("");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@DisplayName("미성년자, 외국인 체크")
	@Test
	void getForeignerAndUnderAgeTest() {
		try (
				MockedStatic<DateUtils> mockDateUtils = mockStatic(DateUtils.class);) {
			mockDateUtils.when(() -> DateUtils.getCurrentDate(DateUtils.YYYYMMDDHHMMSS)).thenReturn("20260529000000");
			assertEquals("N", BankingBizUtils.getForeignerAndUnderAge("8003071999994").get("isLowAge"));
			assertEquals("Y", BankingBizUtils.getForeignerAndUnderAge("8005103999994").get("isLowAge"));
			assertEquals("N", BankingBizUtils.getForeignerAndUnderAge("8007075999994").get("isLowAge"));
			assertEquals("Y", BankingBizUtils.getForeignerAndUnderAge("8005317999994").get("isLowAge"));
			assertEquals("N", BankingBizUtils.getForeignerAndUnderAge("").get("isLowAge"));
		}
	}

	@DisplayName("외국인 허용 가능업무 체크-로그인,모바일뱅킹")
	@ParameterizedTest
	@CsvSource({
			"'TRLR', 'Y', '8007071999994', '5', 'Y', '0', 'Y'",
			"'', '', '8007071999994', '5', 'Y', '0', 'Y'",
			"'TRLR', 'N', '8007071999994', '1', '4', '0', 'Y'",
			"'TRLR', 'N', '8007071999994', '1', '4', '0', 'N'",
			"'TRLR', 'Y', '8007071999994', '1', '4', '0', 'Y'",
			"'TRLR', 'Y', '8007071999994', '5', '4', '0', 'Y'",
			"'TRLR', 'Y', '8007071999994', '5', '4', '1', 'Y'",
			"'TRLR', 'Y', '8007071999994', '5', '1', '5', 'Y'",
			"'TRLR', 'Y', '8007071999994', '5', '4', '5', 'N'",
			"'TRLR', 'Y', '', '', '', '', 'Y'",
	})
	void getCheckForeignerAllowedTest(String bizType, String isForeigner, String PerBusNo, String YOKWASE,
			String YOPASSYN, String YOGEOJU, String allowed) {

		try (
				MockedStatic<SessionUtils> mockSessionUtils = mockStatic(SessionUtils.class);
				MockedStatic<PRCSharedUtils> mockPRCSharedUtils = mockStatic(PRCSharedUtils.class);
				MockedStatic<PropertiesUtils> mockPropertiesUtils = mockStatic(PropertiesUtils.class);) {
			BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
			ISessionContextManager sessionManager = mock(ISessionContextManager.class);

			when(sessionManager.isLogin()).thenReturn(true);
			ReflectionTestUtils.setField(bankingBizUtils, "sessionManager", sessionManager);
			mockPRCSharedUtils.when(() -> PRCSharedUtils.isSB()).thenReturn(true);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOKWASE")).thenReturn(YOKWASE);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOPASSYN")).thenReturn(YOPASSYN);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOGEOJU")).thenReturn(YOGEOJU);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("BIZ_TYPE", String.class))
					.thenReturn("".equals(bizType) ? "MOTP" : bizType);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("PerBusNo", String.class)).thenReturn(PerBusNo);
			mockPropertiesUtils.when(() -> PropertiesUtils.getString(any())).thenReturn(allowed);

			try (MockedStatic<RuntimeContext> r = mockRuntime(sessionManager)) {
				bankingBizUtils.getCheckForeignerAllowed(bizType, isForeigner);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@DisplayName("외국인 허용 가능업무 체크-비로그인,PC뱅킹")
	@ParameterizedTest
	@CsvSource({
			"'TRLR', 'Y', '8007071999994', '5', 'Y', '0', 'Y'",
	})
	void getCheckForeignerAllowedTest2(String bizType, String isForeigner, String PerBusNo, String YOKWASE,
			String YOPASSYN, String YOGEOJU, String allowed) {

		try (
				MockedStatic<SessionUtils> mockSessionUtils = mockStatic(SessionUtils.class);
				MockedStatic<PRCSharedUtils> mockPRCSharedUtils = mockStatic(PRCSharedUtils.class);
				MockedStatic<PropertiesUtils> mockPropertiesUtils = mockStatic(PropertiesUtils.class);) {
			BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
			ISessionContextManager sessionManager = mock(ISessionContextManager.class);

			when(sessionManager.isLogin()).thenReturn(false);
			ReflectionTestUtils.setField(bankingBizUtils, "sessionManager", sessionManager);
			mockPRCSharedUtils.when(() -> PRCSharedUtils.isSB()).thenReturn(false);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOKWASE")).thenReturn(YOKWASE);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOPASSYN")).thenReturn(YOPASSYN);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOGEOJU")).thenReturn(YOGEOJU);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("BIZ_TYPE", String.class))
					.thenReturn("".equals(bizType) ? "MOTP" : bizType);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("PerBusNo", String.class)).thenReturn(PerBusNo);
			mockPropertiesUtils.when(() -> PropertiesUtils.getString(any())).thenReturn(allowed);
			bankingBizUtils.getCheckForeignerAllowed(bizType, isForeigner);
		}
	}

	@DisplayName("외국인 허용 가능업무 체크-비로그인,모바일뱅킹")
	@ParameterizedTest
	@CsvSource({
			"'TRLR', 'Y', '8007071999994', '5', 'Y', '0', 'Y'",
	})
	void getCheckForeignerAllowedTest3(String bizType, String isForeigner, String PerBusNo, String YOKWASE,
			String YOPASSYN, String YOGEOJU, String allowed) {

		try (
				MockedStatic<SessionUtils> mockSessionUtils = mockStatic(SessionUtils.class);
				MockedStatic<PRCSharedUtils> mockPRCSharedUtils = mockStatic(PRCSharedUtils.class);
				MockedStatic<PropertiesUtils> mockPropertiesUtils = mockStatic(PropertiesUtils.class);) {
			BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
			ISessionContextManager sessionManager = mock(ISessionContextManager.class);

			when(sessionManager.isLogin()).thenReturn(false);
			ReflectionTestUtils.setField(bankingBizUtils, "sessionManager", sessionManager);
			mockPRCSharedUtils.when(() -> PRCSharedUtils.isSB()).thenReturn(true);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOKWASE")).thenReturn(YOKWASE);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOPASSYN")).thenReturn(YOPASSYN);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOGEOJU")).thenReturn(YOGEOJU);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("BIZ_TYPE", String.class))
					.thenReturn("".equals(bizType) ? "MOTP" : bizType);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("PerBusNo", String.class)).thenReturn(PerBusNo);
			mockPropertiesUtils.when(() -> PropertiesUtils.getString(any())).thenReturn(allowed);
			try (MockedStatic<RuntimeContext> r = mockRuntime(sessionManager)) {
				bankingBizUtils.getCheckForeignerAllowed(bizType, isForeigner);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@DisplayName("외국인 허용 가능업무 체크-로그인,PC뱅킹")
	@ParameterizedTest
	@CsvSource({
			"'TRLR', 'Y', '8007071999994', '5', 'Y', '0', 'Y'",
	})
	void getCheckForeignerAllowedTest4(String bizType, String isForeigner, String PerBusNo, String YOKWASE,
			String YOPASSYN, String YOGEOJU, String allowed) {

		try (
				MockedStatic<SessionUtils> mockSessionUtils = mockStatic(SessionUtils.class);
				MockedStatic<PRCSharedUtils> mockPRCSharedUtils = mockStatic(PRCSharedUtils.class);
				MockedStatic<PropertiesUtils> mockPropertiesUtils = mockStatic(PropertiesUtils.class);) {
			BankingBizUtils bankingBizUtils = mock(BankingBizUtils.class);
			ISessionContextManager sessionManager = mock(ISessionContextManager.class);

			when(sessionManager.isLogin()).thenReturn(true);
			ReflectionTestUtils.setField(bankingBizUtils, "sessionManager", sessionManager);
			mockPRCSharedUtils.when(() -> PRCSharedUtils.isSB()).thenReturn(false);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOKWASE")).thenReturn(YOKWASE);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOPASSYN")).thenReturn(YOPASSYN);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("YOGEOJU")).thenReturn(YOGEOJU);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("BIZ_TYPE", String.class))
					.thenReturn("".equals(bizType) ? "MOTP" : bizType);
			mockSessionUtils.when(() -> SessionUtils.getSessionValue("PerBusNo", String.class)).thenReturn(PerBusNo);
			mockPropertiesUtils.when(() -> PropertiesUtils.getString(any())).thenReturn(allowed);
			bankingBizUtils.getCheckForeignerAllowed(bizType, isForeigner);
		}
	}

	@DisplayName("영문+숫자+특수문자 입력여부 체크")
	@Test
	void isAlphaNumSpeCheckTest() {
		assertEquals(false, BankingBizUtils.isAlphaNumSpeCheck("ㄱ"));
		assertEquals(false, BankingBizUtils.isAlphaNumSpeCheck("a"));
		assertEquals(false, BankingBizUtils.isAlphaNumSpeCheck("1"));
		assertEquals(false, BankingBizUtils.isAlphaNumSpeCheck("!"));
		assertEquals(false, BankingBizUtils.isAlphaNumSpeCheck("a!"));
		assertEquals(false, BankingBizUtils.isAlphaNumSpeCheck("a1"));
		assertEquals(true, BankingBizUtils.isAlphaNumSpeCheck("a!1"));
	}

}
