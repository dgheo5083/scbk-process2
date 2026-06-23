package com.scbank.process.api.svc.shared.integration.interceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
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
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.store.ThreadLocalStoreDelegator;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.svc.shared.components.accesscontrol.ServiceTimeCheckComponent;
import com.scbank.process.api.svc.shared.components.accesscontrol.dto.ServiceTimeCheckRequest;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class IntegrationServiceTimeCheckInterceptorTest {

	@Mock private ServiceTimeCheckComponent serviceTimeCheckComponent;

	@InjectMocks private IntegrationServiceTimeCheckInterceptor interceptor;

	@Nested
	@DisplayName("전문 거래코드 별 서비스 이용시간 체크 before")
	class Before {

		@Test
		@DisplayName("거래코드가 없으면 체크하지 않음")
		public void emptyTranCdTest() {
			IntegrationContext context = mock(IntegrationContext.class);
			when(context.getAttribute("tranCd")).thenReturn("");

			interceptor.before(context, new Object());

			verify(serviceTimeCheckComponent, never()).checkServiceTime(any());
		}

		@Test
		@DisplayName("성공_거래코드 존재 시 이용시간 체크 컴포넌트 호출")
		public void checkServiceTimeTest() {
			IntegrationContext context = mock(IntegrationContext.class);
			when(context.getAttribute("tranCd")).thenReturn("TR001");

			try (MockedStatic<ThreadLocalStoreDelegator> store = mockStatic(ThreadLocalStoreDelegator.class)) {
				store.when(ThreadLocalStoreDelegator::getForceCheckCode).thenReturn("F");

				interceptor.before(context, new Object());

				verify(serviceTimeCheckComponent, times(1)).checkServiceTime(any(ServiceTimeCheckRequest.class));
			}
		}
	}
}
