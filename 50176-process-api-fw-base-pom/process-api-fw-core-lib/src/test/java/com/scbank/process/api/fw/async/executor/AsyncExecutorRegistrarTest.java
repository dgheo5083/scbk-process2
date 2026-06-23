package com.scbank.process.api.fw.async.executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.core.task.TaskDecorator;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * {@link AsyncExecutorRegistrar} 단위 테스트.
 */
class AsyncExecutorRegistrarTest {

    private MockEnvironment environmentWithExecutor(String key) {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("csl.async.enabled", "true");
        env.setProperty("csl.async.executors." + key + ".pool.core-size", "3");
        env.setProperty("csl.async.executors." + key + ".pool.max-size", "6");
        env.setProperty("csl.async.executors." + key + ".rejection-policy", "ABORT");
        return env;
    }

    @Test
    @DisplayName("getOrder 는 가장 낮은 우선순위를 반환")
    void getOrder() {
        AsyncExecutorRegistrar registrar = new AsyncExecutorRegistrar();

        assertThat(registrar.getOrder()).isEqualTo(Ordered.LOWEST_PRECEDENCE);
    }

    @Test
    @DisplayName("executors 설정이 비어 있으면 아무 빈도 등록하지 않는다")
    void emptyExecutorsRegistersNothing() {
        AsyncExecutorRegistrar registrar = new AsyncExecutorRegistrar();
        MockEnvironment env = new MockEnvironment();
        env.setProperty("csl.async.enabled", "true");
        registrar.setEnvironment(env);

        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        registrar.postProcessBeanDefinitionRegistry(registry);

        assertThat(registry.getBeanDefinitionCount()).isZero();
    }

    @Test
    @DisplayName("executor 이름에 'Executor' 접미사가 없으면 자동으로 붙여 빈을 등록")
    void registersExecutorBeanWithSuffix() {
        AsyncExecutorRegistrar registrar = new AsyncExecutorRegistrar();
        registrar.setEnvironment(environmentWithExecutor("task"));

        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        registrar.postProcessBeanDefinitionRegistry(registry);

        assertThat(registry.containsBeanDefinition("taskExecutor")).isTrue();
        BeanDefinition bd = registry.getBeanDefinition("taskExecutor");
        assertThat(bd.getRole()).isEqualTo(BeanDefinition.ROLE_INFRASTRUCTURE);
    }

    @Test
    @DisplayName("executor 이름이 이미 'Executor' 로 끝나면 접미사를 중복으로 붙이지 않는다")
    void keepsExecutorNameWithSuffix() {
        AsyncExecutorRegistrar registrar = new AsyncExecutorRegistrar();
        registrar.setEnvironment(environmentWithExecutor("mainExecutor"));

        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        registrar.postProcessBeanDefinitionRegistry(registry);

        assertThat(registry.containsBeanDefinition("mainExecutor")).isTrue();
    }

    @Test
    @DisplayName("이미 등록된 빈 이름이면 건너뛴다")
    void skipsExistingBeanDefinition() {
        AsyncExecutorRegistrar registrar = new AsyncExecutorRegistrar();
        registrar.setEnvironment(environmentWithExecutor("task"));

        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        RootBeanDefinition existing = new RootBeanDefinition(String.class);
        registry.registerBeanDefinition("taskExecutor", existing);

        registrar.postProcessBeanDefinitionRegistry(registry);

        // 기존 정의가 그대로 유지되어야 한다.
        assertThat(registry.getBeanDefinition("taskExecutor")).isSameAs(existing);
    }

    @Test
    @DisplayName("등록된 빈의 instanceSupplier 는 TaskDecorator 를 조회해 Executor 를 생성한다")
    void instanceSupplierCreatesExecutor() {
        AsyncExecutorRegistrar registrar = new AsyncExecutorRegistrar();
        registrar.setEnvironment(environmentWithExecutor("task"));

        BeanFactory beanFactory = mock(BeanFactory.class);
        when(beanFactory.getBean(TaskDecorator.class)).thenReturn(runnable -> runnable);
        registrar.setBeanFactory(beanFactory);

        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        registrar.postProcessBeanDefinitionRegistry(registry);

        RootBeanDefinition bd = (RootBeanDefinition) registry.getBeanDefinition("taskExecutor");
        Supplier<?> supplier = bd.getInstanceSupplier();
        assertThat(supplier).isNotNull();

        Object created = supplier.get();
        assertThat(created).isInstanceOf(ThreadPoolTaskExecutor.class);
        ((ThreadPoolTaskExecutor) created).shutdown();
    }
}
