package com.scbank.process.api.svc.shared.components.frs.kftc.interceptor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

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
import com.scbank.process.api.svc.shared.components.frs.IFrsClientInterceptor;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpRequestEntity;
import com.scbank.process.api.svc.shared.dao.FaceRecApiLogDao;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class KftcFrsLogInterceptorTest {

	private FrsHttpRequestEntity entity() {
		FrsHttpRequestEntity entity = mock(FrsHttpRequestEntity.class);
		when(entity.getBodyParameters()).thenReturn(new JSONObject());
		when(entity.getUrl()).thenReturn("http://kftc/frs");
		return entity;
	}

	@Test
	@DisplayName("client 팩토리 - 인터셉터 생성")
	public void clientFactoryTest() {
		assertNotNull(KftcFrsLogInterceptor.client("cif01", "ft01", "cust01", "trad01"));
	}

	@Nested
	@DisplayName("before/after 로깅")
	class Logging {

		@Test
		@DisplayName("before - 정상 입력 시 로그 적재")
		public void beforeValidTest() {
			KftcFrsLogInterceptor interceptor = (KftcFrsLogInterceptor) KftcFrsLogInterceptor.client("cif01", "ft01", "cust01", "trad01");

			try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				FaceRecApiLogDao dao = mock(FaceRecApiLogDao.class);
				runtime.when(() -> RuntimeContext.getBean(FaceRecApiLogDao.class)).thenReturn(dao);

				assertDoesNotThrow(() -> interceptor.before(entity()));
			}
		}

		@Test
		@DisplayName("before - 빈 userCifNo면 적재하지 않음")
		public void beforeBlankTest() {
			IFrsClientInterceptor interceptor = KftcFrsLogInterceptor.client(" ", "ft01", "cust01", "trad01");
			assertDoesNotThrow(() -> interceptor.before(entity()));
		}

		@Test
		@DisplayName("after - 정상 입력 시 로그 적재 (DAO null 안전 처리)")
		public void afterValidTest() {
			KftcFrsLogInterceptor interceptor = (KftcFrsLogInterceptor) KftcFrsLogInterceptor.client("cif01", "ft01", "cust01", "trad01");

			try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				runtime.when(() -> RuntimeContext.getBean(FaceRecApiLogDao.class)).thenReturn(null);

				assertDoesNotThrow(() -> interceptor.after(entity(), new JSONObject().put("response_code", "0000")));
			}
		}
	}
}
