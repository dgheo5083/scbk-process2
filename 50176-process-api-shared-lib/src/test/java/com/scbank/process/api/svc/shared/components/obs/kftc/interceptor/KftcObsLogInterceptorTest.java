package com.scbank.process.api.svc.shared.components.obs.kftc.interceptor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.svc.shared.components.obs.IObsClientInterceptor;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpRequestEntity;
import com.scbank.process.api.svc.shared.dao.OpenBankApiLogDao;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class KftcObsLogInterceptorTest {

	private ObsHttpRequestEntity entity(JSONObject body) {
		ObsHttpRequestEntity entity = mock(ObsHttpRequestEntity.class);
		when(entity.getBodyParameters()).thenReturn(body);
		when(entity.getParameters()).thenReturn(new JSONObject());
		when(entity.getUrl()).thenReturn("http://kftc/openbank");
		return entity;
	}

	@Test
	@DisplayName("client 팩토리 - 인터셉터 생성")
	public void clientFactoryTest() {
		assertNotNull(KftcObsLogInterceptor.client("user01", "cif01", 1));
		assertNotNull(KftcObsLogInterceptor.client("user01", "cif01", 1, "BAT"));
	}

	@Nested
	@DisplayName("before/after 로깅")
	class Logging {

		@Test
		@DisplayName("before - 정상 입력 시 로그 적재 (req_list bank_tran_id 탐색 포함)")
		public void beforeValidTest() {
			JSONObject body = new JSONObject();
			JSONArray reqList = new JSONArray();
			reqList.put(new JSONObject().put("bank_tran_id", "BT0001"));
			body.put("req_list", reqList);

			KftcObsLogInterceptor interceptor = (KftcObsLogInterceptor) KftcObsLogInterceptor.client("user01", "cif01", 1);

			try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				OpenBankApiLogDao dao = mock(OpenBankApiLogDao.class);
				runtime.when(() -> RuntimeContext.getBean(OpenBankApiLogDao.class)).thenReturn(dao);

				assertDoesNotThrow(() -> interceptor.before(entity(body)));
			}
		}

		@Test
		@DisplayName("before - 빈 userId면 적재하지 않음")
		public void beforeBlankUserIdTest() {
			IObsClientInterceptor interceptor = KftcObsLogInterceptor.client(" ", "cif01", 1);
			assertDoesNotThrow(() -> interceptor.before(entity(new JSONObject())));
		}

		@Test
		@DisplayName("after - 정상 입력 시 로그 적재")
		public void afterValidTest() {
			KftcObsLogInterceptor interceptor = (KftcObsLogInterceptor) KftcObsLogInterceptor.client("user01", "cif01", 1);

			try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				OpenBankApiLogDao dao = mock(OpenBankApiLogDao.class);
				runtime.when(() -> RuntimeContext.getBean(OpenBankApiLogDao.class)).thenReturn(dao);

				assertDoesNotThrow(() -> interceptor.after(entity(new JSONObject()), new JSONObject().put("api_tran_id", "AT1")));
			}
		}

		@Test
		@DisplayName("after - DAO 오류 시에도 예외 전파 안함")
		public void afterDaoErrorTest() {
			KftcObsLogInterceptor interceptor = (KftcObsLogInterceptor) KftcObsLogInterceptor.client("user01", "cif01", 1);

			try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				OpenBankApiLogDao dao = mock(OpenBankApiLogDao.class);
				org.mockito.Mockito.doThrow(new RuntimeException("db error")).when(dao).insertLog(any());
				runtime.when(() -> RuntimeContext.getBean(OpenBankApiLogDao.class)).thenReturn(dao);

				assertDoesNotThrow(() -> interceptor.after(entity(new JSONObject()), new JSONObject()));
			}
		}
	}
}
