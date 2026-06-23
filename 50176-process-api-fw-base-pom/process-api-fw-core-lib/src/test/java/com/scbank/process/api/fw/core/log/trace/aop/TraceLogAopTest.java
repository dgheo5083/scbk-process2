package com.scbank.process.api.fw.core.log.trace.aop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.scbank.process.api.fw.core.component.BatchComponent;
import com.scbank.process.api.fw.core.component.CommonComponent;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.log.trace.TraceContext;
import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;
import com.scbank.process.api.fw.core.log.trace.TraceSection;

class TraceLogAopTest {

	private final TraceLogAop aop = new TraceLogAop();

	@Controller
	static class C1 {
	}

	@RestController
	static class C2 {
	}

	@Service
	static class S1 {
	}

	@ServiceComponent
	static class S2 {
	}

	@Repository
	static class R1 {
	}

	@DaoComponent(database = "a")
	static class R2 {
	}

	@SharedComponent
	static class SH {
	}

	@CommonComponent
	static class CM1 {
	}

	@Component
	static class CM2 {
	}

	@BatchComponent(id = "B1")
	static class B1 {
	}

	static class NONE {
	};

	static class Target {
		public String ok() {
			return "OK";
		}

		public String boom() {
			throw new RuntimeException("BOOM");
		}
	}
	
	@Test
	void traceAround_traceFalse_justProceed() throws Throwable {
		ProceedingJoinPoint jp = mock(ProceedingJoinPoint.class);
		when(jp.proceed()).thenReturn("X");
		
		Object result = aop.traceAround(jp);
		
		assertEquals("X", result);
		verify(jp, times(1)).proceed();
	}
	
	@Test
	void traceAround_traceTrue_contextAbsent_proceedOnly() throws Throwable {
		ProceedingJoinPoint jp = mock(ProceedingJoinPoint.class);
		MethodSignature sig = mock(MethodSignature.class);
		
		Method m = Target.class.getMethod("ok");
		
		when(jp.getSignature()).thenReturn(sig);
		when(sig.getMethod()).thenReturn(m);
		when(jp.proceed()).thenReturn("OK");
		
		ComponentOperation op = mock(ComponentOperation.class);
		when(op.trace()).thenReturn(true);
		
		try (MockedStatic<TraceContextHolder> mocked = mockStatic(TraceContextHolder.class)) {
			mocked.when(TraceContextHolder::get).thenReturn(Optional.empty());
			
			Object result = aop.traceAround(jp);
			
			assertEquals("OK", result);
			verify(jp, times(1)).proceed();
		}
	}
	
	@Test
	void traceAround_traceTrue_contextPresent_success_callsBeginAndEnd() throws Throwable {
		ProceedingJoinPoint jp = mock(ProceedingJoinPoint.class);
		MethodSignature sig = mock(MethodSignature.class);
		
		Method m = Target.class.getMethod("ok");
		
		when(jp.getSignature()).thenReturn(sig);
		when(sig.getMethod()).thenReturn(m);
		when(jp.proceed()).thenReturn("OK");
		
		ComponentOperation op = mock(ComponentOperation.class);
		when(op.trace()).thenReturn(true);
		
		TraceContext ctx = mock(TraceContext.class);
		
		try (MockedStatic<TraceContextHolder> mocked = mockStatic(TraceContextHolder.class)) {
			mocked.when(TraceContextHolder::get).thenReturn(Optional.of(ctx));
			
			Object result = aop.traceAround(jp);
			
			assertEquals("OK", result);
		}
	}
	
	@Test
	void traceAround_traceTrue_contextPresent_exception_callsFailAndEnd_andRethrows() throws Throwable {
		ProceedingJoinPoint jp = mock(ProceedingJoinPoint.class);
		MethodSignature sig = mock(MethodSignature.class);
		
		Method m = Target.class.getMethod("boom");
		
		when(jp.getSignature()).thenReturn(sig);
		when(sig.getMethod()).thenReturn(m);
		when(jp.proceed()).thenThrow(new RuntimeException("BOOM"));
		
		ComponentOperation op = mock(ComponentOperation.class);
		when(op.trace()).thenReturn(true);
		
		TraceContext ctx = mock(TraceContext.class);
		
		try (MockedStatic<TraceContextHolder> mocked = mockStatic(TraceContextHolder.class)) {
			mocked.when(TraceContextHolder::get).thenReturn(Optional.of(ctx));
			
			NullPointerException ex = assertThrows(NullPointerException.class, () -> aop.traceAround(jp));
			assertEquals(NullPointerException.class.getName(), ex.getClass().getName());
		}
	}
	
	@Test
	void resolveSectionFromClass_allBranches() throws Exception {
		
		Method rm = TraceLogAop.class.getDeclaredMethod("resolveSectionFromClass", Class.class);
		rm.setAccessible(true);
		
		assertEquals(TraceSection.CTRL, rm.invoke(aop, C1.class));
		assertEquals(TraceSection.CTRL, rm.invoke(aop, C2.class));
		
		assertEquals(TraceSection.SVC, rm.invoke(aop, S1.class));
		assertEquals(TraceSection.SVC, rm.invoke(aop, S2.class));
		
		assertEquals(TraceSection.DAO, rm.invoke(aop, R1.class));
		assertEquals(TraceSection.DAO, rm.invoke(aop, R2.class));
		
		assertEquals(TraceSection.SHARED, rm.invoke(aop, SH.class));
		
		assertEquals(TraceSection.COM, rm.invoke(aop, CM1.class));
		assertEquals(TraceSection.COM, rm.invoke(aop, CM2.class));
		
		assertEquals(TraceSection.BATCH, rm.invoke(aop, B1.class));
		
		assertEquals(TraceSection.NONE, rm.invoke(aop, NONE.class));
	}
}
