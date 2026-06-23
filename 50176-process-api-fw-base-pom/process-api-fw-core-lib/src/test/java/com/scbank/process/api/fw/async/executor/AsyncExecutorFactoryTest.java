package com.scbank.process.api.fw.async.executor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ThreadPoolExecutor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.scbank.process.api.fw.async.AsyncProperties.ThreadPoolProperties;
import com.scbank.process.api.fw.async.AsyncProperties.ThreadPoolProperties.Pool;
import com.scbank.process.api.fw.async.AsyncProperties.ThreadPoolProperties.RejectionPolicy;

/**
 * {@link AsyncExecutorFactory} 단위 테스트.
 */
class AsyncExecutorFactoryTest {

    private ThreadPoolProperties props(RejectionPolicy policy) {
        ThreadPoolProperties props = new ThreadPoolProperties();
        Pool pool = new Pool(3, 9, 50, 45, false, "test-");
        props.setPool(pool);
        props.setRejectionPolicy(policy);
        return props;
    }

    @Test
    @DisplayName("create 는 Pool 설정값을 반영한 초기화된 Executor 를 반환")
    void createAppliesPoolSettings() {
        ThreadPoolTaskExecutor executor = AsyncExecutorFactory.create(props(RejectionPolicy.DISCARD), null);

        assertThat(executor.getCorePoolSize()).isEqualTo(3);
        assertThat(executor.getMaxPoolSize()).isEqualTo(9);
        assertThat(executor.getKeepAliveSeconds()).isEqualTo(45);
        assertThat(executor.getThreadPoolExecutor()).isNotNull();
        executor.shutdown();
    }

    @Test
    @DisplayName("create 는 decorator 가 주어지면 TaskDecorator 를 설정한다")
    void createWithDecorator() {
        TaskDecorator decorator = runnable -> runnable;

        ThreadPoolTaskExecutor executor = AsyncExecutorFactory.create(props(RejectionPolicy.CALLER_RUNS), decorator);

        assertThat(executor).isNotNull();
        executor.shutdown();
    }

    @ParameterizedTest
    @EnumSource(RejectionPolicy.class)
    @DisplayName("모든 RejectionPolicy 에 대해 알맞은 핸들러로 Executor 생성")
    void createForEachRejectionPolicy(RejectionPolicy policy) {
        ThreadPoolTaskExecutor executor = AsyncExecutorFactory.create(props(policy), null);

        ThreadPoolExecutor underlying = executor.getThreadPoolExecutor();
        switch (policy) {
            case DISCARD:
                assertThat(underlying.getRejectedExecutionHandler())
                        .isInstanceOf(ThreadPoolExecutor.DiscardPolicy.class);
                break;
            case DISCARD_OLDEST:
                assertThat(underlying.getRejectedExecutionHandler())
                        .isInstanceOf(ThreadPoolExecutor.DiscardOldestPolicy.class);
                break;
            case CALLER_RUNS:
                assertThat(underlying.getRejectedExecutionHandler())
                        .isInstanceOf(ThreadPoolExecutor.CallerRunsPolicy.class);
                break;
            case ABORT:
                assertThat(underlying.getRejectedExecutionHandler())
                        .isInstanceOf(ThreadPoolExecutor.AbortPolicy.class);
                break;
        }
        executor.shutdown();
    }
}
