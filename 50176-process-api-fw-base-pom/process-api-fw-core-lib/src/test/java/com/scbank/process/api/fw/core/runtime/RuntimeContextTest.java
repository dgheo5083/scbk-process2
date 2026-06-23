package com.scbank.process.api.fw.core.runtime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.scbank.process.api.fw.core.enums.RunMode;

class RuntimeContextTest {

	@AfterEach
	void tearDown() {
		RuntimeContext.setApplicationContext(null);
		RuntimeContext.setEnvironment(null);
	}

	@Test
	@DisplayName("getBean(Class)")
	void getBeanByType() {

		ApplicationContext ctx = mock(ApplicationContext.class);
		SampleBean bean = new SampleBean();

		when(ctx.getBean(SampleBean.class)).thenReturn(bean);

		RuntimeContext.setApplicationContext(ctx);

		SampleBean result = RuntimeContext.getBean(SampleBean.class);

		assertSame(bean, result);
	}

	@Test
	@DisplayName("getBean(String, Class)")
	void getBeanByNameAndType() {

		ApplicationContext ctx = mock(ApplicationContext.class);
		SampleBean bean = new SampleBean();

		when(ctx.getBean("sampleBean", SampleBean.class)).thenReturn(bean);

		RuntimeContext.setApplicationContext(ctx);

		SampleBean result = RuntimeContext.getBean("sampleBean", SampleBean.class);

		assertSame(bean, result);
	}

	@Test
	@DisplayName("getBean(String)")
	void getBeanByName() {

		ApplicationContext ctx = mock(ApplicationContext.class);
		SampleBean bean = new SampleBean();

		when(ctx.getBean("sampleBean")).thenReturn(bean);

		RuntimeContext.setApplicationContext(ctx);

		Object result = RuntimeContext.getBean("sampleBean");

		assertSame(bean, result);
	}

	@Test
	@DisplayName("ApplicationContext 없으면 null 반환")
	void getBeanReturnsNullWhenContextNull() {

		RuntimeContext.setApplicationContext(null);

		assertNull(RuntimeContext.getBean(SampleBean.class));

		assertNull(RuntimeContext.getBean("sampleBean", SampleBean.class));

		assertNull(RuntimeContext.getBean("sampleBean"));
	}

	@Test
	@DisplayName("getEnv(property)")
	void getEnv() {

		Environment env = mock(Environment.class);

		when(env.getProperty("sample.key")).thenReturn("VALUE");

		RuntimeContext.setEnvironment(env);

		assertEquals("VALUE", RuntimeContext.getEnv("sample.key"));
	}

	@Test
	@DisplayName("getEnv(property, default)")
	void getEnvWithDefaultValue() {

		Environment env = mock(Environment.class);

		when(env.getProperty("sample.key", "DEFAULT")).thenReturn("DEFAULT");

		RuntimeContext.setEnvironment(env);

		assertEquals("DEFAULT", RuntimeContext.getEnv("sample.key", "DEFAULT"));
	}

	@Test
	@DisplayName("getEnv(property, type)")
	void getEnvWithType() {

		Environment env = mock(Environment.class);

		when(env.getProperty("timeout", Integer.class)).thenReturn(30);

		RuntimeContext.setEnvironment(env);

		Integer result = RuntimeContext.getEnv("timeout", Integer.class);

		assertEquals(30, result);
	}

	@Test
	@DisplayName("getProperty(property)")
	void getProperty() {

		Environment env = mock(Environment.class);

		when(env.getProperty("prop", "")).thenReturn("VALUE");

		RuntimeContext.setEnvironment(env);

		assertEquals("VALUE", RuntimeContext.getProperty("prop"));
	}

	@Test
	@DisplayName("getProperty(property, default)")
	void getPropertyWithDefault() {

		Environment env = mock(Environment.class);

		when(env.getProperty("prop", "DEFAULT")).thenReturn("DEFAULT");

		RuntimeContext.setEnvironment(env);

		assertEquals("DEFAULT", RuntimeContext.getProperty("prop", "DEFAULT"));
	}

	@Test
	@DisplayName("getProperty(property, type)")
	void getPropertyWithType() {

		Environment env = mock(Environment.class);

		when(env.getProperty("timeout", Integer.class)).thenReturn(10);

		RuntimeContext.setEnvironment(env);

		Integer result = RuntimeContext.getProperty("timeout", Integer.class);

		assertEquals(10, result);
	}

	@Test
	@DisplayName("getProperty(property, type, default)")
	void getPropertyWithTypeAndDefault() {

		Environment env = mock(Environment.class);

		when(env.getProperty("timeout", Integer.class, 5)).thenReturn(5);

		RuntimeContext.setEnvironment(env);

		Integer result = RuntimeContext.getProperty("timeout", Integer.class, 5);

		assertEquals(5, result);
	}

	@Test
	@DisplayName("run-mode 미설정 시 LOCAL")
	void getRunModeDefaultLocal() {

		Environment env = mock(Environment.class);

		when(env.getProperty("csl.runtime.run-mode")).thenReturn(null);

		RuntimeContext.setEnvironment(env);

		assertEquals(RunMode.LOCAL, RuntimeContext.getRunMode());
	}

	@Test
	@DisplayName("run-mode DEV")
	void getRunModeDev() {

		Environment env = mock(Environment.class);

		when(env.getProperty("csl.runtime.run-mode")).thenReturn("ut");

		RuntimeContext.setEnvironment(env);

		assertEquals(RunMode.UT, RuntimeContext.getRunMode());
	}

	@Test
	@DisplayName("센터모드 조회")
	void getCenterMode() {

		Environment env = mock(Environment.class);

		when(env.getProperty("csl.runtime.center-mode")).thenReturn("main");

		RuntimeContext.setEnvironment(env);

		assertEquals("main", RuntimeContext.getCenterMode());
	}

	@Test
	@DisplayName("기본 인코딩 조회")
	void getDefaultEncoding() {

		Environment env = mock(Environment.class);

		when(env.getProperty("csl.runtime.default-encoding")).thenReturn("UTF-8");

		RuntimeContext.setEnvironment(env);

		assertEquals("UTF-8", RuntimeContext.getDefaultEncoding());
	}

	static class SampleBean {
	}

}
