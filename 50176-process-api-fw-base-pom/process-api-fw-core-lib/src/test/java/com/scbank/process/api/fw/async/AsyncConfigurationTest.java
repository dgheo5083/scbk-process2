package com.scbank.process.api.fw.async;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskDecorator;

import com.scbank.process.api.fw.async.executor.AsyncExecutorRegistrar;
import com.scbank.process.api.fw.async.task.DefaultContextCopyingTaskDecorator;

/**
 * {@link AsyncConfiguration} 단위 테스트. (@Bean 팩토리 메서드 검증)
 */
class AsyncConfigurationTest {

    private final AsyncConfiguration configuration = new AsyncConfiguration();

    @Test
    @DisplayName("taskDecorator 는 DefaultContextCopyingTaskDecorator 를 생성")
    void taskDecoratorBean() {
        TaskDecorator decorator = configuration.taskDecorator();

        assertThat(decorator).isInstanceOf(DefaultContextCopyingTaskDecorator.class);
    }

    @Test
    @DisplayName("asyncExecutorRegistrar 는 AsyncExecutorRegistrar 를 생성")
    void asyncExecutorRegistrarBean() {
        AsyncExecutorRegistrar registrar = configuration.asyncExecutorRegistrar();

        assertThat(registrar).isNotNull();
    }
}
